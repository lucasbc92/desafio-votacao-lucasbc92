package br.com.cooperativa.votacao.controller;

import br.com.cooperativa.votacao.dto.PautaRequestDTO;
import br.com.cooperativa.votacao.dto.PautaResponseDTO;
import br.com.cooperativa.votacao.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pautas", description = "APIs para gerenciamento de pautas")
public class PautaController {
    
    private final PautaService pautaService;
    
    @PostMapping
    @Operation(summary = "Criar nova pauta", description = "Cria uma nova pauta para votação")
    @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso")
    public ResponseEntity<PautaResponseDTO> criarPauta(@Valid @RequestBody PautaRequestDTO request) {
        log.info("POST /pautas - Criando nova pauta");
        PautaResponseDTO response = pautaService.criarPauta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Listar pautas", description = "Lista todas as pautas cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de pautas retornada com sucesso")
    public ResponseEntity<List<PautaResponseDTO>> listarPautas() {
        log.info("GET /pautas - Listando pautas");
        List<PautaResponseDTO> pautas = pautaService.listarPautas();
        return ResponseEntity.ok(pautas);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pauta", description = "Busca uma pauta específica por ID")
    @ApiResponse(responseCode = "200", description = "Pauta encontrada")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    public ResponseEntity<PautaResponseDTO> buscarPauta(@PathVariable Long id) {
        log.info("GET /pautas/{} - Buscando pauta", id);
        PautaResponseDTO pauta = pautaService.buscarPauta(id);
        return ResponseEntity.ok(pauta);
    }
}
