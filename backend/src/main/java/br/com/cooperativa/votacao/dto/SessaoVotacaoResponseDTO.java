package br.com.cooperativa.votacao.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacaoResponseDTO {
    private Long id;
    private Long pautaId;
    private String tituloPauta;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private Integer duracaoSegundos;
    private Boolean aberta;
}
