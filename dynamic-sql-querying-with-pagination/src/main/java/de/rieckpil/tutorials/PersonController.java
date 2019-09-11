package de.rieckpil.tutorials;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.text.FieldPosition;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@GetMapping
	public Page<Person> getPersons(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "500") int size,
			@RequestParam(name = "firstname", required = false) String firstname,
			@RequestParam(name = "lastname", required = false) String lastname,
			@RequestParam(name = "budget", required = false) Integer budget,
			@RequestParam(name = "dobLimit", required = false) Long dobLimit) {

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (firstname != null && !firstname.isEmpty()) {
			booleanBuilder.and(QPerson.person.firstName.eq(firstname));
		}

		if (lastname != null && !lastname.isEmpty()) {
			booleanBuilder.and(QPerson.person.lastName.eq(lastname));
		}

		if (budget != null && budget != 0) {
			booleanBuilder.and(QPerson.person.budget.goe(budget));
		}

		if (dobLimit != null && dobLimit != 0) {
			booleanBuilder.and(
					QPerson.person.dob.before(Instant.ofEpochSecond(dobLimit)));
		}

		return personRepository.findAll(booleanBuilder.getValue(),
				PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
	}
	
	@GetMapping("/simplified")
	public Page<Person> getPersonsSimplified(
			@QuerydslPredicate(root = Person.class) Predicate predicate, 
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "500") int size) {
	
		return personRepository.findAll(predicate, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
	}

	@PostMapping("/fetched")
	public Page<Person> getPersonsFetched(@RequestParam(name = "page", defaultValue = "0") int page,
										  @RequestParam(name = "size", defaultValue = "500") int size,
										  @RequestParam(name = "sort") String sort,
										  @RequestBody List<SearchCriteria> searchCriteriaList) throws Exception{

		BooleanBuilder builder = new BooleanBuilder();

		for (SearchCriteria searchCriteria : searchCriteriaList) {
			if (searchCriteria.getFieldName().equals("firstName")) {
				if (searchCriteria.getOperator().equals("equals")) {
					builder.and(QPerson.person.firstName.eq(searchCriteria.getFieldValue()));
				}
			}

			if (searchCriteria.getFieldName().equals("lastName")) {
				if (searchCriteria.getOperator().equals("equals")) {
					builder.and(QPerson.person.lastName.eq(searchCriteria.getFieldValue()));
				}
			}

			if (searchCriteria.getFieldName().equals("city")) {
				if (searchCriteria.getOperator().equals("equals")) {
					builder.and(QPerson.person.address.city.eq(searchCriteria.getFieldValue()));
				}
			}
		}
		return personRepository.findAll(builder.getValue(),
				PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sort)));

	}

}
