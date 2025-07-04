import React, { useState, useRef } from 'react';
import '../..//css/layout/ResizableSidebar.css';

const ResizableSidebar = ({ children }) => {
    const [width, setWidth] = useState(280);
    const sidebarRef = useRef(null);
    const isResizing = useRef(false);

    const handleMouseDown = () => {
        isResizing.current = true;
        document.body.style.cursor = 'col-resize';
    };

    const handleMouseMove = (e) => {
        if (!isResizing.current) return;
        const newWidth = Math.min(600, Math.max(200, e.clientX));
        setWidth(newWidth);
    };

    const handleMouseUp = () => {
        isResizing.current = false;
        document.body.style.cursor = 'default';
    };

    React.useEffect(() => {
        window.addEventListener('mousemove', handleMouseMove);
        window.addEventListener('mouseup', handleMouseUp);
        return () => {
            window.removeEventListener('mousemove', handleMouseMove);
            window.removeEventListener('mouseup', handleMouseUp);
        };
    }, []);

    return (
        <div className="resizable-sidebar" style={{ width }}>
            <div className="resizable-content">{children}</div>
            <div className="resizer" onMouseDown={handleMouseDown} />
        </div>
    );
};

export default ResizableSidebar;

