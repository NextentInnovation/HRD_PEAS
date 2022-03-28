package hu.nextent.peas.jpa.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import hu.nextent.peas.jpa.constans.HibernateConstant;
import hu.nextent.peas.jpa.entity.base.AuditableBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.val;

/**
 * Authentikációs felhasználó tábla leképezése.
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "user", uniqueConstraints = { 
		@UniqueConstraint(name = "uk_user", columnNames = { "company_id", "userName" }), })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class User extends AuditableBaseEntity implements Serializable {

	private static final long serialVersionUID = 4632555736639212702L;

	// Felhasználó azonosító
	@NotNull
	@Column(name = "userName", nullable = false, length = 100)
	private String userName;

	// Felhasználó név
	@NotNull
	@Column(name = "fullName", nullable = false, length = 100)
	private String fullName;

	// Email
	@Column(name = "email", nullable = true, length = 100)
	private String email;

	// Kezdőbetük
	@Column(name = "initial", nullable = true, length = 100)
	private String initial;

	@NotNull
    @Column(name = "active", nullable = false, columnDefinition = "BIT(1)")
    @ColumnDefault(value = "1")
    private Boolean active;
    
	// szervezeti egység
	@Column(name = "organization", nullable = true, length = 100)
	private String organization;

	// szervezeti egység útvonal
	@Column(name = "organizationPath", nullable = true, length = 200)
	private String organizationPath;

	// Jelszó
	@NotNull
	@Column(name = "passwdHash", nullable = false, length = 100)
	private String passwdHash;
	
	// Jelszó
	@Nullable
	@Column(name = "language", nullable = true, length = 10)
	private String language;
	
	// Vezető azonosító
	@ManyToOne
	@JoinColumn(
			name = "leader_id", 
			foreignKey = @ForeignKey(name = "fk_user_user_id"), 
			nullable = true, 
			insertable = true, 
			updatable = false
			)
	private User leader;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "company_id", 
			foreignKey = @ForeignKey(name = "fk_user_company_id"), 
			nullable = false, 
			insertable = true, 
			updatable = false
			)
	private Company company;
	
	@Singular("userXRole")
	@Nullable
	@OneToMany(
			mappedBy="user",
	        cascade = CascadeType.ALL
	    )
	private List<UserXRole> userXRole;


	public String getCalculatesPasswdHash() {
		translatePassword();
		if (StringUtils.isEmpty(passwdHash)) {
			return null;
		} else {
			return passwdHash.replace("{crypt}", "");
		}
	}
	
	private void translatePassword() {
		if (StringUtils.isEmpty(passwdHash)) {
			return;
		} else if (passwdHash.startsWith("{crypt}")) {
			return;
		} else {
			val passwdEncoded = HibernateConstant.encoder.encode(passwdHash);
			passwdHash = "{crypt}" + passwdEncoded;
			return;
		}
	}
	
	@PreUpdate
	public void preUpdate() {
		translatePassword();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		
		return Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(active, company, email, fullName, initial, leader, organization,
				passwdHash, userName, userXRole);
		return result;
	}

	public boolean isLedBy(User user) {
		// a felhasználó vezetője (valahol a hierarchiában) a megadott user?
		return user != null
				&& this.getOrganizationPath().startsWith(user.getOrganizationPath())
				&& this.getOrganizationPath().length() > user.getOrganizationPath().length()
				;
	}
	
	
}
