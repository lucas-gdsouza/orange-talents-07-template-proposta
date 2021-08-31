package br.com.zupacademy.propostas.requests;

import br.com.zupacademy.propostas.models.AvisoDeViagemModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

public class AvisoDeViagemRequest {

    @NotBlank
    private String destino;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime terminaEm;

    public AvisoDeViagemRequest(@NotBlank String destino, @NotNull LocalDateTime terminaEm) {
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

    public AvisoDeViagemModel toModel(CartaoRepository cartaoRepository, String numeroCartao,
                                      String ip, String userAgent) {

        validarParametros(numeroCartao, ip, userAgent);

        Optional<CartaoModel> cartao = cartaoRepository.findByNumeroCartao(numeroCartao);

        if (cartao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado");
        }

        return new AvisoDeViagemModel(cartao.get(), this.destino, this.terminaEm, ip, userAgent);
    }
}