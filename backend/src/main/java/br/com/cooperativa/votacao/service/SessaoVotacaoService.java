package br.com.cooperativa.votacao.service;

import br.com.cooperativa.votacao.dto.SessaoVotacaoRequestDTO;
import br.com.cooperativa.votacao.dto.SessaoVotacaoResponseDTO;
import br.com.cooperativa.votacao.entity.Pauta;
import br.com.cooperativa.votacao.entity.SessaoVotacao;
import br.com.cooperativa.votacao.exception.SessaoVotacaoJaAbertaException;
import br.com.cooperativa.votacao.exception.SessaoVotacaoNotFoundException;
import br.com.cooperativa.votacao.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessaoVotacaoService {
    
    private final SessaoVotacaoRepository sessaoRepository;
    private final PautaService pautaService;
    
    @Transactional
    public SessaoVotacaoResponseDTO abrirSessao(Long pautaId, SessaoVotacaoRequestDTO request) {
        log.info("Abrindo sessão de votação para pauta ID: {}", pautaId);
        
        // Verifica se já existe sessão para esta pauta
        if (sessaoRepository.existsByPautaId(pautaId)) {
            log.warn("Já existe sessão para a pauta ID: {}", pautaId);
            throw new SessaoVotacaoJaAbertaException();
        }
        
        Pauta pauta = pautaService.buscarPautaEntity(pautaId);
        
        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .duracaoSegundos(request.getDuracaoSegundos())
                .build();
        
        SessaoVotacao saved = sessaoRepository.save(sessao);
        log.info("Sessão aberta com sucesso. ID: {}, Duração: {}s", saved.getId(), saved.getDuracaoSegundos());
        
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public SessaoVotacaoResponseDTO buscarSessao(Long pautaId) {
        log.debug("Buscando sessão para pauta ID: {}", pautaId);
        SessaoVotacao sessao = sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new SessaoVotacaoNotFoundException(pautaId));
        return mapToDTO(sessao);
    }
    
    public SessaoVotacao buscarSessaoEntity(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new SessaoVotacaoNotFoundException(pautaId));
    }
    
    private SessaoVotacaoResponseDTO mapToDTO(SessaoVotacao sessao) {
        return SessaoVotacaoResponseDTO.builder()
                .id(sessao.getId())
                .pautaId(sessao.getPauta().getId())
                .tituloPauta(sessao.getPauta().getTitulo())
                .dataAbertura(sessao.getDataAbertura())
                .dataFechamento(sessao.getDataFechamento())
                .duracaoSegundos(sessao.getDuracaoSegundos())
                .aberta(sessao.isAberta())
                .build();
    }
}
