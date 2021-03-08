package ru.stk.eshop.entities;



import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import ru.stk.eshop.validation.FieldMatch;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * SystemUser is used to create a new user and check correctness
 * of all fields
 */
@Getter
@Setter
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "пароли должны совпадать")
public class SystemUser {

    @Size(min = 3, message = "логин должен быть больше 2-х символов")
    private String username;

    @Size(min = 3, message = "пароль должен быть больше 3-х символов")
    private String password;

    @Size(min = 3, message = "пароль должен быть больше 3-х символов")
    private String matchingPassword;

    @Size(min = 1, message = "обязательное поле")
    private String firstName;

    @Size(min = 1, message = "обязательное поле")
    private String lastName;

    @Size(min = 8, message = "обязательное поле")
    private String phone;

    @Size(min = 10, message = "заполните адрес")
    private String address;

    @Size(min = 3, message = "заполните e-mail")
    @Email
    private String email;

    private List<Role> roles;
}
