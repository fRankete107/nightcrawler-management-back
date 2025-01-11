package n2.nightcrawler.management.controller;

import lombok.RequiredArgsConstructor;
import n2.nightcrawler.management.dto.EmpleadoDTO;
import n2.nightcrawler.management.dto.OrganizacionDTO;
import n2.nightcrawler.management.dto.DepartamentoDTO;
import n2.nightcrawler.management.dto.EmpleadoPublicoDTO;
import n2.nightcrawler.management.model.Empleado;
import n2.nightcrawler.management.model.Role;
import n2.nightcrawler.management.service.EmpleadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {
    
    private final EmpleadoService empleadoService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('MECANICO', 'SUPERVISOR', 'GERENTE')")
    public ResponseEntity<List<Empleado>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.findAll());
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE')")
    public ResponseEntity<Empleado> createEmpleado(@RequestBody EmpleadoDTO empleadoDTO) {
        return ResponseEntity.ok(empleadoService.save(empleadoDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE')")
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable Long id, @RequestBody EmpleadoDTO empleadoDTO) {
        empleadoDTO.setId(id);
        return ResponseEntity.ok(empleadoService.update(empleadoDTO));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/organizacion")
    public ResponseEntity<OrganizacionDTO> getOrganizacion() {
        List<DepartamentoDTO> departamentos = Arrays.asList(
            DepartamentoDTO.builder()
                .nombre("Gerencia")
                .nivel(1)
                .empleados(empleadoService.findByRol(Role.GERENTE).stream()
                    .map(this::toEmpleadoPublicoDTO)
                    .collect(Collectors.toList()))
                .build(),
            DepartamentoDTO.builder()
                .nombre("Supervisión")
                .nivel(2)
                .empleados(empleadoService.findByRol(Role.SUPERVISOR).stream()
                    .map(this::toEmpleadoPublicoDTO)
                    .collect(Collectors.toList()))
                .build(),
            DepartamentoDTO.builder()
                .nombre("Mecánicos")
                .nivel(3)
                .empleados(empleadoService.findByRol(Role.MECANICO).stream()
                    .map(this::toEmpleadoPublicoDTO)
                    .collect(Collectors.toList()))
                .build()
        );

        return ResponseEntity.ok(OrganizacionDTO.builder()
            .departamentos(departamentos)
            .build());
    }

    private EmpleadoPublicoDTO toEmpleadoPublicoDTO(Empleado empleado) {
        return EmpleadoPublicoDTO.builder()
            .nombre(empleado.getNombre())
            .cargo(empleado.getCargoActual())
            .imagen(empleado.getImagen())
            .foroId(empleado.getForoId())
            .build();
    }
} 