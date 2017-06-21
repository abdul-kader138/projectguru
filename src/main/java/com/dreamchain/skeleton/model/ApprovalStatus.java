package com.dreamchain.skeleton.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="approve_status",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"companyId","productId", "categoryId","userType","requestName"}))
public class ApprovalStatus {
    private static final long serialVersionUID = 8633413235380776715L;

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

    @NotEmpty
    private String requestName;

    @NotEmpty
    private String requestDetails;

    @NotNull
    @OneToOne
    private User approvedBy;

    @NotNull
    private long approvedById;

    @NotEmpty
    private String status;

    @NotEmpty
    private String userType;

    public ApprovalStatus() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(long approvedById) {
        this.approvedById = approvedById;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
}
