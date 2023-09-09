package com.teste.tgid.repository;

import org.springframework.data.repository.CrudRepository;
import com.teste.tgid.model.Transacao;

public interface TransacaoRepository extends CrudRepository<Transacao, Long> {

}
