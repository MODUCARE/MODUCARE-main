const handleAirPhrase = (airCondition: number): string => {
  if (airCondition < 0 || airCondition > 4) {
    return '';
  } else if (airCondition === 1) {
    return '좋음';
  } else if (airCondition === 2) {
    return '보통';
  } else if (airCondition === 3) {
    return '나쁨';
  } else {
    return '매우 나쁨';
  }
};

const handleUvPhrase = (uvCondition: number): string => {
  if (uvCondition < 0) {
    return '';
  } else if (uvCondition >= 0 && uvCondition <= 2) {
    return '낮음';
  } else if (uvCondition >= 3 && uvCondition <= 5) {
    return '보통';
  } else if (uvCondition >= 6 && uvCondition <= 7) {
    return '높음';
  } else if (uvCondition >= 8 && uvCondition <= 10) {
    return '매우 높음';
  } else {
    return '위험';
  }
};

export {handleAirPhrase, handleUvPhrase};
