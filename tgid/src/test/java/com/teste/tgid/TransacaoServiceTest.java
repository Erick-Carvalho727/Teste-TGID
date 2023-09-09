package com.teste.tgid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.teste.tgid.model.Cliente;
import com.teste.tgid.model.Empresa;
import com.teste.tgid.model.TipoTransacao;
import com.teste.tgid.model.Transacao;
import com.teste.tgid.repository.ClienteRepository;
import com.teste.tgid.repository.EmpresaRepository;
import com.teste.tgid.repository.TransacaoRepository;
import com.teste.tgid.service.CallbackService;
import com.teste.tgid.service.EmailService;
import com.teste.tgid.service.TransacaoService;

@SpringBootTest
public class TransacaoServiceTest {
	
    @Mock
    private TransacaoRepository transacaoRepository;
    @Mock
    private EmpresaRepository empresaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private CallbackService callbackService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TransacaoService transacaoService;
    
    @Test
    public void testDeposito() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Teste");
        empresa.setCnpj("67177904000168");
        empresa.setSaldo(1000.0f);
        empresa.setTaxa(1.0);
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Jhonson");
        cliente.setCpf("48387024007");
        cliente.setTelefone("123456789");
        cliente.setEmail("test@test.com");
        Transacao transacao = new Transacao();
        transacao.setId(1L);
        transacao.setEmpresa(empresa);
        transacao.setCliente(cliente);
        transacao.setValor(100.0f);
        transacao.setTipo(TipoTransacao.DEPOSITO);
        Float valor = 100.0f;

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        doNothing().when(callbackService).enviarCallbackParaEmpresa(any(Transacao.class));
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());
        
        Double taxaEsperada = valor * empresa.getTaxa() / 100;
        Float novoSaldo = empresa.getSaldo() + valor - taxaEsperada.floatValue();

        Transacao resultado = transacaoService.deposito(1L, 1L, valor);

        assertEquals(transacao, resultado);

        assertEquals(taxaEsperada, resultado.getTaxa());
        
        assertEquals(novoSaldo, empresa.getSaldo());

        verify(callbackService).enviarCallbackParaEmpresa(transacao);

        verify(emailService).sendSimpleMessage(anyString(), anyString(), anyString());
    }
}
