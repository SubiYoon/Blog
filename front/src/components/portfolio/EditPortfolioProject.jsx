import 'yet-another-react-lightbox/styles.css';
import {useState, useEffect} from "react";
import TextareaAutosize from 'react-textarea-autosize';
import { $axios } from '../../api'
import { $alert, $confirm } from '../ui/SweetAlert'

export default function EditPortfolioProject({data, onDataUpdate}) {

    const [projectItems, setProjectItems] = useState([]);

    //프로젝트 추가
    function projectAdd() {
        const updated = { ...projectItems };
        const newProjectId = updated.projects.length;

        updated.projects.push({
            id: 'new',
            companyId: updated.id,
            date: '',
            items: [],
            name: '',
        });

        setProjectItems(updated);
        console.log("project", projectItems)

        const updated2 = [...projectItems.projects]
        cardAdd(updated2, newProjectId)

        // setTimeout(() => {
        //     const target = document.querySelector(`.block-${newProjectId}`);
        //     target?.scrollIntoView({ behavior: 'smooth', block: 'start' });
        // }, 0);
    }

    //카드 추가
    function cardAdd(item, i) {
        const j = item[i].items.length;

        console.log(item)
        item[i].items.push({
            id: 'new',
            projectId: item[i].id,
            title: '',
            cont: '',
            imgs: [],
        });

        setProjectItems({ ...projectItems, projects: item });

        // 다음 실행
        setTimeout(() => {
            const target = document.querySelector(`.card-${i}-${j}`);
            if (target) {
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        }, 0);
    }


    // 이미지 자동 업로드 함수
    function uploadImageToServer(data, file, callback) {
        const formData = new FormData();
        formData.append('contentId', data.contentId);
        formData.append('contentGb', data.contentGb);
        formData.append('image', file);

        $axios.post('/portfolio/image', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then((response) => {
            console.log('이미지 업로드 성공:', response.data);
            if (callback) callback(response.data); // 서버에서 반환된 이미지 정보 (id, path 포함)
        }).catch((error) => {
            console.error('이미지 업로드 실패:', error);
            if (callback) callback(null);
        });
    }

    // 이미지 삭제 함수
    function deleteImageFromServer(imageId, callback) {
        $axios.delete(`/portfolio/image/${imageId}`)
            .then((response) => {
                console.log('이미지 삭제 성공:', response.data);
                if (callback) callback(true);
            }).catch((error) => {
                console.error('이미지 삭제 실패:', error);
                if (callback) callback(false);
            });
    }


    useEffect(() => {
        // data.projects 안에 있는 items를 펼쳐서 가져오기
     setProjectItems(data);
    }, [data]);

    return (
        <div className="detail-wrap">
            <div className="cont-wrap">
                <div className="cont-title">
                    <div className="image-box" onClick={() => document.getElementById('logo-upload')?.click()}
                         style={{ cursor: 'pointer' }}>
                        <img
                            // src={`/static${projectItems.logo}`} //TODO: 차후 운영 배포시 주석해제
                            src={`http://localhost:8903/static${projectItems.logo}`} //TODO: 차후 운영 배포시 주석
                            alt="로고 미리보기"
                        />
                        <input
                            id="logo-upload"
                            type="file"
                            accept="image/*"
                            style={{ display: 'none' }}
                            onChange={(e) => {
                                deleteImageFromServer(projectItems.logoId, (response) => {
                                    const file = e.target.files?.[0]
                                    if (file) {
                                        const data = {
                                            contentId: projectItems.id,
                                            contentGb: 'COMPANY'
                                        }
                                        // 즉시 서버에 업로드
                                        uploadImageToServer(data, file, (response) => {
                                            if (response) {
                                                const updatedData = {
                                                    ...projectItems,
                                                    logo: response.img,      // 서버에서 반환된 이미지 경로
                                                    logoId: response.imageId, // 서버에서 반환된 이미지 ID
                                                };
                                                setProjectItems(updatedData);
                                                // 부모 컴포넌트에 변경사항 전달
                                                if (onDataUpdate) onDataUpdate(updatedData);
                                            }
                                        })
                                    }
                                })
                            }}
                        />
                    </div>
                    <div className="text-box">
                        <input
                            type="text"
                            value={projectItems.name}
                            onChange={e =>
                                setProjectItems(prev => ({
                                    ...prev,
                                    name: e.target.value,
                                }))
                            }
                        />
                        <div className="date-box">
                            <input
                                type="date"
                                value={projectItems.date?.split(' ~ ')[0] || ''}
                                onChange={e => {
                                    const [_, end] = projectItems.date?.split(' ~ ') || []
                                    setProjectItems(prev => ({
                                        ...prev,
                                        date: `${e.target.value} ~ ${end || ''}`,
                                    }))
                                }}
                            />
                            <span> ~ </span>
                            <input
                                type="date"
                                value={projectItems.date?.split(' ~ ')[1] || ''}
                                onChange={e => {
                                    const [start] = projectItems.date?.split(' ~ ') || []
                                    setProjectItems(prev => ({
                                        ...prev,
                                        date: `${start || ''} ~ ${e.target.value}`,
                                    }))
                                }}
                            />
                        </div>
                        <div className="edit-button-box">
                            <button onClick={() => {
                                const formData = new FormData();
                                formData.append('id', projectItems.id);
                                formData.append('companyName', projectItems.name);
                                formData.append('date', projectItems.date);
                                
                                // 로고는 이미 서버에 업로드되었으므로 경로만 전송
                                if (projectItems.logoId) {
                                    formData.append('logoId', projectItems.logoId);
                                }
                                
                                // 회사 정보 저장 후 UI 즉시 업데이트
                                if (formData.get('id') === 'new') {
                                    formData.delete("id")
                                    $axios.post(`/portfolio/company`, formData).then((response) => {
                                        console.log('회사 등록 성공:', response);
                                        // 새로 등록된 회사 ID로 업데이트
                                        const updatedData = {
                                            ...projectItems,
                                            id: response.data.companyId || projectItems.id
                                        };
                                        setProjectItems(updatedData);
                                        // 부모 컴포넌트에 변경사항 전달
                                        if (onDataUpdate) onDataUpdate(updatedData);
                                        $alert('성공', '회사 정보가 등록되었습니다.', 'success');
                                    }).catch((error) => {
                                        console.error('회사 등록 실패:', error);
                                        $alert('실패', '회사 등록에 실패했습니다.', 'error');
                                    });
                                } else {
                                    $axios.patch(`/portfolio/company/${formData.get('id')}`, formData).then((response) => {
                                        console.log('회사 수정 성공:', response);
                                        // 부모 컴포넌트에 현재 상태 전달
                                        if (onDataUpdate) onDataUpdate(projectItems);
                                        $alert('성공', '회사 정보가 수정되었습니다.', 'success');
                                    }).catch((error) => {
                                        console.error('회사 수정 실패:', error);
                                        $alert('실패', '회사 수정에 실패했습니다.', 'error');
                                    });
                                }

                            }}>저장</button>
                        </div>
                    </div>
                </div>
                <div className="cont-detail">
                    {projectItems.projects?.map((project, i) => (
                        <div className={`project-block block-${i}`} key={i}>
                            <div className="project-title">
                                <input
                                    type="text"
                                    value={project.name}
                                    onChange={(e) => {
                                        const updated = [...projectItems.projects]
                                        updated[i].name = e.target.value
                                        setProjectItems({ ...projectItems, projects: updated })
                                    }}
                                />
                                <div className="date-box">
                                    <input
                                        type="date"
                                        value={project.date?.split(' ~ ')[0] || ''}
                                        onChange={(e) => {
                                            const [_, end] = project.date?.split(' ~ ') || []
                                            const updated = [...projectItems.projects]
                                            updated[i].date = `${e.target.value} ~ ${end || ''}`
                                            setProjectItems({ ...projectItems, projects: updated })
                                        }}
                                    />
                                    <input
                                        type="date"
                                        value={project.date?.split(' ~ ')[1] || ''}
                                        onChange={(e) => {
                                            const [start] = project.date?.split(' ~ ') || []
                                            const updated = [...projectItems.projects]
                                            updated[i].date = `${start || ''} ~ ${e.target.value}`
                                            setProjectItems({ ...projectItems, projects: updated })
                                        }}
                                    />
                                </div>
                                <div className="edit-button-box">
                                    <button onClick={() => {
                                        const formData = new FormData()
                                        formData.append('id', project.id)
                                        formData.append('companyId', project.companyId)
                                        formData.append('projectName', project.name)
                                        formData.append('date', project.date)
                                        
                                        // 프로젝트 저장 후 UI 즉시 업데이트
                                        if (formData.get('id') === 'new') {
                                            $axios.post(`/portfolio/project`, formData).then((response) => {
                                                console.log('프로젝트 등록 성공:', response);
                                                // 새로 등록된 프로젝트 ID로 업데이트
                                                const updated = [...projectItems.projects]
                                                updated[i].id = response.data.projectId || updated[i].id;
                                                setProjectItems({ ...projectItems, projects: updated });
                                                $alert('성공', '프로젝트가 등록되었습니다.', 'success');
                                            }).catch((error) => {
                                                console.error('프로젝트 등록 실패:', error);
                                                $alert('실패', '프로젝트 등록에 실패했습니다.', 'error');
                                            });
                                        } else {
                                            $axios.patch(`/portfolio/project/${formData.get('id')}`, formData).then((response) => {
                                                console.log('프로젝트 수정 성공:', response);
                                                // UI는 이미 사용자 입력으로 업데이트되어 있으므로 추가 작업 불필요
                                                $alert('성공', '프로젝트가 수정되었습니다.', 'success');
                                            }).catch((error) => {
                                                console.error('프로젝트 수정 실패:', error);
                                                $alert('실패', '프로젝트 수정에 실패했습니다.', 'error');
                                            });
                                        }

                                    }}>저장
                                    </button>
                                    <button onClick={async () => {
                                        if (await $confirm('정말 삭제하시겠습니까?', '삭제후 되돌릴 수 없습니다.', 'question', '삭제', '취소')) {
                                            $axios.delete(`/portfolio/project/${project.id}`).then((response) => {
                                                console.log('프로젝트 삭제 성공:', response);
                                                // UI에서 해당 프로젝트 즉시 제거
                                                const updated = [...projectItems.projects]
                                                updated.splice(i, 1);
                                                setProjectItems({ ...projectItems, projects: updated });
                                                $alert('성공', '프로젝트가 삭제되었습니다.', 'success');
                                            }).catch((error) => {
                                                console.error('프로젝트 삭제 실패:', error);
                                                $alert('실패', '프로젝트 삭제에 실패했습니다.', 'error');
                                            });
                                        }
                                    }}>삭제</button>
                                </div>
                            </div>

                            <div className="project-items">
                                {project.items.map((item, j) => (
                                    <div className={`project-card card-${i}-${j}`} key={j}>
                                        <div className="text-box">
                                            <div className="project-items-title-box">
                                                <input
                                                    type="text"
                                                    value={item.title}
                                                    onChange={(e) => {
                                                        const updated = [...projectItems.projects]
                                                        updated[i].items[j].title = e.target.value
                                                        setProjectItems({ ...projectItems, projects: updated })
                                                    }}
                                                />
                                                <div className="edit-button-box">
                                                    <button onClick={() => {
                                                        const formData = new FormData()
                                                        formData.append('id', item.id)
                                                        formData.append('projectId', item.projectId)
                                                        formData.append('title', item.title)
                                                        formData.append('cont', item.cont)

                                                        // 아이템 저장 후 UI 즉시 업데이트
                                                        if (formData.get('id') === 'new') {
                                                            formData.delete("id")
                                                            $axios.post(`/portfolio/item`, formData).then(async (response) => {
                                                                console.log('아이템 등록 성공:', response);
                                                                // 새로 등록된 아이템 ID로 업데이트
                                                                const updated = [...projectItems.projects]
                                                                updated[i].items[j].id = response.data.itemId || updated[i].items[j].id;
                                                                setProjectItems({ ...projectItems, projects: updated });
                                                                await $alert('성공', '기능이 등록되었습니다.', 'success');
                                                                window.location.reload();
                                                            }).catch((error) => {
                                                                console.error('아이템 등록 실패:', error);
                                                                $alert('실패', '기능 등록에 실패했습니다.', 'error');
                                                            });
                                                        } else {
                                                            $axios.patch(`/portfolio/item/${formData.get('id')}`, formData).then((response) => {
                                                                console.log('아이템 수정 성공:', response);
                                                                // UI는 이미 사용자 입력으로 업데이트되어 있으므로 추가 작업 불필요
                                                                $alert('성공', '기능이 수정되었습니다.', 'success');
                                                            }).catch((error) => {
                                                                console.error('아이템 수정 실패:', error);
                                                                $alert('실패', '기능 수정에 실패했습니다.', 'error');
                                                            });
                                                        }

                                                    }}>기능 저장
                                                    </button>
                                                    <button onClick={async () => {
                                                        if (await $confirm('정말 삭제하시겠습니까?', '삭제후 되돌릴 수 없습니다.', 'question', '삭제', '취소')) {
                                                            $axios.delete(`/portfolio/item/${item.id}`).then((response) => {
                                                                console.log('아이템 삭제 성공:', response);
                                                                // UI에서 해당 아이템 즉시 제거
                                                                const updated = [...projectItems.projects]
                                                                updated[i].items.splice(j, 1);
                                                                setProjectItems({ ...projectItems, projects: updated });
                                                                $alert('성공', '기능이 삭제되었습니다.', 'success');
                                                            }).catch((error) => {
                                                                console.error('아이템 삭제 실패:', error);
                                                                $alert('실패', '기능 삭제에 실패했습니다.', 'error');
                                                            });
                                                        }
                                                    }}>기능 삭제</button>
                                                </div>
                                            </div>
                                            <TextareaAutosize
                                                value={item.cont}
                                                onChange={(e) => {
                                                    const updated = [...projectItems.projects]
                                                    updated[i].items[j].cont = e.target.value
                                                    setProjectItems({ ...projectItems, projects: updated })
                                                }}
                                            />
                                        </div>
                                        <div className="image-add-box">
                                            <button
                                                onClick={() => document.getElementById(`new-file-${i}-${j}`).click()}>
                                                이미지 추가
                                            </button>
                                            <input
                                                type="file"
                                                accept="image/*"
                                                id={`new-file-${i}-${j}`}
                                                style={{ display: 'none' }}
                                                onChange={(e) => {
                                                    const file = e.target.files?.[0]
                                                    const data = {
                                                        contentId: item.id,
                                                        contentGb: 'ITEM'
                                                    }
                                                    if (file) {
                                                        // 즉시 서버에 업로드
                                                        uploadImageToServer(data, file, (response) => {
                                                            console.log(response)
                                                            if (response) {
                                                                const updated = [...projectItems.projects]
                                                                updated[i].items[j].imgs.push({
                                                                    id: response.imageId,
                                                                    img: response.img,
                                                                    preview: response.img,
                                                                    uploaded: true
                                                                })
                                                                setProjectItems({ ...projectItems, projects: updated })
                                                            }
                                                        })
                                                    }
                                                }}
                                            />
                                        </div>
                                        <div className="image-box">
                                            {item.imgs.map((item, k) => (
                                                <div key={k} className="image-edit-box">
                                                    <img
                                                        src={`http://localhost:8903/static${item.preview || item.img}`} // 파일 선택 전에는 원래 이미지, 이후엔 미리보기 //TODO: 차후 운영 배포시 주석
                                                        // src={`/static${items.preview || items.img}`} //TODO: 차후 운영 배포시 주석 해제
                                                        alt="logo"
                                                    />
                                                    <div className="edit-button-box">
                                                        {/* 삭제 버튼 - 즉시 서버에 삭제 요청 */}
                                                        <button
                                                            onClick={() => {
                                                                console.log(item)
                                                                if (item.id) {
                                                                    // 서버에서 이미지 삭제
                                                                    deleteImageFromServer(item.id, (success) => {
                                                                        if (success) {
                                                                            // 성공 시 UI에서도 제거
                                                                            const updated = [...projectItems.projects]
                                                                            updated[i].items[j].imgs.splice(k, 1)
                                                                            setProjectItems({ ...projectItems, projects: updated })
                                                                        } else {
                                                                            $alert('실패', '이미지 삭제에 실패했습니다.', 'error')
                                                                        }
                                                                    })
                                                                } else {
                                                                    // ID가 없는 경우 UI에서만 제거 (업로드되지 않은 이미지)
                                                                    const updated = [...projectItems.projects]
                                                                    updated[i].items[j].imgs.splice(k, 1)
                                                                    setProjectItems({ ...projectItems, projects: updated })
                                                                }
                                                            }}
                                                        >
                                                            삭제
                                                        </button>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>

                                    </div>
                                ))}
                                <div className="edit-button-box">
                                    <button onClick={() => {
                                        const updated = [...projectItems.projects]
                                        console.log('u', updated)
                                        cardAdd(updated, i)
                                    }}>기능 추가
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                    <div className="add-project-box">
                        <button onClick={() => {
                            projectAdd()
                        }}>프로젝트 추가
                        </button>
                    </div>
                </div>
            </div>
            <div className="project-nav">
                {data.projects.map((project, i) => (
                    <div key={i} className="nav-box">
                        <div className="nav-title-box">
                            <h2 onClick={() => {
                                const target = document.querySelector(`.block-${i}`)
                                target?.scrollIntoView({ behavior: 'smooth', block: 'start' })
                            }}>{project.name}</h2>
                        </div>
                        {project.items.map((item, j) => (
                            <div className="nav-cont-box" key={j}>
                                <h3 onClick={() => {
                                    const target = document.querySelector(`.card-${i}-${j}`)
                                    target?.scrollIntoView({ behavior: 'smooth', block: 'start' })
                                }}>{item.title}</h3>
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    )
}
