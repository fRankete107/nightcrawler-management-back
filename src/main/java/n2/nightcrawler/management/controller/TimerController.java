package n2.nightcrawler.management.controller;

import lombok.RequiredArgsConstructor;
import n2.nightcrawler.management.dto.TimerDTO;
import n2.nightcrawler.management.dto.TimerLogDTO;
import n2.nightcrawler.management.model.Timer;
import n2.nightcrawler.management.service.TimerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timers")
@RequiredArgsConstructor
public class TimerController {
    
    private final TimerService timerService;
    
    @GetMapping("/empleado/{empleadoId}")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE') or @securityService.isCurrentUser(#empleadoId)")
    public ResponseEntity<List<Timer>> getTimersByEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(timerService.findByEmpleadoId(empleadoId));
    }
    
    @PostMapping
    public ResponseEntity<Timer> createTimer(@RequestBody TimerDTO timerDTO) {
        return ResponseEntity.ok(timerService.save(timerDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE')")
    public ResponseEntity<Timer> updateTimer(@PathVariable Long id, @RequestBody TimerDTO timerDTO) {
        timerDTO.setId(id);
        return ResponseEntity.ok(timerService.update(timerDTO));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE')")
    public ResponseEntity<Void> deleteTimer(@PathVariable Long id) {
        timerService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<Timer> getActiveTimer() {
        Timer activeTimer = timerService.findActiveTimer();
        return activeTimer != null ? 
               ResponseEntity.ok(activeTimer) : 
               ResponseEntity.noContent().build();
    }
    
    @PutMapping("/stop")
    public ResponseEntity<Timer> stopTimer(@RequestBody TimerDTO timerDTO) {
        return ResponseEntity.ok(timerService.stopTimer(timerDTO));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE')")
    public ResponseEntity<List<TimerLogDTO>> getAllTimers() {
        return ResponseEntity.ok(timerService.findAll());
    }
} 