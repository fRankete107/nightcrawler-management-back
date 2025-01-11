package n2.nightcrawler.management.repository;

import n2.nightcrawler.management.model.Timer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findByEmpleadoId(Long empleadoId);
    Optional<Timer> findByEmpleadoIdAndFechaFinIsNull(Long empleadoId);
    Optional<Timer> findByFechaFinIsNullAndEmpleado_Username(String username);
} 