package br.com.cooperativa.votacao.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacaoRequestDTO {
    
    @Builder.Default
    private Integer duracaoSegundos = 60;
}
