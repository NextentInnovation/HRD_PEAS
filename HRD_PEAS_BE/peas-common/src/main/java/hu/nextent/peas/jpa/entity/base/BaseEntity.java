package hu.nextent.peas.jpa.entity.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@RequiredArgsConstructor 
@ToString 
@EqualsAndHashCode
public abstract class BaseEntity implements JpaEntity<Long>, Serializable {

	private static final long serialVersionUID = -6801823924255242583L;
	
	@Getter
    @Transient
    private boolean isNew = true; 

	@Getter
	@Setter
	@Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
	@Column(updatable = false, nullable = false)
    private Long id;

	@Getter
	@Setter
    @Version
    @Column(nullable = false)
    @ColumnDefault( value = "0" )
    private Long version;

    @PrePersist 
    @PostLoad
    void markNotNew() {
      this.isNew = false;
    }

}
