package br.com.cooperativa.votacao.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"pauta_id", "cpf_associado"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;
    
    @Column(name = "cpf_associado", nullable = false, length = 11)
    private String cpfAssociado;
    
    @Column(name = "voto_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private Choice voto;
    
    @Column(name = "data_voto", nullable = false)
    private LocalDateTime dataVoto;
    
    @PrePersist
    protected void onCreate() {
        dataVoto = LocalDateTime.now();
    }
    
    public enum Choice {
        SIM, NAO
    }
}
