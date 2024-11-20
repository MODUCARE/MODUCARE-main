from locust import HttpUser, task, between


class ScalpDiagnosisUser(HttpUser):
    wait_time = between(7, 8)  # 요청 간의 대기 시간 설정

    host = "http://localhost:5050"

    @task
    def diagnose_scalp(self):
        # 테스트용 이미지 URL
        image_url = "https://moducare.s3.ap-northeast-2.amazonaws.com/uploads/HeadSkinTest.jpg"

        # JSON 형태로 데이터 전송
        payload = {"img": image_url}

        response = self.client.post("/ai", json=payload)

        # 응답 상태 코드가 200이 아닌 경우 테스트 실패
        if response.status_code != 200:
            print(f"Error: {response.status_code}")
            response.raise_for_status()

        # 성공적인 응답이 들어오면 내용 출력 (디버깅 용)
        print(response.json())
