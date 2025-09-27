package br.ifsp.contacts_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para atualização parcial de contatos (PATCH).
 * Todos os campos são opcionais para permitir atualizações parciais.
 * Utilizado para receber apenas os campos que devem ser atualizados.
 */
public class ContactUpdateRequest {
    
    @Size(min = 8, max = 15, message = "O telefone deve ter entre 8 e 15 caracteres")
    private String nome;
    
    @Size(min = 8, max = 15, message = "O telefone deve ter entre 8 e 15 caracteres")
    private String telefone;
    
    @Email(message = "O email deve ter um formato válido")
    private String email;
    
    // Construtores
    public ContactUpdateRequest() {}
    
    public ContactUpdateRequest(String nome, String telefone, String email) {
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
