import Layout from '@theme/Layout'
import '../css/portfolio.css';
import PortfolioMenu from "../components/PortfolioMenu";
import { useState } from "react";
import PortfolioProject from "../components/PortfolioProject";
import { $axios } from '../api';

export default function portfolioForm() {

    const [companyList, setCompanyList] = useState([
        {
            id: 1,
            name: '리액트',
            logo: 'https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg',
            date: '2025-06-10 ~ 2025-06-20',
            projects: [
                {
                    id: 1,
                    name: '결제 고도화 프로젝트',
                    date: '2025-06-10 ~ 2025-06-20',
                    items: [
                        {
                            id: 1,
                            title: '카카오 페이',
                            cont: '기존 카카오페이의 결제 시스템을 React 기반으로 전면 리팩토링하였고, ' +
                                '결제 처리 속도를 20% 이상 개선하였습니다. Spring Boot 기반 백엔드와 연동하여 ' +
                                '결제 승인, 취소 API를 안정적으로 처리하였으며, TypeScript 도입을 통해 유지보수성을 향상시켰습니다.',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?auto=format&fit=crop&w=800&q=80' },
                                { img: 'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?auto=format&fit=crop&w=800&q=80' },
                                { img: 'https://images.unsplash.com/photo-1550439062-609e1531270e?auto=format&fit=crop&w=800&q=80' },
                                { img: 'https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=800&q=80' },
                            ]
                        },
                        {
                            id: 2,
                            title: '간편 송금',
                            cont: '간편송금 UX 개선 프로젝트에 참여하여 React와 Styled-components를 활용해 ' +
                                '모바일 UI/UX를 최적화하였고, 사용자 피드백 기반으로 UI 반응 속도를 개선하였습니다. ' +
                                '또한, 보안 강화를 위해 클라이언트 측 암호화 모듈을 적용했습니다.',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 3,
                            title: '데이터 시각화',
                            cont: '데이터 시각화 대시보드 개발: D3.js와 Chart.js를 적용해 관리자의 KPI 모니터링 정확도를 90% 이상 도달.',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 4,
                            title: '대시보드 개발',
                            cont: '데이터 시각화 대시보드 개발: D3.js와 Chart.js를 적용해 관리자의 KPI 모니터링 정확도를 90% 이상 도달.',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                    ],
                },
                {
                    id: 2,
                    name: '식약처 eCTD',
                    date: '2025-06-10 ~ 2025-06-20',
                    items: [
                        {
                            id: 1,
                            title: '뷰어 화면 개편',
                            cont: '프로젝트2-1의 내용을 서술',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 2,
                            title: '타이틀',
                            cont: '프로젝트2-2의 내용을 서술',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 3,
                            title: '타이틀',
                            cont: '프로젝트2-3의 내용을 서술',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 3,
                            title: '타이틀',
                            cont: '프로젝트2-3의 내용을 서술',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 3,
                            title: '타이틀',
                            cont: '프로젝트2-3의 내용을 서술',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 3,
                            title: '타이틀',
                            cont: '프로젝트2-3의 내용을 서술',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 3,
                            title: '타이틀',
                            cont: '프로젝트2-3의 내용을 서술',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },

                    ],
                },
            ],
        },
        {
            id: 2,
            name: '네이버',
            logo: 'https://upload.wikimedia.org/wikipedia/commons/9/95/Vue.js_Logo_2.svg',
            date: '2025-06-23 ~ 2025-06-30',
            projects: [
                {
                    id: 1,
                    name: '프로젝트1',
                    date: '2025-06-10 ~ 2025-06-20',
                    items: [
                        {
                            id: 1,
                            cont: '프로젝트3-1의 내용을 서술',
                            title: '타이틀',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 2,
                            cont: '프로젝트3-2의 내용을 서술',
                            title: '타이틀',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                        {
                            id: 3,
                            cont: '프로젝트3-3의 내용을 서술',
                            title: '타이틀',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        },
                    ],
                },
            ],
        },
        {
            id: 3,
            name: '카카오',
            logo: 'https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png',
            date: '2025-07-01 ~ 2025.07-20',
            projects: [
                {
                    id: 1,
                    name: '프로젝트3',
                    date: '2025-06-10 ~ 2025-06-20',
                    items: [
                        {
                            id: 1,
                            cont: '프로젝트3-1의 내용을 서술',
                            title: '타이틀',
                            imgs: [
                                { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                                , { img: 'https://images.unsplash.com/photo-1547658719-da2b51169166?auto=format&fit=crop&w=400&q=80' }
                            ]
                        }
                    ],
                },
            ],
        },
    ]);

    const [selectedCompany, setSelectedCompany] = useState(companyList[0]);

    const logout = () => {
        $axios.post('/logout').then(() => {
            history.push('/')
            localStorage.removeItem('DEVSTAT-JWT');
        });
    }

    return (
        <Layout title="포트폴리오">
            <main className="wrap">
                <section className="menu-top">
                    <div className="navbar-inner">
                        <a>문서편집</a>
                        <a>포토폴리오 편집</a>
                        <div className="logout-box">
                            <a onClick={logout}>로그아웃</a>
                        </div>
                    </div>
                </section>
                <section className="menu-section">
                    <div className="menu-box">
                        {companyList.map(company => (
                            <PortfolioMenu key={company.id} data={company} isSelected={selectedCompany?.id === company.id} onClick={() => setSelectedCompany(company)} />
                        ))}
                    </div>
                    <div className="button-section">
                        <div className="button-box">
                            <button>삭제</button>
                            <button>수정</button>
                            <button>입력</button>
                        </div>
                    </div>
                </section>
                <section className="cont-section">
                    <div className="cont-box">
                        <PortfolioProject data={selectedCompany} />
                    </div>
                </section>
            </main>
        </Layout>
    )
}
