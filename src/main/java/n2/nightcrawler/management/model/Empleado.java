package n2.nightcrawler.management.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empleados")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
public class Empleado implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String nacionalidad;
    
    @Column(nullable = false)
    private String telefono;
    
    @Column(nullable = false)
    private String residencia;
    
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
    
    @Column(name = "cargo_actual", nullable = false)
    private String cargoActual;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rol;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private Boolean activo = false;
    
    @Column
    private String imagen;
    
    @Column(name = "foro_id")
    private String foroId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
} 