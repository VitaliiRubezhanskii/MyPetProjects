package de.rieckpil.tutorials;

import java.time.Instant;

import javax.persistence.*;

import com.querydsl.core.annotations.QueryEntity;

import lombok.Data;

@Data
@Entity
@QueryEntity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	private Instant dob;

	private Integer budget;

	@ManyToOne
	@JoinColumn(name="address_id", nullable=false)
	private Address address;
}
