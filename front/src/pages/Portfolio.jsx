import Layout from '@theme/Layout'
import '../css/portfolio.css';
import PortfolioMenu from "../components/PortfolioMenu";
import { useEffect, useState } from 'react'
import PortfolioProject from "../components/PortfolioProject";
import Modal from 'react-modal';
import { useUser } from '../store/globalStore'
import EditPortfolioProject from '../components/EditPortfolioProject'
import { $axios } from '../api'

Modal.setAppElement('#__docusaurus');

export default function portfolioForm() {

    const { logoutUser, isLoggedIn } = useUser();


    //신규 프로젝트 추가 데이터
    const [logoFile, setLogoFile] = useState(null);
    const [logoPreview, setLogoPreview] = useState(null);
    const [name, setName] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');


    const [companyList, setCompanyList] = useState([]);
    const [loading, setLoading] = useState(true);

    const [selectedCompany, setSelectedCompany] = useState(null);
    const [isEditMode, setIsEditMode] = useState(false);

    const [isModalOpen, setIsModalOpen] = useState(false);

    //취소를 대비한 상태 복사
    const [isDetailData, setIsDetailData] = useState(null);

    // 백엔드 데이터를 프론트엔드 형식으로 변환
    const transformPortfolioData = (backendData) => {
        return backendData.map(company => ({
            id: company.companyId,
            name: company.name,
            logo: company.logo,
            date: company.date,
            projects: company.projectList?.map(project => ({
                id: project.projectId,
                name: project.name,
                date: project.date,
                items: project.itemList?.map(item => ({
                    id: item.itemId,
                    title: item.name,
                    cont: item.cont,
                    imgs: item.imageList?.map(image => ({
                        img: image.img
                    })) || []
                })) || []
            })) || []
        }));
    };

    // 포트폴리오 데이터 가져오기
    const fetchPortfolioData = async () => {
        try {
            setLoading(true);
            const response = await $axios.get('/info/portfolio');
            const transformedData = transformPortfolioData(response.data);
            setCompanyList(transformedData);

            // 첫 번째 회사를 기본 선택
            if (transformedData.length > 0) {
                setSelectedCompany(transformedData[0]);
                setIsDetailData(transformedData[0]);
            }
        } catch (error) {
            console.error('포트폴리오 데이터 로딩 실패:', error);
        } finally {
            setLoading(false);
        }
    };

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


    // 컴포넌트 마운트 시 데이터 로드
    useEffect(() => {
        fetchPortfolioData();
    }, []);

    useEffect(() => {
        if (selectedCompany) {
            //깊은 복사 사용
            setIsDetailData(JSON.parse(JSON.stringify(selectedCompany)));
        }
    }, [isEditMode, selectedCompany])

    // 로딩 중이면 로딩 메시지 표시
    if (loading) {
        return (
            <Layout title="포트폴리오">
                <main className="wrap">
                    <div style={{ padding: '20px', textAlign: 'center' }}>
                        포트폴리오 데이터를 로딩중입니다...
                    </div>
                </main>
            </Layout>
        );
    }

    return (
        <Layout title="포트폴리오">
            <main className="wrap">
                <section className="menu-section">
                    <div className="menu-box">
                        {companyList.map(company => (
                            <PortfolioMenu key={company.id} data={company} isSelected={selectedCompany?.id === company.id} onClick={() => { setSelectedCompany(company); setIsDetailData(company); }} />
                        ))}
                    </div>
                    {isLoggedIn() && <div className="button-section">
                        <div className="button-box">
                            <button onClick={() => deleteItem()}>삭제</button>
                            {isEditMode ? (<button onClick={() => setIsEditMode(false)}>취소</button>) : (<button onClick={() => setIsEditMode(true)}>수정</button>)}
                            <button onClick={() => setIsModalOpen(true)}>추가</button>
                        </div>
                    </div>}
                </section>
                <section className="cont-section">
                    <div className="cont-box">
                        {isLoggedIn() && isEditMode ? (<EditPortfolioProject data={isDetailData} />) : (<PortfolioProject data={selectedCompany} />)}
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
