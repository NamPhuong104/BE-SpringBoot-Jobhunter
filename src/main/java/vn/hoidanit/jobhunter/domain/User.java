package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.AudiTable;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AudiTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private int age;

    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
}
