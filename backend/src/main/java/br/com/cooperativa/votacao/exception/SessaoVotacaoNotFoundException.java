package br.com.cooperativa.votacao.exception;

public class SessaoVotacaoNotFoundException extends RuntimeException {
    public SessaoVotacaoNotFoundException(Long pautaId) {
        super("Sessão de votação não encontrada para a pauta: " + pautaId);
    }
}
