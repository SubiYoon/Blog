import React from 'react';
import { LogIn, LogOut } from 'lucide-react'
import './styles.css';
import { $axios } from '../../api'
import { useHistory } from '@docusaurus/router'
import { useUser } from '../../store/globalStore'

const Logout = () => {
    const { logoutUser, isLoggedIn } = useUser();
    const history = useHistory();

    const logout = () => {
        $axios.post('/logout').then(() => {
            history.push('/')
            logoutUser()
        });
    }

    const login = () => {
        history.push('/login')
    }
    return (
        <>
            {!isLoggedIn() ?
                <button
                    className="navbar__search-button"
                    title="Login"
                    aria-label="Login"
                    onClick={login}
                >
                    <LogIn size={18} />
                </button> : null
            }
            {isLoggedIn() ?
                <button
                    className="navbar__search-button"
                    title="Logout"
                    aria-label="Logout"
                    onClick={logout}
                >
                    <LogOut size={18} />
                </button> : null
            }
        </>
    );
};

export default Logout;
