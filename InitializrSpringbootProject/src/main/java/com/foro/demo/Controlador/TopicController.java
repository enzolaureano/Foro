package com.foro.demo.Controlador;

import com.tu.foro.modelo.Topic;
import com.tu.foro.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// DTO (Data Transfer Object) para recibir la petición de crear un topic
record TopicRequest(Long usuarioId, String mensaje) {}

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private UsuarioService usuarioService; // Reutilizamos el servicio de usuario

    @PostMapping
    public ResponseEntity<Topic> crearTopic(@RequestBody TopicRequest request) {
        try {
            Topic nuevoTopic = usuarioService.crearNuevoTopic(request.usuarioId(), request.mensaje());
            return new ResponseEntity<>(nuevoTopic, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Si el usuario no existe
        }
    }
}