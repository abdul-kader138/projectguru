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
public class RoleRight implements Serializable
{



    private static final long serialVersionUID = 8633415090980966715L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private long id;

    @Version
    private long version;


    @NotNull
    private long roleId;

    @NotNull
    @OneToOne
    private Roles roles;


    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "rights",
            joinColumns=@JoinColumn(name = "id", referencedColumnName = "id")
    )
    @Column(name="rights")
    private Set<String> rights = new HashSet<>();


    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;


    public RoleRight() {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public long getVersion() {
        return version;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
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
