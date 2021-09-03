package br.com.zupacademy.propostas.models;

import br.com.zupacademy.propostas.customizations.security.JasyptConfig;
import br.com.zupacademy.propostas.models.enums.EstadoProposta;
import br.com.zupacademy.propostas.response.NovoCartaoResponse;
import br.com.zupacademy.propostas.response.SolicitacaoAnaliseResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.transaction.Transactional;
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
    private String documentoUnico;

    @Column(unique = true)
    @NotBlank
    private String documentoLegivel;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    @Column(nullable = false)
    @NotBlank
    private String endereco;

    @Column(nullable = false)
    @NotNull
    @PositiveOrZero
    private BigDecimal salarioBruto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoProposta estadoProposta;

    @OneToOne(cascade = CascadeType.ALL)
    private CartaoModel cartao;

    @Transient
    private String documentoLimpo;

    /**
     * Para uso do Hibernate
     */
    @Deprecated
    public PropostaModel() {
    }

    public PropostaModel(@NotBlank String documentoLimpo, @NotBlank String email, @NotBlank String nome,
                         @NotBlank String endereco, @NotNull BigDecimal salarioBruto) {
        validarAtributos(documentoLimpo, email, nome, endereco, salarioBruto);

        this.documentoLimpo = documentoLimpo;
        this.documentoLegivel = criptografarDocumento();
        this.documentoUnico = hashearDocumento();
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

    public String getDocumentoUnico() {
        return documentoUnico;
    }

    public String getDocumentoLegivel() {
        return documentoLegivel;
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

    private String hashearDocumento() {
        return new JasyptConfig().gerarHash(this.documentoLimpo);
    }

    private String criptografarDocumento() {
        return new JasyptConfig().criptografar(this.documentoLimpo);
    }

    public String descriptografarDocumento() {
        return new JasyptConfig().descriptografar(this.documentoLegivel);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PropostaModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("documentoUnico='" + documentoUnico + "'")
                .add("documentoLegivel='" + documentoLegivel + "'")
                .add("email='" + email + "'")
                .add("nome='" + nome + "'")
                .add("endereco='" + endereco + "'")
                .add("salarioBruto=" + salarioBruto)
                .add("estadoProposta=" + estadoProposta)
                .toString();
    }
}