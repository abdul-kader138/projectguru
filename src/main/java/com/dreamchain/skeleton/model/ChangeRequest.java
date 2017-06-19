package com.dreamchain.skeleton.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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

    @NotEmpty
    private String companyName;

    @NotEmpty
    private String productName;

    @NotEmpty
    private String categoryName;


    @NotNull
    private long companyId;


    @NotNull
    private long productId;

    @NotNull
    private long categoryId;

    @NotEmpty
    @Length(max = 60)
    private String name;

    @NotEmpty
    @Length(max = 150)
    private String description;


    @NotEmpty
    @Length(max = 150)
    private String docPath;


    @NotEmpty
    private String wipStatus;


    @NotEmpty
    private String status;


    @Column
    private String createdBy;

    @Column
    private String updatedBy;


    @Column
    private Date createdOn;


    @Column
    private Date updatedOn;


    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public ChangeRequest() {
    }
}
