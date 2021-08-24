package br.com.zupacademy.propostas.response;

import java.util.StringJoiner;

public class ConsultaDadosDaPropostaResponse {

    private Long idProposta;
    private String documento;
    private String nome;
    private String resultadoSolicitacao;

    public ConsultaDadosDaPropostaResponse(Long idProposta, String documento,
                                           String nome, String resultadoSolicitacao) {
        this.idProposta = idProposta;
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConsultaDadosDaPropostaResponse.class.getSimpleName() + "[", "]")
                .add("idProposta=" + idProposta)
                .add("documento='" + documento + "'")
                .add("nome='" + nome + "'")
                .add("resultadoSolicitacao='" + resultadoSolicitacao + "'")
                .toString();
    }
}