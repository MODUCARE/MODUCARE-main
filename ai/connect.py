import requests
import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse
from pydantic import BaseModel

app = FastAPI()

# 대상 서버 URL
TARGET_SERVER_URL = "http://TARGET_SERVER_IP:7777/ai"


# 요청 데이터 모델
class ImageRequest(BaseModel):
    img: str


@app.post("/ai")
def handle_request(request: ImageRequest):
    try:
        # 데이터를 JSON으로 변환
        json_data = jsonable_encoder(request)

        # 데이터를 대상 서버로 전달
        response = requests.post(TARGET_SERVER_URL, json=json_data)

        # 대상 서버의 응답을 반환
        return JSONResponse(
            status_code=response.status_code,  # 대상 서버의 상태 코드 사용
            content={
                "result": response.json().get("result"),  # 'result' 값만 추출
                "headType": response.json().get("headType")  # 'headType' 값만 추출
            }
        )
    except requests.RequestException as e:
        # 기타 요청 관련 예외 처리
        raise HTTPException(
            status_code=500,
            detail={
                "error": "Request error",
                "message": str(e)
            }
        )


if __name__ == '__main__':
    uvicorn.run(app, host="0.0.0.0", port=5050)
