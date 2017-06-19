package com.dreamchain.skeleton.model;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="team_allocation",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"companyId","productId","categoryId","requestById","checkedById"}))
public class TeamAllocation implements Serializable {

    private static final long serialVersionUID = 8633415080370876715L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Version
    private long version;

    @NotEmpty
    private String companyName;

//    @NotEmpty
//    private String departmentName;

    @NotEmpty
    private String productName;

    @NotEmpty
    private String categoryName;


    @NotNull
    private long companyId;

//    @NotNull
//    private long departmentId;

    @NotNull
    private long productId;


    @NotNull
    private long categoryId;

    @NotNull
    private long requestById;

    @NotNull
    private long checkedById;

    @NotNull
    @Column
    private String requestedBy;

    @NotNull
    @Column
    private String checkedBy;

    @Column
    @NotNull
    private String userType;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;

    public TeamAllocation() {
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

//    public String getDepartmentName() {
//        return departmentName;
//    }
//
//    public void setDepartmentName(String departmentName) {
//        this.departmentName = departmentName;
//    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }
}
