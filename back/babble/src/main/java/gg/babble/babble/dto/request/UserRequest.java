package gg.babble.babble.dto.request;

import gg.babble.babble.domain.user.Nickname;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull
    @Size(min = Nickname.MIN_NICKNAME_LENGTH, max = Nickname.MAX_NICKNAME_LENGTH, message = "유저 닉네임은 {min}자 이상 {max}자 이하입니다. 현재 닉네임: ${validatedValue}")
    @Pattern(regexp = Nickname.NICKNAME_REGEXP, message = "닉네임은 한글, 영어, 숫자, 공백만 포함 가능합니다. 입력 닉네임: ${validatedValue}")
    private String nickname;
}
