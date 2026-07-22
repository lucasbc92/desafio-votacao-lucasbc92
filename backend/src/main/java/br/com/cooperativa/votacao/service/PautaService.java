package br.com.cooperativa.votacao.service;

import br.com.cooperativa.votacao.dto.PautaRequestDTO;
import br.com.cooperativa.votacao.dto.PautaResponseDTO;
import br.com.cooperativa.votacao.entity.Pauta;
import br.com.cooperativa.votacao.exception.PautaNotFoundException;
import br.com.cooperativa.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {
    
    private final PautaRepository pautaRepository;
    
    @Transactional
    public PautaResponseDTO criarPauta(PautaRequestDTO request) {
        log.info("Criando nova pauta: {}", request.getTitulo());
        
        Pauta pauta = Pauta.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .build();
        
        Pauta saved = pautaRepository.save(pauta);
        log.info("Pauta criada com sucesso. ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public List<PautaResponseDTO> listarPautas() {
        log.debug("Listando todas as pautas");
        return pautaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PautaResponseDTO buscarPauta(Long id) {
        log.debug("Buscando pauta com ID: {}", id);
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNotFoundException(id));
        return mapToDTO(pauta);
    }
    
    public Pauta buscarPautaEntity(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNotFoundException(id));
    }
    
    private PautaResponseDTO mapToDTO(Pauta pauta) {
        return PautaResponseDTO.builder()
                .id(pauta.getId())
                .titulo(pauta.getTitulo())
                .descricao(pauta.getDescricao())
                .dataCriacao(pauta.getDataCriacao())
                .build();
    }
}
