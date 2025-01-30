package com.example.clienteAPI.feign;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;

@Configuration // Indica que esta classe é uma configuração Spring e será usada para definir Beans personalizados.
public class FeignConfig {

    @Bean // Define um Bean gerenciado pelo Spring, que substituirá o cliente HTTP padrão do Feign.
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault(); // Cria uma instância do Apache HttpClient, que é um cliente HTTP completo e suporta todos os métodos HTTP.
        return new feign.httpclient.ApacheHttpClient(httpClient); // Retorna uma implementação do Feign Client que utiliza o Apache HttpClient para processar as requisições.
    }
    
}
