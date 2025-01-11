package n2.nightcrawler.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import n2.nightcrawler.management.model.Role;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {
    private Long id;
    private String nombre;
    private String nacionalidad;
    private String telefono;
    private String residencia;
    private LocalDate fechaIngreso;
    private String cargoActual;
    private Role rol;
    private String username;
    private String password;
    private Boolean activo;
    private String imagen;
    private String foroId;
} 