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
public class MedicineController {

        @Autowired
        private HttpSession httpSession;
        EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("spring-mvc-lecture");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        @GetMapping(value = "/medicine", produces = MediaType.APPLICATION_JSON_VALUE)
        public SampleResponse getSample() {
            return new SampleResponse("You editing MEDICINE");
        }

        @GetMapping(value = "/medicineRead", produces = MediaType.APPLICATION_JSON_VALUE)
        public String getMedicineRead() {
            Medicine medicine = new Medicine();
            return medicine.Read(entityManager);
        }


    @PostMapping(value = "/medicineInsrt", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postMedicineInsert(@RequestBody MedicineDTO medicineDto){
            if(isAdmin(httpSession, entityManager) || isUser(httpSession, entityManager)) {
                Medicine medicine = new Medicine();
                medicine.Insert(medicineDto.getId(), medicineDto.getName(), medicineDto.getDepartment_id(), entityManager);
            }else{
                new SampleResponse("You are not login");
            }

        }

        @PostMapping(value = "/medicineUp", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postMedicineUpdate(@RequestBody MedicineDTO medicineDto){
            if(isAdmin(httpSession, entityManager) || isUser(httpSession, entityManager)) {
                Medicine medicine = new Medicine();
                medicine.Update(medicineDto.getId(), medicineDto.getName(), medicineDto.getDepartment_id(), entityManager);
            }else{
                new SampleResponse("You are not login");
            }
        }

        @PostMapping(value = "/medicineDlt", produces = MediaType.APPLICATION_JSON_VALUE)
        public void postMedicineDelete(@RequestBody Medicine medicine){
            if(isAdmin(httpSession, entityManager)) {
                medicine.Delete(medicine.getId(), entityManager);
            }else{
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
