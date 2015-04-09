package entity;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import security.SecurityPermission;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ValentineS.
 */

@Entity
@Table(name = "user")
public class User implements Subject {

    private Long id;
    private String token;
    private Contact contactByContactId;
    private String identifier;
    private String password;
    private SecurityRole roleByRoleId;
    public List<SecurityPermission> permissions;

    @Override
    @Transient
    public List<? extends Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SecurityPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    @Transient
    public List<? extends Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(roleByRoleId);
        return roles;
    }

    ;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    public Contact getContactByContactId() {
        return contactByContactId;
    }

    public void setContactByContactId(Contact contactByContactId) {
        this.contactByContactId = contactByContactId;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    public SecurityRole getRoleByRoleId() {
        return roleByRoleId;
    }

    public void setRoleByRoleId(SecurityRole roleById) {
        this.roleByRoleId = roleById;
    }


    @Basic
    @Column(name = "login", nullable = false, insertable = true, updatable = true)
    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Basic
    @Column(name = "token", nullable = true, insertable = true, updatable = true)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
