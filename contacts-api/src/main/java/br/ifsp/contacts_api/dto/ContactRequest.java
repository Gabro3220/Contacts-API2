package br.ifsp.contacts_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para requisições de criação e atualização de contatos.
 * Contém as validações necessárias para os campos obrigatórios.
 * Utilizado para receber dados do cliente sem expor a estrutura interna da entidade.
 */
public class ContactRequest {
    
    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;
    
    @Size(min = 8, max = 15, message = "O telefone deve ter entre 8 e 15 caracteres")
    private String telefone;
    
    @Email(message = "O email deve ter um formato válido")
    private String email;
    
    // Construtores
    public ContactRequest() {}
    
    public ContactRequest(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }
    
    // Getters e Setters
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
