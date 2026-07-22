package br.com.cooperativa.votacao.repository;

import br.com.cooperativa.votacao.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    
    Optional<Voto> findByPautaIdAndCpfAssociado(Long pautaId, String cpfAssociado);
    
    boolean existsByPautaIdAndCpfAssociado(Long pautaId, String cpfAssociado);
    
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.pauta.id = :pautaId AND v.voto = 'SIM'")
    Long countVotosSim(@Param("pautaId") Long pautaId);
    
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.pauta.id = :pautaId AND v.voto = 'NAO'")
    Long countVotosNao(@Param("pautaId") Long pautaId);
    
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.pauta.id = :pautaId")
    Long countTotalVotos(@Param("pautaId") Long pautaId);
}
