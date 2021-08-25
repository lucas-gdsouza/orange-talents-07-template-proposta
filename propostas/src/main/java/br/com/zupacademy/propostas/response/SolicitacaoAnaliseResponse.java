package br.com.zupacademy.propostas.response;

import br.com.zupacademy.propostas.models.enums.EstadoProposta;
import br.com.zupacademy.propostas.response.enums.DevolutivaSolicitacaoAnalise;

public class SolicitacaoAnaliseResponse {

    private Long idProposta;
    private String documento;
    private String nome;
    private String resultadoSolicitacao;

    public SolicitacaoAnaliseResponse(Long idProposta, String documento,
                                      String nome, String resultadoSolicitacao) {
        this.idProposta = idProposta;
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public EstadoProposta getResultadoSolicitacao() {
        return DevolutivaSolicitacaoAnalise.valueOf(this.resultadoSolicitacao).retornarEstado();
    }
}