package br.com.cooperativa.votacao.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessao_votacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false, unique = true)
    private Pauta pauta;
    
    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;
    
    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;
    
    @Column(nullable = false)
    private Integer duracaoSegundos;
    
    @PrePersist
    protected void onCreate() {
        dataAbertura = LocalDateTime.now();
    }
    
    public boolean isAberta() {
        if (dataFechamento != null) {
            return false;
        }
        LocalDateTime dataLimite = dataAbertura.plusSeconds(duracaoSegundos);
        return LocalDateTime.now().isBefore(dataLimite);
    }
}
