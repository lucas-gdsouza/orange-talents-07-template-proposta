package br.com.zupacademy.propostas.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class BloqueioCartaoResponse {
    @JsonProperty(value = "resultado")
    private String resultado;

    public BloqueioCartaoResponse(@JsonProperty("resultado") String resultado) {
        this.resultado = resultado;
    }

    public boolean getResultado() {
        if (this.resultado.equalsIgnoreCase("BLOQUEADO")) {
            return true;
        }
        return false;
    }
}