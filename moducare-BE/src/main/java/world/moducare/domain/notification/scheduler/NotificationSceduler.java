package world.moducare.domain.notification.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.domain.mychallenge.repository.MyChallengeRepository;
import world.moducare.domain.notification.entity.Notification;
import world.moducare.domain.notification.repository.NotificationRepository;
import world.moducare.domain.notification.service.FirebaseMessageService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationSceduler {

    private final MemberService memberService;
    private final MyChallengeRepository myChallengeRepository;
    private final NotificationRepository notificationRepository;
    private final FirebaseMessageService messageService;

    // 매일 오전 9시마다 돌아가는 스케줄러
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void schedule() {
        // 마이 챌린지가 있는 member 조회.
        List<Member> members = myChallengeRepository.findDistinctMemberId();

        for (Member member : members) {
            System.out.println(member.getId() + " , " + member.getName());
            // 멤버 테이블에 해당 멤버가 있는지 확인
            Member findMember = memberService.findById(member.getId());

            // fcm 토큰 가져오기
            String fcmToken = findMember.getFcmToken();

            // fcm 토큰이 있는 멤버에게만 푸시 알림 보내기
            if (fcmToken != null && !fcmToken.isEmpty()) {
                // 메시기 보내기
                boolean isSent = messageService.sendMessage(fcmToken);
                if (isSent) {
                    // notification 테이블에 저장.
                    saveNotification(findMember);
                }
            }
        }
    }

    public void saveNotification(Member member) {

        Notification notification = Notification.builder()
                .member(member)
                .build();

        notificationRepository.save(notification);
    }
}
