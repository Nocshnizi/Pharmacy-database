package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name="country")
public class Country {
    @Id
    Integer id;
    String country_name;
    Integer population;

    @OneToMany(mappedBy = "country")
    private List<Department> departmentList;

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", country_name='" + country_name + '\'' +
                ", population=" + population +
                '}'+ '\n';
    }

    public Integer getId() {
        return id;
    }
    public Integer getPopulation() {
        return population;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }


    public void Insert(Integer id, String name, Integer fk, EntityManager entityManager){
        setId(id);
        setCountry_name(name);
        setPopulation(fk);
        entityManager.persist(this);
    }

    public void Delete(Integer id,EntityManager entityManager){
        Country country = entityManager.find(Country.class, id);
        Query queryforremove = entityManager.createQuery("SELECT N FROM Department N WHERE N.country = :country");
        queryforremove.setParameter("country", country);
        List<Department> dep = queryforremove.getResultList();
        for (Department d:dep){
            entityManager.remove(d);
        }
        entityManager.remove(country);
    }

    public void Update(Integer id, String name, Integer fk, EntityManager entityManager){
        Country country = entityManager.find(Country.class, id);
        country.setCountry_name(name);
        country.setPopulation(fk);
        entityManager.persist(country);
    }

    public String Read(EntityManager entityManager) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT S FROM Country S");
        List<Country> countries = query.getResultList();
        entityManager.getTransaction().commit();

        // Використання ObjectMapper для перетворення списку об'єктів Country у JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = "";

        try {
            jsonResult = objectMapper.writeValueAsString(countries);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResult;
    }
}
