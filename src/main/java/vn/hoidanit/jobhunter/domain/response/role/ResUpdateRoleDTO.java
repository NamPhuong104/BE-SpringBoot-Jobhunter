package vn.hoidanit.jobhunter.domain.response.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateRoleDTO {
    private long id;
    private String name;
    private String description;
    private boolean active;
    private List<Permission> permissions;
    private Instant updatedAt;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Permission {
        private long id;
        private String name;
    }
}
