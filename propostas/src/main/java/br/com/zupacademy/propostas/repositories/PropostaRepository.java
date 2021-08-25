package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.PropostaModel;
import br.com.zupacademy.propostas.models.enums.EstadoProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropostaRepository extends JpaRepository<PropostaModel, Long> {

    Optional<PropostaModel> findByDocumento(String documento);
    List<PropostaModel> findAllByEstadoPropostaAndCartao(EstadoProposta estadoProposta, Long idCarro);
}