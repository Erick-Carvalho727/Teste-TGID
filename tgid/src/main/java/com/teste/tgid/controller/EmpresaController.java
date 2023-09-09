package com.teste.tgid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teste.tgid.model.Empresa;
import com.teste.tgid.service.EmpresaService;

@RestController
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@PostMapping
    public ResponseEntity<Empresa> criarEmpresa(@RequestBody Empresa empresa) {
        try {
            Empresa novaEmpresa = empresaService.salvar(empresa);
            return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        Empresa empresa = empresaService.buscarPorId(id);
        if (empresa != null) {
            return new ResponseEntity<>(empresa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Empresa>> buscarTodos() {
        Iterable<Empresa> empresas = empresaService.buscarTodos();
        return new ResponseEntity<>(empresas, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Empresa empresa = empresaService.buscarPorId(id);
        if (empresa != null) {
            empresaService.excluir(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
}
