package com.labanta.servidorlocal.repository;

import com.labanta.servidorlocal.model.Servico;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByEstadoTrue();

    List<Servico> findByPrecoLessThan(Double valorMaximo);

    Optional<Servico> findById(@NonNull Long id);


    // Encontra todos os serviços cujo título contenha esta palavra, ignorando maiúsculas e minúsculas
    List<Servico> findByTituloContainingIgnoreCase(String termo);
}
