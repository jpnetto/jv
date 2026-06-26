package com.gp.persistencia;

public class PersistenceException extends Exception {
    private final String operacao;
    private final Object valor;

    public PersistenceException(String operacao, String mensagem, Object valor) {
        super(mensagem);
        this.operacao = operacao;
        this.valor = valor;
    }

    public PersistenceException(String operacao, String mensagem, Object valor, Throwable cause) {
        super(mensagem, cause);
        this.operacao = operacao;
        this.valor = valor;
    }

    public String getOperacao() {
        return operacao;
    }

    public Object getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "PersistenceException [operacao=" + operacao + ", valor=" + valor + ", mensagem=" + getMessage() + "]";
    }
}
