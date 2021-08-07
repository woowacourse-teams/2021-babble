import { Caption2, Subtitle3 } from '../../core/Typography';

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
      <Subtitle3>Default Tag</Subtitle3>
      <br />
      <Tag {...args}>
        <Caption2>실버</Caption2>
      </Tag>
    </div>
    <br />
    <div style={{ margin: '1rem' }}>
      <Subtitle3>Erasable Tag</Subtitle3>
      <br />
      <TagErasable {...args}>
        <Caption2>실버</Caption2>
      </TagErasable>
    </div>
  </>
);

export const General = TagTemplate.bind({});
