package vn.hoidanit.jobhunter.domain.response.company;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateCompanyDTO {
    private String name;
    private String description;
    private String address;
    private String logo;
    private Instant createdAt;
}
