package vn.hoidanit.jobhunter.domain.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;
import vn.hoidanit.jobhunter.validation.ValueOfEnum;

public class UserDTO {

    @Setter
    @Getter
    public static class Create {

        @NotBlank(message = "Trường 'name' không được để trống!!!")
        private String name;

        @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
        @NotEmpty(message = "Trường 'email' không được để trống!!!")
        private String email;

        @NotEmpty(message = "Trường 'password' không được để trống!!!")
        @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
        private String password;

        @NotEmpty(message = "Trường 'age' không được để trống!!!")
        @Min(value = 0, message = "Tuổi phải >= 0")
        private String age;

        @NotNull(message = "Trường 'gender' không được để trống!!!")
        @ValueOfEnum(enumClass = GenderEnum.class, message = "gender phải là một trong: FEMALE, MALE, OTHER")
        private String gender;

        @NotEmpty(message = "Trường 'address' không được để trống!!!")
        private String address;
    }

    @Setter
    @Getter
    public static class Update {
        @NotNull(message = "Trường 'id' không được thiếu")
        private Long id;

        @NotBlank(message = "Trường 'name' không được để trống!!!")
        private String name;


        @NotEmpty(message = "Trường 'age' không được để trống!!!")
        private String age;

        @NotNull(message = "Trường 'gender' không được để trống!!!")
        @ValueOfEnum(enumClass = GenderEnum.class, message = "gender phải là một trong: FEMALE, MALE, OTHER")
        private String gender;

        @NotEmpty(message = "Trường 'address' không được để trống!!!")
        private String address;
    }

    @Setter
    @Getter
    public static class Delete {
        @NotNull(message = "Trường 'id' không được thiếu")
        private Long id;
    }

}
