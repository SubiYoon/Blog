import { $axios } from '/src/api'
import { useEffect, useState } from 'react';
import { useHistory } from '@docusaurus/router';

export default function MyReactPage() {
    const history = useHistory();
    const [initPath, setInitPath] = useState();

    useEffect(() => {
        $axios.get(`/info/ABCD/notion`).then((response) => {
            window.open(response.data);
        })

        $axios.get(`/info/ABCD`).then((response) => {
            setInitPath(response.data.blogInitPath);
        })

        history.push(initPath);
    }, [initPath]);

    return null;
}
