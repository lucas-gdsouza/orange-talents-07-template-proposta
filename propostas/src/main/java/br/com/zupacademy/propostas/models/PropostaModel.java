package br.com.zupacademy.propostas.models;

import br.com.zupacademy.propostas.models.enums.EstadoProposta;
import br.com.zupacademy.propostas.response.NovoCartaoResponse;
import br.com.zupacademy.propostas.response.SolicitacaoAnaliseResponse;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.StringJoiner;

@Entity
@Table(name = "Propostas")
public class PropostaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O documento necessário deve ser o CPF/CNPJ
     */

    @Column(unique = true)
    @NotBlank
    private String documento;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @NotNull
    @PositiveOrZero
    private BigDecimal salarioBruto;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoProposta estadoProposta;

    @OneToOne(cascade = CascadeType.ALL)
    private CartaoModel cartao;

    /**
     * Para uso do Hibernate
     */
    @Deprecated
    public PropostaModel() {
    }

    public PropostaModel(@NotBlank String documento, @NotBlank String email, @NotBlank String nome,
                         @NotBlank String endereco, @NotNull BigDecimal salarioBruto) {
        validarAtributos(documento, email, nome, endereco, salarioBruto);

        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salarioBruto = salarioBruto;
        this.estadoProposta = EstadoProposta.EM_ANALISE;
    }

    private void validarAtributos(String documento, String email, String nome,
                                  String endereco, BigDecimal salarioBruto) {

        Assert.hasText(documento, "O atributo 'documento' deve ser preenchido.");
        Assert.hasText(email, "O atributo 'email' deve ser preenchido.");
        Assert.hasText(nome, "O atributo 'nome' deve ser preenchido.");
        Assert.hasText(endereco, "O atributo 'endereco' deve ser preenchido.");

        Assert.notNull(salarioBruto, "O atributo 'salarioBruto' não possui valor definido.");
        Assert.isTrue(salarioBruto.compareTo(new BigDecimal(0)) != -1,
                "Um valor positivo deve ser atribuído ao 'salarioBruto'");
    }

    public Long getId() {
        return id;
    }

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

    public void adicionarCartaoComPropostaElegivel(NovoCartaoResponse response) {
        Assert.notNull(response, "Cartão precisa ser preenchido.");
        this.cartao = response.toModel();
    }

    public void alterarStatusDaAnaliseDeProposta(SolicitacaoAnaliseResponse response) {
        Assert.notNull(response, "A proposta precisa ter estado.");
        this.estadoProposta = response.getResultadoSolicitacao();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PropostaModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("documento='" + documento + "'")
                .add("email='" + email + "'")
                .add("nome='" + nome + "'")
                .add("endereco='" + endereco + "'")
                .add("salarioBruto=" + salarioBruto)
                .add("estadoProposta='" + estadoProposta + "'")
                .toString();
    }
}