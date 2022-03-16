package com.gesker;

import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.io.Serial;
import java.io.Serializable;

@NamedQueries(value = {
        @NamedQuery(name = "MyJPAObject.findAll", query = "SELECT o FROM MyJPAObject o ORDER BY o.id")
})

@Vetoed
@Immutable
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "my_jpa_data_table"
        , schema = "public"
        , uniqueConstraints = @UniqueConstraint(columnNames = "nme")
)

@SQLDelete(sql = "UPDATE my_jpa_data_table SET enabled = false WHERE id = ?")
public class MyJPAObject extends MyJpaMappedSuperClass implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private long id;
    private String nme;
    private String description;

    public MyJPAObject() {
    }


    public MyJPAObject(long id, String nme, String description) {
        this.id = id;
        this.nme = nme;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Column(name = "nme", length = 10)
    public String getNme() {
        return this.nme;
    }

    public void setNme(String nme) {
        this.nme = nme;
    }


    @Column(name = "description", length = 25)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}


