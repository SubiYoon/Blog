import React from 'react';
import Logout from './Logout';
import { UserProvider } from '../store/globalStore';

// Default implementation, that you can customize
export default function Root({children}) {
  return (
    <UserProvider>
      {children}
      <Logout />
    </UserProvider>
  );
}