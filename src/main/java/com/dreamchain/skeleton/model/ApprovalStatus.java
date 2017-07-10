package com.dreamchain.skeleton.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "approve_status",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"category", "userType", "requestName"}),
        indexes = {
                @Index(columnList = "approvedById,status", name = "approved_by_id_status"),
                @Index(columnList = "approvedById,requestId", name = "approved_by_id_request_by_id"),
                @Index(columnList = "userType,requestId", name = "user_type_request_by_id"),
                @Index(columnList = "approvedById", name = "approved_by_id"),
                @Index(columnList = "requestId", name = "request_by_id")}
)
@DynamicUpdate
public class ApprovalStatus {
    private static final long serialVersionUID = 8633413235380776715L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Version
    private long version;


    @NotNull
    @OneToOne
    private Category category;


    @NotEmpty
    private String requestName;

    @NotNull
    @OneToOne
    private User approvedBy;

    @NotEmpty
    private String status;

    @NotEmpty
    private String userType;

    @NotEmpty
    private String approveType;


    @NotNull
    private long approvedById;

    @NotNull
    private long requestId;

    @Column
    private Date deliverDate;

    @Column
    private Integer requiredDay;


    @NotEmpty
    @Length(max = 150)
    private String docPath;


    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;


    public ApprovalStatus() {
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Integer getRequiredDay() {
        return requiredDay;
    }

    public void setRequiredDay(Integer requiredDay) {
        this.requiredDay = requiredDay;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
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

    public long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(long approvedById) {
        this.approvedById = approvedById;
    }
}