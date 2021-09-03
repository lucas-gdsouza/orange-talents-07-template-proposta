package br.com.zupacademy.propostas.resources;

import br.com.zupacademy.propostas.models.BloqueioModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import br.com.zupacademy.propostas.repositories.CartaoRepository;
import br.com.zupacademy.propostas.requests.externals.BloqueioCartaoExternalRequest;
import br.com.zupacademy.propostas.requests.internals.BloqueioCartaoInternalRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import br.com.zupacademy.propostas.response.BloqueioCartaoResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bloqueios")
public class BloqueioResource {

    @Autowired
    private CartaoRepository cartaoRepository;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private CartoesExternalResource apiCartoes;

    @Value("${spring.application.name}")
    private String systemName;

    @PostMapping(value = "/{numeroCartao}/cartoes")
    @Transactional
    public ResponseEntity solicitarBloqueioDeCartao(@PathVariable String numeroCartao,
                                                    @RequestHeader(value = "ip") String ip,
                                                    @RequestHeader(value = "User-Agent") String userAgent) {

        Optional<CartaoModel> cartao = cartaoRepository.findByNumeroCartao(numeroCartao);

        if (cartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (cartao.get().getBloqueios().size() > 0) {
            return ResponseEntity.unprocessableEntity().body("O cartão já está bloqueado no sistema.");
        }

        BloqueioCartaoInternalRequest request = new BloqueioCartaoInternalRequest(numeroCartao, ip, userAgent);
        BloqueioModel bloqueioModel = request.toModel(cartao.get());

        try {
            BloqueioCartaoResponse bloqueioCartaoResponse = apiCartoes.solicitarBloqueioDeCartao(numeroCartao,
                    new BloqueioCartaoExternalRequest(systemName));

            bloqueioModel.estadoDoCartao(bloqueioCartaoResponse);
            manager.persist(bloqueioModel);
            manager.flush();

            return ResponseEntity.ok().build();

        } catch (FeignException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Erro em API Cartões");
        }
    }
}