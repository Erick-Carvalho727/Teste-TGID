package com.teste.tgid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.tgid.model.Empresa;
import com.teste.tgid.repository.EmpresaRepository;
import com.teste.tgid.util.ValidacaoUtil;

@Service
public class EmpresaService {
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	public Empresa salvar(Empresa empresa) {
		validarCnpj(empresa.getCnpj());
		try {
	        return empresaRepository.save(empresa);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	public Empresa buscarPorId(Long id) {
		return empresaRepository.findById(id).orElse(null);
	}
	
	public Iterable<Empresa> buscarTodos() {
		return empresaRepository.findAll();	
	}
	
	public void excluir(Long id) {
		empresaRepository.deleteById(id);
	}
	
	public void validarCnpj(String cnpj) {
	    boolean valido = ValidacaoUtil.cnpjValido(cnpj);
	    if (!valido) {
	        throw new IllegalArgumentException("CNPJ inválido");
	    }
	}
	
}
