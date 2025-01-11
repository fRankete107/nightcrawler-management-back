package n2.nightcrawler.management.controller;

import lombok.RequiredArgsConstructor;
import n2.nightcrawler.management.dto.AuthenticationRequest;
import n2.nightcrawler.management.dto.AuthenticationResponse;
import n2.nightcrawler.management.dto.EmpleadoDTO;
import n2.nightcrawler.management.model.Empleado;
import n2.nightcrawler.management.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody EmpleadoDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticationResponse> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Empleado empleado = (Empleado) auth.getPrincipal();
        
        // Convertir Empleado a EmpleadoDTO para no exponer información sensible
        EmpleadoDTO empleadoDTO = EmpleadoDTO.builder()
                .id(empleado.getId())
                .nombre(empleado.getNombre())
                .nacionalidad(empleado.getNacionalidad())
                .telefono(empleado.getTelefono())
                .residencia(empleado.getResidencia())
                .fechaIngreso(empleado.getFechaIngreso())
                .cargoActual(empleado.getCargoActual())
                .rol(empleado.getRol())
                .username(empleado.getUsername())
                .build();

        return ResponseEntity.ok(AuthenticationResponse.builder()
                .token(null)  // No necesitamos generar un nuevo token
                .empleado(empleadoDTO)
                .build());
    }
} 