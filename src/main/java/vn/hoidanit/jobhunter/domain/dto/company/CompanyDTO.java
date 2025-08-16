package vn.hoidanit.jobhunter.domain.dto.company;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public class CompanyDTO {

    @Setter
    @Getter
    public static class Create {
        @NotNull(message = "Trường 'name' không được thiếu")
        @NotBlank(message = "Trường 'name' không được để trống!!!")
        private String name;
        private String description;
        private String address;
        private String logo;
    }

    @Setter
    @Getter
    public static class Update {
        @NotNull(message = "Trường 'id' không được thiếu")
        private Long id;
        private String name;
        private String description;
        private String address;
        private String logo;
    }

    @Setter
    @Getter
    public static class Delete {

        @NotNull(message = "Trường 'id' không được thiếu")
        @NotBlank(message = "Trường 'id' không được để trống")
        private Long id;
    }


}


