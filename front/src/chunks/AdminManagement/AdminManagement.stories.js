import AdminManagement from './AdminManagement';
import React from 'react';

export default {
  title: 'components/AdminManagement',
  component: AdminManagement,
};

const AdminManagementTemplate = (args) => <AdminManagement {...args} />;

export const Default = AdminManagementTemplate.bind({});

Default.args = {
  admins: [
    { id: 0, ip: '127.0.0.1', name: 'fortune' },
    { id: 1, ip: '127.0.0.1', name: 'root' },
    { id: 2, ip: '127.0.0.1', name: 'wilder' },
    { id: 3, ip: '127.0.0.1', name: 'hyeon9mak' },
    { id: 4, ip: '127.0.0.1', name: 'grooming' },
    { id: 5, ip: '127.0.0.1', name: 'peter' },
  ],
};
