package br.com.cooperativa.votacao.service;

import br.com.cooperativa.votacao.client.CpfValidationClient;
import br.com.cooperativa.votacao.dto.ResultadoVotacaoDTO;
import br.com.cooperativa.votacao.dto.VotoRequestDTO;
import br.com.cooperativa.votacao.dto.VotoResponseDTO;
import br.com.cooperativa.votacao.entity.Pauta;
import br.com.cooperativa.votacao.entity.SessaoVotacao;
import br.com.cooperativa.votacao.entity.Voto;
import br.com.cooperativa.votacao.exception.SessaoVotacaoFechadaException;
import br.com.cooperativa.votacao.exception.VotoDuplicadoException;
import br.com.cooperativa.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotoService {
    
    private final VotoRepository votoRepository;
    private final SessaoVotacaoService sessaoService;
    private final PautaService pautaService;
    private final CpfValidationClient cpfValidationClient;
    
    @Transactional
    @CacheEvict(value = "resultado", key = "#pautaId")
    public VotoResponseDTO registrarVoto(Long pautaId, VotoRequestDTO request) {
        log.info("Registrando voto para pauta ID: {}, CPF: {}", pautaId, request.getCpfAssociado());
        
        // Valida CPF no serviço externo
        cpfValidationClient.validarCpf(request.getCpfAssociado());
        
        // Busca sessão de votação
        SessaoVotacao sessao = sessaoService.buscarSessaoEntity(pautaId);
        
        // Verifica se sessão está aberta
        if (!sessao.isAberta()) {
            log.warn("Tentativa de voto em sessão fechada. Pauta ID: {}", pautaId);
            throw new SessaoVotacaoFechadaException();
        }
        
        // Verifica se já votou
        if (votoRepository.existsByPautaIdAndCpfAssociado(pautaId, request.getCpfAssociado())) {
            log.warn("Associado já votou. Pauta ID: {}, CPF: {}", pautaId, request.getCpfAssociado());
            throw new VotoDuplicadoException();
        }
        
        // Converte voto
        Voto.Choice choice;
        try {
            choice = Voto.Choice.valueOf(request.getVoto().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Voto deve ser 'SIM' ou 'NAO'");
        }
        
        // Registra voto
        Voto voto = Voto.builder()
                .pauta(sessao.getPauta())
                .cpfAssociado(request.getCpfAssociado())
                .voto(choice)
                .build();
        
        Voto saved = votoRepository.save(voto);
        log.info("Voto registrado com sucesso. ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "resultado", key = "#pautaId")
    public ResultadoVotacaoDTO calcularResultado(Long pautaId) {
        log.info("Calculando resultado para pauta ID: {}", pautaId);
        
        Pauta pauta = pautaService.buscarPautaEntity(pautaId);
        
        Long votosSim = votoRepository.countVotosSim(pautaId);
        Long votosNao = votoRepository.countVotosNao(pautaId);
        Long totalVotos = votoRepository.countTotalVotos(pautaId);
        
        String resultado;
        if (votosSim > votosNao) {
            resultado = "APROVADA";
        } else if (votosNao > votosSim) {
            resultado = "REJEITADA";
        } else {
            resultado = "EMPATE";
        }
        
        log.info("Resultado calculado. Pauta ID: {}, Resultado: {}", pautaId, resultado);
        
        return ResultadoVotacaoDTO.builder()
                .pautaId(pautaId)
                .tituloPauta(pauta.getTitulo())
                .totalVotos(totalVotos)
                .votosSim(votosSim)
                .votosNao(votosNao)
                .resultado(resultado)
                .build();
    }
    
    private VotoResponseDTO mapToDTO(Voto voto) {
        return VotoResponseDTO.builder()
                .id(voto.getId())
                .pautaId(voto.getPauta().getId())
                .cpfAssociado(voto.getCpfAssociado())
                .voto(voto.getVoto().name())
                .dataVoto(voto.getDataVoto())
                .build();
    }
}
