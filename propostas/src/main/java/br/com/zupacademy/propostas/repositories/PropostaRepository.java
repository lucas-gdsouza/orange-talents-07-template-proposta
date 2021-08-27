package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.models.enums.EstadoProposta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropostaRepository extends PagingAndSortingRepository<PropostaModel, Long> {
    Optional<PropostaModel> findByDocumento(String documento);

    Page<PropostaModel> findAllByEstadoPropostaAndCartaoIsNull(Pageable pageable, EstadoProposta estadoProposta);
}