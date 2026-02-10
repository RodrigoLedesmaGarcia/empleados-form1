package com.spring.www.empleadosform1.controller;

import com.spring.www.empleadosform1.entity.EmployeeCreateRequest;
import com.spring.www.empleadosform1.entity.EmployeeUpdateRequest;
import com.spring.www.empleadosform1.service.EmployeeServiceImpl;
import com.spring.www.empleadosform1.utils.EmployeeProjection;
import com.spring.www.empleadosform1.utils.OnCreate;
import com.spring.www.empleadosform1.utils.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeServiceImpl service;

    public EmployeeController(EmployeeServiceImpl service) {
        this.service = service;
    }

    // =========================
    // REST (JSON)
    // =========================
    @GetMapping("/api/buscar")
    public ResponseEntity<Map<String, Object>> buscarTodoApi(
            @RequestParam(required = false) Long empNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDate,
            @RequestParam(required = false) String deptNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        firstName = normalize(firstName);
        lastName  = normalize(lastName);
        deptNo    = normalize(deptNo);
        gender    = normalize(gender);

        Page<EmployeeProjection> pageResult = service.buscarConFiltros(
                empNo, birthDate, firstName, lastName, gender, hireDate, deptNo, fromDate, toDate, page, size
        );

        List<Map<String, Object>> empleados = pageResult.getContent().stream()
                .map(emp -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("empNo", emp.get_empNo());
                    map.put("birthDate", emp.get_birthDate());
                    map.put("firstName", emp.get_firstName());
                    map.put("lastName", emp.get_lastName());
                    map.put("gender", emp.get_gender());
                    map.put("hireDate", emp.get_hireDate());
                    map.put("deptNo", emp.get_deptNo());
                    map.put("fromDate", emp.get_fromDate());
                    map.put("toDate", emp.get_toDate());
                    return map;
                })
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("content", empleados);
        result.put("totalPages", pageResult.getTotalPages());
        result.put("totalElements", pageResult.getTotalElements());
        result.put("number", pageResult.getNumber());
        result.put("size", pageResult.getSize());

        return ResponseEntity.ok(result);
    }

    // =========================
    // Vistas (Thymeleaf)
    // =========================
    @GetMapping("/home")
    public String view() {
        return "employees";
    }

    @GetMapping("/buscar")
    public String buscarGet(
            @RequestParam(required = false) Long empNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDate,
            @RequestParam(required = false) String deptNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        return doBuscar(empNo, birthDate, firstName, lastName, gender, hireDate, deptNo, fromDate, toDate, page, size, model);
    }

    @PostMapping("/buscar")
    public String buscarPost(
            @RequestParam(required = false) Long empNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDate,
            @RequestParam(required = false) String deptNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        return doBuscar(empNo, birthDate, firstName, lastName, gender, hireDate, deptNo, fromDate, toDate, page, size, model);
    }

    private String doBuscar(
            Long empNo, LocalDate birthDate, String firstName, String lastName, String gender,
            LocalDate hireDate, String deptNo, LocalDate fromDate, LocalDate toDate,
            int page, int size, Model model
    ) {
        firstName = normalize(firstName);
        lastName  = normalize(lastName);
        deptNo    = normalize(deptNo);
        gender    = normalize(gender);

        Page<EmployeeProjection> result = service.buscarConFiltros(
                empNo, birthDate, firstName, lastName, gender, hireDate, deptNo, fromDate, toDate, page, size
        );

        model.addAttribute("results", result);

        if (empNo != null && result.isEmpty()) {
            model.addAttribute("error", "Usuario no existente");
        }

        model.addAttribute("empNo", empNo);
        model.addAttribute("birthDate", birthDate);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("gender", gender);
        model.addAttribute("hireDate", hireDate);
        model.addAttribute("deptNo", deptNo);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "employees";
    }

    private String normalize(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    // =========================
    // Crear empleado (Thymeleaf)
    // =========================
    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new EmployeeCreateRequest());
        }
        return "new-employee";
    }

    @PostMapping("/nuevo")
    public String crearEmpleadoThymeleaf(
            @Valid @ModelAttribute("employee") EmployeeCreateRequest req,
            BindingResult binding
    ) {
        if (binding.hasErrors()) {
            return "new-employee";
        }
        service.crearEmpleado(req);
        return "redirect:/employees/nuevo?ok";
    }

    // =========================
    // REST create/update (JSON)
    // =========================
    @PostMapping("/api")
    public ResponseEntity<Void> crearApi(
            @Validated(OnCreate.class) @RequestBody EmployeeCreateRequest request) {
        service.crearEmpleado(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/api")
    public ResponseEntity<Void> actualizarApi(
            @Validated(OnUpdate.class) @RequestBody EmployeeUpdateRequest request) {
        service.actualizarEmpleado(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/{empNo}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long empNo) {
        service.eliminarEmpleado(empNo);
        return ResponseEntity.noContent().build(); // 204
    }

}
