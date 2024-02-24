package org.example;

import jakarta.persistence.*;

import javax.servlet.http.HttpSession;

@Entity
@Table(name = "user1")
public class User {
  @Id
  @Column(name = "username")
  public String username;
  @Column(name = "password")
  public String password;
  @Column(name = "isadmin")
  public boolean isAdmin;
  @Column(name = "sessionid")
  public String sessionId;

  public String getUser_name() {
    return username;
  }

  public void setUser_name(String user_name) {
    this.username = user_name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isIs_admin() {
    return isAdmin;
  }

  public void setIs_admin(boolean is_admin) {
    this.isAdmin = is_admin;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  private boolean isAdmin(HttpSession httpSession, EntityManager entityManager) {
    String sessionId = (String) httpSession.getAttribute("sessionid");

    TypedQuery<Boolean> query = entityManager.createQuery(
            "SELECT u.isAdmin FROM User u WHERE u.sessionId = :sessionId", Boolean.class);
    query.setParameter("sessionId", sessionId);

    try {
      Boolean isAdmin = query.getSingleResult();
      return isAdmin != null && isAdmin;
    } catch (NoResultException e) {
      // Обробка ситуації, коли не знайдено результату (наприклад, користувач не знайдений)
      return false;
    }
  }

  private boolean isUser(HttpSession httpSession, EntityManager entityManager) {
    String sessionId = (String) httpSession.getAttribute("sessionid");

    TypedQuery<Boolean> query = entityManager.createQuery(
            "SELECT u.isAdmin FROM User u WHERE u.sessionId = :sessionId", Boolean.class);
    query.setParameter("sessionId", sessionId);

    try {
      Boolean isUser = query.getSingleResult();
      return isUser != null && !isUser;
    } catch (NoResultException e) {
      // Обробка ситуації, коли не знайдено результату (наприклад, користувач не знайдений)
      return false;
    }
  }




}