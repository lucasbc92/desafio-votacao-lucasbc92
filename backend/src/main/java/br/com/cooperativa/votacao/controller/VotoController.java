package br.com.cooperativa.votacao.controller;

import br.com.cooperativa.votacao.dto.ResultadoVotacaoDTO;
import br.com.cooperativa.votacao.dto.VotoRequestDTO;
import br.com.cooperativa.votacao.dto.VotoResponseDTO;
import br.com.cooperativa.votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pautas/{pautaId}/votos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Votos", description = "APIs para registro e consulta de votos")
public class VotoController {
    
    private final VotoService votoService;
    
    @PostMapping
    @Operation(summary = "Registrar voto", description = "Registra um voto para uma pauta")
    @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Sessão fechada ou dados inválidos")
    @ApiResponse(responseCode = "404", description = "CPF inválido")
    @ApiResponse(responseCode = "422", description = "Associado já votou")
    public ResponseEntity<VotoResponseDTO> registrarVoto(
            @PathVariable Long pautaId,
            @Valid @RequestBody VotoRequestDTO request) {
        
        log.info("POST /pautas/{}/votos - Registrando voto", pautaId);
        VotoResponseDTO response = votoService.registrarVoto(pautaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/resultado")
    @Operation(summary = "Calcular resultado", description = "Calcula o resultado da votação de uma pauta")
    @ApiResponse(responseCode = "200", description = "Resultado calculado com sucesso")
    public ResponseEntity<ResultadoVotacaoDTO> calcularResultado(@PathVariable Long pautaId) {
        log.info("GET /pautas/{}/votos/resultado - Calculando resultado", pautaId);
        ResultadoVotacaoDTO resultado = votoService.calcularResultado(pautaId);
        return ResponseEntity.ok(resultado);
    }
}
