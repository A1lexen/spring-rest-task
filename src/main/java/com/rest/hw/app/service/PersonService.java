package com.rest.hw.app.service;

import com.rest.hw.app.entity.Person;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final Map<String , Person> persons = new HashMap<>();

    private PersonService(){
        this.persons.put("1",new Person("1","Max",21,"max@gmail.com"));
        this.persons.put("2",new Person("2","Bob",10,"bob@gmail.com"));
        this.persons.put("3",new Person("3","John",32,"john@gmail.com"));
    }

    public List<Person> getAll(){
        return persons.values().stream()
                .sorted(Comparator.comparing(Person::getId))
                .collect(Collectors.toList());
    }

    public Person getById(String personId){
        return persons.get(personId);
    }

    public Person deleteById(String personId){
        return persons.remove(personId);
    }

    public Person addPerson(Person person){
        return persons.put(person.getId(),person);
    }
}
