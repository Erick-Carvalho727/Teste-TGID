package com.teste.tgid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.tgid.model.Transacao;
import com.teste.tgid.service.TransacaoService;

@RestController
@RequestMapping("/empresa")
public class TransacaoController {
	
	@Autowired
	private TransacaoService transacaoService;
	
	@PostMapping
    public ResponseEntity<Transacao> criarTransacao(@RequestBody Transacao transacao) {
        try {
            Transacao novaTransacao = transacaoService.salvarTransacao(transacao);
            return new ResponseEntity<>(novaTransacao, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        Transacao transacao = transacaoService.buscarPorId(id);
        if (transacao != null) {
            return new ResponseEntity<>(transacao, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Transacao>> buscarTodos() {
        Iterable<Transacao> transacoes = transacaoService.buscarTodos();
        return new ResponseEntity<>(transacoes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Transacao transacao = transacaoService.buscarPorId(id);
        if (transacao != null) {
            transacaoService.excluir(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
}
