import { useEffect, useState } from 'react';
import { useHistory } from '@docusaurus/router';
import { $axios } from '/src/api';

export default function HomeRedirect() {
    const history = useHistory();
    const tempJwtToken = localStorage.getItem('DEVSTAT-JWT');

    const [initPath, setInitPath] = useState();

    useEffect(() => {
        if (tempJwtToken) {
            setInitPath('/MarkDown')
        } else {
            $axios.get(`/info/ABCD`).then((response) => {
                setInitPath(response.data.memberInfo.blogInitPath);
            })
        }
        history.push(initPath);
    }, [initPath]);
}
