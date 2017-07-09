package com.dreamchain.skeleton.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@DynamicUpdate
public class User extends org.springframework.security.core.userdetails.User implements Serializable  {

	private static final long serialVersionUID = -4060739788760795254L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Version
	private long version;

	@NotEmpty
	@Length(min=4,max=50)
	private String name;

	@NotEmpty
	@Length(min=8,max = 100)
	private String password;

	@Email
	@NotEmpty
	@Column(unique = true)
	private String email;

	@NotEmpty
	@Length(min=11)
	private String phone;

	@NotEmpty
	private String role;


	@NotEmpty
	@Length(max = 150)
	private String imagePath;


	@NotEmpty
	@Length(max = 50)
	private String designation;

	@Column
	private String createdBy;

	@Column
	private String updatedBy;


	@Column
	private Date createdOn;


	@Column
	private Date updatedOn;

	@Column
	private Long roleRightsId;

	@NotNull
	@OneToOne
	private RoleRight roleRight;

	@NotNull
	@OneToOne
	private Company company;

	@Column
	@NotNull
	private long companyId;


	@Column
	@NotNull
	private String userType;

	@Column
	@NotEmpty
	private String clientId;



	public User(){
		super("test", "test1234", true, true,true, true, User.getALLAuthority());}

	public User(String username, String password,boolean enabled,
				boolean accountNonExpired, boolean credentialsNonExpired,
				boolean accountNonLocked,
					Collection authorities,
					String name,String email, String role,
					String phone, String imagePath,String designation,Long roleRightsId,
				RoleRight roleRight,Long companyId,Company company,String userType,String clientId,String createdBy,
				String updatedBy,Date createdOn,Date updatedOn) {
		super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);


		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.phone = phone;
		this.designation = designation;
		this.imagePath = imagePath;
		this.roleRightsId=roleRightsId;
		this.roleRight=roleRight;
		this.companyId=companyId;
		this.company=company;
		this.userType=userType;
		this.clientId=clientId;
		this.createdBy = createdBy;
		this.updatedBy=updatedBy;
		this.createdOn=createdOn;
		this.updatedOn=updatedOn;

	}


	public Long getRoleRightsId() {
		return roleRightsId;
	}

	public void setRoleRightsId(Long roleRightsId) {
		this.roleRightsId = roleRightsId;
	}

	public RoleRight getRoleRight() {
		return roleRight;
	}

	public void setRoleRight(RoleRight roleRight) {
		this.roleRight = roleRight;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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

	public static ArrayList<GrantedAuthority> getALLAuthority() {
		ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("Role_USER");
		grantedAuthorities.add(grantedAuthority);
		return grantedAuthorities;

	}


	@Override
	public boolean equals(Object o) {
		if (o instanceof UserDetails) {
			return name.equals(((UserDetails) o).getUsername());
		}
		return false;
	}


	@Override
	public int hashCode() {
		return name.hashCode();
	}
}