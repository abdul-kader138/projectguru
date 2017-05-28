package com.dreamchain.skeleton.model;


import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Role implements Serializable
{


    private static final long serialVersionUID = 8633415090980966715L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private long id;

    @Version
    private long version;


    @NotEmpty
    @Length(max = 60)
    @Column(unique = true)
    private String name;

    @NotEmpty
    @Length(max = 150)
    private String description;


    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rights")
    private Set<String> rights = new HashSet<>();


    @Column
    private long createdBy;

    @Column
    private long updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;


    public Role() {
    }

    public Set<String> getRights() {
        return rights;
    }

    public void setRights(Set<String> rights) {
        this.rights = rights;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
