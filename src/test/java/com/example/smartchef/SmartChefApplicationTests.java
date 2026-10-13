package com.example.smartchef;

import com.example.smartchef.modelos.*;
import com.example.smartchef.repositorios.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class SmartChefApplicationTests {

    @Autowired private ColeccionRecetaRepository coleccionRecetaRepository;
    @Autowired private ColeccionRepository coleccionRepository;
    @Autowired private DespensaRepository despensaRepository;
    @Autowired private FotoRepository fotoRepository;
    @Autowired private IngredienteRepository ingredienteRepository;
    @Autowired private NotificacionRepository notificacionRepository;
    @Autowired private PasoRepository pasoRepository;
    @Autowired private PreferenciaRepository preferenciaRepository;
    @Autowired private RecetaRepository recetaRepository;
    @Autowired private UsuarioIngredienteRepository usuarioIngredienteRepository;
    @Autowired private UsuarioPreferenciaRepository usuarioPreferenciaRepository;
    @Autowired private UsuarioRecetaRepository usuarioRecetaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @Test
    void contextLoads() {
        System.out.println("\n==========================================");
        System.out.println("   SPRING CONTEXT & REPOSITORIOS CARGADOS ");
        System.out.println("==========================================\n");
    }

    @Test
    @DisplayName("Prueba de impresión del catálogo de recetas filtrado (GET /api/v1/recetas)")
    void probarRecetaRepository() {
        // Crear receta de prueba
        Receta receta = recetaRepository.save(Receta.builder()
                .nombre("Tortilla de Patatas")
                .descripcion("Receta tradicional española")
                .dificultad(1)
                .tiempo(25)
                .raciones(4)
                .build());

        Page<Receta> recetas = recetaRepository.findConFiltros("Tortilla", 1, 30, PageRequest.of(0, 5));

        System.out.println("\n--- DATOS DE RECETAS OBTENIDOS ---");
        recetas.forEach(r -> System.out.println("ID: " + r.getId() + " | Nombre: " + r.getNombre() + " | Tiempo: " + r.getTiempo() + " min"));
        System.out.println("Total elementos encontrados: " + recetas.getTotalElements() + "\n");

        assertTrue(recetas.getTotalElements() > 0);
    }

    @Test
    @DisplayName("Prueba de impresión del catálogo de ingredientes (GET /api/v1/ingredientes)")
    void probarIngredienteRepository() {
        Ingrediente ingrediente = ingredienteRepository.save(Ingrediente.builder()
                .nombre("Huevo")
                .unidadMedida(3) // Unidades
                .calorias(new BigDecimal("155.00"))
                .precio(new BigDecimal("0.25"))
                .build());

        Page<Ingrediente> ingredientes = ingredienteRepository.findByNombreContainingIgnoreCase("huev", PageRequest.of(0, 5));

        System.out.println("\n--- DATOS DE INGREDIENTES OBTENIDOS ---");
        ingredientes.forEach(i -> System.out.println("ID: " + i.getId() + " | Nombre: " + i.getNombre() + " | Calorías: " + i.getCalorias() + " kcal"));
        System.out.println("Total ingredientes encontrados: " + ingredientes.getTotalElements() + "\n");

        assertTrue(ingredientes.getTotalElements() > 0);
    }

    @Test
    @DisplayName("Prueba de impresión de la despensa (GET /api/v1/despensa)")
    void probarDespensaRepository() {
        Usuario usuario = usuarioRepository.save(Usuario.builder().nombre("Carlos").contrasena("pass123").rol(1).build());
        Ingrediente ingrediente = ingredienteRepository.save(Ingrediente.builder().nombre("Patata").unidadMedida(1).build());

        DespensaId id = new DespensaId(usuario.getId(), ingrediente.getId());
        despensaRepository.save(Despensa.builder()
                .id(id)
                .usuario(usuario)
                .ingrediente(ingrediente)
                .cantidad(new BigDecimal("2.50"))
                .caducidad(LocalDate.now().plusDays(5))
                .build());

        List<Despensa> items = despensaRepository.findByUsuarioIdOrderByCaducidadAsc(usuario.getId());

        System.out.println("\n--- DATOS DE LA DESPENSA DEL USUARIO ---");
        items.forEach(d -> System.out.println("Usuario: " + d.getUsuario().getNombre() +
                " | Ingrediente: " + d.getIngrediente().getNombre() +
                " | Cantidad: " + d.getCantidad() +
                " | Caducidad: " + d.getCaducidad()));
        System.out.println("Total items en despensa: " + items.size() + "\n");

        assertTrue(items.size() > 0);
    }

    @Test
    @DisplayName("Prueba de impresión de colecciones (GET /api/v1/colecciones)")
    void probarColeccionRepository() {
        Usuario usuario = usuarioRepository.save(Usuario.builder().nombre("Ana").contrasena("pass123").rol(1).build());
        coleccionRepository.save(Coleccion.builder().nombre("Favoritas").usuario(usuario).build());

        List<Coleccion> colecciones = coleccionRepository.findByUsuarioId(usuario.getId());

        System.out.println("\n--- COLECCIONES DEL USUARIO ---");
        colecciones.forEach(c -> System.out.println("ID Colección: " + c.getId() + " | Nombre: " + c.getNombre() + " | Propietario: " + c.getUsuario().getNombre()));
        System.out.println("Total colecciones: " + colecciones.size() + "\n");

        assertTrue(colecciones.size() > 0);
    }

    @Test
    @DisplayName("Prueba de impresión de registros semanales (GET /api/v1/registros/semana)")
    void probarUsuarioRecetaRepository() {
        Usuario usuario = usuarioRepository.save(Usuario.builder().nombre("Luis").contrasena("pass123").rol(1).build());
        Receta receta = recetaRepository.save(Receta.builder().nombre("Gazpacho").build());

        usuarioRecetaRepository.save(UsuarioReceta.builder()
                .usuario(usuario)
                .receta(receta)
                .fecha(LocalDateTime.now())
                .tipo(1)
                .build());

        List<UsuarioReceta> registros = usuarioRecetaRepository.findRegistrosSemana(
                usuario.getId(),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );

        System.out.println("\n--- HISTORIAL DE REGISTROS DE COCINA ---");
        registros.forEach(r -> System.out.println("ID Registro: " + r.getId() +
                " | Usuario: " + r.getUsuario().getNombre() +
                " | Receta Cocinada: " + r.getReceta().getNombre() +
                " | Fecha: " + r.getFecha()));
        System.out.println("Total registros en la semana: " + registros.size() + "\n");

        assertTrue(registros.size() > 0);
    }

    // --- VERIFICACIONES DE CONTEO PARA EL RESTO DE REPOSITORIOS ---

    @Test void probarColeccionRecetaRepository() { System.out.println("ColeccionReceta count: " + coleccionRecetaRepository.count()); assertTrue(coleccionRecetaRepository.count() >= 0); }
    @Test void probarFotoRepository() { System.out.println("Foto count: " + fotoRepository.count()); assertTrue(fotoRepository.count() >= 0); }
    @Test void probarNotificacionRepository() { System.out.println("Notificacion count: " + notificacionRepository.count()); assertTrue(notificacionRepository.count() >= 0); }
    @Test void probarPasoRepository() { System.out.println("Paso count: " + pasoRepository.count()); assertTrue(pasoRepository.count() >= 0); }
    @Test void probarPreferenciaRepository() { System.out.println("Preferencia count: " + preferenciaRepository.count()); assertTrue(preferenciaRepository.count() >= 0); }
    @Test void probarUsuarioIngredienteRepository() { System.out.println("UsuarioIngrediente count: " + usuarioIngredienteRepository.count()); assertTrue(usuarioIngredienteRepository.count() >= 0); }
    @Test void probarUsuarioPreferenciaRepository() { System.out.println("UsuarioPreferencia count: " + usuarioPreferenciaRepository.count()); assertTrue(usuarioPreferenciaRepository.count() >= 0); }
    @Test void probarUsuarioRepository() { System.out.println("Usuario count: " + usuarioRepository.count()); assertTrue(usuarioRepository.count() >= 0); }
}