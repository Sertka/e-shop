package ru.stk.eshop.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import ru.stk.eshop.validation.FieldMatch;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "пароли должны совпадать")
public class UserPassword {

    private Long userId;

    @Size(min = 3, message = "пароль должен быть больше 3-х символов")
    private String password;

    @Size(min = 3, message = "пароль должен быть больше 3-х символов")
    private String matchingPassword;
}

