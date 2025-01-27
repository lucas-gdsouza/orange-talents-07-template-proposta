package br.com.zupacademy.propostas.requests.both;

import br.com.zupacademy.propostas.models.AvisoViagemModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoViagemRequest {

    @NotBlank
    private String destino;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate terminaEm;

    public AvisoViagemRequest(@NotBlank String destino, @NotNull LocalDate terminaEm) {
        this.destino = destino;
        this.terminaEm = terminaEm;
    }

    private void validarParametros(String numeroCartao, String ip, String userAgent) {

        if (numeroCartao == null || numeroCartao.trim().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cartão Inválido");
        }

        if (!InetAddressUtils.isIPv4Address(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de IPv4 Inválido.");
        }

        if (userAgent == null || userAgent.trim().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User-Agent não recebido.");
        }
    }

    public AvisoViagemModel toModel(CartaoModel cartao, String numeroCartao, String ip, String userAgent) {
        validarParametros(numeroCartao, ip, userAgent);
        return new AvisoViagemModel(cartao, this.destino, this.terminaEm, ip, userAgent);
    }
}