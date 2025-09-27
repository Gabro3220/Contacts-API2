package br.ifsp.contacts_api.controller;

import br.ifsp.contacts_api.dto.ContactRequest;
import br.ifsp.contacts_api.dto.ContactResponse;
import br.ifsp.contacts_api.dto.ContactUpdateRequest;
import br.ifsp.contacts_api.dto.PageResponse;
import br.ifsp.contacts_api.entity.Contact;
import br.ifsp.contacts_api.repository.ContactRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * Controller REST para gerenciamento de contatos.
 * Implementa operações CRUD, busca por nome e paginação.
 */
@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
@Tag(name = "Contatos", description = "Operações para gerenciamento de contatos")
public class ContactController {
    
    @Autowired
    private ContactRepository contactRepository;
    
    /**
     * Lista todos os contatos.
     * 
     * @return Lista de todos os contatos
     */
    @Operation(summary = "Listar todos os contatos", description = "Retorna uma lista com todos os contatos cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de contatos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ContactResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        List<ContactResponse> response = contacts.stream()
                .map(ContactResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * EXERCÍCIO 3: Lista todos os contatos com paginação e ordenação.
     * 
     * @param page Número da página (padrão: 0)
     * @param size Tamanho da página (padrão: 10)
     * @param sort Campo para ordenação (padrão: id)
     * @param direction Direção da ordenação (ASC/DESC, padrão: ASC)
     * @return Página de contatos
     */
    @Operation(summary = "Listar contatos com paginação", description = "Retorna uma página de contatos com paginação e ordenação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de contatos retornada com sucesso")
    })
    @GetMapping("/paginated")
    public ResponseEntity<PageResponse<ContactResponse>> getAllContactsPaginated(
            @Parameter(description = "Número da página (começando em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação (id, nome, email, telefone)") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação (ASC ou DESC)") @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<Contact> contactPage = contactRepository.findAll(pageable);
        Page<ContactResponse> responsePage = contactPage.map(ContactResponse::fromEntity);
        
        return ResponseEntity.ok(PageResponse.fromPage(responsePage));
    }
    
    /**
     * Busca um contato por ID.
     * 
     * @param id ID do contato
     * @return Contato encontrado ou 404 se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            return ResponseEntity.ok(ContactResponse.fromEntity(contact.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * EXERCÍCIO 1: Busca contatos pelo nome.
     * Endpoint: GET /api/contacts/search?name=João
     * 
     * @param name Nome ou parte do nome a ser buscado
     * @return Lista de contatos que correspondem ao nome
     */
    @Operation(summary = "Buscar contatos por nome", description = "Busca contatos que contenham o nome fornecido (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de contatos encontrados")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ContactResponse>> searchContactsByName(
            @Parameter(description = "Nome ou parte do nome para buscar") @RequestParam String name) {
        List<Contact> contacts = contactRepository.findByNomeContainingIgnoreCase(name);
        List<ContactResponse> response = contacts.stream()
                .map(ContactResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    /**
     * EXERCÍCIO 3: Busca contatos pelo nome com paginação e ordenação.
     * 
     * @param name Nome ou parte do nome a ser buscado
     * @param page Número da página (padrão: 0)
     * @param size Tamanho da página (padrão: 10)
     * @param sort Campo para ordenação (padrão: id)
     * @param direction Direção da ordenação (ASC/DESC, padrão: ASC)
     * @return Página de contatos que correspondem ao nome
     */
    @GetMapping("/search/paginated")
    public ResponseEntity<PageResponse<ContactResponse>> searchContactsByNamePaginated(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<Contact> contactPage = contactRepository.findByNomeContainingIgnoreCase(name, pageable);
        Page<ContactResponse> responsePage = contactPage.map(ContactResponse::fromEntity);
        
        return ResponseEntity.ok(PageResponse.fromPage(responsePage));
    }
    
    /**
     * Cria um novo contato.
     * 
     * @param contactRequest Dados do contato a ser criado
     * @return Contato criado
     */
    @Operation(summary = "Criar novo contato", description = "Cria um novo contato com validação de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contato criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do contato a ser criado")
            @Valid @RequestBody ContactRequest contactRequest) {
        Contact contact = new Contact();
        contact.setNome(contactRequest.getNome());
        contact.setTelefone(contactRequest.getTelefone());
        contact.setEmail(contactRequest.getEmail());
        
        Contact savedContact = contactRepository.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ContactResponse.fromEntity(savedContact));
    }
    
    /**
     * Atualiza um contato completamente (PUT).
     * 
     * @param id ID do contato
     * @param contactRequest Novos dados do contato
     * @return Contato atualizado ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long id, 
                                                       @Valid @RequestBody ContactRequest contactRequest) {
        Optional<Contact> existingContact = contactRepository.findById(id);
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.setNome(contactRequest.getNome());
            contact.setTelefone(contactRequest.getTelefone());
            contact.setEmail(contactRequest.getEmail());
            
            Contact updatedContact = contactRepository.save(contact);
            return ResponseEntity.ok(ContactResponse.fromEntity(updatedContact));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * EXERCÍCIO 2: Atualiza parcialmente um contato (PATCH).
     * Permite atualizar apenas os campos enviados na requisição.
     * 
     * @param id ID do contato
     * @param updateRequest Campos a serem atualizados (apenas os enviados)
     * @return Contato atualizado ou 404 se não existir
     */
    @Operation(summary = "Atualizar contato parcialmente", description = "Atualiza apenas os campos especificados do contato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ContactResponse> partialUpdateContact(
            @Parameter(description = "ID do contato a ser atualizado") @PathVariable Long id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Campos a serem atualizados")
            @RequestBody ContactUpdateRequest updateRequest) {
        Optional<Contact> existingContact = contactRepository.findById(id);
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            
            // Atualiza apenas os campos que foram enviados (não nulos)
            if (updateRequest.getNome() != null) {
                contact.setNome(updateRequest.getNome());
            }
            if (updateRequest.getTelefone() != null) {
                contact.setTelefone(updateRequest.getTelefone());
            }
            if (updateRequest.getEmail() != null) {
                contact.setEmail(updateRequest.getEmail());
            }
            
            Contact updatedContact = contactRepository.save(contact);
            return ResponseEntity.ok(ContactResponse.fromEntity(updatedContact));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Remove um contato.
     * 
     * @param id ID do contato
     * @return 204 No Content se removido com sucesso, 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
