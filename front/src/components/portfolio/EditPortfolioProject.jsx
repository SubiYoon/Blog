import 'yet-another-react-lightbox/styles.css';
import {useState, useEffect} from "react";
import TextareaAutosize from 'react-textarea-autosize';
import { $axios } from '../../api'
import { $confirm } from '../ui/SweetAlert'

export default function EditPortfolioProject({data}) {

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

    //수정 로직 및 세이브 로직
    function saveData(data, gb) {
        const formData = new FormData();

        for (let pair of data.entries()) {

            if (pair[0] === 'date') {
                pair[1] = pair[1].replace('근무중', '');
                pair[1] = pair[1].replace('진행중', '');
            }
            formData.append(pair[0], pair[1]);
            console.log(pair[0], pair[1]);
        }

        if (formData.get('id') === 'new') {
            $axios.post(`/portfolio/${gb}`, formData).then((response) => {
                console.log(response);
            });
        } else {
            $axios.patch(`/portfolio/${gb}/${formData.get('id')}`, formData).then((response) => {
                console.log(response);
            });
        }
    }

    //삭제
    async function deleteItem(id, gb) {
        if (await $confirm('정말 삭제하시겠습니까?', '삭제후 되돌릴 수 없습니다.', 'question', '삭제', '취소')) {
            $axios.delete(`/portfolio/${gb}/${id}`).then((response) => {
            });
        }
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
                                const file = e.target.files?.[0]
                                if (file) {
                                    const reader = new FileReader()
                                    reader.onloadend = () => {
                                        setProjectItems(prev => ({
                                            ...prev,
                                            logo: reader.result,      // base64 미리보기
                                            logoFile: file,            // 실제 파일 저장은 로고파일로 저장
                                        }))
                                    }
                                    reader.readAsDataURL(file)
                                }
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
                                if(projectItems?.logoFile) {
                                    formData.append('logo', projectItems.logoFile);
                                }
                                formData.append('id', projectItems.id);
                                formData.append('companyName', projectItems.name);
                                formData.append('date', projectItems.date);
                                saveData(formData, 'company');

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
                                        saveData(formData, 'project')

                                    }}>저장
                                    </button>
                                    <button onClick={() => deleteItem(project.id, 'project')}>삭제</button>
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
                                                        if (projectItems?.logoFile) {
                                                            formData.append('logo', projectItems.logoFile)
                                                        }
                                                        formData.append('id', item.id)
                                                        formData.append('projectId', item.projectId)
                                                        formData.append('title', item.title)
                                                        formData.append('cont', item.cont)

                                                        item.imgs.forEach((imgObj, index) => {
                                                            if (imgObj.file) {
                                                                // 새 이미지
                                                                formData.append('images', imgObj.file)
                                                            } else {
                                                                // 기존 이미지 URL
                                                                // formData.append('url', imgObj.img)
                                                            }
                                                        })

                                                        saveData(formData, 'item')

                                                    }}>기능 저장
                                                    </button>
                                                    <button onClick={() => deleteItem(item.id, 'item')}>기능 삭제</button>
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
                                                    if (file) {
                                                        const updated = [...projectItems.projects]
                                                        const previewUrl = URL.createObjectURL(file)
                                                        updated[i].items[j].imgs.push({
                                                            img: '',
                                                            preview: previewUrl,
                                                            file: file,
                                                        })
                                                        setProjectItems({ ...projectItems, projects: updated })
                                                    }
                                                }}
                                            />
                                        </div>
                                        <div className="image-box">
                                            {item.imgs.map((items, k) => (
                                                <div key={k} className="image-edit-box">
                                                    <img
                                                        src={items.preview || items.img} // 파일 선택 전에는 원래 이미지, 이후엔 미리보기
                                                        alt="logo"
                                                    />

                                                    <div className="edit-button-box">
                                                        <button
                                                            onClick={() => document.getElementById(`file-${i}-${j}-${k}`).click()}>
                                                            변경
                                                        </button>

                                                        {/* 삭제 버튼 */}
                                                        <button
                                                            onClick={() => {
                                                                const updated = [...projectItems.projects]
                                                                updated[i].items[j].imgs.splice(k, 1)
                                                                setProjectItems({ ...projectItems, projects: updated })
                                                            }}
                                                        >
                                                            삭제
                                                        </button>
                                                    </div>

                                                    {/* 파일 input (숨김) */}
                                                    <input
                                                        type="file"
                                                        accept="image/*"
                                                        id={`file-${i}-${j}-${k}`}
                                                        style={{ display: 'none' }}
                                                        onChange={(e) => {
                                                            const file = e.target.files?.[0]
                                                            if (file) {
                                                                const updated = [...projectItems.projects]
                                                                const previewUrl = URL.createObjectURL(file)
                                                                updated[i].items[j].imgs[k] = {
                                                                    ...updated[i].items[j].imgs[k],
                                                                    img: updated[i].items[j].imgs[k].img, // 유지
                                                                    preview: previewUrl,
                                                                    file: file, // 전송용 원본 파일
                                                                }
                                                                setProjectItems({ ...projectItems, projects: updated })
                                                            }
                                                        }}
                                                    />
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
