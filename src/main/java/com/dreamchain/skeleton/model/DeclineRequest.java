package com.dreamchain.skeleton.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="decline_request",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"approvalStatusId","requestsId","declineCause"}))
@DynamicUpdate
public class DeclineRequest implements Serializable {

    private static final long serialVersionUID = 8633345655380776715L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Version
    private long version;

    @NotNull
    private long approvalStatusId;

    @NotNull
    private long requestsId;

    @NotEmpty
    @Length(max = 500)
    private String declineCause;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getApprovalStatusId() {
        return approvalStatusId;
    }

    public void setApprovalStatusId(long approvalStatusId) {
        this.approvalStatusId = approvalStatusId;
    }

    public long getRequestsId() {
        return requestsId;
    }

    public void setRequestsId(long requestsId) {
        this.requestsId = requestsId;
    }

    public String getDeclineCause() {
        return declineCause;
    }

    public void setDeclineCause(String declineCause) {
        this.declineCause = declineCause;
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

    public DeclineRequest() {
    }
}

