package com.foro.demo.Controlador;

import com.foro.demo.Entidades.Topic;
import com.foro.demo.Entidades.Usuario;
import com.tuproyecto.foro.servicio.ForoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// --- DTOs (Data Transfer Objects) para manejar las peticiones de forma limpia ---
record LoginRequest(String email, String contrasena) {}
record RegistroRequest(String nombre, String email, String contrasena) {}
record RespuestaRequest(Long usuarioId, String mensaje) {}

@RestController
@RequestMapping("/api/foro") // Ruta base para este controlador
public class ForoController {

    @Autowired
    private ForoService foroService;

    /**
     * Endpoint para registrar un nuevo usuario.
     * URL: POST /api/foro/registro
     */
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody RegistroRequest request) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.nombre());
        nuevoUsuario.setEmail(request.email());
        nuevoUsuario.setContrasena(request.contrasena());
        Usuario usuarioGuardado = foroService.crearUsuario(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    /**
     * Endpoint para iniciar sesión.
     * URL: POST /api/foro/login
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody LoginRequest request) {
        Optional<Usuario> usuario = foroService.login(request.email(), request.contrasena());
        if (usuario.isPresent()) {
            return ResponseEntity.ok("Login exitoso para el usuario: " + usuario.get().getNombre() + " (ID: " + usuario.get().getId() + ")");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    /**
     * Endpoint que muestra el menú principal del foro con los últimos temas.
     * URL: GET /api/foro/menu
     */
    @GetMapping("/menu")
    public ResponseEntity<List<Topic>> verUltimosMensajes() {
        List<Topic> ultimosTopics = foroService.obtenerUltimosTopicsPrincipales();
        return ResponseEntity.ok(ultimosTopics);
    }

    /**
     * Endpoint para responder a un mensaje específico.
     * URL: POST /api/foro/topics/{topicId}/responder
     */
    @PostMapping("/topics/{topicId}/responder")
    public ResponseEntity<Topic> responderMensaje(
            @PathVariable Long topicId,
            @RequestBody RespuestaRequest request) {
        try {
            Topic respuesta = foroService.responderATopic(topicId, request.usuarioId(), request.mensaje());
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Devuelve 404 si el usuario o el topic padre no se encuentran.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
