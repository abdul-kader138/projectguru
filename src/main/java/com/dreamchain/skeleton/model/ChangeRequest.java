package com.dreamchain.skeleton.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="change_request",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"companyId","productId", "categoryId","name"}))
public class ChangeRequest implements Serializable {

    private static final long serialVersionUID = 8633416055380776715L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Version
    private long version;

    @NotNull
    @OneToOne
    private Company company;

    @NotNull
    @OneToOne
    private Product product;


    @NotNull
    @OneToOne
    private Category category;


    @NotNull
    private long companyId;


    @NotNull
    private long productId;

    @NotNull
    private long categoryId;

    @NotNull
    @OneToOne
    private User requestBy;

    @NotNull
    private long requestById;

    @NotNull
    @OneToOne
    private User checkedBy;

    @NotNull
    private long checkedById;

    @NotNull
    @OneToOne
    private User itCoordinator;

    @NotNull
    private long itCoordinatorId;

    @NotNull
    @OneToOne
    private User approvedBy;

    @NotNull
    private long approvedById;


    @NotNull
    @OneToOne
    private User acknowledgedItCoordinator;

    @NotNull
    private long acknowledgedItCoordinatorId;

    @NotNull
    @OneToOne
    private User acknowledgeChecked;

    @NotNull
    private long acknowledgeCheckedId;

    @NotNull
    @OneToOne
    private User acknowledgement;

    @NotNull
    private long acknowledgementId;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public User getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(User requestBy) {
        this.requestBy = requestBy;
    }

    public long getRequestById() {
        return requestById;
    }

    public void setRequestById(long requestById) {
        this.requestById = requestById;
    }

    public User getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(User checkedBy) {
        this.checkedBy = checkedBy;
    }

    public long getCheckedById() {
        return checkedById;
    }

    public void setCheckedById(long checkedById) {
        this.checkedById = checkedById;
    }

    public User getItCoordinator() {
        return itCoordinator;
    }

    public void setItCoordinator(User itCoordinator) {
        this.itCoordinator = itCoordinator;
    }

    public long getItCoordinatorId() {
        return itCoordinatorId;
    }

    public void setItCoordinatorId(long itCoordinatorId) {
        this.itCoordinatorId = itCoordinatorId;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(long approvedById) {
        this.approvedById = approvedById;
    }

    public User getAcknowledgeChecked() {
        return acknowledgeChecked;
    }

    public void setAcknowledgeChecked(User acknowledgeChecked) {
        this.acknowledgeChecked = acknowledgeChecked;
    }

    public long getAcknowledgeCheckedId() {
        return acknowledgeCheckedId;
    }

    public void setAcknowledgeCheckedId(long acknowledgeCheckedId) {
        this.acknowledgeCheckedId = acknowledgeCheckedId;
    }

    public User getAcknowledgement() {
        return acknowledgement;
    }

    public void setAcknowledgement(User acknowledgement) {
        this.acknowledgement = acknowledgement;
    }

    public long getAcknowledgementId() {
        return acknowledgementId;
    }

    public void setAcknowledgementId(long acknowledgementId) {
        this.acknowledgementId = acknowledgementId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWipStatus() {
        return wipStatus;
    }

    public void setWipStatus(String wipStatus) {
        this.wipStatus = wipStatus;
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

    public User getAcknowledgedItCoordinator() {
        return acknowledgedItCoordinator;
    }

    public void setAcknowledgedItCoordinator(User acknowledgedItCoordinator) {
        this.acknowledgedItCoordinator = acknowledgedItCoordinator;
    }

    public long getAcknowledgedItCoordinatorId() {
        return acknowledgedItCoordinatorId;
    }

    public void setAcknowledgedItCoordinatorId(long acknowledgedItCoordinatorId) {
        this.acknowledgedItCoordinatorId = acknowledgedItCoordinatorId;
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

    public String getDeclineCause() {
        return declineCause;
    }

    public void setDeclineCause(String declineCause) {
        this.declineCause = declineCause;
    }

    public ChangeRequest() {
    }
}
