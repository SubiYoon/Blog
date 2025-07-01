import { $axios } from '/src/api'
import { useEffect } from 'react';
import { useHistory } from '@docusaurus/router';

async function loadDocsOwnerInfo() {
    const response = await $axios.get('/info/ABCD');
    return response.data.memberInfo;       // memberInfo 반환
}

export default function MyReactPage() {
    const history = useHistory();

    useEffect(() => {
        $axios.get(`/info/ABCD/notion`).then((response) => {
            window.open(response.data);
        })

        async function fetchMemberInfo() {
            const data = await loadDocsOwnerInfo();
            history.push(data.blogInitPath);
        }

        fetchMemberInfo();
    }, [history]);

    return null;
}
