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
        @UniqueConstraint(columnNames={"categoryId","name"}),
        indexes = {
                @Index(columnList = "status,deliverDate", name = "status_deliver_date"),
                @Index(columnList = "id,checkedByStatus", name = "id_checked_by_status"),
                @Index(columnList = "name,categoryId", name = "name_category_id"),
                @Index(columnList = "categoryId", name = "category_id"),
                @Index(columnList = "departmentId", name = "department_id"),
                @Index(columnList = "clientId", name = "client_id"),
                @Index(columnList = "teamAllocationId", name = "team_allocation_id")
        }
)

@DynamicUpdate
public class ChangeRequest implements Serializable {

    private static final long serialVersionUID = 8633416055380776715L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Version
    private long version;

    @Version
    private long categoryId;


    @OneToOne
    private Category category;

    @NotNull
    private long departmentId;

    @NotNull
    private long requestById;


    @NotNull
    private long checkedById;


    @NotNull
    private long itCoordinatorId;


    @NotNull
    private long approvedById;


    @NotNull
    private long acknowledgedItCoordinatorId;


    @NotNull
    private long acknowledgeCheckedId;

    @NotNull
    private long acknowledgementId;

    @NotNull
    private long teamAllocationId;


    @NotNull
    private long userAllocationId;

    @Column
    @NotEmpty
    private String clientId;


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
    private Date deployedOn;

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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public long getRequestById() {
        return requestById;
    }

    public void setRequestById(long requestById) {
        this.requestById = requestById;
    }

    public long getCheckedById() {
        return checkedById;
    }

    public void setCheckedById(long checkedById) {
        this.checkedById = checkedById;
    }

    public long getItCoordinatorId() {
        return itCoordinatorId;
    }

    public void setItCoordinatorId(long itCoordinatorId) {
        this.itCoordinatorId = itCoordinatorId;
    }

    public long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(long approvedById) {
        this.approvedById = approvedById;
    }

    public long getAcknowledgedItCoordinatorId() {
        return acknowledgedItCoordinatorId;
    }

    public void setAcknowledgedItCoordinatorId(long acknowledgedItCoordinatorId) {
        this.acknowledgedItCoordinatorId = acknowledgedItCoordinatorId;
    }

    public long getAcknowledgeCheckedId() {
        return acknowledgeCheckedId;
    }

    public void setAcknowledgeCheckedId(long acknowledgeCheckedId) {
        this.acknowledgeCheckedId = acknowledgeCheckedId;
    }

    public long getAcknowledgementId() {
        return acknowledgementId;
    }

    public void setAcknowledgementId(long acknowledgementId) {
        this.acknowledgementId = acknowledgementId;
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

    public Date getDeployedOn() {
        return deployedOn;
    }

    public void setDeployedOn(Date deployedOn) {
        this.deployedOn = deployedOn;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
