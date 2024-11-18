# embedding_server.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from transformers import AutoModel, AutoTokenizer
import numpy as np

# FastAPI 애플리케이션 생성
app = FastAPI()

# BERT 모델과 토크나이저 로드
model_name = "sentence-transformers/all-MiniLM-L6-v2"  # 경량화된 임베딩 모델
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModel.from_pretrained(model_name)

def get_embedding(text):
    # 텍스트를 벡터화하는 함수
    inputs = tokenizer(text, return_tensors="pt", padding=True, truncation=True)
    outputs = model(**inputs)
    embeddings = outputs.last_hidden_state[:, 0, :].detach().numpy()
    normalized_vector = (embeddings[0] - np.min(embeddings[0])) / (np.max(embeddings[0]) - np.min(embeddings[0])) * 2 - 1  # -1 ~ 1로 변환

    return normalized_vector.tolist()

# 요청 데이터 모델 정의
class EmbedRequest(BaseModel):
    text: str

@app.post("/embed")
async def embed(request: EmbedRequest):
    """
    텍스트를 입력받아 임베딩 벡터를 반환하는 API
    """
    if not request.text:
        raise HTTPException(status_code=400, detail="No text provided")

    embedding = get_embedding(request.text)
    return embedding


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)