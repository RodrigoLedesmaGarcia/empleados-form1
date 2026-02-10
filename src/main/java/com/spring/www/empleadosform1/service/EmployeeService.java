package com.spring.www.empleadosform1.service;

import com.spring.www.empleadosform1.entity.EmployeeCreateRequest;
import com.spring.www.empleadosform1.entity.EmployeeUpdateRequest;
import com.spring.www.empleadosform1.utils.EmployeeProjection;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface EmployeeService {
    Page<EmployeeProjection> buscarConFiltros(
            Long empNo,
            LocalDate birthDate,
            String firstName,
            String lastName,
            String gender,
            LocalDate hireDate,
            String deptNo,
            LocalDate fromDate,
            LocalDate toDate,
            int page,
            int size
    );

    void crearEmpleado(EmployeeCreateRequest request);

    void actualizarEmpleado (EmployeeUpdateRequest request);

    void eliminarEmpleado (Long empNo);
}
