package n2.nightcrawler.management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimerDTO {
    private Long id;
    private Long empleadoId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String descripcion;
} 