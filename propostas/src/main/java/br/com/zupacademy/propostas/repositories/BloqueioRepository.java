package br.com.zupacademy.propostas.repositories;

import br.com.zupacademy.propostas.models.BloqueioModel;
import br.com.zupacademy.propostas.models.CartaoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloqueioRepository extends CrudRepository<BloqueioModel, Long> {
    Optional<BloqueioModel> findByCartao(CartaoModel Cartao);
}