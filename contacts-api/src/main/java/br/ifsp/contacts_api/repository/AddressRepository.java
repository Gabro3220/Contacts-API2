package br.ifsp.contacts_api.repository;

import br.ifsp.contacts_api.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de banco de dados relacionadas a Address.
 * Estende JpaRepository para operações CRUD básicas.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    /**
     * Busca todos os endereços de um contato específico.
     * 
     * @param contactId ID do contato
     * @return Lista de endereços do contato
     */
    List<Address> findByContactId(Long contactId);
    
    /**
     * Busca todos os endereços de um contato específico com paginação.
     * 
     * @param contactId ID do contato
     * @param pageable Configuração de paginação e ordenação
     * @return Página de endereços do contato
     */
    Page<Address> findByContactId(Long contactId, Pageable pageable);
    
    /**
     * Busca todos os endereços com paginação e ordenação.
     * 
     * @param pageable Configuração de paginação e ordenação
     * @return Página de endereços
     */
    Page<Address> findAll(Pageable pageable);
}
