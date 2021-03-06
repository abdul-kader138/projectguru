package com.dreamchain.skeleton.model;

import com.sun.istack.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="user_allocation",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"companyId","productId","category"}))
@DynamicUpdate
public class UserAllocation implements Serializable {

    private static final long serialVersionUID = 8633415080370876715L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Version
    private long version;

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
    private User approvedBy;

    @NotNull
    @OneToOne
    private User itCoordinator;


    @Column
    @NotNull
    private String userType;

    @Column
    @NotEmpty
    private String clientId;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;

    public UserAllocation() {
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

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public User getItCoordinator() {
        return itCoordinator;
    }

    public void setItCoordinator(User itCoordinator) {
        this.itCoordinator = itCoordinator;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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
}
