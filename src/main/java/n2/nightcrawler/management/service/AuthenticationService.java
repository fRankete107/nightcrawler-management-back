package n2.nightcrawler.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import n2.nightcrawler.management.config.JwtService;
import n2.nightcrawler.management.dto.AuthenticationRequest;
import n2.nightcrawler.management.dto.AuthenticationResponse;
import n2.nightcrawler.management.dto.EmpleadoDTO;
import n2.nightcrawler.management.model.Empleado;
import n2.nightcrawler.management.repository.EmpleadoRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(EmpleadoDTO request) {
        var empleado = Empleado.builder()
                .nombre(request.getNombre())
                .nacionalidad(request.getNacionalidad())
                .telefono(request.getTelefono())
                .residencia(request.getResidencia())
                .fechaIngreso(request.getFechaIngreso())
                .cargoActual(request.getCargoActual())
                .rol(request.getRol())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        empleadoRepository.save(empleado);
        var jwtToken = jwtService.generateToken(empleado);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            log.debug("Intentando autenticar usuario: {}", request.getUsername());
            
            // Verificar si el usuario existe
            var empleado = empleadoRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            log.debug("Usuario encontrado: {}", empleado.getUsername());
            
            // Intentar autenticar
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            log.debug("Autenticación exitosa, generando token");
            
            var jwtToken = jwtService.generateToken(empleado);

            // Convertir Empleado a EmpleadoDTO
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

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .empleado(empleadoDTO)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error durante la autenticación: ", e);
            throw new RuntimeException("Error de autenticación: " + e.getMessage());
        }
    }
} 