package br.com.zupacademy.propostas.models;

import br.com.zupacademy.propostas.models.enums.Emissor;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "CarteiraDigital")
public class CarteiraDigitalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime associadaEm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Emissor emissor;

    @NotNull
    @ManyToOne
    private CartaoModel cartao;

    /**
     * Para uso do Hibernate
     */
    @Deprecated
    public CarteiraDigitalModel() {
    }

    public CarteiraDigitalModel(@NotBlank String email,
                                @NotBlank Emissor emissor, @NotNull CartaoModel cartao) {

        validarAtributos(email, emissor, cartao);
        this.email = email;
        this.associadaEm = LocalDateTime.now();
        this.emissor = emissor;
        this.cartao = cartao;
    }

    private void validarAtributos(String email, Emissor emissor, CartaoModel cartao) {
        Assert.hasText(email, "O atributo 'email' deve ser preenchido.");
        Assert.notNull(emissor, "O atributo 'emissor' deve ser preenchido.");
        Assert.notNull(cartao, "O atributo 'cartao' não possui valor definido.");
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CarteiraDigitalModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("email='" + email + "'")
                .add("associadaEm=" + associadaEm)
                .add("emissor='" + emissor + "'")
                .add("cartao=" + cartao)
                .toString();
    }
}