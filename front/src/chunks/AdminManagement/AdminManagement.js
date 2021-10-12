import './AdminManagement.scss';

import React, { useEffect, useState } from 'react';

import Headline2 from '../../core/Typography/Headline2';
import { ModalError } from '../../components';
import NameList from '../../components/NameList/NameList';
import PropTypes from 'prop-types';
import { TEST_URL } from '../../constants/api';
import axios from 'axios';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const AdminManagement = () => {
  const [adminList, setAdminList] = useState([]);
  const { openModal } = useDefaultModal();

  const getAdmins = async () => {
    try {
      const response = await axios.get(`${TEST_URL}/api/admins`);
      const admins = response.data;
      setAdminList(admins);
    } catch (error) {
      openModal(<ModalError>{error}</ModalError>);
    }
  };

  useEffect(() => {
    getAdmins();
  }, []);

  return (
    <section className='admin-management-container'>
      <Headline2>운영자 목록</Headline2>
      <NameList list={adminList} erasable={false} />
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
