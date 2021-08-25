package br.com.zupacademy.propostas.services;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.models.enums.EstadoProposta;
import br.com.zupacademy.propostas.repositories.PropostaRepository;
import br.com.zupacademy.propostas.requests.NovoCartaoRequest;
import br.com.zupacademy.propostas.resources.externals.CartoesExternalResource;
import br.com.zupacademy.propostas.response.NovoCartaoResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VerificacaoDePropostasService {

    /**
     * A primeira unidade representa a quantidade de minutos;
     */

    private final long time = 1 * 60 * 1000;

    private final Logger logger = LoggerFactory.getLogger(VerificacaoDePropostasService.class);

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartoesExternalResource cartoesExternalResource;

    @Scheduled(fixedDelay = time)
    public void associarPropostasComCartao() {

        List<PropostaModel> listaDePropostas = propostaRepository.
                findAllByEstadoPropostaAndCartao(EstadoProposta.ELEGIVEL, null);

        if (listaDePropostas.isEmpty()) {
            return;
        }

        listaDePropostas.forEach(propostaModel -> {
            NovoCartaoRequest novoCartaoRequest =
                    new NovoCartaoRequest(propostaModel.getId(), propostaModel.getDocumento(), propostaModel.getNome());

            try {
                NovoCartaoResponse novoCartaoResponse =
                        cartoesExternalResource.realizarPedidoDeNovoCartao(novoCartaoRequest);

                propostaModel.associarCartaoComPropostaElegivel(novoCartaoResponse);

                propostaRepository.save(propostaModel);

            } catch (FeignException exception) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Erro ao tentar comunicar com API externa");
            }
        });
    }
}