package br.com.cooperativa.votacao.service;

import br.com.cooperativa.votacao.dto.PautaRequestDTO;
import br.com.cooperativa.votacao.dto.PautaResponseDTO;
import br.com.cooperativa.votacao.entity.Pauta;
import br.com.cooperativa.votacao.exception.PautaNotFoundException;
import br.com.cooperativa.votacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {
    
    @Mock
    private PautaRepository pautaRepository;
    
    @InjectMocks
    private PautaService pautaService;
    
    private Pauta pauta;
    
    @BeforeEach
    void setUp() {
        pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta de Teste")
                .descricao("Descrição de Teste")
                .dataCriacao(LocalDateTime.now())
                .build();
    }
    
    @Test
    void deveCriarPautaComSucesso() {
        PautaRequestDTO request = PautaRequestDTO.builder()
                .titulo("Nova Pauta")
                .descricao("Nova Descrição")
                .build();
        
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
        
        PautaResponseDTO response = pautaService.criarPauta(request);
        
        assertNotNull(response);
        assertEquals(pauta.getId(), response.getId());
        assertEquals(pauta.getTitulo(), response.getTitulo());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }
    
    @Test
    void deveListarPautas() {
        List<Pauta> pautas = Arrays.asList(pauta);
        when(pautaRepository.findAll()).thenReturn(pautas);
        
        List<PautaResponseDTO> response = pautaService.listarPautas();
        
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(pautaRepository, times(1)).findAll();
    }
    
    @Test
    void deveBuscarPautaPorId() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        
        PautaResponseDTO response = pautaService.buscarPauta(1L);
        
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(pautaRepository, times(1)).findById(1L);
    }
    
    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(PautaNotFoundException.class, () -> pautaService.buscarPauta(99L));
    }
}
