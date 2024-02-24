package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="department")

public class Department {
    @Id
    Integer id;
    String illness_type;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    Country country;

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", illness_type='" + illness_type + '\'' +
                ", country=" + country +
                '}'+ '\n';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIllness_type() {
        return illness_type;
    }

    public void setIllness_type(String illness_type) {
        this.illness_type = illness_type;
    }

    public Country getCountry_id() {
        return country;
    }

    public void setCountry_id(Country country_id) {
        this.country = country_id;
    }


    public void Insert(Integer id, String name, Integer fk, EntityManager entityManager){
        entityManager.getTransaction().begin();
        setId(id);
        setIllness_type(name);
        Country country_id = entityManager.find(Country.class, fk);
        setCountry_id(country_id);
        entityManager.persist(this);
        entityManager.getTransaction().commit();

    }

    public void Delete(Integer id,EntityManager entityManager){
        entityManager.getTransaction().begin();
        Department department2 = entityManager.find(Department.class, id);
        Query queryforremove = entityManager.createQuery("SELECT N FROM Medicine N WHERE N.department = :department");
        queryforremove.setParameter("department", department2);
        List<Medicine> med = queryforremove.getResultList();
        for (Medicine m:med){
            entityManager.remove(m);
        }
        entityManager.remove(department2);

        entityManager.getTransaction().commit();

    }

    public void Update(Integer id, String name, Integer fk, EntityManager entityManager){
        entityManager.getTransaction().begin();
        Department department = entityManager.find(Department.class, id);
        department.setIllness_type(name);
        Country country_id = entityManager.find(Country.class, fk);
        department.setCountry_id(country_id);
        entityManager.persist(department);

        entityManager.getTransaction().commit();

    }

    public String Read(EntityManager entityManager) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT S FROM Department S");
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
