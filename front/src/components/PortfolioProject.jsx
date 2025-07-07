import Lightbox from 'yet-another-react-lightbox';
import 'yet-another-react-lightbox/styles.css';
import {useState} from "react";

export default function PortfolioProject({data}) {

    const [lightboxIndex, setLightboxIndex] = useState(-1);
    const [lightboxImages, setLightboxImages] = useState([]);

    return (
        <div className="detail-wrap">
            <div className="cont-wrap">
                <div className="cont-title">
                    <div className="image-box">
                        <img
                            src={data.logo}
                            alt="React Logo"
                        />
                    </div>
                    <div className="text-box">
                        <h2>{data.name}</h2>
                        <h2>{data.date}</h2>
                    </div>
                </div>
                <div className="cont-detail">
                    {data.projects.map((project, i) => (
                        <div className={`project-block block-${i}`} key={i}>
                            <div className="project-title">
                                <h2>
                                    {project.name}
                                </h2>
                                <p>{project.date}</p>
                            </div>
                            <div className="project-items">
                                {project.items.map((item, j) => (
                                    <div className={`project-card card-${i}-${j}`} key={j}>
                                        <div className="text-box">
                                            <strong>{item.title}</strong>{item.cont}</div>
                                        <div className="image-box">
                                            {item.imgs.map((items, k) => (
                                                <img key={k} src={items.img} alt="logo" onClick={() => {
                                                    setLightboxImages(item.imgs);
                                                    console.log("item.imags", item.imgs)
                                                    setLightboxIndex(k);
                                                }}/>
                                            ))}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            <div className="project-nav">
                {data.projects.map((project, i) => (
                    <div key={i} className="nav-box">
                        <div className="nav-title-box">
                            <h2 onClick={() => {
                                const target = document.querySelector(`.block-${i}`);
                                target?.scrollIntoView({behavior: 'smooth', block: 'start'});
                            }}>{project.name}</h2>
                        </div>
                        {project.items.map((item, j) => (
                            <div className="nav-cont-box" key={j}>
                                <h3 onClick={() => {
                                    const target = document.querySelector(`.card-${i}-${j}`);
                                    target?.scrollIntoView({ behavior: 'smooth', block: 'start' });
                                }}>{item.title}</h3>
                            </div>
                        ))}
                    </div>
                ))}
            </div>
            <Lightbox
                open={lightboxIndex >= 0}
                close={() => setLightboxIndex(-1)}
                index={lightboxIndex}
                slides={lightboxImages.map(imgObj => ({ src: imgObj.img }))}
                on={{ view: ({ index }) => setLightboxIndex(index) }}
                render={{
                    buttonPrev: lightboxIndex > 0 ? undefined : () => null,
                    buttonNext: lightboxIndex < lightboxImages.length - 1 ? undefined : () => null,

                    slide: ({ slide }) => (
                        <div
                            style={{
                                width: '100%',
                                height: '100%',
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                justifyContent: 'center',
                                overflow: 'hidden',
                            }}
                            onTouchStart={e => e.stopPropagation()}
                            onTouchMove={e => e.stopPropagation()}
                            onTouchEnd={e => e.stopPropagation()}
                            onPointerDown={e => e.stopPropagation()}
                            onPointerMove={e => e.stopPropagation()}
                            onPointerUp={e => e.stopPropagation()}
                            draggable={false}
                        >
                            {/* 메인 이미지 */}
                            <div style={{
                                flex: 1,
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                            }}>
                                <img
                                    src={slide.src}
                                    alt=""
                                    style={{
                                        maxWidth: '100%',
                                        maxHeight: '100%',
                                        userSelect: 'none',
                                        pointerEvents: 'none',
                                    }}
                                />
                            </div>

                            {/* 하단 썸네일 */}
                            <div style={{
                                position: 'absolute',
                                bottom: '20px',
                                left: 0,
                                right: 0,
                                display: 'flex',
                                justifyContent: 'center',
                                gap: '20px',
                                padding: '8px 0',
                            }}>
                                {lightboxImages.map((imgObj, idx) => (
                                    <img
                                        key={idx}
                                        src={imgObj.img}
                                        onClick={() => setLightboxIndex(idx)}
                                        style={{
                                            width: '80px',
                                            height: '80px',
                                            objectFit: 'cover',
                                            borderRadius: '50%',
                                            border: idx === lightboxIndex ? '2px solid white' : '2px solid transparent',
                                            cursor: 'pointer',
                                            flexShrink: 0,
                                        }}
                                    />
                                ))}
                            </div>
                        </div>
                    ),
                }}
            />
        </div>
    )
}
