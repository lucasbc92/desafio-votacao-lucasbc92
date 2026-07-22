package br.com.cooperativa.votacao.exception;

public class SessaoVotacaoFechadaException extends RuntimeException {
    public SessaoVotacaoFechadaException() {
        super("A sessão de votação está fechada");
    }
}
