package vn.hoidanit.jobhunter.domain.response.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateJobDTO {
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private Company company;

    private List<SkillDTO> skills;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillDTO {
        private long id;
        private String name;
    }
}
