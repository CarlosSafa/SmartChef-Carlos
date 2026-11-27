package com.example.smartchef;

import com.example.smartchef.dtos.response.RecetaPopularDto;
import com.example.smartchef.modelos.*;
import com.example.smartchef.repositorios.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Cada test prueba la consulta que usa un endpoint. El nombre del test dice qué endpoint es.
// @Transactional: al acabar cada test se deshace todo, así no se ensucia la base de datos.
@SpringBootTest
@Transactional
class SmartChefApplicationTests {

    @Autowired private ColeccionRepository coleccionRepository;
    @Autowired private DespensaRepository despensaRepository;
    @Autowired private IngredienteRepository ingredienteRepository;
    @Autowired private RecetaRepository recetaRepository;
    @Autowired private UsuarioRecetaRepository usuarioRecetaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    // ---------- Datos de prueba ----------

    private Usuario crearUsuario(String nombre) {
        return usuarioRepository.save(Usuario.builder().nombre(nombre).contrasena("pass123").rol(1).build());
    }

    private Receta crearReceta(String nombre, int dificultad, int tiempo) {
        return recetaRepository.save(Receta.builder()
                .nombre(nombre).descripcion("Receta de prueba")
                .dificultad(dificultad).tiempo(tiempo).raciones(4)
                .build());
    }

    private Ingrediente crearIngrediente(String nombre) {
        return ingredienteRepository.save(Ingrediente.builder()
                .nombre(nombre).unidadMedida(1)
                .calorias(new BigDecimal("1.00")).precio(new BigDecimal("0.01"))
                .build());
    }

    // ---------- Tests ----------

    @Test
    @DisplayName("La aplicación arranca y las entidades coinciden con las tablas (ddl-auto=validate)")
    void contextLoads() {
    }

    @Test
    @DisplayName("Endpoint 1 · GET /recetas · Specification con filtros combinables (Q7) y paginación (Q6)")
    void endpoint1_catalogoConFiltros() {
        crearReceta("Tortilla de patatas", 1, 25);
        crearReceta("Tortilla francesa", 1, 10);
        crearReceta("Cocido", 3, 180);

        Specification<Receta> filtro = RecetaSpecifications.todas()
                .and(RecetaSpecifications.nombreContiene("TORTILLA"))
                .and(RecetaSpecifications.tiempoMaximo(20));
        Page<Receta> pagina = recetaRepository.findAll(filtro, PageRequest.of(0, 5));

        System.out.println("Endpoint 1 -> recetas encontradas:");
        pagina.forEach(r -> System.out.println("  " + r.getNombre() + " (" + r.getTiempo() + " min)"));

        assertEquals(1, pagina.getTotalElements(), "Solo la tortilla francesa cumple nombre + tiempo <= 20");
        assertEquals("Tortilla francesa", pagina.getContent().get(0).getNombre());
    }

