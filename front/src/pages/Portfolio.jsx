import Layout from '@theme/Layout'
import '../css/portfolio.css';
import PortfolioMenu from "../components/portfolio/PortfolioMenu";
import { useEffect, useState } from 'react'
import PortfolioProject from "../components/portfolio/PortfolioProject";
import Modal from 'react-modal';
import { useUser } from '../store/globalStore'
import EditPortfolioProject from '../components/portfolio/EditPortfolioProject'
import { $axios } from '../api'
import { $confirm } from '../components/ui/SweetAlert'

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
        return backendData.length > 0 ? backendData.map(company => ({
            id: company.companyId,
            name: company.name,
            logoId: company.logoId,
            logo: company.logo,
            date: company.date,
            projects: company.projectList?.map(project => ({
                id: project.projectId,
                companyId: project.companyId,
                name: project.name,
                date: project.date,
                items: project.itemList?.map(item => ({
                    id: item.itemId,
                    projectId: item.projectId,
                    title: item.name,
                    cont: item.cont,
                    imgs: item.imageList?.map(image => ({
                        id: image.imageId,
                        img: image.img
                    })) || []
                })) || []
            })) || []
        })) : [
            {
                id: 0,
                name: '',
                logo: '',
                date: '',
                projects: [
                    {
                        id: 0,
                        companyId: 0,
                        name: '',
                        date: '',
                        items: [
                            {
                                id: 0,
                                projectId: 0,
                                title: '',
                                cont: '',
                                imgs: [
                                    {
                                        id: 0,
                                        img: ''
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ];
    };

    // EditPortfolioProject에서 데이터가 변경될 때 호출되는 콜백 함수
    const handleDataUpdate = (updatedData) => {
        // companyList에서 해당 회사 데이터 업데이트
        const updatedCompanyList = companyList.map(company => 
            company.id === updatedData.id ? updatedData : company
        );
        setCompanyList(updatedCompanyList);
        
        // 현재 선택된 회사가 업데이트된 회사라면 selectedCompany도 업데이트
        if (selectedCompany && selectedCompany.id === updatedData.id) {
            setSelectedCompany(updatedData);
        }
        
        // isDetailData도 업데이트
        setIsDetailData(updatedData);
    };

    // 포트폴리오 데이터 가져오기
    const fetchPortfolioData = async () => {
        try {
            setLoading(true);
            var response;
            const alias = localStorage.getItem("USER_ALIAS");
            if (alias !== null && alias !== undefined) {
                response = await $axios.get('/portfolio');
            } else {
                const split = window.location.href.split('#')
                response = await $axios.get('/info/portfolio/' + split[split.length - 1]);
            }
            const transformedData = transformPortfolioData(response.data);
            setCompanyList(transformedData);

            return transformedData;
        } catch (error) {
            console.error('포트폴리오 데이터 로딩 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    function insertCompany() {
        //여기에 신규 추가 저장 로직 추가
        const formData = new FormData();

        formData.append('companyName', name)
        formData.append('date', startDate + ' ~ ' + endDate)
        formData.append('logo', logoFile);

        $axios.post('/portfolio/company', formData).then((response) => {
            fetchPortfolioData().then(result => {
                if (result.length > 0 && selectedCompany) {
                    setSelectedCompany(result[result.length - 1]);
                    setIsDetailData(result[result.length - 1]);
                }

                setIsModalOpen(false)
            });
        });
    }

    async function deleteCompany(id){
        if (await $confirm('정말 삭제하시겠습니까?', '삭제후 되돌릴 수 없습니다.', 'question', '삭제', '취소')) {
            $axios.delete(`/portfolio/company/${id}`).then((response) => {
                fetchPortfolioData().then(result => {
                    // 첫 번째 회사를 기본 선택
                    if (result.length > 0 && selectedCompany) {
                        setSelectedCompany(result[0]);
                        setIsDetailData(result[0]);
                    }
                });
            })
        }
    }


    // 컴포넌트 마운트 시 데이터 로드
    useEffect(() => {
        fetchPortfolioData().then(result => {
            // 첫 번째 회사를 기본 선택
            if (result.length > 0 && !selectedCompany) {
                setSelectedCompany(result[0]);
                setIsDetailData(result[0]);
            }
        });
    }, []);

    useEffect(() => {
        if (selectedCompany) {
            //깊은 복사 사용 - 편집 모드 시작할 때만 복사
            if (isEditMode) {
                setIsDetailData(JSON.parse(JSON.stringify(selectedCompany)));
            }
        }
    }, [selectedCompany]) // isEditMode를 의존성에서 제거

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
                            <button onClick={() => deleteCompany(selectedCompany.id)}>삭제</button>
                            {isEditMode ? (<button onClick={() => setIsEditMode(false)}>취소</button>) : (<button onClick={() => setIsEditMode(true)}>수정</button>)}
                            <button onClick={() => setIsModalOpen(true)}>추가</button>
                        </div>
                    </div>}
                </section>
                <section className="cont-section">
                    <div className="cont-box">
                        {isLoggedIn() && isEditMode ? (<EditPortfolioProject data={isDetailData} onDataUpdate={handleDataUpdate} />) : (<PortfolioProject data={selectedCompany} />)}
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
                        <button onClick={() => insertCompany()}>저장</button>
                        <button onClick={() => setIsModalOpen(false)}>닫기</button>
                    </div>
                </Modal>
            </main>
        </Layout>
    )
}
