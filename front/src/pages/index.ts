import { useEffect } from 'react';
import { useHistory } from '@docusaurus/router';

export default function HomeRedirect() {
    const history = useHistory();

    useEffect(() => {
        history.push('/Browser/Cross%20Origin%20Resource%20Sharing');
    }, [history]);
}