    @Test
    @DisplayName("Endpoint 2 · GET /recetas/{id} · findById con @EntityGraph (Q8)")
    void endpoint2_detalleReceta() {
        Receta receta = crearReceta("Ensalada", 1, 10);

        Optional<Receta> encontrada = recetaRepository.findById(receta.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("Ensalada", encontrada.get().getNombre());
    }

    @Test
    @DisplayName("Endpoint 6 · GET /ingredientes?texto= · findByNombreContainingIgnoreCase paginado (Q1, Q6)")
    void endpoint6_catalogoIngredientes() {
        crearIngrediente("Huevo campero");

        Page<Ingrediente> pagina = ingredienteRepository.findByNombreContainingIgnoreCase("HUEVO", PageRequest.of(0, 5));

        System.out.println("Endpoint 6 -> ingredientes encontrados: " + pagina.getTotalElements());
        assertTrue(pagina.getTotalElements() >= 1);
    }

    @Test
    @DisplayName("Alta de ingrediente · existsByNombreIgnoreCase evita duplicados (Q1)")
    void altaIngrediente_noDuplicados() {
        crearIngrediente("Azafrán de prueba");

        assertTrue(ingredienteRepository.existsByNombreIgnoreCase("AZAFRÁN DE PRUEBA"));
        assertFalse(ingredienteRepository.existsByNombreIgnoreCase("No existe este ingrediente"));
    }

    @Test
    @DisplayName("Endpoint 8 · GET /despensa · findByUsuarioIdOrderByCaducidadAsc (Q1)")
    void endpoint8_despensaOrdenadaPorCaducidad() {
        Usuario usuario = crearUsuario("Carlos");
        Ingrediente leche = crearIngrediente("Leche de prueba");
        Ingrediente pan = crearIngrediente("Pan de prueba");

        despensaRepository.save(Despensa.builder()
                .id(new DespensaId(usuario.getId(), leche.getId()))
                .usuario(usuario).ingrediente(leche)
                .cantidad(new BigDecimal("1.00")).caducidad(LocalDate.now().plusDays(10))
                .build());
        despensaRepository.save(Despensa.builder()
                .id(new DespensaId(usuario.getId(), pan.getId()))
                .usuario(usuario).ingrediente(pan)
                .cantidad(new BigDecimal("1.00")).caducidad(LocalDate.now().plusDays(2))
                .build());

        List<Despensa> despensa = despensaRepository.findByUsuarioIdOrderByCaducidadAsc(usuario.getId());

        System.out.println("Endpoint 8 -> despensa:");
        despensa.forEach(d -> System.out.println("  " + d.getIngrediente().getNombre() + " caduca " + d.getCaducidad()));

        assertEquals(2, despensa.size());
        assertEquals("Pan de prueba", despensa.get(0).getIngrediente().getNombre(), "El que caduca antes va primero");
    }

    @Test
    @DisplayName("Endpoints 12 y 13 · /colecciones · findByUsuarioIdAndNombreIgnoreCase y @ManyToMany")
    void endpoints12y13_colecciones() {
        Usuario usuario = crearUsuario("Ana");
        Receta receta = crearReceta("Lentejas", 2, 60);

        Coleccion favoritas = Coleccion.builder().nombre("Favoritas").usuario(usuario).build();
        favoritas.getRecetas().add(receta);
        coleccionRepository.save(favoritas);

        List<Coleccion> colecciones = coleccionRepository.findByUsuarioId(usuario.getId());
        Optional<Coleccion> porNombre = coleccionRepository.findByUsuarioIdAndNombreIgnoreCase(usuario.getId(), "FAVORITAS");

        assertEquals(1, colecciones.size());
        assertTrue(porNombre.isPresent());
        assertTrue(porNombre.get().getRecetas().contains(receta));
    }

    @Test
    @DisplayName("Endpoint 14 · POST /registros · existsByUsuarioIdAndTipoAndFechaBetween (Q1, RN-07)")
    void endpoint14_registroDuplicado() {
        Usuario usuario = crearUsuario("Luis");
        Receta receta = crearReceta("Gazpacho", 1, 15);
        usuarioRecetaRepository.save(UsuarioReceta.builder()
                .usuario(usuario).receta(receta).tipo(3).fecha(LocalDateTime.now())
                .build());

        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);

        assertTrue(usuarioRecetaRepository.existsByUsuarioIdAndTipoAndFechaBetween(usuario.getId(), 3, inicio, fin),
                "Ya hay una cena hoy");
        assertFalse(usuarioRecetaRepository.existsByUsuarioIdAndTipoAndFechaBetween(usuario.getId(), 2, inicio, fin),
                "No hay comida hoy");
    }

    @Test
    @DisplayName("Endpoint 16 · GET /informes/recetas-populares · SELECT new RecetaPopularDto (Q2, Q5)")
    void endpoint16_informeRecetasPopulares() {
        Usuario usuario = crearUsuario("Marta");
        Receta paella = crearReceta("Paella de prueba", 2, 50);
        Receta sopa = crearReceta("Sopa de prueba", 1, 20);

        // La paella se cocina 2 veces y la sopa 1
        usuarioRecetaRepository.save(UsuarioReceta.builder().usuario(usuario).receta(paella).tipo(2).fecha(LocalDateTime.now()).build());
        usuarioRecetaRepository.save(UsuarioReceta.builder().usuario(usuario).receta(paella).tipo(3).fecha(LocalDateTime.now()).build());
        usuarioRecetaRepository.save(UsuarioReceta.builder().usuario(usuario).receta(sopa).tipo(3).fecha(LocalDateTime.now()).build());

        Coleccion coleccion = Coleccion.builder().nombre("Para el táper").usuario(usuario).build();
        coleccion.getRecetas().add(sopa);
        coleccionRepository.save(coleccion);

        List<RecetaPopularDto> cocinadas = recetaRepository.findRecetasMasCocinadas(
                LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59));
        List<RecetaPopularDto> guardadas = coleccionRepository.findRecetasMasGuardadas();

        System.out.println("Endpoint 16 -> más cocinadas:");
        cocinadas.forEach(d -> System.out.println("  " + d.nombre() + ": " + d.total()));
        System.out.println("Endpoint 16 -> más guardadas:");
        guardadas.forEach(d -> System.out.println("  " + d.nombre() + ": " + d.total()));

        RecetaPopularDto primera = cocinadas.get(0);
        assertEquals("Paella de prueba", primera.nombre(), "La más cocinada va primero");
        assertEquals(2L, primera.total());
        boolean sopaGuardada = false;
        for (RecetaPopularDto fila : guardadas) {
            if (fila.nombre().equals("Sopa de prueba")) {
                sopaGuardada = true;
            }
        }
        assertTrue(sopaGuardada, "La sopa está guardada en una colección");
    }
}
