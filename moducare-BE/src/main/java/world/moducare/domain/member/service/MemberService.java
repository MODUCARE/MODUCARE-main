package world.moducare.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.moducare.domain.member.dto.ModifyRequest;
import world.moducare.domain.member.dto.SocialLoginResponse;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.repository.MemberRepository;
import world.moducare.domain.product.repository.LatestProductRepository;
import world.moducare.global.exception.ErrorCode;
import world.moducare.global.exception.RestApiException;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));
    }

    public void logout(Member member) {
        member.logout();
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public void modifyMember(Member member, ModifyRequest modifyRequest) {
        String name = modifyRequest.getName();
        String birth = modifyRequest.getBirth();
        member.modify(name, birth);
        memberRepository.save(member);
    }

    public void saveMemberAndToken(Member member, String fcmToken, String refreshToken) {
        member.updateRefreshAndFcm(refreshToken, fcmToken);
        memberRepository.save(member);
    }

    public Member saveOrUpdateMember(String email, String name) {

        Member member = memberRepository.findByEmail(email)
                .map(Member::findMember)
                .orElseGet(() -> Member.builder()
                        .email(email)
                        .name(name)
                        .build());

        memberRepository.save(member);
        return member;
    }

    public SocialLoginResponse getSocialMember(Member member, String jwtToken) {
        return SocialLoginResponse.builder()
                .jwtAccessToken(jwtToken)
                .refreshToken(member.getRefreshToken())
                .name(member.getName())
                .email(member.getEmail())
                .birth(member.getBirth())
                .build();
    }
}
