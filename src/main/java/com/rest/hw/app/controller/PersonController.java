package com.rest.hw.app.controller;

import com.rest.hw.app.entity.CreatePersonDTO;
import com.rest.hw.app.entity.Person;
import com.rest.hw.app.service.PersonService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonController {
    PersonService personService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> getAllPersons() {
        return new ResponseEntity<>(personService.getAll(), HttpStatus.OK);
    }

    @PostMapping(
            value = "create_person",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Person> createPerson(@RequestBody CreatePersonDTO createPersonDTO) {
        Person newPerson = personService.addPerson(new Person(
                UUID.randomUUID().toString(),
                createPersonDTO.getName(),
                createPersonDTO.getAge(),
                createPersonDTO.getEmail()));
        return new ResponseEntity<>(newPerson, HttpStatus.OK);
    }

    @GetMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getById(@PathVariable(value = "personId") String personId) {
        Person person = personService.getById(personId);
        if (person == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> deleteById(@PathVariable(value = "personId") String personId) {
        Person person = personService.deleteById(personId);
        if (person == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person, HttpStatus.OK);
    }


}
