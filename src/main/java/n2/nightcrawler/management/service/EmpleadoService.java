package n2.nightcrawler.management.service;

import lombok.RequiredArgsConstructor;
import n2.nightcrawler.management.dto.EmpleadoDTO;
import n2.nightcrawler.management.model.Empleado;
import n2.nightcrawler.management.model.Role;
import n2.nightcrawler.management.repository.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    public Empleado save(EmpleadoDTO empleadoDTO) {
        Empleado empleado = new Empleado();
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setNacionalidad(empleadoDTO.getNacionalidad());
        empleado.setTelefono(empleadoDTO.getTelefono());
        empleado.setResidencia(empleadoDTO.getResidencia());
        empleado.setFechaIngreso(empleadoDTO.getFechaIngreso());
        empleado.setCargoActual(empleadoDTO.getCargoActual());
        empleado.setRol(empleadoDTO.getRol());
        empleado.setUsername(empleadoDTO.getUsername());
        empleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        empleado.setImagen(empleadoDTO.getImagen());
        empleado.setForoId(empleadoDTO.getForoId());
        
        return empleadoRepository.save(empleado);
    }

    public Empleado update(EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(empleadoDTO.getId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
                
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setNacionalidad(empleadoDTO.getNacionalidad());
        empleado.setTelefono(empleadoDTO.getTelefono());
        empleado.setResidencia(empleadoDTO.getResidencia());
        empleado.setCargoActual(empleadoDTO.getCargoActual());
        empleado.setRol(empleadoDTO.getRol());
        
        if (empleadoDTO.getPassword() != null && !empleadoDTO.getPassword().isEmpty()) {
            empleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        }
        
        empleado.setImagen(empleadoDTO.getImagen());
        empleado.setForoId(empleadoDTO.getForoId());
        
        return empleadoRepository.save(empleado);
    }

    public void delete(Long id) {
        empleadoRepository.deleteById(id);
    }

    public List<Empleado> findByRol(Role rol) {
        return empleadoRepository.findByRol(rol);
    }
} 