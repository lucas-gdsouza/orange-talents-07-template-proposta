package br.com.zupacademy.propostas.models;

import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.StringJoiner;

@Entity
@Table(name = "AvisoViagem")
public class AvisoViagemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private CartaoModel cartao;

    @Column(nullable = false)
    @NotBlank
    private String destino;

    @Column(nullable = false)
    @NotNull
    private LocalDate criadoEm;

    @Column(nullable = false)
    @NotNull
    @FutureOrPresent
    private LocalDate terminoEm;

    @Column(nullable = false)
    @NotBlank
    private String ip;

    @Column(nullable = false)
    @NotBlank
    private String userAgent;

    /**
     * Para uso do Hibernate
     */
    @Deprecated
    public AvisoViagemModel() {
    }

    public AvisoViagemModel(@NotNull CartaoModel cartao, @NotBlank String destino, @NotNull LocalDate terminoEm,
                            @NotBlank String ip, @NotBlank String userAgent) {
        validarAtributos(cartao, destino, terminoEm, ip, userAgent);
        this.cartao = cartao;
        this.destino = destino;
        this.criadoEm = LocalDate.now();
        this.terminoEm = terminoEm;
        this.ip = ip;
        this.userAgent = userAgent;
    }

    private void validarAtributos(CartaoModel cartao, String destino, LocalDate terminoEm,
                                  String ip, String userAgent) {
        Assert.notNull(cartao, "O atributo 'cartaoModel' não possui valor definido.");
        Assert.hasText(destino, "O atributo 'destino' deve ser preenchido.");
        Assert.notNull(terminoEm, "O atributo 'terminoEm' não possui valor definido.");
        Assert.isTrue(InetAddressUtils.isIPv4Address(ip), "O atributo 'ip' deve ser preenchido");
        Assert.hasText(userAgent, "O atributo 'userAgent' deve ser preenchido.");
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AvisoViagemModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cartao=" + cartao)
                .add("destino='" + destino + "'")
                .add("criadoEm=" + criadoEm)
                .add("terminoEm=" + terminoEm)
                .add("ip='" + ip + "'")
                .add("userAgent='" + userAgent + "'")
                .toString();
    }
}