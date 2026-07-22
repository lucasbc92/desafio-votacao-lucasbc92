package br.com.cooperativa.votacao.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
}
