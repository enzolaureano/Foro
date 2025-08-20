package com.foro.demo.Entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private com.tu.foro.modelo.Usuario usuario;

    @Lob
    @Column(nullable = false)
    private String mensaje;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // --- NUEVO: Relación para respuestas anidadas ---

    // La respuesta conoce a su padre.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference // Evita bucles infinitos al serializar a JSON
    private Topic parent;

    // El topic padre conoce a sus respuestas.
    // FetchType.EAGER para que al cargar un topic, se carguen también sus respuestas.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference // Evita bucles infinitos al serializar a JSON
    private List<Topic> respuestas = new ArrayList<>();
}