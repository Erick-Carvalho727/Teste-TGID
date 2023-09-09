package com.teste.tgid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCriarCliente() throws Exception {
    	String json = "{\"nome\":\"Fulano\",\"cpf\":\"48387024007\",\"email\":\"fulano@example.com\",\"telefone\":\"123456789\"}";
        
        mockMvc.perform(post("/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
