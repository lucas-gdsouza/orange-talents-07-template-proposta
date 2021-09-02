package br.com.zupacademy.propostas.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvisoViagemResponse {

    @JsonProperty
    private String resultado;

    public AvisoViagemResponse(@JsonProperty("resultado") String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
