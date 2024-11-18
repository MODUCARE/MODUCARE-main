import { createGlobalStyle } from "styled-components";
import PretendardRegular from "./Pretendard-Regular.ttf"; // 경로 수정

const GlobalFont = createGlobalStyle`
    @font-face {
        font-family: 'Pretendard';  // 폰트 패밀리명 수정
        src: local('Pretendard'),
             url(${PretendardRegular}) format('truetype');
        font-weight: 400;
        font-style: normal;
    }

    /* 전역 스타일 적용 */
    * {
        font-family: 'Pretendard', sans-serif;
    }
`;

export default GlobalFont;
