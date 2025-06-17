import React, { useState } from 'react'
import '../css/loginForm.css';
import { Mail, Lock, IdCardLanyard } from 'lucide-react'
import Layout from '@theme/Layout';
import { $axios } from '../api'

export default function LoginForm() {
    const [alias, setAlias] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMsg('');
        setLoading(true);

        try {
            const response = await $axios.post('http://localhost:8903/login', {
                id: alias,
                password: password
            });

            if (!response.ok) {
                const errorData = await response.json();
                setErrorMsg(errorData.message || '로그인 실패');
            } else {
                alert('로그인 성공!');
                // 예: window.location.href = '/dashboard';
            }
        } catch {
            setErrorMsg('서버 요청 실패');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Layout title="로그인">
            <div className="login-wrapper">
                <div className="login-card">
                    <h1 className="login-title">로그인</h1>
                    <p className="login-subtitle">Docusaurus에 오신 것을 환영합니다 🚀</p>

                    <form className="login-form" onSubmit={handleSubmit}>
                        <div className="input-group">
                            <IdCardLanyard className="input-icon" size={20} />
                            <input
                                placeholder="별칭"
                                className="input-field"
                                required
                                value={alias}
                                onChange={e => setAlias(e.target.value)}
                                disabled={loading}
                            />
                        </div>

                        <div className="input-group">
                            <Lock className="input-icon" size={20} />
                            <input
                                type="password"
                                placeholder="비밀번호"
                                className="input-field"
                                required
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                                disabled={loading}
                            />
                        </div>

                        {errorMsg && <p style={{ color: 'red', marginBottom: '1rem' }}>{errorMsg}</p>}

                        <button type="submit" className="login-button" disabled={loading}>
                            {loading ? '로그인 중...' : '로그인'}
                        </button>
                    </form>
                </div>
            </div>
        </Layout>
    );
}