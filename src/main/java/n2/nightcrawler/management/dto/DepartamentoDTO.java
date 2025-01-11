package n2.nightcrawler.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoDTO {
    private String nombre;
    private int nivel;
    private List<EmpleadoPublicoDTO> empleados;
}

