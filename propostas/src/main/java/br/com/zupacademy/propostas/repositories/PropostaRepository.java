package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.PropostaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropostaRepository extends CrudRepository<PropostaModel, Long> {
    Optional<PropostaModel> findByDocumento(String documento);
}
