package world.moducare.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 추가
public class ModifyRequest {
    private String name;
    private String birth;
}
