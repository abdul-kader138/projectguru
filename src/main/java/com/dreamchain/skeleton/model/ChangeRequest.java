package com.dreamchain.skeleton.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="change_request",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"category","name"}))
@DynamicUpdate
public class ChangeRequest implements Serializable {

    private static final long serialVersionUID = 8633416055380776715L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Version
    private long version;


    @OneToOne
    private Category category;

    @NotNull
    private long departmentId;

    @NotNull
    @OneToOne
    private User requestBy;


    @NotNull
    @OneToOne
    private User checkedBy;


    @NotNull
    @OneToOne
    private User itCoordinator;


    @NotNull
    @OneToOne
    private User approvedBy;


    @NotNull
    @OneToOne
    private User acknowledgedItCoordinator;


    @NotNull
    @OneToOne
    private User acknowledgeChecked;

    @NotNull
    @OneToOne
    private User acknowledgement;

    @NotNull
    private long teamAllocationId;


    @NotNull
    private long userAllocationId;


    @NotEmpty
    @Length(max = 60)
    private String name;

    @com.sun.istack.NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "change_request_description",
            joinColumns=@JoinColumn(name = "id", referencedColumnName = "id")
    )
    @Column(name="description")
    private Set<String> description = new HashSet<>();



    @NotEmpty
    @Length(max = 150)
    private String docPath;

    @NotEmpty
    private String wipStatus;


    @NotEmpty
    private String status;

    @NotEmpty
    private String checkedByStatus;

    @Column
    private Date deliverDate;

    @Column
    private Integer requiredDay;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;

    private String declineCause;



    public ChangeRequest() {
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

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public User getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(User requestBy) {
        this.requestBy = requestBy;
    }

    public User getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(User checkedBy) {
        this.checkedBy = checkedBy;
    }

    public User getItCoordinator() {
        return itCoordinator;
    }

    public void setItCoordinator(User itCoordinator) {
        this.itCoordinator = itCoordinator;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public User getAcknowledgedItCoordinator() {
        return acknowledgedItCoordinator;
    }

    public void setAcknowledgedItCoordinator(User acknowledgedItCoordinator) {
        this.acknowledgedItCoordinator = acknowledgedItCoordinator;
    }

    public User getAcknowledgeChecked() {
        return acknowledgeChecked;
    }

    public void setAcknowledgeChecked(User acknowledgeChecked) {
        this.acknowledgeChecked = acknowledgeChecked;
    }

    public User getAcknowledgement() {
        return acknowledgement;
    }

    public void setAcknowledgement(User acknowledgement) {
        this.acknowledgement = acknowledgement;
    }

    public long getTeamAllocationId() {
        return teamAllocationId;
    }

    public void setTeamAllocationId(long teamAllocationId) {
        this.teamAllocationId = teamAllocationId;
    }

    public long getUserAllocationId() {
        return userAllocationId;
    }

    public void setUserAllocationId(long userAllocationId) {
        this.userAllocationId = userAllocationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getDescription() {
        return description;
    }

    public void setDescription(Set<String> description) {
        this.description = description;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public String getWipStatus() {
        return wipStatus;
    }

    public void setWipStatus(String wipStatus) {
        this.wipStatus = wipStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckedByStatus() {
        return checkedByStatus;
    }

    public void setCheckedByStatus(String checkedByStatus) {
        this.checkedByStatus = checkedByStatus;
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

    public String getDeclineCause() {
        return declineCause;
    }

    public void setDeclineCause(String declineCause) {
        this.declineCause = declineCause;
    }
}
