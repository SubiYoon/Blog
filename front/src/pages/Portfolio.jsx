import Layout from '@theme/Layout'
import '../css/portfolio.css';
import PortfolioMenu from "../components/PortfolioMenu";
import { useEffect, useState } from 'react'
import PortfolioProject from "../components/PortfolioProject";
import Modal from 'react-modal';
import { useUser } from '../store/globalStore'
import EditPortfolioProject from '../components/EditPortfolioProject'

Modal.setAppElement('#__docusaurus');

export default function portfolioForm() {

    const { logoutUser, isLoggedIn } = useUser();


    //신규 프로젝트 추가 데이터
    const [logoFile, setLogoFile] = useState(null);
    const [logoPreview, setLogoPreview] = useState(null);
    const [name, setName] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');


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
    const [isEditMode, setIsEditMode] = useState(false);

    const [isModalOpen, setIsModalOpen] = useState(false);

    //취소를 대비한 상태 복사
    const [isDetailData, setIsDetailData] = useState(companyList[0]);

    function insertTitle() {
        //여기에 신규 추가 저장 로직 추가
        const formData = new FormData();
        formData.append('name', name);
        formData.append('date', `${startDate} ~ ${endDate}`);
        formData.append('file', logoFile);
    }

    function deleteItem() {
        console.log(selectedCompany);
    }


    useEffect(() => {

        //깊은 복사 사용
        setIsDetailData(JSON.parse(JSON.stringify(selectedCompany)));
    }, [isEditMode])

    return (
        <Layout title="포트폴리오">
            <main className="wrap">
                <section className="menu-section">
                    <div className="menu-box">
                        {companyList.map(company => (
                            <PortfolioMenu key={company.id} data={company} isSelected={selectedCompany?.id === company.id} onClick={() => {setSelectedCompany(company); setIsDetailData(company);}} />
                        ))}
                    </div>
                    {isLoggedIn() && <div className="button-section">
                        <div className="button-box">
                            <button onClick={() => deleteItem()}>삭제</button>
                            {isEditMode ? (    <button onClick={()=> setIsEditMode(false)}>취소</button>) : (<button onClick={() => setIsEditMode(true)}>수정</button>)}
                            <button onClick={() => setIsModalOpen(true)}>추가</button>
                        </div>
                    </div>}
                </section>
                <section className="cont-section">
                    <div className="cont-box">
                        {isLoggedIn() && isEditMode ? (<EditPortfolioProject data={isDetailData}/>) : (   <PortfolioProject data={selectedCompany} />)}
                    </div>
                </section>
                <Modal
                    isOpen={isModalOpen}
                    onRequestClose={() => setIsModalOpen(false)}
                    className="modal-content"
                    overlayClassName="modal-overlay"
                >
                    {logoPreview && <img src={logoPreview} alt="미리보기" />}

                    {/* 파일 첨부 */}
                    <div>
                        <input
                            type="file"
                            accept="image/*"
                            onChange={(e) => {
                                const file = e.target.files[0]
                                if (file) {
                                    setLogoFile(file) // 실제 파일 저장
                                    const reader = new FileReader()
                                    reader.onloadend = () => {
                                        setLogoPreview(reader.result) // 미리보기용
                                    }
                                    reader.readAsDataURL(file)
                                }
                            }}
                        />
                    </div>
                    <div className="title-box">
                        <input
                            type="text"
                            placeholder="회사명을 입력하세요."
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </div>
                    <div className="date-box">
                        <input
                            type="date"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                        />

                        <span> ~ </span>
                        <input
                            type="date"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                        />
                    </div>
                    <div className="modal-button-box">
                        <button onClick={() => insertTitle()}>저장</button>
                        <button onClick={() => setIsModalOpen(false)}>닫기</button>
                    </div>
                </Modal>
            </main>
        </Layout>
    )
}
