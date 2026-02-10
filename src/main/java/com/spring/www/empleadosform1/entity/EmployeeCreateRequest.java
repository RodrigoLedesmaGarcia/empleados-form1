package com.spring.www.empleadosform1.entity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class EmployeeCreateRequest {

    private Long empNo;

    @NotNull(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "birth_date")
    @NotNull(message = "este campo no puede estar vacío")
    private LocalDate birthDate;

    @NotBlank(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "first_name")
    @NotNull(message = "este campo no puede estar vacío")
    private String firstName;

    @NotBlank(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "last_name")
    @NotNull(message = "este campo no puede estar vacío")
    private String lastName;

    @NotBlank(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "gender")
    @NotNull(message = "este campo no puede estar vacío")
    private String gender;

    @NotNull(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "hire_date")
    @NotNull(message = "este campo no puede estar vacío")
    private LocalDate hireDate;

    @NotBlank(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "dept_no")
    @NotNull(message = "este campo no puede estar vacío")
    private String deptNo;


    @NotNull(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "from_date")
    @NotNull(message = "este campo no puede estar vacío")
    private LocalDate fromDate;

    @NotNull(message = "este campo no puede estar vacio")
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE)
    @Column(name = "to_date")
    @NotNull(message = "este campo no puede estar vacío")
    private LocalDate toDate;

    public EmployeeCreateRequest() {
    }

    public EmployeeCreateRequest(Long empNo, LocalDate birthDate, String firstName, String lastName, String gender, LocalDate hireDate, String deptNo, LocalDate fromDate, LocalDate toDate) {
        this.empNo = empNo;
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.hireDate = hireDate;
        this.deptNo = deptNo;
        this.fromDate = fromDate;
        this.toDate = toDate;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
