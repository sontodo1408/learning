package vn.io.sontd.learning.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.io.sontd.learning.server.constant.TableField;
import vn.io.sontd.learning.server.constant.enums.EGender;
import vn.io.sontd.learning.server.constant.enums.EUserStatus;

import java.time.LocalDate;

/**
 * Maps to the {@code users} table (see {@code db/create.sql}).
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableField.TBL_USERS)
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TableField.ID)
    private Long id;

    @Column(name = TableField.USERNAME)
    private String username;

    /** BCrypt-encoded password; never expose this field in an API response. */
    @Column(name = TableField.PASSWORD)
    private String password;

    @Column(name = TableField.FULL_NAME)
    private String fullName;

    @Column(name = TableField.EMAIL)
    private String email;

    @Column(name = TableField.PHONE_NUMBER)
    private String phoneNumber;

    @Column(name = TableField.BIRTHDAY)
    private LocalDate birthday;

    @Column(name = TableField.GENDER)
    private EGender gender;

    /** Spring Security authority string, e.g. {@code ROLE_ADMIN} (see {@link vn.io.sontd.learning.server.constant.enums.ERole}). */
    @Column(name = TableField.ROLE)
    private String role;

    @Column(name = TableField.STATUS)
    private EUserStatus status;
}
