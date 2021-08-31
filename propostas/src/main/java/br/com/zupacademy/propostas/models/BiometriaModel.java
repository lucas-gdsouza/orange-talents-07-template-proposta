package br.com.zupacademy.propostas.models;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "Biometrias")
public class BiometriaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    @NotBlank
    private String biometria;

    @NotNull
    @ManyToOne
    private CartaoModel cartao;

    /**
     * Para uso do Hibernate
     */
    @Deprecated
    public BiometriaModel() {
    }

    public BiometriaModel(@NotBlank String biometria, @NotNull CartaoModel cartao) {
        validarAtributos(biometria, cartao);
        this.criadoEm = LocalDateTime.now();
        this.biometria = biometria;
        this.cartao = cartao;
    }

    private void validarAtributos(String biometria, CartaoModel cartao) {
        Assert.isTrue(Base64.isBase64(biometria), "Biometria Inválida");
        Assert.notNull(cartao, "O atributo 'cartao' não foi definido.");
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BiometriaModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("criadoEm=" + criadoEm)
                .add("biometria='" + biometria + "'")
                .add("cartao=" + cartao)
                .toString();
    }
}