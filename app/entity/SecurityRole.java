package entity;

import be.objectify.deadbolt.core.models.Role;

import javax.persistence.*;

/**
 * @Author ValentineS.
 */
@Entity
@Table(name = "role")
public class SecurityRole implements Role {

    private Integer id;
    private String title;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Override
    @Column(name = "title", nullable = false, insertable = true, updatable = true)
    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }
}
