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

    @ManyToOne
    private CartaoModel cartao;

    @NotNull
    private LocalDateTime bloqueadoEm;

    @Column(nullable = false)
    @NotBlank
    private String ip;

    @Column(nullable = false)
    @NotBlank
    private String userAgent;

    private String estadoDoCartao;

    /**
     * Para uso do Hibernate
     */
    @Deprecated
    public BloqueioModel() {
    }

    public BloqueioModel(CartaoModel cartaoModel, String ip, String userAgent) {
        validarAtributos(cartaoModel, ip, userAgent);
        this.cartao = cartaoModel;
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
        this.estadoDoCartao = response.getResultado();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BloqueioModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cartaoModel=" + cartao)
                .add("bloqueadoEm=" + bloqueadoEm)
                .add("ip='" + ip + "'")
                .add("userAgent='" + userAgent + "'")
                .toString();
    }
}