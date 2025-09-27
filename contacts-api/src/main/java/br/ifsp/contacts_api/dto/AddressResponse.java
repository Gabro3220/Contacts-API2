package br.ifsp.contacts_api.dto;

import br.ifsp.contacts_api.entity.Address;

/**
 * DTO para respostas de endereços.
 */
public class AddressResponse {
    
    private Long id;
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private Long contactId;
    
    // Construtores
    public AddressResponse() {}
    
    public AddressResponse(Long id, String rua, String cidade, String estado, String cep, Long contactId) {
        this.id = id;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.contactId = contactId;
    }
    
    /**
     * Converte uma entidade Address para AddressResponse.
     * 
     * @param address Entidade Address
     * @return AddressResponse correspondente
     */
    public static AddressResponse fromEntity(Address address) {
        return new AddressResponse(
            address.getId(),
            address.getRua(),
            address.getCidade(),
            address.getEstado(),
            address.getCep(),
            address.getContact().getId()
        );
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRua() {
        return rua;
    }
    
    public void setRua(String rua) {
        this.rua = rua;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCep() {
        return cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }
    
    public Long getContactId() {
        return contactId;
    }
    
    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
