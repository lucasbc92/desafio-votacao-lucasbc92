package br.com.cooperativa.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaRequestDTO {
    
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    
    private String descricao;
}
