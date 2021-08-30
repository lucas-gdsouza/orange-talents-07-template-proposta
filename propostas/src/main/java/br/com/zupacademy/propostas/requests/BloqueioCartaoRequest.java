package br.com.zupacademy.propostas.requests;

import br.com.zupacademy.propostas.models.BloqueioModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.BloqueioRepository;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Optional;

public class BloqueioCartaoRequest {
    private Long idCartao;
    private String ip;
    private String userAgent;

    public BloqueioCartaoRequest(@NotNull @Positive Long idCartao, @NotBlank String ip, @NotBlank String userAgent) {
        validarAtributos(idCartao, ip, userAgent);

        this.idCartao = idCartao;
        this.ip = ip;
        this.userAgent = userAgent;
    }

    private void validarAtributos(Long idCartao, String ip, String userAgent) {

        if (idCartao == null || idCartao <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID inválido");
        }

        if (!InetAddressUtils.isIPv4Address(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de IPv4 Inválido.");
        }

        if (userAgent == null || userAgent.trim().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User-Agent não recebido.");
        }
    }

    public @NotNull BloqueioModel toModel(BloqueioRepository bloqueioRepository, CartaoRepository cartaoRepository) {

        Optional<CartaoModel> cartaoModel = cartaoRepository.findById(this.idCartao);

        if (cartaoModel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cartão não existe.");
        }

        Optional<BloqueioModel> bloqueioModel = bloqueioRepository.findByCartao(cartaoModel.get());

        if (bloqueioModel.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O documento já está bloqueado.");
        }

        return new BloqueioModel(cartaoModel.get(), this.ip, this.userAgent);
    }
}