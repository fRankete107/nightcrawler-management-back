package n2.nightcrawler.management.service;

import lombok.RequiredArgsConstructor;
import n2.nightcrawler.management.dto.TimerDTO;
import n2.nightcrawler.management.dto.TimerLogDTO;
import n2.nightcrawler.management.model.Empleado;
import n2.nightcrawler.management.model.Timer;
import n2.nightcrawler.management.repository.EmpleadoRepository;
import n2.nightcrawler.management.repository.TimerRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerService {
    private final TimerRepository timerRepository;
    private final EmpleadoRepository empleadoRepository;

    public List<Timer> findByEmpleadoId(Long empleadoId) {
        return timerRepository.findByEmpleadoId(empleadoId);
    }

    public Timer save(TimerDTO timerDTO) {
        var empleado = empleadoRepository.findById(timerDTO.getEmpleadoId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Si solo recibimos descripción, iniciamos timer y activamos empleado
        if (timerDTO.getFechaInicio() == null && timerDTO.getFechaFin() == null) {
            Timer timer = new Timer();
            timer.setEmpleado(empleado);
            timer.setDescripcion(timerDTO.getDescripcion());
            timer.setFechaInicio(LocalDateTime.now());
            
            // Activar empleado
            empleado.setActivo(true);
            empleadoRepository.save(empleado);
            
            return timerRepository.save(timer);
        }
        
        // Si recibimos fechas, finalizamos timer y desactivamos empleado
        Timer timer = timerRepository.findByEmpleadoIdAndFechaFinIsNull(timerDTO.getEmpleadoId())
                .orElseThrow(() -> new RuntimeException("No hay timer activo para este empleado"));
        
        timer.setFechaFin(LocalDateTime.now());
        
        // Desactivar empleado
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
        
        return timerRepository.save(timer);
    }

    public Timer update(TimerDTO timerDTO) {
        Timer timer = timerRepository.findById(timerDTO.getId())
                .orElseThrow(() -> new RuntimeException("Timer no encontrado"));
                
        timer.setFechaInicio(timerDTO.getFechaInicio());
        timer.setFechaFin(timerDTO.getFechaFin());
        timer.setDescripcion(timerDTO.getDescripcion());
        
        return timerRepository.save(timer);
    }

    public void delete(Long id) {
        timerRepository.deleteById(id);
    }

    public Timer findActiveTimer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return timerRepository.findByFechaFinIsNullAndEmpleado_Username(username)
                .orElse(null);
    }

    public Timer stopTimer(TimerDTO timerDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Timer activeTimer = timerRepository.findByFechaFinIsNullAndEmpleado_Username(username)
                .orElseThrow(() -> new RuntimeException("No hay timer activo para finalizar"));
        
        activeTimer.setFechaFin(LocalDateTime.now());
        
        // Desactivar empleado
        Empleado empleado = activeTimer.getEmpleado();
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
        
        return timerRepository.save(activeTimer);
    }

    public List<TimerLogDTO> findAll() {
        return timerRepository.findAll().stream()
            .<TimerLogDTO>map(timer -> {
                double duracionHoras = 0.0;
                if (timer.getFechaFin() != null) {
                    duracionHoras = java.time.Duration.between(
                        timer.getFechaInicio(), 
                        timer.getFechaFin()
                    ).toMinutes() / 60.0;
                }

                return TimerLogDTO.builder()
                    .id(timer.getId())
                    .empleadoId(timer.getEmpleado().getId())
                    .empleadoNombre(timer.getEmpleado().getNombre())
                    .descripcion(timer.getDescripcion())
                    .fechaInicio(timer.getFechaInicio().toString())
                    .fechaFin(timer.getFechaFin() != null ? timer.getFechaFin().toString() : null)
                    .duracionHoras(duracionHoras)
                    .build();
            })
            .collect(Collectors.toList());
    }
} 