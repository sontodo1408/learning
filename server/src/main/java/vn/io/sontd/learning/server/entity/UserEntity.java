package vn.io.sontd.learning.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.io.sontd.learning.server.constant.TableField;
import vn.io.sontd.learning.server.constant.enums.EUserStatus;

import java.time.LocalDate;

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

    @Column(name = TableField.ROLE)
    private String role;

    @Column(name = TableField.STATUS)
    private EUserStatus status;
}
