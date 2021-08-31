package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.BloqueioModel;
import br.com.zupacademy.propostas.repositories.BloqueioRepository;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.BloqueioCartaoExternalRequest;
import br.com.zupacademy.propostas.requests.BloqueioCartaoInternalRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import br.com.zupacademy.propostas.response.BloqueioCartaoResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/bloqueios")
public class BloqueioResource {

    @Autowired
    private BloqueioRepository bloqueioRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private CartoesExternalResource cartoesExternalResource;

    @Value("${spring.application.name}")
    private String systemName;

    @PostMapping(value = "/{numeroCartao}/cartoes")
    public ResponseEntity solicitarBloqueioDeCartao(@PathVariable String numeroCartao,
                                                    @RequestHeader(value = "ip") String ip,
                                                    @RequestHeader(value = "User-Agent") String userAgent) {

        BloqueioCartaoInternalRequest request = new BloqueioCartaoInternalRequest(numeroCartao, ip, userAgent);
        BloqueioModel bloqueioModel = request.toModel(bloqueioRepository, cartaoRepository);

        try {
            BloqueioCartaoResponse bloqueioCartaoResponse =
                    cartoesExternalResource.solicitarBloqueioDeCartao(numeroCartao,
                            new BloqueioCartaoExternalRequest(systemName));
            bloqueioModel.estadoDoCartao(bloqueioCartaoResponse);

            bloqueioRepository.save(bloqueioModel);

        } catch (FeignException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Erro ao tentar comunicar com API externa");
        }

        return ResponseEntity.ok().build();
    }
}