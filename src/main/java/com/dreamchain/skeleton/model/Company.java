package com.dreamchain.skeleton.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LAPTOP DREAM on 5/19/2017.
 */

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private long id;

    @Version
    private long version;

    private String name;

    @Column
    private long createdBy;

    @Column
    private long updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;

    public Company() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
