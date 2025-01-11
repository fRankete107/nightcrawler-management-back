package n2.nightcrawler.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimerLogDTO {
    private Long id;
    private Long empleadoId;
    private String empleadoNombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private Double duracionHoras;
} 