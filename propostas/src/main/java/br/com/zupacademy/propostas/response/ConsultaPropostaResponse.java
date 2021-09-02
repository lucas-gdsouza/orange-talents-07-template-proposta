package br.com.zupacademy.propostas.response;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.models.enums.EstadoProposta;

import java.math.BigDecimal;

public class ConsultaPropostaResponse {

    private String documento;
    private String email;
    private String nome;
    private String endereco;
    private BigDecimal salarioBruto;
    private EstadoProposta estadoProposta;

    public ConsultaPropostaResponse(PropostaModel propostaModel) {
        this.documento = propostaModel.getDocumento();
        this.email = propostaModel.getEmail();
        this.nome = propostaModel.getNome();
        this.endereco = propostaModel.getEndereco();
        this.salarioBruto = propostaModel.getSalarioBruto();
        this.estadoProposta = propostaModel.getEstadoProposta();
    }

    /**
     *
     * Cada gettter foi criado para utilização da biblioteca de JSON.
     */

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalarioBruto() {
        return salarioBruto;
    }

    public EstadoProposta getEstadoProposta() {
        return estadoProposta;
    }
}