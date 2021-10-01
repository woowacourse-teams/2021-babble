import './NameList.scss';

import { Tag, TagErasable } from '..';

import PropTypes from 'prop-types';
import React from 'react';

const NameList = ({ list, erasable, onClickNames = null }) => {
  return (
    <div className='name-list-container'>
      {list.map((item) =>
        erasable ? (
          <TagErasable onClickTagName={onClickNames} key={item.id}>
            {item.name}
          </TagErasable>
        ) : (
          <Tag onClickTagName={onClickNames} key={item.id}>
            {item.name}
          </Tag>
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
  onClickNames: PropTypes.func,
};

export default NameList;
