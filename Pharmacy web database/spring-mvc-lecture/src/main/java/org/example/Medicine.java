package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    Integer id;
    String name;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Department department;

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + department +
                '}' + '\n';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment_id() {
        return department;
    }

    public void setDepartment_id(Department department_id) {
        this.department = department_id;
    }


    public void Insert(Integer id, String name, Integer fk, EntityManager entityManager){
        entityManager.getTransaction().begin();
        setId(id);
        setName(name);
        Department department_id = entityManager.find(Department.class, fk);
        setDepartment_id(department_id);
        entityManager.persist(this);
        entityManager.getTransaction().commit();

    }

    public void Delete(Integer id,EntityManager entityManager){
        entityManager.getTransaction().begin();
        Medicine medicine = entityManager.find(Medicine.class, id);
        entityManager.remove(medicine);
        entityManager.getTransaction().commit();

    }

    public void Update(Integer id, String name, Integer fk, EntityManager entityManager){
        entityManager.getTransaction().begin();
        Medicine medicine = entityManager.find(Medicine.class, id);
        medicine.setName(name);
        Department department = entityManager.find(Department.class, fk);
        medicine.setDepartment_id(department);
        entityManager.persist(medicine);
        entityManager.getTransaction().commit();

    }

    public String Read(EntityManager entityManager) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT S FROM Medicine S");
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
