package com.teste.tgid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teste.tgid.model.Cliente;
import com.teste.tgid.model.Empresa;
import com.teste.tgid.model.TipoTransacao;
import com.teste.tgid.model.Transacao;
import com.teste.tgid.repository.ClienteRepository;
import com.teste.tgid.repository.EmpresaRepository;
import com.teste.tgid.repository.TransacaoRepository;

@Service
@RequestMapping("/transacao")
public class TransacaoService {
	
	private TransacaoRepository transacaoRepository;
    private EmpresaRepository empresaRepository;
    private ClienteRepository clienteRepository;
	private CallbackService callbackService;
	private EmailService emailService;
	
	@Autowired
    public TransacaoService(TransacaoRepository transacaoRepository, EmpresaRepository empresaRepository, ClienteRepository clienteRepository,CallbackService callbackService, EmailService emailService) {
        this.transacaoRepository = transacaoRepository;
        this.empresaRepository = empresaRepository;
        this.clienteRepository = clienteRepository;
        this.callbackService = callbackService;
        this.emailService = emailService;
    }
	
	public Transacao salvarTransacao(Transacao transacao) {
		Transacao novaTransacao = transacaoRepository.save(transacao);
		novaTransacao.setTaxa(transacao.getTaxa());
        callbackService.enviarCallbackParaEmpresa(novaTransacao);
        notificarCliente(novaTransacao);
        try {
	        return novaTransacao;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	public Transacao buscarPorId(Long id) {
		return transacaoRepository.findById(id).orElse(null);
	}
	
	public Iterable<Transacao> buscarTodos() {
		return transacaoRepository.findAll();	
	}
	
	public void excluir(Long id) {
		transacaoRepository.deleteById(id);
	}
	
	private void notificarCliente(Transacao transacao) {
        Cliente cliente = transacao.getCliente();
        String email = cliente.getEmail();
        String subject = "Transação realizada";
        String text = "Uma transação de " + transacao.getValor() + " foi realizada na sua conta.";
        emailService.sendSimpleMessage(email, subject, text);
    }
	
	public Transacao deposito(Long empresaId, Long clienteId, Float valor) {
		Empresa empresa = empresaRepository.findById(empresaId)
				.orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
		
		Transacao transacao = new Transacao();
		transacao.setEmpresa(empresa);
	    transacao.setCliente(cliente);
	    transacao.setValor(valor);
	    transacao.setTipo(TipoTransacao.DEPOSITO);
		
	    Double taxa = calcularTaxa(transacao);
	    transacao.setTaxa(taxa);
	    
	    Float novoSaldo = empresa.getSaldo() + valor - taxa.floatValue();
	    empresa.setSaldo(novoSaldo);
	    empresaRepository.save(empresa);
	    
	    return salvarTransacao(transacao);
	}
	
	public Transacao saque(Long empresaId, Long clienteId, Float valor) {
	    Empresa empresa = empresaRepository.findById(empresaId)
	            .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));
	    Cliente cliente = clienteRepository.findById(clienteId)
	            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

	    if (empresa.getSaldo() < valor) {
	        throw new IllegalArgumentException("Saldo insuficiente");
	    }

	    Transacao transacao = new Transacao();
	    transacao.setEmpresa(empresa);
	    transacao.setCliente(cliente);
	    transacao.setValor(valor);
	    transacao.setTipo(TipoTransacao.SAQUE);

	    Double taxa = calcularTaxa(transacao);
	    transacao.setTaxa(taxa);

	    Float novoSaldo = empresa.getSaldo() - valor - taxa.floatValue();
	    empresa.setSaldo(novoSaldo);
	    empresaRepository.save(empresa);

	    return salvarTransacao(transacao);
	}
	
	public Double calcularTaxa(Transacao transacao) {
	    Empresa empresa = transacao.getEmpresa();
	    Double taxa = empresa.getTaxa();
	    Float valor = transacao.getValor();
	    return valor * taxa / 100;
	}
}
