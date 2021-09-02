package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.AvisoViagemModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisoViagemRepository extends CrudRepository<AvisoViagemModel, Long> {
}