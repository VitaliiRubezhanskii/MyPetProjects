package de.rieckpil.tutorials;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;


    private String city;

    private String country;

    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private List<Person> persons;



}
