package com.teste.tgid.repository;

import org.springframework.data.repository.CrudRepository;
import com.teste.tgid.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

}
