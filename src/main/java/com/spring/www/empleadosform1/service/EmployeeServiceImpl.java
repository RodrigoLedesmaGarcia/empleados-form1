package com.spring.www.empleadosform1.service;

import com.spring.www.empleadosform1.entity.*;
import com.spring.www.empleadosform1.repository.EmployeesMainRepository;
import com.spring.www.empleadosform1.utils.EmployeeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeesMainRepository repository;

    public EmployeeServiceImpl(EmployeesMainRepository repository) {
        this.repository = repository;
    }

    private String blankToNull(String string){
        return (string == null) ? null : string.trim();
    }

    @Override
    public Page<EmployeeProjection> buscarConFiltros(Long empNo, LocalDate birthDate, String firstName, String lastName, String gender, LocalDate hireDate, String deptNo, LocalDate fromDate, LocalDate toDate, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.buscarConFiltros(
                empNo == null ? null : empNo.longValue(),
                birthDate,
                blankToNull(firstName),
                blankToNull(lastName),
                blankToNull(gender),
                hireDate,
                blankToNull(deptNo),
                fromDate,
                toDate,
                pageable
        );
    }

    @Override
    public void crearEmpleado(EmployeeCreateRequest request) {

        long nextEmpNo = repository.maxEmpNo() + 1;

        Employee employee = new Employee();
        employee.setEmpNo(nextEmpNo);
        employee.setBirthDate(request.getBirthDate());
        employee.setLastName(request.getLastName());
        employee.setGender(request.getGender());
        employee.setHireDate(request.getHireDate());


        DeptEmp departmentEmployee = new DeptEmp();
        departmentEmployee.setId(new DeptEmpId(nextEmpNo, request.getDeptNo()));
        departmentEmployee.setFromDate(request.getFromDate());
        departmentEmployee.setToDate(request.getToDate());

        employee.getDepartaments().add(departmentEmployee);
        repository.save(employee);

    }


    @Transactional
    @Override
    public void actualizarEmpleado(EmployeeUpdateRequest request) {

        if (request.getEmpNo() == null) {
            throw new IllegalArgumentException("empNo es obligatorio para actualizar");
        }

        Employee employee = repository.findById(request.getEmpNo())
                .orElseThrow(() -> new RuntimeException("No existe emp_no=" + request.getEmpNo()));

        // 1) Actualiza campos de employees (solo si vienen en request)
        if (request.getBirthDate() != null) employee.setBirthDate(request.getBirthDate());
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty())
            employee.setFirstName(request.getFirstName().trim());
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty())
            employee.setLastName(request.getLastName().trim());
        if (request.getGender() != null && !request.getGender().trim().isEmpty())
            employee.setGender(request.getGender().trim());
        if (request.getHireDate() != null) employee.setHireDate(request.getHireDate());

        // 2) Actualiza dept_emp (si viene info de dept)
        boolean vieneDept = request.getDeptNo() != null && !request.getDeptNo().trim().isEmpty();
        boolean vieneFechasDept = request.getFromDate() != null || request.getToDate() != null;

        if (vieneDept || vieneFechasDept) {

            if (employee.getDepartaments() == null || employee.getDepartaments().isEmpty()) {
                // Si no existe relación dept_emp, la creas
                if (!vieneDept) {
                    throw new IllegalArgumentException("deptNo es obligatorio si el empleado no tiene dept_emp");
                }
                DeptEmp nuevo = new DeptEmp();
                nuevo.setId(new DeptEmpId(employee.getEmpNo(), request.getDeptNo().trim()));
                nuevo.setFromDate(request.getFromDate() != null ? request.getFromDate() : LocalDate.now());
                nuevo.setToDate(request.getToDate() != null ? request.getToDate() : LocalDate.of(9999, 1, 1));


                employee.getDepartaments().add(nuevo);

            } else {
                // Tomo el "activo" (ejemplo: el primero). Si manejas historial, aquí defines tu regla.
                DeptEmp actual = employee.getDepartaments().get(0);

                // Si cambia deptNo: NO puedes "editar" el id compuesto; debes reemplazar el registro.
                if (vieneDept) {
                    String nuevoDept = request.getDeptNo().trim();
                    String deptActual = actual.getId() != null ? actual.getId().getDeptNo() : null;

                    if (deptActual == null || !deptActual.equals(nuevoDept)) {
                        // quitar el actual (orphanRemoval lo elimina) y crear uno nuevo
                        employee.getDepartaments().remove(actual);

                        DeptEmp reemplazo = new DeptEmp();
                        reemplazo.setId(new DeptEmpId(employee.getEmpNo(), nuevoDept));
                        reemplazo.setFromDate(request.getFromDate() != null ? request.getFromDate() : actual.getFromDate());
                        reemplazo.setToDate(request.getToDate() != null ? request.getToDate() : actual.getToDate());

                        // Si ya usas @MapsId:
                        // reemplazo.setEmployee(employee);

                        employee.getDepartaments().add(reemplazo);
                    }
                }

                // Si solo cambian fechas del dept_emp (y no cambió deptNo)
                DeptEmp deptFinal = employee.getDepartaments().get(0);
                if (request.getFromDate() != null) deptFinal.setFromDate(request.getFromDate());
                if (request.getToDate() != null) deptFinal.setToDate(request.getToDate());
            }
        }

        // No es estrictamente necesario llamar save() dentro de @Transactional si la entidad está managed,
        // pero lo dejo explícito por claridad.
        repository.save(employee);
    }

    @Transactional
    @Override
    public void eliminarEmpleado(Long empNo) {
        Employee employee = repository.findById(empNo)
                .orElseThrow(() -> new RuntimeException("No existe emp_no=" + empNo));

        employee.getDepartaments().clear();

        repository.delete(employee);
    }
}
