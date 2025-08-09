package vn.hoidanit.jobhunter.domain.dto.company;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.validation.validation;


public class CompanyDTO {

    @Setter
    @Getter
    @GroupSequence({Create.class, validation.NotNullGroup.class, validation.NotBlankGroup.class})
    public static class Create {
        @NotNull(message = "Trường 'name' không được thiếu", groups = validation.NotNullGroup.class)
        @NotBlank(message = "Trường 'name' không được để trống", groups = validation.NotBlankGroup.class)
        private String name;
        private String description;
        private String address;
        private String logo;
    }

    @Setter
    @Getter
    @GroupSequence({Update.class, validation.NotNullGroup.class})
    public static class Update {
        @NotNull(message = "Trường 'id' không được thiếu", groups = validation.NotNullGroup.class)
        private Long id;
        private String name;
        private String description;
        private String address;
        private String logo;
    }

    @Setter
    @Getter
    @GroupSequence({Delete.class, validation.NotNullGroup.class, validation.NotBlankGroup.class})
    public static class Delete {

        @NotNull(message = "Trường 'id' không được thiếu", groups = validation.NotNullGroup.class)
        @NotBlank(message = "Trường 'id' không được để trống", groups = validation.NotBlankGroup.class)
        private Long id;
    }


}


