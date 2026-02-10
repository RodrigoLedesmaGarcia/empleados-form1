package com.spring.www.empleadosform1.utils;

import jakarta.persistence.Column;

import java.time.LocalDate;

public interface EmployeeProjection {

    Long get_empNo();

    LocalDate get_birthDate();

    LocalDate get_hireDate();

    String get_firstName();

    String get_lastName();

    String get_gender();

    LocalDate get_fromDate();

    LocalDate get_toDate();

    String get_deptNo();

}
