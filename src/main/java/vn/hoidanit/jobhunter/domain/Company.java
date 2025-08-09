package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.AudiTable;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company extends AudiTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;

    private String logo;
    

}
