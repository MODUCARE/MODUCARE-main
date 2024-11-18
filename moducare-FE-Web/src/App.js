import {
  HeaderLogo,
  BodyLogo,
  MainCharacter,
  MainHead,
  QR,
} from "./assets/index";
import "./App.css";
import GlobalFont from "./assets/fonts/fonts";

function App() {
  return (
    <div className="App">
      <GlobalFont />
      <header className="App-header">
        <img src={HeaderLogo} className="App-logo" alt="logo" />
        <h6>STUDIO B203</h6>
      </header>
      <main className="App-body">
        <h3>머리가 걱정이신 여러분들을 위해</h3>
        <img src={BodyLogo} className="Body-logo" alt="logo" />
        <h3>모두케어가 사진을 통해 당신의 두피 상태를 확인해드릴게요</h3>
        <div className="App-QRBox">
          <img src={QR} className="App-QRBox-QR-img" alt="logo" />
          <div className="App-QRBox-text">
            <h5>두피 상태를 간단히 체크해봐요!</h5>
            <h5>Downlad App</h5>
          </div>
        </div>
        <img src={MainCharacter} className="Main-character" alt="logo" />
        <img src={MainHead} className="Main-head" alt="logo" />
      </main>
    </div>
  );
}

export default App;
