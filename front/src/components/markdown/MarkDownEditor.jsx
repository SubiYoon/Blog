import React, { useState, useEffect } from 'react';
import remarkGfm from 'remark-gfm';
import { Edit, Eye, Send } from 'lucide-react';
import ReactMarkdown from 'react-markdown';
import rehypeRaw from 'rehype-raw';
import '../../css/MarkDownEditor.css';

const MarkdownEditor = () => {
    const [title, setTitle] = useState('');
    const [markdown, setMarkdown] = useState(`# 안녕하세요 👋\n\n이것은 **마크다운** 에디터입니다.`);
    const [content, setContent] = useState('');

    // markdown이 변경될 때마다 content 갱신
    useEffect(() => {
        setContent(markdown); // gray-matter 없이 그대로 사용
    }, [markdown]);

    const handleSubmit = async () => {
        try {
            const response = await fetch('/api/save-markdown', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ title, content }) // frontMatter 제거
            });

            if (!response.ok) throw new Error('서버 오류');

            alert('✅ 저장 성공!');
        } catch (err) {
            console.error(err);
            alert('❌ 저장 실패!');
        }
    };

    return (
        <div className="editor-container">
            <div className="editor-pane">
                <div className="editor-header">
                    <Edit size={16} />
                    <span>제목 / 마크다운 입력</span>
                </div>
                <input
                    type="text"
                    className="title-input"
                    placeholder="파일 제목을 입력하세요"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
                <textarea
                    className="editor-textarea"
                    value={markdown}
                    onChange={(e) => setMarkdown(e.target.value)}
                    rows={15}
                />
                <button className="submit-button" onClick={handleSubmit}>
                    <Send size={16} style={{ marginRight: '6px' }} />
                    저장
                </button>
            </div>

            <div className="preview-pane">
                <div className="editor-header">
                    <Eye size={16} />
                    <span>미리보기</span>
                </div>
                <div className="markdown-preview">
                    <ReactMarkdown remarkPlugins={[remarkGfm]} rehypePlugins={[rehypeRaw]}>
                        {content}
                    </ReactMarkdown>
                </div>
            </div>
        </div>
    );
};

export default MarkdownEditor;
