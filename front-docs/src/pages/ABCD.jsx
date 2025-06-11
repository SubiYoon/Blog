import { $axios } from '../api'
import { useEffect } from 'react';
import { useHistory } from '@docusaurus/router';

export default function MyReactPage() {
    const history = useHistory();

    useEffect(() => {
        $axios.get(`http://localhost:8903/info/ABCD/notion`).then((response) => {
            window.open(response.data);
        })

        history.push('/Browser/Cross%20Origin%20Resource%20Sharing');
    }, [history]);

    return null;
}