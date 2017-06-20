package com.dreamchain.skeleton.model;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="user_allocation",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"companyId","productId","categoryId","clientId"}))
public class UserAllocation implements Serializable {

    private static final long serialVersionUID = 8633415080370876715L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Version
    private long version;


//    @NotEmpty
//    private String departmentName;


    @NotEmpty
    @OneToOne
    private Category category;


    @NotNull
    private long companyId;

//    @NotNull
//    private long departmentId;

    @NotNull
    private long productId;


    @NotNull
    private long categoryId;


    @NotNull
    private long itCoordinatorId;

    @NotNull
    private long approvedById;

    @NotNull
    @Column
    private String approvedBy;

    @NotNull
    @Column
    private String itCoordinator;


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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

//    public long getDepartmentId() {
//        return departmentId;
//    }
//
//    public void setDepartmentId(long departmentId) {
//        this.departmentId = departmentId;
//    }

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }



//    public String getDepartmentName() {
//        return departmentName;
//    }
//
//    public void setDepartmentName(String departmentName) {
//        this.departmentName = departmentName;
//    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getItCoordinator() {
        return itCoordinator;
    }

    public void setItCoordinator(String itCoordinator) {
        this.itCoordinator = itCoordinator;
    }
}
