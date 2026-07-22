package br.com.cooperativa.votacao.controller;

import br.com.cooperativa.votacao.dto.SessaoVotacaoRequestDTO;
import br.com.cooperativa.votacao.dto.SessaoVotacaoResponseDTO;
import br.com.cooperativa.votacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pautas/{pautaId}/sessoes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sessões de Votação", description = "APIs para gerenciamento de sessões de votação")
public class SessaoVotacaoController {
    
    private final SessaoVotacaoService sessaoService;
    
    @PostMapping
    @Operation(summary = "Abrir sessão de votação", description = "Abre uma sessão de votação para uma pauta")
    @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso")
    @ApiResponse(responseCode = "422", description = "Já existe sessão aberta para esta pauta")
    public ResponseEntity<SessaoVotacaoResponseDTO> abrirSessao(
            @PathVariable Long pautaId,
            @RequestBody(required = false) SessaoVotacaoRequestDTO request) {
        
        log.info("POST /pautas/{}/sessoes - Abrindo sessão de votação", pautaId);
        
        if (request == null) {
            request = SessaoVotacaoRequestDTO.builder().duracaoSegundos(60).build();
        }
        
        SessaoVotacaoResponseDTO response = sessaoService.abrirSessao(pautaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Buscar sessão", description = "Busca a sessão de votação de uma pauta")
    @ApiResponse(responseCode = "200", description = "Sessão encontrada")
    @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    public ResponseEntity<SessaoVotacaoResponseDTO> buscarSessao(@PathVariable Long pautaId) {
        log.info("GET /pautas/{}/sessoes - Buscando sessão", pautaId);
        SessaoVotacaoResponseDTO sessao = sessaoService.buscarSessao(pautaId);
        return ResponseEntity.ok(sessao);
    }
}
