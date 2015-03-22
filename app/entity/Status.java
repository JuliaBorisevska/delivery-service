package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @Author ValentineS.
 */
public class Status {

    private int id;
    private String title;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, insertable = true, updatable = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
