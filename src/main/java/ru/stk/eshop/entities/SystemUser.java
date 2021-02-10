package ru.stk.eshop.entities;



import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import ru.stk.eshop.validation.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "пароли должны совпадать")
public class SystemUser {
    @NotNull(message = "обязательное поле")
    @Size(min = 3, message = "логин должен быть больше 2-х символов")
    private String username;

    @NotNull(message = "обязательное поле")
    @Size(min = 3, message = "пароль должен быть больше 3-х символов")
    private String password;

    @NotNull(message = "обязательное поле")
    @Size(min = 3, message = "пароль должен быть больше 3-х символов")
    private String matchingPassword;

    @NotNull(message = "обязательное поле")
    @Size(min = 1, message = "обязательное поле")
    private String firstName;

    @NotNull(message = "обязательное поле")
    @Size(min = 1, message = "обязательное поле")
    private String lastName;

    @NotNull(message = "обязательное поле")
    @Email
    private String email;

    private List<Role> roles;
}
