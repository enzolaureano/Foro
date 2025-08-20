package com.tuproyecto.foro.servicio;

import com.foro.demo.Entidades.Topic;
import com.foro.demo.Entidades.Usuario;
import com.foro.demo.Repositorio.TopicRepository;
import com.foro.demo.Repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ForoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicRepository topicRepository;

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Topic crearNuevoTopic(Long usuarioId, String mensaje) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));
        Topic nuevoTopic = new Topic();
        nuevoTopic.setUsuario(usuario);
        nuevoTopic.setMensaje(mensaje);
        nuevoTopic.setFechaCreacion(LocalDateTime.now());
        return topicRepository.save(nuevoTopic);
    }

    // --- NUEVOS MÉTODOS ---

    /**
     * Valida las credenciales de un usuario.
     */
    public Optional<Usuario> login(String email, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getContrasena().equals(contrasena)) {
            return usuarioOpt;
        }
        return Optional.empty();
    }

    /**
     * Obtiene los hilos de conversación principales, ordenados por fecha.
     */
    public List<Topic> obtenerUltimosTopicsPrincipales() {
        return topicRepository.findByParentIsNull(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
    }

    /**
     * Crea una respuesta para un topic existente.
     */
    @Transactional
    public Topic responderATopic(Long parentTopicId, Long usuarioId, String mensaje) {
        Topic parentTopic = topicRepository.findById(parentTopicId)
                .orElseThrow(() -> new RuntimeException("Topic padre no encontrado con id: " + parentTopicId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        Topic respuesta = new Topic();
        respuesta.setUsuario(usuario);
        respuesta.setMensaje(mensaje);
        respuesta.setParent(parentTopic); // Se establece la relación padre-hijo
        respuesta.setFechaCreacion(LocalDateTime.now());

        return topicRepository.save(respuesta);
    }
}