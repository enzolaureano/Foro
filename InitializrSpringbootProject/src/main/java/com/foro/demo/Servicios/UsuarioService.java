package com.tu.foro.servicio;

import com.tu.foro.modelo.Topic;
import com.tu.foro.modelo.Usuario;
import com.foro.demo.Repositorio.TopicRepository;
import com.foro.demo.Repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicRepository topicRepository;

    // --- Métodos para Usuario ---
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // --- Lógica de negocio principal ---
    @Transactional
    public Topic crearNuevoTopic(Long usuarioId, String mensaje) {
        // 1. Buscamos el usuario que está creando el topic
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        // 2. Creamos la nueva instancia de Topic
        Topic nuevoTopic = new Topic();
        nuevoTopic.setUsuario(usuario);
        nuevoTopic.setMensaje(mensaje);
        nuevoTopic.setFechaCreacion(LocalDateTime.now());

        // 3. Guardamos el topic en la base de datos
        return topicRepository.save(nuevoTopic);
    }
}