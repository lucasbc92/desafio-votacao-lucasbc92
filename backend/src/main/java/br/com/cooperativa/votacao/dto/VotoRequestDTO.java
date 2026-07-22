package br.com.cooperativa.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoRequestDTO {
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas 11 dígitos numéricos")
    private String cpfAssociado;
    
    @NotNull(message = "Voto é obrigatório")
    private String voto; // "SIM" ou "NAO"
}
