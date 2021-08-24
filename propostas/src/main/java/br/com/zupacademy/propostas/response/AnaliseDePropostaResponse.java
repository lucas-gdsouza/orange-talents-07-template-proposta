package br.com.zupacademy.propostas.response;

import br.com.zupacademy.propostas.models.enums.EstadoDaProposta;
import br.com.zupacademy.propostas.response.enums.ResultadoDaSolicitacao;

public class AnaliseDePropostaResponse {

    private Long idProposta;
    private String documento;
    private String nome;
    private String resultadoSolicitacao;

    public AnaliseDePropostaResponse(Long idProposta, String documento,
                                     String nome, String resultadoSolicitacao) {
        this.idProposta = idProposta;
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public EstadoDaProposta getResultadoSolicitacao() {
        return ResultadoDaSolicitacao.valueOf(resultadoSolicitacao).resultadoFinal();
    }
}