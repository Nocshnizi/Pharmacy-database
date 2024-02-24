package org.example;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Queue;

@RestController
public class CountryController {

    @Autowired
    private HttpSession httpSession;

        EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("spring-mvc-lecture");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        @GetMapping(value = "/country", produces = MediaType.APPLICATION_JSON_VALUE)
        public SampleResponse getSample() {
            return new SampleResponse("You editing COUNTRY");
        }

        @GetMapping(value = "/countryRead", produces = MediaType.APPLICATION_JSON_VALUE)
        public String getCountryRead() {
            Country country = new Country();
            return country.Read(entityManager);
        }

        @PostMapping(value = "/countryInsrt", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postCountryInsert(@RequestBody Country country){
            if (isAdmin(httpSession, entityManager) || isUser(httpSession, entityManager)) {
                entityManager.getTransaction().begin();
                country.Insert(country.getId(), country.getCountry_name(), country.getPopulation(), entityManager);
                entityManager.getTransaction().commit();
            }else{
                new SampleResponse("You are not login");
            }
        }

        @PostMapping(value = "/countryUp", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postCountryUpdate(@RequestBody Country country){
            if (isAdmin(httpSession, entityManager) || isUser(httpSession, entityManager)) {
                entityManager.getTransaction().begin();
                country.Update(country.getId(), country.getCountry_name(), country.getPopulation(), entityManager);
                entityManager.getTransaction().commit();
            }else{
                new SampleResponse("You are not login");
            }
        }


    @PostMapping(value = "/countryDlt", produces = MediaType.APPLICATION_JSON_VALUE)
    public void postCountryDelete(@RequestBody Country country){
        if(isAdmin(httpSession, entityManager)) {
            entityManager.getTransaction().begin();
            country.Delete(country.getId(), entityManager);
            entityManager.getTransaction().commit();
        }else {
            new SampleResponse("You havent permission");
        }

    }

    private boolean isAdmin(HttpSession httpSession, EntityManager entityManager) {
        String sessionId = (String) httpSession.getAttribute("sessionid");

        TypedQuery<Boolean> query = entityManager.createQuery(
                "SELECT u.isAdmin FROM User u WHERE u.sessionId = " + sessionId, Boolean.class);

        try {
            Boolean isAdmin = query.getSingleResult();
            return isAdmin != null && isAdmin;
        } catch (NoResultException e) {
            return false;
        }
    }

    private boolean isUser(HttpSession httpSession, EntityManager entityManager) {
        String sessionId = (String) httpSession.getAttribute("sessionid");

        TypedQuery<Boolean> query = entityManager.createQuery(
                "SELECT u.isAdmin FROM User u WHERE u.sessionId = " + sessionId, Boolean.class);

        try {
            Boolean isUser = query.getSingleResult();
            return isUser != null && !isUser;
        } catch (NoResultException e) {

            return false;
        }
    }



}
