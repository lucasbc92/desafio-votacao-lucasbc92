package br.com.cooperativa.votacao.exception;

public class SessaoVotacaoJaAbertaException extends RuntimeException {
    public SessaoVotacaoJaAbertaException() {
        super("Já existe uma sessão de votação aberta para esta pauta");
    }
}
