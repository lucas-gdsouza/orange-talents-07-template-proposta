package br.com.zupacademy.propostas.models;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "Cartoes")
public class CartaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String numeroCartao;

    @NotNull
    private LocalDateTime emitidoEm;

    @NotBlank
    private String titular;

    /**
     * Para uso do Hibernate
     */
    @Deprecated
    public CartaoModel() {
    }

    public CartaoModel(@NotBlank String numeroCartao, @NotNull LocalDateTime emitidoEm,
                       @NotBlank String titular) {

        validarAtributos(numeroCartao, emitidoEm, titular);

        this.numeroCartao = numeroCartao;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
    }

    private void validarAtributos(String numeroCartao, LocalDateTime emitidoEm, String titular) {
        Assert.hasText(numeroCartao, "O atributo 'numeroCartao' deve ser preenchido.");
        Assert.notNull(emitidoEm, "O atributo 'emitidoEm' não possui valor definido.");
        Assert.hasText(titular, "O atributo 'titular' deve ser preenchido.");
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CartaoModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("numeroCartao='" + numeroCartao + "'")
                .add("emitidoEm=" + emitidoEm)
                .add("titular='" + titular + "'")
                .toString();
    }
}