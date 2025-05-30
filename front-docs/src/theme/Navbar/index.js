// src/theme/Navbar/index.js
import React, { useEffect, useState } from 'react';
import OriginalNavbar from '@theme-original/Navbar';
import axios from 'axios';

export default function Navbar(props) {
    debugger
    const [items, setItems] = useState([]);

    useEffect(() => {
        debugger
        // 예시: JSON 형태의 메뉴 구조를 가진 API 호출
        axios.get('/api/navbar') // 또는 외부 주소
            .then((res) => {
                // setItems(res.data.items);
                setItems(
                       [
                            { "label": "홈", "to": "/" },
                            { "label": "문서", "to": "/docs/intro" },
                            { "label": "GitHub", "href": "https://github.com" }
                        ]
                )
            })
            .catch((err) => {
                console.error('Navbar API error:', err);
            });
    }, []);

    // 로딩 중이면 기존 설정을 그대로 보여줌
    if (items.length === 0) {
        debugger
        return <OriginalNavbar {...props} />;
    }

    return (
        <OriginalNavbar
            {...props}
            items={items}
        />
    );
}
