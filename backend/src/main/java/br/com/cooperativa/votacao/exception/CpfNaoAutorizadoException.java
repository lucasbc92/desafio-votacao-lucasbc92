package br.com.cooperativa.votacao.exception;

public class CpfNaoAutorizadoException extends RuntimeException {
    public CpfNaoAutorizadoException() {
        super("CPF não autorizado a votar");
    }
}
