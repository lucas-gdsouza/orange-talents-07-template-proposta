package br.com.zupacademy.propostas.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class BloqueioCartaoExternalRequest {

    @JsonProperty(value = "sistemaResponsavel")
    @NotBlank
    private String sistemaResponsavel;

    public BloqueioCartaoExternalRequest(@NotBlank @JsonProperty(value = "sistemaResponsavel") String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }
}