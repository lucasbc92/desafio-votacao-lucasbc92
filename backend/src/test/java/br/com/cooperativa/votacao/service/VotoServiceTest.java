package br.com.cooperativa.votacao.service;

import br.com.cooperativa.votacao.client.CpfValidationClient;
import br.com.cooperativa.votacao.dto.CpfValidationResponseDTO;
import br.com.cooperativa.votacao.dto.ResultadoVotacaoDTO;
import br.com.cooperativa.votacao.dto.VotoRequestDTO;
import br.com.cooperativa.votacao.entity.Pauta;
import br.com.cooperativa.votacao.entity.SessaoVotacao;
import br.com.cooperativa.votacao.entity.Voto;
import br.com.cooperativa.votacao.exception.SessaoVotacaoFechadaException;
import br.com.cooperativa.votacao.exception.VotoDuplicadoException;
import br.com.cooperativa.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {
    
    @Mock
    private VotoRepository votoRepository;
    
    @Mock
    private SessaoVotacaoService sessaoService;
    
    @Mock
    private PautaService pautaService;
    
    @Mock
    private CpfValidationClient cpfValidationClient;
    
    @InjectMocks
    private VotoService votoService;
    
    private Pauta pauta;
    private SessaoVotacao sessao;
    
    @BeforeEach
    void setUp() {
        pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta de Teste")
                .build();
        
        sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(pauta)
                .dataAbertura(LocalDateTime.now())
                .duracaoSegundos(60)
                .build();
    }
    
    @Test
    void deveRegistrarVotoComSucesso() {
        VotoRequestDTO request = VotoRequestDTO.builder()
                .cpfAssociado("12345678901")
                .voto("SIM")
                .build();
        
        when(cpfValidationClient.validarCpf(anyString()))
                .thenReturn(CpfValidationResponseDTO.builder().status("ABLE_TO_VOTE").build());
        when(sessaoService.buscarSessaoEntity(1L)).thenReturn(sessao);
        when(votoRepository.existsByPautaIdAndCpfAssociado(1L, "12345678901")).thenReturn(false);
        
        Voto voto = Voto.builder()
                .id(1L)
                .pauta(pauta)
                .cpfAssociado("12345678901")
                .voto(Voto.Choice.SIM)
                .dataVoto(LocalDateTime.now())
                .build();
        
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);
        
        var response = votoService.registrarVoto(1L, request);
        
        assertNotNull(response);
        assertEquals("SIM", response.getVoto());
        verify(votoRepository, times(1)).save(any(Voto.class));
    }
    
    @Test
    void deveLancarExcecaoQuandoSessaoFechada() {
        VotoRequestDTO request = VotoRequestDTO.builder()
                .cpfAssociado("12345678901")
                .voto("SIM")
                .build();
        
        sessao.setDataAbertura(LocalDateTime.now().minusMinutes(2));
        
        when(cpfValidationClient.validarCpf(anyString()))
                .thenReturn(CpfValidationResponseDTO.builder().status("ABLE_TO_VOTE").build());
        when(sessaoService.buscarSessaoEntity(1L)).thenReturn(sessao);
        
        assertThrows(SessaoVotacaoFechadaException.class, () -> votoService.registrarVoto(1L, request));
    }
    
    @Test
    void deveLancarExcecaoQuandoVotoDuplicado() {
        VotoRequestDTO request = VotoRequestDTO.builder()
                .cpfAssociado("12345678901")
                .voto("SIM")
                .build();
        
        when(cpfValidationClient.validarCpf(anyString()))
                .thenReturn(CpfValidationResponseDTO.builder().status("ABLE_TO_VOTE").build());
        when(sessaoService.buscarSessaoEntity(1L)).thenReturn(sessao);
        when(votoRepository.existsByPautaIdAndCpfAssociado(1L, "12345678901")).thenReturn(true);
        
        assertThrows(VotoDuplicadoException.class, () -> votoService.registrarVoto(1L, request));
    }
    
    @Test
    void deveCalcularResultadoAprovada() {
        when(pautaService.buscarPautaEntity(1L)).thenReturn(pauta);
        when(votoRepository.countVotosSim(1L)).thenReturn(10L);
        when(votoRepository.countVotosNao(1L)).thenReturn(5L);
        when(votoRepository.countTotalVotos(1L)).thenReturn(15L);
        
        ResultadoVotacaoDTO resultado = votoService.calcularResultado(1L);
        
        assertNotNull(resultado);
        assertEquals("APROVADA", resultado.getResultado());
        assertEquals(15L, resultado.getTotalVotos());
    }
}
