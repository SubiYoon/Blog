// ContextMenu.jsx
import React, { useEffect } from 'react';
import '../../css/markdown/ContextMenu.css';

const ContextMenu = ({
    x,
    y,
    node,
    onAddFile,
    onAddFolder,
    onDelete,
    onRename,
    onClose
}) => {
    useEffect(() => {
        const closeOnClick = () => onClose();
        window.addEventListener('click', closeOnClick);
        return () => window.removeEventListener('click', closeOnClick);
    }, [onClose]);

    const handleClick = (e, action) => {
        e.stopPropagation();
        action();
        onClose();
    };


    return (
        <div className="context-menu" style={{ top: y, left: x }} onContextMenu={(e) => e.preventDefault()}>
            {node?.filePath !== '/' && (
                <div onClick={(e) => handleClick(e, onAddFile)}>📄 파일 추가</div>
            )}
            <div onClick={(e) => handleClick(e, onAddFolder)}>📁 폴더 추가</div>
            {node?.type === 'folder' && node?.filePath !== '/' && (
                <div onClick={(e) => handleClick(e, onRename)}>✏️ 이름 변경</div>
            )}
            <div className="delete" onClick={(e) => handleClick(e, onDelete)}>🗑 삭제</div>
        </div>
    );
};

export default ContextMenu;

