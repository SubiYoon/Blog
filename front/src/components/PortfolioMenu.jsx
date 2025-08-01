
export default function PortfolioMenu({data, isSelected, onClick}) {
    return (
        <div className={`title-wrap ${isSelected ? 'active' : ''}`} onClick={onClick}>
            <input type="checkbox"/>
            <div className="cont-box">
                <div className="image-box">
                    <img
                        src={data.logo}
                        alt="React Logo"
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
