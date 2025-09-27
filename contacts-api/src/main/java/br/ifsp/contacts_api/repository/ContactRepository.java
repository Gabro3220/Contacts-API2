package br.ifsp.contacts_api.repository;

import br.ifsp.contacts_api.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de banco de dados relacionadas a Contact.
 * Estende JpaRepository para operações CRUD básicas.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    /**
     * Busca contatos pelo nome (case-insensitive).
     * Utiliza LIKE para busca parcial no nome.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @return Lista de contatos que correspondem ao nome fornecido
     */
    @Query("SELECT c FROM Contact c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Contact> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca contatos pelo nome com paginação e ordenação.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @param pageable Configuração de paginação e ordenação
     * @return Página de contatos que correspondem ao nome fornecido
     */
    @Query("SELECT c FROM Contact c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Contact> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca todos os contatos com paginação e ordenação.
     * 
     * @param pageable Configuração de paginação e ordenação
     * @return Página de contatos
     */
    Page<Contact> findAll(Pageable pageable);
}
