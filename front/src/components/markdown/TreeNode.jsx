// TreeNode.jsx
import React, { useState } from 'react';
import {
    Folder,
    FolderOpen,
    FileText,
    FileCode,
    FileJson,
    File
} from 'lucide-react';

const getFileIcon = (filename) => {
    if (!filename || typeof filename !== 'string') return <File size={16} />;
    const ext = filename.split('.').pop().toLowerCase();
    switch (ext) {
        case 'md': return <FileText size={16} />;
        case 'js':
        case 'ts': return <FileCode size={16} />;
        case 'json': return <FileJson size={16} />;
        default: return <File size={16} />;
    }
};

const TreeNode = ({ node, depth = 0, onRightClick, onClick, refreshKey }) => {
    const [isOpen, setIsOpen] = useState(false);
    const isFolder = node.type === 'folder';
    const paddingLeft = `${depth * 16}px`;

    if (node.status === 'delete') return null;

    const toggle = () => {
        if (isFolder) setIsOpen(!isOpen);
    };

    const handleContextMenu = (e) => {
        e.preventDefault();
        onRightClick(e.pageX, e.pageY, node);
    };

    const handleClick = () => {
        if (!isFolder) {
            onClick(node);
        } else {
            toggle();
        }
    };

    return (
        <div>
            <div
                className="tree-node"
                style={{ paddingLeft }}
                onContextMenu={handleContextMenu}
                onClick={handleClick}
            >
                <span className="tree-node-icon">
                    {isFolder ? (isOpen ? <FolderOpen size={16} /> : <Folder size={16} />) : getFileIcon(node.title)}
                </span>
                <span className="tree-node-label" title={node.title}>{node.title}</span>
            </div>
            {isFolder && isOpen && node.children?.map((child, idx) => (
                <TreeNode
                    key={`${child.filePath}-${refreshKey}`}
                    node={child}
                    depth={depth + 1}
                    onRightClick={onRightClick}
                    onClick={onClick}
                    refreshKey={refreshKey}
                />
            ))}
        </div>
    );
};

export default TreeNode;
