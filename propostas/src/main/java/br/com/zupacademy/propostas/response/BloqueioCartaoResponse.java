package br.com.zupacademy.propostas.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BloqueioCartaoResponse {
    private String resultado;

    public BloqueioCartaoResponse(@JsonProperty("resultado") String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}