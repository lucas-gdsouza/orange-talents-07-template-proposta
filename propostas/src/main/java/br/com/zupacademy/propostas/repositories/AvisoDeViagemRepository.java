package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.AvisoDeViagemModel;
import br.com.zupacademy.propostas.models.BloqueioModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvisoDeViagemRepository extends CrudRepository<AvisoDeViagemModel, Long> {
    Optional<AvisoDeViagemModel> findByCartao(CartaoModel Cartao);
}