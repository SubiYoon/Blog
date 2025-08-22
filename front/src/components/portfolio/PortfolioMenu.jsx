export default function PortfolioMenu({data, isSelected, onClick}) {
    return (
        <div className={`title-wrap ${isSelected ? 'active' : ''}`} onClick={onClick}>
            <div className="cont-box">
                <div className="image-box">
                    <img
                        src={`/static${data.logo}`} //TODO: 차후 운영 배포시 주석해제
                        // src={`http://localhost:8903/static${data.logo}`} //TODO: 차후 운영 배포시 주석
                        alt={data.name}
                    />
                </div>
                <div className="text-box">
                    <h2>{data.name}</h2>
                    <span>{data.date}</span>
                </div>
            </div>
        </div>
    )
}
