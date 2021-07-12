import PropTypes from 'prop-types';
import React from 'react';

const Logo = ({ width = 100 }) => {
  const height = width * (146 / 388);
  return (
    <svg width={width} height={height} viewBox='0 0 388 146' fill='none'>
      <path
        d='M37.4998 145.3C29.8332 145.3 24.3999 141.933 21.1999 135.2V144.5H0V78.6L21.1999 75.5V103.8C22.8666 100.267 25.0332 97.7 27.6999 96.1C30.3665 94.5 33.5999 93.7 37.3998 93.7C41.8665 93.7 45.9665 94.8333 49.6998 97.1C53.4998 99.3 56.4664 102.367 58.5998 106.3C60.7997 110.233 61.8997 114.633 61.8997 119.5C61.8997 124.3 60.7664 128.667 58.4998 132.6C56.2998 136.533 53.2998 139.633 49.4998 141.9C45.7665 144.167 41.7665 145.3 37.4998 145.3ZM31.0999 129.5C33.8332 129.5 36.1665 128.5 38.0998 126.5C40.0332 124.5 40.9998 122.167 40.9998 119.5C40.9998 116.7 39.9998 114.333 37.9998 112.4C36.0665 110.467 33.7665 109.5 31.0999 109.5C28.3665 109.5 26.0332 110.467 24.0999 112.4C22.1666 114.267 21.1999 116.5 21.1999 119.1V119.5C21.1999 122.3 22.1666 124.667 24.0999 126.6C26.0999 128.533 28.4332 129.5 31.0999 129.5Z'
        fill='black'
      />
      <path
        d='M91.6262 145.3C87.2929 145.3 83.2262 144.167 79.4262 141.9C75.6929 139.567 72.6929 136.433 70.4263 132.5C68.1596 128.5 67.0263 124.067 67.0263 119.2C67.0263 114.4 68.1596 110.067 70.4263 106.2C72.6929 102.267 75.7262 99.2 79.5262 97C83.3262 94.8 87.3595 93.7 91.6262 93.7C95.0928 93.7 98.2928 94.6667 101.226 96.6C104.226 98.5333 106.493 101.433 108.026 105.3V93.7H129.026V144.5H108.026V135.2C104.693 141.933 99.2261 145.3 91.6262 145.3ZM98.1261 129.5C100.793 129.5 103.059 128.567 104.926 126.7C106.859 124.767 107.893 122.5 108.026 119.9V119.1C107.893 116.433 106.826 114.167 104.826 112.3C102.893 110.433 100.659 109.5 98.1261 109.5C95.3262 109.5 92.9262 110.5 90.9262 112.5C88.9928 114.433 88.0262 116.767 88.0262 119.5C88.0262 122.3 89.0262 124.667 91.0262 126.6C93.0262 128.533 95.3928 129.5 98.1261 129.5Z'
        fill='black'
      />
      <path
        d='M176.855 145.3C169.188 145.3 163.755 141.933 160.555 135.2V144.5H139.355V78.6L160.555 75.5V103.8C162.221 100.267 164.388 97.7 167.055 96.1C169.721 94.5 172.955 93.7 176.755 93.7C181.221 93.7 185.321 94.8333 189.055 97.1C192.855 99.3 195.821 102.367 197.955 106.3C200.155 110.233 201.255 114.633 201.255 119.5C201.255 124.3 200.121 128.667 197.855 132.6C195.655 136.533 192.655 139.633 188.855 141.9C185.121 144.167 181.121 145.3 176.855 145.3ZM170.455 129.5C173.188 129.5 175.521 128.5 177.455 126.5C179.388 124.5 180.355 122.167 180.355 119.5C180.355 116.7 179.355 114.333 177.355 112.4C175.421 110.467 173.121 109.5 170.455 109.5C167.721 109.5 165.388 110.467 163.455 112.4C161.521 114.267 160.555 116.5 160.555 119.1V119.5C160.555 122.3 161.521 124.667 163.455 126.6C165.455 128.533 167.788 129.5 170.455 129.5Z'
        fill='black'
      />
      <path
        d='M246.581 145.3C238.914 145.3 233.481 141.933 230.281 135.2V144.5H209.081V78.6L230.281 75.5V103.8C231.948 100.267 234.114 97.7 236.781 96.1C239.448 94.5 242.681 93.7 246.481 93.7C250.948 93.7 255.048 94.8333 258.781 97.1C262.581 99.3 265.548 102.367 267.681 106.3C269.881 110.233 270.981 114.633 270.981 119.5C270.981 124.3 269.848 128.667 267.581 132.6C265.381 136.533 262.381 139.633 258.581 141.9C254.848 144.167 250.848 145.3 246.581 145.3ZM240.181 129.5C242.914 129.5 245.248 128.5 247.181 126.5C249.114 124.5 250.081 122.167 250.081 119.5C250.081 116.7 249.081 114.333 247.081 112.4C245.148 110.467 242.848 109.5 240.181 109.5C237.448 109.5 235.114 110.467 233.181 112.4C231.248 114.267 230.281 116.5 230.281 119.1V119.5C230.281 122.3 231.248 124.667 233.181 126.6C235.181 128.533 237.514 129.5 240.181 129.5Z'
        fill='black'
      />
      <path
        d='M296.207 145.3C290.541 145.3 286.141 143.533 283.007 140C279.941 136.4 278.407 131.5 278.407 125.3V78.6L299.507 75.5V123.8C299.507 125.667 299.941 127.033 300.807 127.9C301.741 128.7 303.341 129.1 305.607 129.1C307.274 129.1 308.907 128.7 310.507 127.9L311.207 141.9C306.741 144.167 301.741 145.3 296.207 145.3Z'
        fill='black'
      />
      <path
        d='M342.3 145.3C336.767 145.3 331.833 144.2 327.5 142C323.233 139.733 319.9 136.667 317.5 132.8C315.1 128.867 313.9 124.433 313.9 119.5C313.9 114.433 315.134 109.933 317.6 106C320.067 102.067 323.4 99.0333 327.6 96.9C331.8 94.7667 336.467 93.7 341.6 93.7C346.533 93.7 350.733 94.8 354.2 97C357.733 99.1333 360.367 102.067 362.1 105.8C363.833 109.467 364.7 113.6 364.7 118.2C364.7 119.4 364.633 120.267 364.5 120.8L334.5 124.7C335.3 127.033 336.6 128.733 338.4 129.8C340.2 130.8 342.567 131.3 345.5 131.3C350.033 131.3 354.933 130.133 360.2 127.8L362.7 141.2C356.5 143.933 349.7 145.3 342.3 145.3ZM349.2 112.1C348.133 107.9 345.6 105.8 341.6 105.8C339.2 105.8 337.3 106.7 335.9 108.5C334.5 110.3 333.767 112.633 333.7 115.5L349.2 112.1Z'
        fill='black'
      />
      <path
        d='M318.077 71.4959L318.673 66.9829L319.291 62.3005L314.652 61.4165L294.715 57.6177L286.253 24.4187L307.742 6.46943L370.239 5.065L381.52 19.458L370.672 51.1347L337.411 63.3149L337.294 63.3575L337.18 63.4059L318.077 71.4959Z'
        stroke='#FF005C'
        stroke-width='10'
      />
    </svg>
  );
};

Logo.propTypes = {
  width: PropTypes.number,
};

export default Logo;
