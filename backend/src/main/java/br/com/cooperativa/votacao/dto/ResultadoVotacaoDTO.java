package br.com.cooperativa.votacao.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoVotacaoDTO {
    private Long pautaId;
    private String tituloPauta;
    private Long totalVotos;
    private Long votosSim;
    private Long votosNao;
    private String resultado; // "APROVADA", "REJEITADA", ou "EMPATE"
}
