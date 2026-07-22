package br.com.cooperativa.votacao.exception;

public class PautaNotFoundException extends RuntimeException {
    public PautaNotFoundException(Long id) {
        super("Pauta não encontrada com id: " + id);
    }
}
