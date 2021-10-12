import './NameList.scss';

import { Tag, TagErasable } from '..';

import PropTypes from 'prop-types';
import React from 'react';

const NameList = ({
  list = [],
  erasable,
  onClickName = () => {},
  onDeleteName = () => {},
}) => {
  return (
    <div className='name-list-container'>
      {!list.length ? (
        <div className='no-data'>조회된 데이터가 없습니다.</div>
      ) : (
        list.map((item) =>
          erasable ? (
            <TagErasable
              onDeleteTag={() => onDeleteName(item.id)}
              onClickTagName={() => onClickName(item.id)}
              key={item.id}
            >
              {item.name}
            </TagErasable>
          ) : (
            <Tag onClickTagName={() => onClickName(item.id)} key={item.id}>
              {item.name}
            </Tag>
          )
        )
      )}
    </div>
  );
};

NameList.propTypes = {
  list: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      name: PropTypes.string.isRequired,
    })
  ),
  erasable: PropTypes.bool,
  onClickName: PropTypes.func,
  onDeleteName: PropTypes.func,
};

export default NameList;
