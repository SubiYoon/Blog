// DocsTreeSidebar.jsx
import { useState } from 'react';
import '../../css/markdown/DocsTreeSidebar.css';
import ContextMenu from './ContextMenu';
import TreeNode from './TreeNode';
import { insertChildNode } from './TreeDataUtils';
import { $alert, $confirm, $prompt } from '../ui/SweetAlert';
import { $axios } from '../../api';

const DocsTreeSidebar = ({ treeData, setTreeData, onSelectFile }) => {
    const [contextMenu, setContextMenu] = useState(null);
    const [refreshKey, setRefreshKey] = useState(0);

    const handleRightClick = (x, y, node) => {
        setContextMenu({ x, y, node });
    };

    const isDuplicateNode = (tree, title, filePath, type) => {
        for (const node of tree) {
            if (node.title === title && node.filePath === filePath && node.type === type) return true;
            if (node.children?.length > 0) {
                if (isDuplicateNode(node.children, title, filePath, type)) return true;
            }
        }
        return false;
    };

    const handleAdd = async (type) => {
        const title = await $prompt('파일을 생성합니다.', `생성할 ${type} 이름을 입력해주세요`);
        if (!title || !contextMenu?.node) return;

        const targetNode = contextMenu.node;
        const isTargetFolder = targetNode.type === 'folder';

        const parentPath = isTargetFolder
            ? targetNode.filePath
            : targetNode.filePath.substring(0, targetNode.filePath.lastIndexOf('/'));

        const newFullPath = parentPath === '/' ? `/${title}` : `${parentPath}/${title}`;
        const newNodeType = type === 'file' ? 'md' : 'folder';

        const titleArr = title.split('.');

        if (type === 'file' && titleArr.length < 2) {
            $alert('파일은 확장자를 입력해야 합니다.', '가능한 확장자 .json, .js, .md, .ts', 'error');
            return
        }

        if (type === 'file' && !titleArr[1].includes('md', 'js', 'ts*', 'json')) {
            $alert('사용할 수 없는 확장자입니다.', '가능한 확장자 .json, .js, .md, .ts', 'error');
            return
        }

        if (isDuplicateNode(treeData, title, newFullPath, newNodeType)) {
            $alert('이미 존재하는 항목입니다.', '같은 이름과 경로의 항목이 이미 존재합니다.', 'warning');
            setContextMenu(null);
            return;
        }

        const newNode = {
            title,
            type: newNodeType,
            filePath: newFullPath,
            ...(type === 'folder' ? { children: [] } : { content: '' })
        };

        const updatedTree =
            parentPath === '/'
                ? [...treeData, newNode]
                : insertChildNode(treeData, parentPath, newNode);

        setTreeData(updatedTree);
        setRefreshKey((prev) => prev + 1);
        setContextMenu(null);

        if (type === 'file') {
            onSelectFile({ ...newNode, reset: true });
        }
    };

    const handleRenameFolder = async (node) => {
        const newTitle = await $prompt('폴더 이름 변경', '새 이름을 입력해주세요');
        if (!newTitle || !node) return;

        const parentPath = node.filePath.substring(0, node.filePath.lastIndexOf('/'));
        const newFilePath = parentPath === '' ? `/${newTitle}` : `${parentPath}/${newTitle}`;

        const isDup = isDuplicateNode(treeData, newTitle, newFilePath, 'folder');
        if (isDup) {
            $alert('이미 존재하는 폴더입니다.', '중복된 폴더 이름입니다.', 'warning');
            return;
        }

        const updateFolderTitle = (nodes) =>
            nodes.map((n) => {
                if (n.filePath === node.filePath && n.type === 'folder') {
                    return {
                        ...n,
                        title: newTitle,
                        filePath: newFilePath,
                        children: n.children
                    };
                } else if (n.children) {
                    return {
                        ...n,
                        children: updateFolderTitle(n.children)
                    };
                }
                return n;
            });

        setTreeData(updateFolderTitle(treeData));
        setContextMenu(null);
        setRefreshKey((prev) => prev + 1);
    };

    const handleDelete = async (node) => {
        await $confirm(`${node.title}를 제거 하시겠습니까?`, '작성중인 파일은 소멸됩니다.<br> 삭제는 즉시 반영되며 되돌릴 수 없습니다.', 'question', '제거', '취소')
            .then((response) => {
                if (response) {
                    $axios.delete('/doc', { params: node.type === 'folder' ? { filePath: node.filePath } : node }).then(async response => {
                        if (response.data.customCode === 'SUCCESS') {
                            await $alert('제거 성공!', '', 'success');
                        } else {
                            $alert('제거 실패!', `서버 관리자에게 문의 하세요.`, 'error');
                        }
                    });
                } else {
                    $alert('제거 취소!', `제거 요청을 취소하였습니다.`, 'info');
                }
            });
    };

    const handleClickFile = (node) => {
        if (node.type === 'md') {
            onSelectFile(node);
        }
    };

    return (
        <div className="docs-tree-sidebar">
            <div
                className="docs-tree-scrollable"
                onContextMenu={(e) => {
                    if (e.target.closest('.tree-node')) return;
                    e.preventDefault();
                    setContextMenu({
                        x: e.pageX,
                        y: e.pageY,
                        node: { title: '/', filePath: '/', type: 'folder' }
                    });
                }}
            >
                {treeData.map((node, idx) => (
                    <TreeNode
                        key={idx}
                        node={node}
                        onRightClick={handleRightClick}
                        onClick={handleClickFile}
                        refreshKey={refreshKey}
                    />
                ))}
            </div>
            {contextMenu && (
                <ContextMenu
                    x={contextMenu.x}
                    y={contextMenu.y - 100}
                    node={contextMenu.node}
                    onAddFile={() => handleAdd('file')}
                    onAddFolder={() => handleAdd('folder')}
                    onRename={() => handleRenameFolder(contextMenu.node)}
                    onDelete={() => handleDelete(contextMenu.node)}
                    onClose={() => setContextMenu(null)}
                />
            )}
        </div>
    );
};

export default DocsTreeSidebar;
