package br.com.cooperativa.votacao.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CpfValidationResponseDTO {
    private String status; // "ABLE_TO_VOTE" ou "UNABLE_TO_VOTE"
}
