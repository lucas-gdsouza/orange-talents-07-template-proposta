package br.com.zupacademy.propostas.services;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.models.enums.EstadoProposta;
import br.com.zupacademy.propostas.repositories.PropostaRepository;
import br.com.zupacademy.propostas.requests.externals.NovoCartaoExternalRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import br.com.zupacademy.propostas.response.NovoCartaoResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerificacaoDePropostasService {

    /**
     * A primeira unidade representa a quantidade de minutos;
     */

    private final long time = 1 * 05 * 1000;

    private final Logger logger = LoggerFactory.getLogger(VerificacaoDePropostasService.class);

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartoesExternalResource cartoesExternalResource;

    @Scheduled(fixedDelay = time)
    public void associarPropostasComCartao() {

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id"));

        Page<PropostaModel> listaDePropostas = propostaRepository.
                findAllByEstadoPropostaAndCartaoIsNull(pageable, EstadoProposta.ELEGIVEL);

        if (listaDePropostas.isEmpty()) {
            logger.info("Não houve associação de propostas com cartões nesta rodada.");
            return;
        }

        listaDePropostas.forEach(propostaModel -> {

            NovoCartaoExternalRequest novoCartaoExternalRequest = new NovoCartaoExternalRequest(propostaModel);

            try {
                NovoCartaoResponse novoCartaoResponse =
                        cartoesExternalResource.realizarPedidoDeNovoCartao(novoCartaoExternalRequest);

                propostaModel.adicionarCartaoComPropostaElegivel(novoCartaoResponse);

                propostaRepository.save(propostaModel);

                logger.info("Associação de propostas com cartões efetuada.");

            } catch (FeignException exception) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Erro ao tentar comunicar com API Cartões");
            }
        });
    }
}