package com.teste.tgid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.tgid.model.Cliente;
import com.teste.tgid.repository.ClienteRepository;
import com.teste.tgid.util.ValidacaoUtil;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente salvar(Cliente cliente) {
		validarCpf(cliente.getCpf());
		try {
	        return clienteRepository.save(cliente);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}
	
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();	
	}
	
	public void excluir(Long id) {
		clienteRepository.deleteById(id);
	}
	
	public void validarCpf(String cpf) {
	    boolean valido = ValidacaoUtil.cpfValido(cpf);
	    if (!valido) {
	        throw new IllegalArgumentException("CPF inválido");
	    }
	}
	
}
