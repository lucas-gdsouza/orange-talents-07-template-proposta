package br.com.zupacademy.propostas.requests.internals;

import br.com.zupacademy.propostas.models.BloqueioModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.BloqueioRepository;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Optional;

public class BloqueioCartaoInternalRequest {
    private String numeroCartao;
    private String ip;
    private String userAgent;

    public BloqueioCartaoInternalRequest(@NotBlank String numeroCartao, @NotBlank String ip, @NotBlank String userAgent) {
        validarAtributos(numeroCartao, ip, userAgent);

        this.numeroCartao = numeroCartao;
        this.ip = ip;
        this.userAgent = userAgent;
    }

    private void validarAtributos(String numeroCartao, String ip, String userAgent) {

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

    public @NotNull BloqueioModel toModel(BloqueioRepository bloqueioRepository, CartaoRepository cartaoRepository) {

        Optional<CartaoModel> cartaoModel = cartaoRepository.findByNumeroCartao(this.numeroCartao);

        if (cartaoModel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado.");
        }

        Optional<BloqueioModel> bloqueioModel = bloqueioRepository.findByCartao(cartaoModel.get());

        if (bloqueioModel.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O documento já está bloqueado.");
        }

        return new BloqueioModel(cartaoModel.get(), this.ip, this.userAgent);
    }
}