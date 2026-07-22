package br.com.cooperativa.votacao.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoResponseDTO {
    private Long id;
    private Long pautaId;
    private String cpfAssociado;
    private String voto;
    private LocalDateTime dataVoto;
}
