import { useEffect, useState } from 'react'
import { useHistory } from '@docusaurus/router';
import '../css/loginForm.css';
import { Lock, IdCardLanyard } from 'lucide-react'
import Layout from '@theme/Layout';
import { $axios } from '../api'

export default function LoginForm() {
    const history = useHistory();
    const [id, setId] = useState('');
    const [alias, setAlias] = useState('');
    const [password, setPassword] = useState('');
    const [msg, setMsg] = useState('');
    const [messageColor, setMessageColor] = useState('red');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMsg('');
        setLoading(true);

        await $axios.post('/login', {
            id: alias,
            password: password
        }).then((response) => {
            setMessageColor('green')
            setMsg("로그인에 성공했습니다.")
            localStorage.setItem('DEVSTAT-JWT', 'temp-token');
            history.push('/');
        }).catch((error) => {
            setMessageColor('red')
            setMsg("로그인에 실패했습니다.")
            console.log(error);
        });
    };

    useEffect(() => {
        $axios.get('/info/ABCD').then((response) => {
            setId(response.data.alias);
        })
    }, [id]);

    return (
        <Layout title="로그인">
            <div className="login-wrapper">
                <div className="login-card">
                    <h1 className="login-title">로그인</h1>
                    <p className="login-subtitle">{id}&apos;s Docusaurus에 오신 것을 환영합니다 🚀</p>

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

                        {msg && <p style={{ color: messageColor, marginBottom: '1rem' }}>{msg}</p>}

                        <button type="submit" className="login-button" disabled={loading}>
                            {loading ? '로그인 중...' : '로그인'}
                        </button>
                    </form>
                </div>
            </div>
        </Layout>
    );
}
