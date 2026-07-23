package br.com.cooperativa.votacao.client;

import br.com.cooperativa.votacao.dto.CpfValidationResponseDTO;
import br.com.cooperativa.votacao.exception.CpfInvalidoException;
import br.com.cooperativa.votacao.exception.CpfNaoAutorizadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Facade/Client Fake para validação de CPF.
 * Simula um serviço externo que valida CPFs.
 */
@Component
@Slf4j
public class CpfValidationClient {
    
    private final Random random = new Random();
    
    /**
     * Valida se um CPF é válido e se pode votar.
     * 
     * @param cpf CPF a ser validado (apenas números)
     * @return Status de validação
     * @throws CpfInvalidoException se o CPF for inválido
     * @throws CpfNaoAutorizadoException se o CPF não puder votar
     */
    public CpfValidationResponseDTO validarCpf(String cpf) {
        log.debug("Validando CPF: {}", cpf);
        
        // Validação básica do CPF
        if (!isCpfValido(cpf)) {
            log.warn("CPF inválido: {}", cpf);
            throw new CpfInvalidoException();
        }
        
        // Simula validação aleatória (70% de chance de poder votar)
        boolean podeVotar = random.nextDouble() < 0.7;
        
        if (!podeVotar) {
            log.warn("CPF não autorizado a votar: {}", cpf);
            throw new CpfNaoAutorizadoException();
        }
        
        log.info("CPF validado com sucesso: {}", cpf);
        return CpfValidationResponseDTO.builder()
                .status("ABLE_TO_VOTE")
                .build();
    }
    
    /**
     * Validação básica de CPF (apenas verifica se tem 11 dígitos).
     * Em produção, implementaria a validação completa dos dígitos verificadores.
     */
    private boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais (CPF inválido)
        long distinctCpfCharsCount = cpf.chars().distinct().count();
        return distinctCpfCharsCount != 1;
    }
}
