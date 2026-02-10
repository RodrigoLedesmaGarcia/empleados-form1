package com.spring.www.empleadosform1.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Column(name = "emp_no", nullable = false)
    private Long empNo;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "hire_date" , nullable = false)
    private LocalDate hireDate;

    @Column(name = "first_name" , nullable = false)
    private String firstName;

    @Column(name = "last_name" , nullable = false)
    private String lastName;

    @Column(name = "gender" , nullable = false)
    private String gender;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private List<DeptEmp> departaments = new ArrayList<>();

    public List<DeptEmp> getDepartaments() {
        return departaments;
    }

    public void setDepartaments(List<DeptEmp> departaments) {
        this.departaments = departaments;
    }

    public Employee() {
    }

    public Employee(Long empNo, LocalDate birthDate, LocalDate hireDate, String lastName, String firstName, String gender) {
        this.empNo = empNo;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
    }

    public Long getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Long empNo) {
        this.empNo = empNo;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
