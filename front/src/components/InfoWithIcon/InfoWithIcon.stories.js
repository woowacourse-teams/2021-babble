import { AiOutlineEye } from '@react-icons/all-files/ai/AiOutlineEye';
import { AiOutlineLike } from '@react-icons/all-files/ai/AiOutlineLike';
import { BiTimeFive } from '@react-icons/all-files/bi/BiTimeFive';
import { BsPersonFill } from '@react-icons/all-files/bs/BsPersonFill';
import InfoWithIcon from './InfoWithIcon';
import React from 'react';

export default {
  title: 'components/InfoWithIcon',
  component: InfoWithIcon,
};

const InfoWithIconTemplate = (args) => (
  <div>
    <InfoWithIcon {...args} />
  </div>
);

export const Views = InfoWithIconTemplate.bind({});

Views.args = {
  icon: <AiOutlineEye size='16px' />,
  content: '18792',
};

export const Like = InfoWithIconTemplate.bind({});

Like.args = {
  icon: <AiOutlineLike size='15px' />,
  content: '10000',
};

export const Person = InfoWithIconTemplate.bind({});

Person.args = {
  icon: <BsPersonFill size='15px' />,
  content: '그루밍',
};

export const Time = InfoWithIconTemplate.bind({});

Time.args = {
  icon: <BiTimeFive size='15px' />,
  content: '10/18 09:23',
};
