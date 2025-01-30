package com.example.clienteStorage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.clienteStorage.entity.Cliente;
import com.example.clienteStorage.exception.CannotDeleteException;
import com.example.clienteStorage.exception.CannotDisableException;
import com.example.clienteStorage.exception.EntityNotFoundException;
import com.example.clienteStorage.exception.NegativeSaldoException;
import com.example.clienteStorage.exception.RequiredNameException;
import com.example.clienteStorage.exception.RequiredPhoneException;
import com.example.clienteStorage.exception.RequiredSaldoException;
import com.example.clienteStorage.exception.TelefoneUniqueViolationException;
import com.example.clienteStorage.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Transactional
    public Cliente salvar(Cliente cliente) {
        validarClienteCreate(cliente); // Validação ocorre apenas no POST
        try {
            return clienteRepository.save(cliente);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new TelefoneUniqueViolationException("Este telefone já foi cadastrado");
        }
    }
	 // Método usado para atualizar um cliente existente (PUT)
    
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarporId(id); // Verifica se o cliente existe
        
        try {
        	// Atualiza apenas os campos enviados na requisição
            if (clienteAtualizado.getNome() != null) {
            	if (clienteAtualizado.getNome().trim().isEmpty()) {
            		throw new IllegalArgumentException("O nome não pode estar em branco.");
                }
            	if (!clienteAtualizado.getNome().matches("[A-Za-zÀ-ÿ\\s]+")) {
    	        	throw new IllegalArgumentException("O nome deve conter apenas letras e espaços.");
    	        }
                clienteExistente.setNome(clienteAtualizado.getNome());
            }
            if (clienteAtualizado.getTelefone() != null) {
            	if (String.valueOf(clienteAtualizado.getTelefone()).length() != 11) {
            			throw new IllegalArgumentException("Telefone deve conter 11 dígitos.");
            	}
                clienteExistente.setTelefone(clienteAtualizado.getTelefone());
            }
            if (clienteAtualizado.getSaldo_cc() != null) {
            	if (clienteAtualizado.getSaldo_cc() < 0) {
                    throw new NegativeSaldoException("O saldo não pode ser negativo.");
                }
            	clienteExistente.setSaldo_cc(clienteAtualizado.getSaldo_cc());
            }
            
            if (clienteAtualizado.getCorrentista() != null) {
                clienteExistente.setCorrentista(clienteAtualizado.getCorrentista());
            }
            
            return clienteRepository.save(clienteExistente);
            
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new TelefoneUniqueViolationException("Este telefone já foi cadastrado");
        }
    }
	
	public List<Cliente> listaCliente(){
		return clienteRepository.findAll();
	}

	
	public Cliente buscarporId(Long id){
		return clienteRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException(String.format("Cliente id=%s não encontrado", id))
		);
	}	
	
	
	@Transactional
    public Cliente removerPorId(Long id) {
            Cliente cliente = buscarporId(id);
            
            if (!cliente.getCorrentista()) {
                clienteRepository.deleteById(id); // Exclui o cliente se não for correntista
            } else {
                throw new CannotDeleteException("A conta corrente está ativa e não pode ser excluída.");
            }
            
            return null;
	}
	
	@Transactional
	public void ativarConta(Long id) {	
		//Busca o cliente no banco
		Cliente cliente = buscarporId(id);
			cliente.ativarCorrentista();
		clienteRepository.save(cliente);

	}
	
	
	 @Transactional
	    public void desativarConta(Long id) {
	            // Busca o cliente no banco
	            Cliente cliente = buscarporId(id);

	            if (cliente.getSaldo_cc() == 0) {
	                cliente.DesativarCorrentista();
	                clienteRepository.save(cliente);
	            } else {
	                throw new CannotDisableException("É necessário estar com saldo zerado para desativar a conta corrente.");
	            }	
	 
	        }
  
	 
	 public void validarClienteCreate(Cliente cliente) {
	        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
	            throw new RequiredNameException("O nome é obrigatório.");
	        }
	        
	        // Verifica se o nome contém apenas letras
	        if (!cliente.getNome().matches("[A-Za-zÀ-ÿ\\s]+")) {
	        	throw new IllegalArgumentException("O nome deve conter apenas letras e espaços.");
	        }

	        if (cliente.getCorrentista() == null) {
	            throw new IllegalArgumentException("O correntista é obrigatório e deve ser verdadeiro ou falso.");
	        }
                
	        if (cliente.getSaldo_cc() == null) {
	            throw new RequiredSaldoException("O saldo é obrigatório");
	        }
	        
	        
	        if (cliente.getSaldo_cc() < 0 ) {
	            throw new NegativeSaldoException("O saldo deve ser um número positivo.");
	        }
	        
	        
	        if (cliente.getTelefone() == null || String.valueOf(cliente.getTelefone()).length() != 11) {
            throw new RequiredPhoneException("O telefone do cliente é obrigatório e só pode ter 11 caracteres.");
	        }
	        
	    }
	
}

