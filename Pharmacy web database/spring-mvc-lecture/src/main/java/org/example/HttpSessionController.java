package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController

public class HttpSessionController {
        EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("spring-mvc-lecture");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        @Autowired
        private HttpSession httpSession;


        @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
        public SampleResponse getSample() {
            return new SampleResponse("LOGIN");
        }


        @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
                User user = findByUsername(username, password);

                if (user != null ) {
                        String sessionId = generateSessionId();
                        entityManager.getTransaction().begin();
                        user.setSessionId(sessionId);
                        entityManager.persist(user);
                        entityManager.getTransaction().commit();
                        entityManager.merge(user);
                        httpSession.setAttribute("sessionId", sessionId);
                        return ResponseEntity.ok().body("\"sessionId\": \"" + sessionId + "\" ");
                } else {
                        return ResponseEntity.status(401).body("Invalid username or password");
                }

        }
        private User findByUsername(String username, String password) {
                       // entityManager.getTransaction().begin();
                       /* return entityManager.createQuery("SELECT u.isadmin FROM User u WHERE u.sessionid = sessionid(string) AND u.password = 'pipa'", User.class)
                                .setParameter("username", username)
                                .setParameter("password", password)
                                .getSingleResult();*/
                        User user = entityManager.find(User.class, username);
                        if(user.getPassword().equals(password)){
                               // entityManager.getTransaction().commit();
                                return user;
                        }
                      //  entityManager.getTransaction().rollback();
                        return null;


        }

        private String generateSessionId() {
                return UUID.randomUUID().toString();
        }


}
