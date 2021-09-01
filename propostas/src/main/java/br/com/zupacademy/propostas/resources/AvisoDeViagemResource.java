package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.AvisoDeViagemModel;
import br.com.zupacademy.propostas.repositories.AvisoDeViagemRepository;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.AvisoDeViagemRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/avisos")
public class AvisoDeViagemResource {

    @Autowired
    private CartoesExternalResource cartoesExternalResource;

    @Autowired
    private AvisoDeViagemRepository avisoDeViagemRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @PostMapping(value = "/{numeroCartao}/cartoes")
    public ResponseEntity emitirAvisoViagem(@PathVariable String numeroCartao,
                                            @RequestHeader(value = "ip") String ip,
                                            @RequestHeader(value = "User-Agent") String userAgent,
                                            @RequestBody @Valid AvisoDeViagemRequest avisoDeViagemRequest) {

        AvisoDeViagemModel avisoDeViagemModel =
                avisoDeViagemRequest.toModel(cartaoRepository, numeroCartao, ip, userAgent);

        try {
            cartoesExternalResource.enviarAvisoDeViagem(numeroCartao, avisoDeViagemRequest);
            avisoDeViagemRepository.save(avisoDeViagemModel);

        } catch (FeignException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Erro ao tentar comunicar com API externa");
        }

        return ResponseEntity.ok().build();
    }
}