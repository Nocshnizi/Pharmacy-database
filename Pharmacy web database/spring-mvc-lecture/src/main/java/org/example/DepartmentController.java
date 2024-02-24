package org.example;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class DepartmentController {
        @Autowired
        private HttpSession httpSession;
        EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("spring-mvc-lecture");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        @GetMapping(value = "/department", produces = MediaType.APPLICATION_JSON_VALUE)
        public SampleResponse getSample() {
            return new SampleResponse("You editing DEPARTMENT");
        }

        @GetMapping(value = "/departmentRead", produces = MediaType.APPLICATION_JSON_VALUE)
        public String getDepartmentRead() {
            Department department = new Department();
            return department.Read(entityManager);
        }

        @PostMapping(value = "/departmentInsrt", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postDepartmentInsert( @RequestBody DepartmentDTO departmentDto) {
            if (isAdmin(httpSession, entityManager) || isUser(httpSession, entityManager)) {
                Department department = new Department();
                department.Insert(departmentDto.getId(), departmentDto.getIllness_type(), departmentDto.getCountry_id(), entityManager);
            }else{
                new SampleResponse("You are not login");
            }
        }


        @PostMapping(value = "/departmentUp", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postDepartmentUpdate(@RequestBody DepartmentDTO departmentDto) {
            if (isAdmin(httpSession, entityManager) || isUser(httpSession, entityManager)) {
                Department department = new Department();
                department.Update(departmentDto.getId(), departmentDto.getIllness_type(), departmentDto.getCountry_id(), entityManager);
            } else {
                new SampleResponse("You are not login");
            }
        }

        @PostMapping(value = "/departmentDlt", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postDepartmentDelete(@RequestBody DepartmentDTO departmentDto){
            if(isAdmin(httpSession, entityManager)) {
                Department department = new Department();
                department.Delete(departmentDto.getId(), entityManager);
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
