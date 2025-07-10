import { useEffect, useRef, useState } from 'react';
import '../../css/markdown/MarkDownEditor.css';
import remarkGfm from 'remark-gfm';
import ReactMarkdown from 'react-markdown';
import rehypeRaw from 'rehype-raw';
import rehypeHighlight from 'rehype-highlight';
import 'highlight.js/styles/tokyo-night-dark.min.css';
import { DownloadCloud, Edit, Eye, Save, UploadCloud } from 'lucide-react';
import { $alert, $confirm, $prompt } from '../ui/SweetAlert';
import { $axios } from '../../api';

const MarkdownEditor = ({ selectedFile, treeData, setTreeData, updateTreeData, onSubmit }) => {
    const [title, setTitle] = useState('');
    const [filePath, setFilePath] = useState('');
    const [type, setType] = useState('');
    const [markdown, setMarkdown] = useState('');
    const [wasCleared, setWasCleared] = useState(false);
    const textareaRef = useRef(null);

    // selectedFile이 바뀌면 초기화
    useEffect(() => {
        if (selectedFile) {
            setTitle(selectedFile.title || '');
            setFilePath(selectedFile.filePath || '');
            setType(selectedFile.type || '');
            setMarkdown(selectedFile.content || '');
            setWasCleared(false);

            if (selectedFile.reset) {
                setTimeout(() => textareaRef.current?.focus(), 0);
            }
        }
    }, [selectedFile]);

    // markdown 입력 시 실시간 tree 반영
    useEffect(() => {
        if (filePath && type === 'md') {
            updateTreeData(filePath, markdown);
        }
    }, [markdown]);

    // title이 바뀌면 filePath도 바뀌고 treeData도 업데이트
    useEffect(() => {
        if (!filePath || title.trim() === '') return;

        const parentDir = filePath.substring(0, filePath.lastIndexOf('/'));
        const newPath = `${parentDir}/${title.trim()}`;
        setFilePath(newPath);

        const updatedTree = treeData.map((node) =>
            updateNodeTitleAndPath(node, filePath, type, title.trim(), newPath)
        );
        setTreeData(updatedTree);
    }, [title]);

    const handleKeyDown = (e) => {
        if (e.key !== 'Tab') return;

        e.preventDefault();
        const tabSpace = '    ';
        const textarea = textareaRef.current;

        const start = textarea.selectionStart;
        const end = textarea.selectionEnd;
        const selectedText = markdown.slice(start, end);
        const before = markdown.slice(0, start);
        const after = markdown.slice(end);

        let newText = markdown;
        let newStart = start;
        let newEnd = end;

        const isMultiline = selectedText.includes('\n');

        if (isMultiline) {
            const lines = selectedText.split('\n');

            if (e.shiftKey) {
                // Shift + Tab: 내어쓰기
                const updatedLines = lines.map(line =>
                    line.startsWith(tabSpace) ? line.slice(tabSpace.length) :
                        line.startsWith('   ') ? line.slice(3) :
                            line.startsWith('  ') ? line.slice(2) :
                                line.startsWith(' ') ? line.slice(1) : line
                );
                const updated = updatedLines.join('\n');
                newText = before + updated + after;
                newEnd = start + updated.length;
            } else {
                // Tab: 들여쓰기
                const updatedLines = lines.map(line => tabSpace + line);
                const updated = updatedLines.join('\n');
                newText = before + updated + after;
                newEnd = start + updated.length;
            }
        } else {
            if (e.shiftKey) {
                // 단일 줄 내어쓰기
                const beforeTab = markdown.slice(start - tabSpace.length, start);
                if (beforeTab === tabSpace) {
                    newText = before.slice(0, -tabSpace.length) + after;
                    newStart = newEnd = start - tabSpace.length;
                }
            } else {
                // 단일 줄 들여쓰기
                newText = before + tabSpace + after;
                newStart = newEnd = start + tabSpace.length;
            }
        }

        setMarkdown(newText);

        // selection 위치 다시 설정
        setTimeout(() => {
            textarea.selectionStart = newStart;
            textarea.selectionEnd = newEnd;
        }, 0);
    };

    // type 포함해서 정확히 일치하는 노드만 업데이트
    const updateNodeTitleAndPath = (node, oldPath, oldType, newTitle, newPath) => {
        if (node.filePath === oldPath && node.type === oldType) {
            return {
                ...node,
                title: newTitle,
                filePath: newPath
            };
        } else if (node.children) {
            return {
                ...node,
                children: node.children.map((child) =>
                    updateNodeTitleAndPath(child, oldPath, oldType, newTitle, newPath)
                )
            };
        }
        return node;
    };

    const handleTitleChange = (e) => {
        const value = e.target.value;

        if (value.trim() === '') {
            setWasCleared(true);
            setTitle('');
        } else {
            if (wasCleared) {
                setTitle(value.trim());
                setWasCleared(false);
            } else {
                setTitle(value);
            }
        }
    };

    const handleSubmit = async () => {
        if (!filePath || !type) return;

        const confirmed = await $confirm(
            '저장 하시겠습니까?',
            '저장 시 모든 변경 내역이 저장됩니다.',
            'question',
            '저장',
            '취소'
        );

        if (!confirmed) return;

        const commitMessage = await $prompt('커밋 메시지를 입력해주세요.', "빈값 입력시 'docusaurus update'로 입력됩니다.", "text", "docusaurus update");

        try {
            await onSubmit({ path: filePath, content: markdown, commitMessage: commitMessage || 'docusaurus update' });
        } catch (err) {
            console.error(err);
            $alert('실패', '저장 실패..', 'error');
        }
    };

    const gitPush = async () => {
        if (await $confirm('Github에 반영 하시겠습니까?', '저장하지 않은 데이터는 Push에 반영되지 않습니다.', 'question', '반영', '취소')) {
            $axios.post('/git', { task: 'push origin main' }).then(res => {
                if (res.data.customCode === 'SUCCESS') {
                    $alert('Git Push 성공!', '', 'success');
                } else {
                    $alert('Git Push 실패', `서버 관리자에게 문의 하세요.`, 'error');
                }
            });
        }
    }

    return (
        <div className="editor-container">
            <div className="editor-pane">
                <div className="editor-header">
                    <div className="editor-header-left">
                        <Edit size={16} />
                        <span> 제목 / 마크다운 입력(escape 필수로 사용 &apos; \ &apos; )</span>
                    </div>
                    <div className='submit-button-wrapper'>
                        <button className="submit-button" onClick={handleSubmit}>
                            <Save size={16} style={{ marginRight: '6px' }} />
                            저장
                        </button>
                    </div>
                </div>
                <input
                    type="text"
                    className="title-input"
                    placeholder="파일 제목을 입력하세요"
                    value={title}
                    onChange={handleTitleChange}
                />
                <textarea
                    ref={textareaRef}
                    className="editor-textarea"
                    value={markdown}
                    onChange={(e) => setMarkdown(e.target.value)}
                    onKeyDown={handleKeyDown}
                />
            </div>
            <div className="preview-pane">
                <div className="editor-header">
                    <div className="editor-header-center">
                        <Eye size={16} />
                        <span> 미리보기</span>
                    </div>
                </div>
                <div className="markdown-preview">
                    <ReactMarkdown
                        remarkPlugins={[remarkGfm]}
                        rehypePlugins={[rehypeRaw, rehypeHighlight]}
                    >
                        {markdown}
                    </ReactMarkdown>
                </div>
            </div>
        </div>
    );
};

export default MarkdownEditor;
