import './AdminManagement.scss';

import Headline2 from '../../core/Typography/Headline2';
import NameList from '../../components/NameList/NameList';
import PropTypes from 'prop-types';
import React from 'react';

const AdminManagement = ({ admins = [] }) => {
  return (
    <section className='admin-management-container'>
      <Headline2>운영자 목록</Headline2>
      <NameList list={admins} erasable={false} />
    </section>
  );
};

AdminManagement.propTypes = {
  admins: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number,
      ip: PropTypes.string,
      name: PropTypes.string,
    })
  ),
};

export default AdminManagement;
