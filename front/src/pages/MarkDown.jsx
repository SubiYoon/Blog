import React, { useEffect, useState } from 'react';
import MarkdownEditor from '../components/markdown/MarkDownEditor';
import DocsTreeSidebar from '../components/markdown/DocsTreeSidebar';
import '../css/markdown/MarkDownEditor.css';
import '../css/markdown/DocsTreeSidebar.css';
import { updateNodeContent } from '../components/markdown/TreeDataUtils';
import { $axios } from '../api';
import Layout from '@theme/Layout';
import ResizableSidebar from '../components/layout/ResizableSidebar';
import { $alert } from '../components/ui/SweetAlert';

const MarkDown = () => {
    const [docsTree, setDocsTree] = useState([]);
    const [selectedFile, setSelectedFile] = useState(null);

    useEffect(() => {
        $axios.get('/doc/docsTree').then((response) => {
            setDocsTree(response.data);
        });
    }, []);

    const handleSelectFile = async (node) => {
        if (!node || node.type !== 'md') return;

        if (typeof node.content !== 'undefined') {
            setSelectedFile(node);
            return;
        }

        const data = {
            params: node
        };

        try {
            const res = await $axios.get('/doc', data);

            const content = res.data?.content || '';
            const updatedTree = updateNodeContent(docsTree, node.filePath, content);

            setDocsTree(updatedTree);
            setSelectedFile({ ...node, content });
        } catch (err) {
            console.error('❌ 문서 로딩 실패:', err);
        }
    };

    const isEmptyFolderCheck = (node) => {
        return node.isRootFolder && (node.children.length === 0 ? true : childrenCheck(node.children));
    };

    const childrenCheck = (children) => {
        for (let i = 0; i < children.length; i++) {
            if (children[i].type === 'md') {
                return false;
            }
        }
        return true;
    };

    const handleSubmit = ({ path, content, commitMessage }) => {
        const updatedTree = updateNodeContent(docsTree, path, content);

        setDocsTree(updatedTree);

        docsTree.forEach(node => {
            if (node.filePath === path) {
                setSelectedFile({ ...node, content });
            }
        });

        // 저장 API 호출 위치
        $axios.post('/doc',
            {
                params: {
                    docsTree: updatedTree,
                    commitMessage: commitMessage,
                }
            }
        ).then((response) => {
            const data = response.data;
            if (response.data.customCode === 'SUCCESS') {
                $alert('저장 성공!', 'GitHub에 반영되었습니다.', 'success');
            } else if (response.data.customCode === 'EXECUTE_FRONT_RESTART') {
                $axios.post('/doc/restart');
            } else if (response.data.customCode === 'INCLUDE_NOT_FILE_AND_FOLDER') {
                $alert('저장 실패!', `가능한 확장자 .md, .mdx`, 'error');
            } else {
                $alert('저장 실패!', `${data.message} 서버 관리자에게 문의 하세요.`, 'error');
            }
        }).catch((err) => {
            $alert('저장 실패!', '서버 관리자에게 문의 하세요.', 'error');
        });
    };

    return (
        <Layout title="문서">
            <div className="editor-container">
                <ResizableSidebar>
                    <DocsTreeSidebar
                        treeData={docsTree}
                        setTreeData={setDocsTree}
                        onSelectFile={handleSelectFile}
                    />
                </ResizableSidebar>
                <div className="markdown-editor-wrapper">
                    <MarkdownEditor
                        selectedFile={selectedFile}
                        treeData={docsTree}
                        setTreeData={setDocsTree}
                        updateTreeData={(path, content) => {
                            const updated = updateNodeContent(docsTree, path, content);
                            setDocsTree(updated);
                        }}
                        onSubmit={handleSubmit}
                    />
                </div>
            </div>
        </Layout>
    );
};

export default MarkDown;
