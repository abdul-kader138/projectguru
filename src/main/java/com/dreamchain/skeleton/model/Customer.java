package com.dreamchain.skeleton.model;


//import org.codehaus.jackson.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Index;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name = "customer",
        indexes = {
                @Index(name = "customer_created_by_id_idx", columnList = "createdBy"),
                @Index(name = "customer_updated_by_id_idx", columnList = "updatedBy")
        })
public class Customer {
    private static final long serialVersionUID = -4060739788760795255L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private long version;


    @NotEmpty
    @Column(name = "customerCode",unique = true,length = 6)
    private String customerCode;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "address")
    private String address;

    @NotEmpty
    @Column(name = "nid",unique = true,length = 13)
    private String nid;


    @NotEmpty
    @Column(name = "email",unique = true)
    private String email;

    @NotEmpty
    @Column(name = "phone",unique = true)
    private String phone;

    @JsonIgnore
    @Column
    private long createdBy;

    @JsonIgnore
    @Column
    private long updatedBy;

    @JsonIgnore
    @Column
    private Date createdOn;

    @JsonIgnore
    @Column
    private Date updatedOn;


    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public long getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }


    public long getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }
}
