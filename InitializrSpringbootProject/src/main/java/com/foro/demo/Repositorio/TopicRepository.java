package com.foro.demo.Repositorio;

import com.foro.demo.Entidades.Topic;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    // NUEVO: Busca todos los topics que no tienen un padre (parent es null).
    // Esto nos da los hilos de conversación principales.
    List<Topic> findByParentIsNull(Sort sort);
}
