package br.com.zupacademy.propostas.controllers.externals;

import br.com.zupacademy.propostas.requests.ConsultaDadosDaPropostaRequest;
import br.com.zupacademy.propostas.response.ConsultaDadosDaPropostaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "propostas", url = "http://localhost:9999")
public interface EndpointValidadorDePropostas {

    @PostMapping(value = "/api/solicitacao/")
    ConsultaDadosDaPropostaResponse enviarParaAnalise(ConsultaDadosDaPropostaRequest request);
}