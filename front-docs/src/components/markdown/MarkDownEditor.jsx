import React, { useState, useEffect } from 'react';
import { Edit, Eye, Send } from 'lucide-react';
import ReactMarkdown from 'react-markdown';
import rehypeRaw from 'rehype-raw';
import matter from 'gray-matter';
import '../../css/MarkDownEditor.css';

const MarkdownEditor = () => {
    const [title, setTitle] = useState('');
    const [markdown, setMarkdown] = useState(`# 안녕하세요 👋\n\n이것은 **마크다운** 에디터입니다.`);
    const [content, setContent] = useState('');
    const [frontMatter, setFrontMatter] = useState<Record<string, any>>({});

    // markdown이 변경될 때마다 front matter 분리 및 content 갱신
    useEffect(() => {
        const parsed = matter(markdown);
        setFrontMatter(parsed.data);
        setContent(parsed.content);
    }, [markdown]);

    const handleSubmit = async () => {
        try {
            // 서버에 frontMatter와 content를 같이 보내기 (필요 시)
            const response = await fetch('/api/save-markdown', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ title, frontMatter, content })
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
                <div className="frontmatter-display">
                    <pre>{JSON.stringify(frontMatter, null, 2)}</pre>
                </div>
                <div className="markdown-preview">
                    <ReactMarkdown rehypePlugins={[rehypeRaw]}>
                        {content}
                    </ReactMarkdown>
                </div>
            </div>
        </div>
    );
};

export default MarkdownEditor;
