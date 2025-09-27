package br.ifsp.contacts_api.controller;

import br.ifsp.contacts_api.dto.AddressRequest;
import br.ifsp.contacts_api.dto.AddressResponse;
import br.ifsp.contacts_api.entity.Address;
import br.ifsp.contacts_api.entity.Contact;
import br.ifsp.contacts_api.repository.AddressRepository;
import br.ifsp.contacts_api.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciamento de endereços.
 * Implementa operações CRUD para endereços associados a contatos.
 */
@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
@Tag(name = "Endereços", description = "Operações para gerenciamento de endereços")
public class AddressController {
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private ContactRepository contactRepository;
    
    /**
     * Lista todos os endereços.
     * 
     * @return Lista de todos os endereços
     */
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressResponse> response = addresses.stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca um endereço por ID.
     * 
     * @param id ID do endereço
     * @return Endereço encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            return ResponseEntity.ok(AddressResponse.fromEntity(address.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * DESAFIO 1: Lista todos os endereços de um contato específico.
     * Endpoint: GET /api/contacts/{contactId}/addresses
     * 
     * @param contactId ID do contato
     * @return Lista de endereços do contato
     */
    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<List<AddressResponse>> getAddressesByContactId(@PathVariable Long contactId) {
        List<Address> addresses = addressRepository.findByContactId(contactId);
        List<AddressResponse> response = addresses.stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Cria um novo endereço para um contato.
     * 
     * @param contactId ID do contato
     * @param addressRequest Dados do endereço
     * @return Endereço criado ou 404 se o contato não existir
     */
    @PostMapping("/contacts/{contactId}")
    public ResponseEntity<AddressResponse> createAddress(@PathVariable Long contactId, 
                                                        @RequestBody AddressRequest addressRequest) {
        Optional<Contact> contact = contactRepository.findById(contactId);
        if (contact.isPresent()) {
            Address address = new Address();
            address.setRua(addressRequest.getRua());
            address.setCidade(addressRequest.getCidade());
            address.setEstado(addressRequest.getEstado());
            address.setCep(addressRequest.getCep());
            address.setContact(contact.get());
            
            Address savedAddress = addressRepository.save(address);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AddressResponse.fromEntity(savedAddress));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Atualiza um endereço.
     * 
     * @param id ID do endereço
     * @param addressRequest Novos dados do endereço
     * @return Endereço atualizado ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long id, 
                                                      @RequestBody AddressRequest addressRequest) {
        Optional<Address> existingAddress = addressRepository.findById(id);
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            address.setRua(addressRequest.getRua());
            address.setCidade(addressRequest.getCidade());
            address.setEstado(addressRequest.getEstado());
            address.setCep(addressRequest.getCep());
            
            Address updatedAddress = addressRepository.save(address);
            return ResponseEntity.ok(AddressResponse.fromEntity(updatedAddress));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Remove um endereço.
     * 
     * @param id ID do endereço
     * @return 204 No Content se removido com sucesso, 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
