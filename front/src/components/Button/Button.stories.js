import Body1 from '../../core/Typography/Body1';
import Body2 from '../../core/Typography/Body2';
import Caption2 from '../../core/Typography/Caption2';
import React from 'react';
import RoundButton from './RoundButton';
import SquareButton from './SquareButton';

export default {
  title: 'components/Button',
  component: [SquareButton, RoundButton],
};

const SquareButtonTemplate = () => (
  <>
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-around',
        alignItems: 'center',
        width: '54rem',
      }}
    >
      <SquareButton size='tiny'>
        <Caption2>방 생성하기</Caption2>
      </SquareButton>
      <SquareButton size='small'>
        <Caption2>방 생성하기</Caption2>
      </SquareButton>
      <SquareButton size='medium'>
        <Body2>방 생성하기</Body2>
      </SquareButton>
      <SquareButton size='large'>
        <Body1>방 생성하기</Body1>
      </SquareButton>
    </div>
    <br />
    <div style={{ width: '54rem', height: '9rem', padding: '1rem 0' }}>
      <SquareButton size='block'>
        <Body1>방 생성하기</Body1>
      </SquareButton>
    </div>
    <br />
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-around',
        alignItems: 'center',
        width: '54rem',
      }}
    >
      <SquareButton size='tiny' colored={false}>
        <Caption2>방 생성하기</Caption2>
      </SquareButton>
      <SquareButton size='small' colored={false}>
        <Caption2>방 생성하기</Caption2>
      </SquareButton>
      <SquareButton size='medium' colored={false}>
        <Body2>방 생성하기</Body2>
      </SquareButton>
      <SquareButton size='large' colored={false}>
        <Body1>방 생성하기</Body1>
      </SquareButton>
    </div>
    <br />
    <div style={{ width: '54rem', height: '9rem', padding: '1rem 0' }}>
      <SquareButton size='block' colored={false}>
        <Body1>방 생성하기</Body1>
      </SquareButton>
    </div>
  </>
);

const RoundButtonTemplate = () => (
  <>
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-around',
        alignItems: 'center',
        width: '54rem',
      }}
    >
      <RoundButton size='tiny' colored={true}>
        <Caption2>방 생성하기</Caption2>
      </RoundButton>
      <RoundButton size='small' colored={true}>
        <Caption2>방 생성하기</Caption2>
      </RoundButton>
      <RoundButton size='medium' colored={true}>
        <Body2>방 생성하기</Body2>
      </RoundButton>
      <RoundButton size='large' colored={true}>
        <Body1>방 생성하기</Body1>
      </RoundButton>
    </div>
    <br />
    <div style={{ width: '54rem', height: '9rem', padding: '1rem 0' }}>
      <RoundButton size='block' colored={true}>
        <Body1>방 생성하기</Body1>
      </RoundButton>
    </div>
    <br />
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-around',
        alignItems: 'center',
        width: '54rem',
      }}
    >
      <RoundButton size='tiny'>
        <Caption2>방 생성하기</Caption2>
      </RoundButton>
      <RoundButton size='small'>
        <Caption2>방 생성하기</Caption2>
      </RoundButton>
      <RoundButton size='medium'>
        <Body2>방 생성하기</Body2>
      </RoundButton>
      <RoundButton size='large'>
        <Body1>방 생성하기</Body1>
      </RoundButton>
    </div>
    <br />
    <div style={{ width: '54rem', height: '9rem', padding: '1rem 0' }}>
      <RoundButton size='block'>
        <Body1>방 생성하기</Body1>
      </RoundButton>
    </div>
  </>
);

export const Square = SquareButtonTemplate.bind({});
Square.args = {};

export const Round = RoundButtonTemplate.bind({});
Round.args = {};
