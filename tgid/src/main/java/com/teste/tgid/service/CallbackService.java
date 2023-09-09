package com.teste.tgid.service;

import org.hibernate.CallbackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.teste.tgid.model.Transacao;

@Service
public class CallbackService {
	
	 private RestTemplate restTemplate;

	@Autowired
	    public CallbackService(RestTemplate restTemplate) {
	        this.restTemplate = restTemplate;
	    }

	    public void enviarCallbackParaEmpresa(Transacao transacao) {
	        String url = "https://webhook.site/"; // substitua pelo seu URL de webhook
	        HttpEntity<Transacao> request = new HttpEntity<>(transacao);
	        try {
	            restTemplate.postForObject(url, request, Transacao.class);
	        } catch (RestClientException e) {
	        	throw new CallbackException("Não foi possível enviar o callback para a empresa", e);
	        }
	    }
}
