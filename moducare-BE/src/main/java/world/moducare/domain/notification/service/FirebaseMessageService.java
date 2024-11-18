package world.moducare.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseMessageService {

    private static final Logger log = LoggerFactory.getLogger(FirebaseMessageService.class);

    public boolean sendMessage(String fcmToken) {

        // 메시지가 전송 여부 플래그
        boolean isMessageSent = false;

        Message message = Message.builder()
                .setToken(fcmToken)
                .putData("title", "챌린지 알림")
                .putData("body", "오늘의 챌린지를 시작해봐요!!")
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            isMessageSent = true;
            log.info("Message sent successfully");

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            log.info("Failed to send message");
        }
        return isMessageSent;
    }
}
