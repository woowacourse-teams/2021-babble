import Caption2 from '../../core/Typography/Caption2';
import React from 'react';
import Tag from './Tag';
import TagErasable from './TagErasable';

export default {
  title: 'components/Tag',
  component: [Tag, TagErasable],
};

const TagTemplate = (args) => (
  <>
    <div style={{ margin: '1rem' }}>
      <Tag {...args}>
        <Caption2>실버</Caption2>
      </Tag>
    </div>
    <div style={{ margin: '1rem' }}>
      <TagErasable {...args}>
        <Caption2>실버</Caption2>
      </TagErasable>
    </div>
  </>
);

export const General = TagTemplate.bind({});
