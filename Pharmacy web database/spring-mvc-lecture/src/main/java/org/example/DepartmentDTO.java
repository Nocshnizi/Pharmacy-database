package org.example;

public class DepartmentDTO {

    Integer id;
    String illness_type;
    Integer country_id;

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

    public Integer getCountry_id() {
        return country_id;
    }

    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }
}
