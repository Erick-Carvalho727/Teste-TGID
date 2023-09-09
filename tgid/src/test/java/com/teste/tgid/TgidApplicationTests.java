package com.teste.tgid;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.teste.tgid.model.Cliente;
import com.teste.tgid.model.Empresa;
import com.teste.tgid.model.TipoTransacao;
import com.teste.tgid.model.Transacao;
import com.teste.tgid.repository.ClienteRepository;
import com.teste.tgid.service.CallbackService;
import com.teste.tgid.service.ClienteService;

@SpringBootTest
class TgidApplicationTests {

	@Mock
    private RestTemplate restTemplate;
	
	@Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CallbackService callbackService;
    
    @InjectMocks
    private ClienteService clienteService;

    @SuppressWarnings("deprecation")
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    void testEnviarCallbackParaEmpresa() {
        Transacao transacao = new Transacao();
        transacao.setId(1L);
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setValor((float) 1000);
        transacao.setEmpresa(new Empresa());
        transacao.setCliente(new Cliente());

        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.eq(Transacao.class)))
            .thenReturn(transacao);

        callbackService.enviarCallbackParaEmpresa(transacao);

        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.eq(Transacao.class));
    }
    
    @Test
    public void testSalvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setCpf("48387024007");

		when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente salvo = clienteService.salvar(cliente);

        assertEquals(cliente, salvo);
        verify(clienteRepository).save(cliente);
    }

}
