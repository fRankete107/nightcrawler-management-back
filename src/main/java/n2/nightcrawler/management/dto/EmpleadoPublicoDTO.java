package n2.nightcrawler.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoPublicoDTO {
    private String nombre;
    private String cargo;
    private String imagen;
    private String foroId;
} 