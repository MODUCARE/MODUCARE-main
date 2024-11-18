package world.moducare.domain.mychallenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import world.moducare.domain.challenge.entity.Challenge;
import world.moducare.domain.challenge.repository.ChallengeRepository;
import world.moducare.domain.challengefeed.repository.ChallengeFeedRepository;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.mychallenge.dto.MyChallengeResponseDto;
import world.moducare.domain.mychallenge.entity.MyChallenge;
import world.moducare.domain.mychallenge.entity.Status;
import world.moducare.domain.mychallenge.repository.MyChallengeRepository;
import world.moducare.global.exception.ErrorCode;
import world.moducare.global.exception.RestApiException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MyChallengeRepository myChallengeRepository;
    private final ChallengeFeedRepository feedRepository;

    public void addMyChallenge(Member member, Long challengeId) {

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));

        // 마이 챌린지로 추가할 때 이미 참여했다가 나간 챌린지인지 조회
        MyChallenge exists = myChallengeRepository.findByMemberIdAndChallengeIdAndStatus(member.getId(), challengeId, Status.OUT);
        if (exists != null) {

            // 존재한다면 status를 OUT -> IN으로 update 해줌.
            exists.updateStatus(Status.IN);
            myChallengeRepository.save(exists);

        } else {

            MyChallenge myChallenge = MyChallenge.builder()
                    .member(member)
                    .status(Status.IN)
                    .challenge(challenge)
                    .build();

            myChallengeRepository.save(myChallenge);
        }

        // 챌린지 인원수 증가
        challenge.increaseHeadCount();
        challengeRepository.save(challenge);
    }

    public void deleteMyChallenge(Member member, Long challengeId) {

        MyChallenge myChallenge = myChallengeRepository.findByMemberIdAndChallengeIdAndStatus(member.getId(), challengeId, Status.IN);
        myChallenge.updateStatus(Status.OUT);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));

        // 챌린지 인원수 감소
        challenge.decreaseHeadCount();

        // 감소한 인원수가 0이라면 챌린지 database 에서 챌린지 삭제, myChallenge database에서도 삭제
        if (challenge.getHeadCount() == 0) {
            challengeRepository.delete(challenge);
            myChallengeRepository.delete(myChallenge);
        } else {
            challengeRepository.save(challenge);
            myChallengeRepository.save(myChallenge);

        }
    }

    public List<MyChallengeResponseDto> getMyChallenges(Member member) {

        List<MyChallenge> myChallenges = myChallengeRepository.findByMemberIdAndStatus(member.getId());
        LocalDate today = LocalDate.now();

        return myChallenges.stream().map(challenge -> {
            boolean isDone = feedRepository.existsByChallengeAndMemberAndToday(member.getId(), challenge.getChallenge().getId(), today);

            return MyChallengeResponseDto.builder()
                    .challengeId(challenge.getChallenge().getId())
                    .challengeImg(challenge.getChallenge().getImage())
                    .challengeName(challenge.getChallenge().getTitle())
                    .isDone(isDone ? 1 : 0)
                    .build();
        }).collect(Collectors.toList());
    }
}
