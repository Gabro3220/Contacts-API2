package br.ifsp.contacts_api.dto;

import br.ifsp.contacts_api.entity.Contact;
import java.util.List;
import java.util.ArrayList;

/**
 * DTO para respostas de contatos.
 * Usado para formatar a resposta da API sem expor detalhes internos da entidade.
 * Inclui lista de endereços para facilitar a visualização completa do contato.
 */
public class ContactResponse {
    
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private List<AddressResponse> addresses;
    
    // Construtores
    public ContactResponse() {
        this.addresses = new ArrayList<>();
    }
    
    public ContactResponse(Long id, String nome, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.addresses = new ArrayList<>();
    }
    
    public ContactResponse(Long id, String nome, String telefone, String email, List<AddressResponse> addresses) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.addresses = addresses != null ? addresses : new ArrayList<>();
    }
    
    /**
     * Converte uma entidade Contact para ContactResponse.
     * 
     * @param contact Entidade Contact
     * @return ContactResponse correspondente
     */
    public static ContactResponse fromEntity(Contact contact) {
        List<AddressResponse> addressResponses = new ArrayList<>();
        if (contact.getAddresses() != null) {
            addressResponses = contact.getAddresses().stream()
                    .map(AddressResponse::fromEntity)
                    .toList();
        }
        
        return new ContactResponse(
            contact.getId(),
            contact.getNome(),
            contact.getTelefone(),
            contact.getEmail(),
            addressResponses
        );
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public List<AddressResponse> getAddresses() {
        return addresses;
    }
    
    public void setAddresses(List<AddressResponse> addresses) {
        this.addresses = addresses;
    }
}
