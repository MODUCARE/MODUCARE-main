# 베이스 이미지를 Python 3.9로 설정
FROM python:3.10.11

# 작업 디렉토리 설정
WORKDIR /app

# 시스템 의존성 설치
RUN apt-get update && apt-get install -y \
    libsm6 libxext6 libxrender-dev \
    && apt-get clean

# 필요한 Python 패키지 설치
COPY requirements.txt /app/
RUN pip install --no-cache-dir -r requirements.txt

# 코드 복사
COPY . /app

# Flask가 실행될 포트를 Docker에 알림
EXPOSE 5050

# 컨테이너가 실행될 때 사용할 기본 명령어 설정
CMD ["python", "app.py"]
