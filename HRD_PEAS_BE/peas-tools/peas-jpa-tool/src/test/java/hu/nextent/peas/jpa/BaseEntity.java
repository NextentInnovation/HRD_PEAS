package hu.nextent.peas.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -6801823924255242583L;

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

    @Version
    @Column(nullable = false)
    @ColumnDefault( value = "0" )
    private Long version;

}
