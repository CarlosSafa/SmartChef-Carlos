package com.example.smartchef;

import com.example.smartchef.dtos.response.RecetaPopularDto;
import com.example.smartchef.dtos.response.ResumenDiaDto;
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
    @Autowired private RegistroComidaRepository registroComidaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    // ---------- Datos de prueba ----------

    private Usuario crearUsuario(String nombre, Rol rol) {
        return usuarioRepository.save(Usuario.builder()
                .nombre(nombre).email(nombre.toLowerCase() + "@prueba.es")
                .contrasena("pass123").rol(rol)
                .build());
    }

    private Receta crearReceta(String nombre, Dificultad dificultad, int tiempo) {
        Usuario chef = crearUsuario("Chef" + nombre.replace(" ", ""), Rol.CHEF);
        return recetaRepository.save(Receta.builder()
                .nombre(nombre).descripcion("Receta de prueba")
                .dificultad(dificultad).tiempo(tiempo).raciones(4)
                .autor(chef)
                .build());
    }

    private Ingrediente crearIngrediente(String nombre) {
        return ingredienteRepository.save(Ingrediente.builder()
                .nombre(nombre).unidadMedida(UnidadMedida.GRAMOS).categoria(Categoria.DESPENSA)
                .calorias(new BigDecimal("1.00")).precio(new BigDecimal("0.0100"))
                .build());
    }

    private void registrar(Usuario usuario, Receta receta, Momento momento) {
        registroComidaRepository.save(RegistroComida.builder()
                .usuario(usuario).receta(receta).fecha(LocalDate.now()).momento(momento)
                .build());
    }

    // ---------- Tests ----------

    @Test
    @DisplayName("La aplicación arranca y las entidades coinciden con las tablas (ddl-auto=validate)")
    void contextLoads() {
    }

    @Test
    @DisplayName("Endpoint 1 · GET /recetas · consulta derivada con proyección RecetaTarjetaView (Q1, Q5, Q6)")
    void endpoint1_catalogoConFiltros() {
        crearReceta("Tortilla de prueba", Dificultad.MEDIA, 25);
        crearReceta("Tortilla rápida de prueba", Dificultad.FACIL, 10);
        Receta retirada = crearReceta("Tortilla retirada de prueba", Dificultad.FACIL, 5);
        retirada.setActiva(false);

        Page<RecetaTarjetaView> pagina = recetaRepository
                .findByActivaTrueAndNombreContainingIgnoreCaseAndDificultadInAndTiempoLessThanEqual(
                        "TORTILLA", List.of(Dificultad.FACIL, Dificultad.MEDIA, Dificultad.DIFICIL), 20, PageRequest.of(0, 5));

        System.out.println("Endpoint 1 -> recetas encontradas:");
        pagina.forEach(r -> System.out.println("  " + r.getNombre() + " (" + r.getTiempo() + " min)"));

        assertEquals(1, pagina.getTotalElements(), "Solo la rápida: la de 25 min tarda más y la retirada no sale (RN-09)");
        assertEquals("Tortilla rápida de prueba", pagina.getContent().get(0).getNombre());
    }

    @Test
    @DisplayName("Endpoint 2 · GET /recetas/{id} · findById con @EntityGraph (Q8) y sumas de calorías y coste (Q2, RN-02)")
    void endpoint2_detalleReceta() {
        Receta receta = crearReceta("Ensalada de prueba", Dificultad.FACIL, 10);
        Ingrediente lechuga = crearIngrediente("Lechuga de prueba"); // 1 caloría y 0,01 € por gramo
        receta.getIngredientes().add(RecetaIngrediente.builder()
                .receta(receta).ingrediente(lechuga).cantidad(new BigDecimal("400"))
                .build());
        recetaRepository.flush();

        Optional<Receta> encontrada = recetaRepository.findById(receta.getId());

        assertTrue(encontrada.isPresent());
        assertNotNull(encontrada.get().getCreadoEn(), "La auditoría rellena creadoEn sola (@PrePersist)");
        assertEquals(0, new BigDecimal("400").compareTo(recetaRepository.sumarCalorias(receta.getId())), "400 g x 1 caloría");
        assertEquals(0, new BigDecimal("4").compareTo(recetaRepository.sumarCoste(receta.getId())), "400 g x 0,01 €");
    }

    @Test
    @DisplayName("Endpoint 6 · GET /ingredientes?texto= · findByNombreContainingIgnoreCase paginado (Q1, Q6)")
    void endpoint6_catalogoIngredientes() {
        crearIngrediente("Huevo campero de prueba");

        Page<Ingrediente> pagina = ingredienteRepository.findByNombreContainingIgnoreCase("CAMPERO", PageRequest.of(0, 5));

        System.out.println("Endpoint 6 -> ingredientes encontrados: " + pagina.getTotalElements());
        assertEquals(1, pagina.getTotalElements());
    }


    @Test
    @DisplayName("Endpoints 8 y 9 · /despensa · findByUsuarioIdOrderByCaducidadAsc y findByUsuarioIdAndIngredienteId (Q1)")
    void endpoints8y9_despensa() {
        Usuario usuario = crearUsuario("Carlos", Rol.USUARIO);
        Ingrediente leche = crearIngrediente("Leche de prueba");
        Ingrediente pan = crearIngrediente("Pan de prueba");

        despensaRepository.save(Despensa.builder()
                .usuario(usuario).ingrediente(leche)
                .cantidad(new BigDecimal("1.00")).caducidad(LocalDate.now().plusDays(10))
                .build());
        despensaRepository.save(Despensa.builder()
                .usuario(usuario).ingrediente(pan)
                .cantidad(new BigDecimal("1.00")).caducidad(LocalDate.now().plusDays(2))
                .build());

        List<Despensa> despensa = despensaRepository.findByUsuarioIdOrderByCaducidadAsc(usuario.getId());

        System.out.println("Endpoint 8 -> despensa:");
        despensa.forEach(d -> System.out.println("  " + d.getIngrediente().getNombre() + " caduca " + d.getCaducidad()));

        assertEquals(2, despensa.size());
        assertEquals("Pan de prueba", despensa.get(0).getIngrediente().getNombre(), "El que caduca antes va primero");
        assertTrue(despensaRepository.findByUsuarioIdAndIngredienteId(usuario.getId(), leche.getId()).isPresent());
    }

    @Test
    @DisplayName("Endpoints 12 y 13 · /colecciones · findByUsuarioIdAndNombreIgnoreCase y @ManyToMany")
    void endpoints12y13_colecciones() {
        Usuario usuario = crearUsuario("Ana", Rol.USUARIO);
        Receta receta = crearReceta("Lentejas de prueba", Dificultad.MEDIA, 60);

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
    @DisplayName("Endpoint 14 · POST /registros · existsByUsuarioIdAndFechaAndMomento (Q1, RN-07)")
    void endpoint14_registroDuplicado() {
        Usuario usuario = crearUsuario("Luis", Rol.USUARIO);
        Receta receta = crearReceta("Gazpacho de prueba", Dificultad.FACIL, 15);
        registrar(usuario, receta, Momento.CENA);

        assertTrue(registroComidaRepository.existsByUsuarioIdAndFechaAndMomento(usuario.getId(), LocalDate.now(), Momento.CENA),
                "Ya hay una cena hoy");
        assertFalse(registroComidaRepository.existsByUsuarioIdAndFechaAndMomento(usuario.getId(), LocalDate.now(), Momento.COMIDA),
                "No hay comida hoy");
    }

    @Test
    @DisplayName("Endpoint 15 · GET /registros/semana · SELECT new ResumenDiaDto (Q5)")
    void endpoint15_semanaConCalorias() {
        Usuario usuario = crearUsuario("Rosa", Rol.USUARIO);
        Usuario chef = crearUsuario("ChefRosa", Rol.CHEF);
        Ingrediente arroz = crearIngrediente("Arroz de prueba"); // 1 caloría por gramo

        // Receta de 4 raciones con 400 g de arroz: 400 calorías en total, 100 por ración
        Receta receta = Receta.builder()
                .nombre("Arroz blanco de prueba").dificultad(Dificultad.FACIL)
                .tiempo(20).raciones(4).autor(chef)
                .build();
        receta.getIngredientes().add(RecetaIngrediente.builder()
                .receta(receta).ingrediente(arroz).cantidad(new BigDecimal("400"))
                .build());
        recetaRepository.save(receta);

        // Hoy: comida y cena con la receta (100 + 100) y un desayuno libre sin receta (0)
        registrar(usuario, receta, Momento.COMIDA);
        registrar(usuario, receta, Momento.CENA);
        registroComidaRepository.save(RegistroComida.builder()
                .usuario(usuario).descripcion("Café con tostada").fecha(LocalDate.now()).momento(Momento.DESAYUNO)
                .build());
        // Hace 7 días (la semana anterior): no debe contar
        registroComidaRepository.save(RegistroComida.builder()
                .usuario(usuario).receta(receta).fecha(LocalDate.now().minusDays(7)).momento(Momento.CENA)
                .build());

        LocalDate lunes = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        List<ResumenDiaDto> dias = registroComidaRepository.findCaloriasPorDia(usuario.getId(), lunes, lunes.plusDays(6));

        System.out.println("Endpoint 15 -> calorías por día:");
        dias.forEach(d -> System.out.println("  " + d.fecha() + ": " + d.calorias()));

        assertEquals(1, dias.size(), "Solo hoy: lo de hace 7 días es de otra semana");
        assertEquals(LocalDate.now(), dias.get(0).fecha());
        assertEquals(0, new BigDecimal("200").compareTo(dias.get(0).calorias()), "100 + 100 + 0 (la comida libre no suma)");
    }

    @Test
    @DisplayName("Endpoint 16 · GET /informes/recetas-populares · SELECT new RecetaPopularDto (Q2, Q5)")
    void endpoint16_informeRecetasPopulares() {
        Usuario marta = crearUsuario("Marta", Rol.USUARIO);
        Usuario pedro = crearUsuario("Pedro", Rol.USUARIO);
        Receta paella = crearReceta("Paella de prueba", Dificultad.MEDIA, 50);
        Receta sopa = crearReceta("Sopa de prueba", Dificultad.FACIL, 20);

        // La paella se cocina 3 veces hoy (más que cualquier receta de data.sql) y la sopa 1
        registrar(marta, paella, Momento.DESAYUNO);
        registrar(marta, paella, Momento.COMIDA);
        registrar(marta, paella, Momento.CENA);
        registrar(pedro, sopa, Momento.CENA);

        Coleccion coleccion = Coleccion.builder().nombre("Para el táper").usuario(marta).build();
        coleccion.getRecetas().add(sopa);
        coleccionRepository.save(coleccion);

        List<RecetaPopularDto> cocinadas = recetaRepository.findRecetasMasCocinadas(LocalDate.now(), LocalDate.now());
        List<RecetaPopularDto> guardadas = coleccionRepository.findRecetasMasGuardadas();

        System.out.println("Endpoint 16 -> más cocinadas:");
        cocinadas.forEach(d -> System.out.println("  " + d.nombre() + ": " + d.total()));
        System.out.println("Endpoint 16 -> más guardadas:");
        guardadas.forEach(d -> System.out.println("  " + d.nombre() + ": " + d.total()));

        RecetaPopularDto primera = cocinadas.get(0);
        assertEquals("Paella de prueba", primera.nombre(), "La más cocinada va primero");
        assertEquals(3L, primera.total());

        boolean sopaGuardada = false;
        for (RecetaPopularDto fila : guardadas) {
            if (fila.nombre().equals("Sopa de prueba")) {
                sopaGuardada = true;
            }
        }
        assertTrue(sopaGuardada, "La sopa está guardada en una colección");
    }
}
