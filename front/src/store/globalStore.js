import React, { createContext, useContext, useState, useEffect } from 'react';

const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState({ alias: null });

  // 로그인 상태 확인 (localStorage에서)
  useEffect(() => {
    const token = localStorage.getItem('DEVSTAT-JWT');
    const savedAlias = localStorage.getItem('USER_ALIAS');
    if (token && savedAlias) {
      setUser({ alias: savedAlias });
    }
  }, []);

  const loginUser = (alias) => {
    setUser({ alias });
    localStorage.setItem('USER_ALIAS', alias);
  };

  const logoutUser = () => {
    setUser({ alias: null });
    localStorage.removeItem('USER_ALIAS');
    localStorage.removeItem('DEVSTAT-JWT');
  };

  const isLoggedIn = () => {
    return user.alias !== null && localStorage.getItem('DEVSTAT-JWT');
  };

  return (
    <UserContext.Provider value={{ user, loginUser, logoutUser, isLoggedIn }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};