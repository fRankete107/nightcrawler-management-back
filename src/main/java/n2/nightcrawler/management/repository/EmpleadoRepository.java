package n2.nightcrawler.management.repository;

import n2.nightcrawler.management.model.Empleado;
import n2.nightcrawler.management.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByUsername(String username);
    boolean existsByUsername(String username);
    List<Empleado> findByRol(Role rol);
} 