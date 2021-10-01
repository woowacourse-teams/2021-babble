import './NameList.scss';

import { Tag, TagErasable } from '..';

import PropTypes from 'prop-types';
import React from 'react';

const NameList = ({ list, erasable, onClickNames }) => {
  return (
    <div className='name-list-container'>
      {list.map((itemName, index) =>
        erasable ? (
          <TagErasable onClickTagName={onClickNames} key={index}>
            {itemName}
          </TagErasable>
        ) : (
          <Tag onClickTagName={onClickNames} key={index}>
            {itemName}
          </Tag>
        )
      )}
    </div>
  );
};

NameList.propTypes = {
  list: PropTypes.arrayOf(PropTypes.string),
  erasable: PropTypes.bool,
  onClickNames: PropTypes.func,
};

export default NameList;
