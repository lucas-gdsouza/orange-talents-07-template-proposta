package br.com.zupacademy.propostas.models;

import br.com.zupacademy.propostas.response.BloqueioCartaoResponse;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "Bloqueios")
public class BloqueioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private CartaoModel cartao;

    @NotNull
    @Column(nullable = false)
    private boolean bloqueado;

    @NotNull
    private LocalDateTime bloqueadoEm;

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
    public BloqueioModel() {
    }

    public BloqueioModel(@NotNull CartaoModel cartaoModel, @NotBlank String ip, @NotBlank String userAgent) {
        validarAtributos(cartaoModel, ip, userAgent);
        this.cartao = cartaoModel;
        this.bloqueado = false;
        this.bloqueadoEm = LocalDateTime.now();
        this.ip = ip;
        this.userAgent = userAgent;
    }

    private void validarAtributos(CartaoModel cartao, String ip, String userAgent) {
        Assert.notNull(cartao, "O atributo 'cartaoModel' não possui valor definido.");
        Assert.isTrue(InetAddressUtils.isIPv4Address(ip), "O atributo 'ip' deve ser preenchido");
        Assert.hasText(userAgent, "O atributo 'userAgent' deve ser preenchido.");
    }

    public void estadoDoCartao(BloqueioCartaoResponse response) {
        Assert.notNull(response.getResultado(), "O estado do cartão deve ser preenchido.");
        this.bloqueado = response.getResultado();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BloqueioModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cartaoModel=" + cartao)
                .add("estaBloqueado? " + bloqueado)
                .add("bloqueadoEm=" + bloqueadoEm)
                .add("ip='" + ip + "'")
                .add("userAgent='" + userAgent + "'")
                .toString();
    }
}