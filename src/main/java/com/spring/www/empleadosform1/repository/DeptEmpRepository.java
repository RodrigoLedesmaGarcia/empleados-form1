package com.spring.www.empleadosform1.repository;

import com.spring.www.empleadosform1.entity.DeptEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptEmpRepository extends JpaRepository<DeptEmp, Long> {
}
