# SmartChef · Backend

API REST de SmartChef (recetas y alimentación) con Spring Boot, PostgreSQL, Spring Data JPA, MapStruct y Lombok.
Proyecto gestionado con **Maven**.

## Cómo arrancarlo

1. Tener PostgreSQL en marcha y ejecutar `docs/schema.sql` y después `docs/data.sql` (crean el esquema `smartchef` con datos de prueba; todos los usuarios tienen la contraseña `password123`).
2. Definir las variables de entorno:

| Variable | Ejemplo |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/postgres` |
| `DB_USER` | `postgres` |
| `DB_PASSWORD` | *(la contraseña de tu PostgreSQL)* |
| `SCHEMA` | `smartchef` |

3. Arrancar la aplicación:

```bash
./mvnw spring-boot:run
```

4. Lanzar todos los tests:

```bash
./mvnw test
```

## Cómo comprobar los endpoints

Hay dos formas, y las dos llaman a las **mismas URL**:

1. **Tests de controlador** (`src/test/java/.../controladores`): un test por cada endpoint y cada caso de error, hechos con `@WebMvcTest` y `MockMvc`. Cada test llama a la URL real (por ejemplo `GET /api/v1/recetas/99`) y comprueba el código HTTP y el JSON de la respuesta. El nombre de cada test dice qué endpoint y qué caso prueba.
2. **Colección de Postman** (`docs/SmartChef.postman_collection.json`): una carpeta por endpoint con el caso correcto y los de error. Se importa con *Import → File*. Las variables `baseUrl` (`http://localhost:8080/api/v1`), `usuarioId` (5, Julián) y `chefId` (2, María) se cambian en la pestaña *Variables* de la colección.

> Hasta la Fase 4 (seguridad) no hay login, así que los endpoints de un usuario reciben provisionalmente `?usuarioId=`.

## Endpoints

| # | Método | Ruta | Estado | Servicio | Test de controlador (casos) |
|---|---|---|---|---|---|
| 1 | GET | `/api/v1/recetas` | ✅ | `RecetaService.listar` | `RecetaControllerTest` (200) |
| 2 | GET | `/api/v1/recetas/{id}` | ✅ | `RecetaService.buscarPorId` | `RecetaControllerTest` (200, 404) |
| 3 | POST | `/api/v1/recetas` | ✅ | `RecetaService.crear` | `RecetaControllerTest` (201, 400, 422) |
| 4 | PUT | `/api/v1/recetas/{id}` | ⏳ | | |
| 5 | DELETE | `/api/v1/recetas/{id}` | ⏳ | | |
| 6 | GET | `/api/v1/ingredientes?texto=` | ✅ | `IngredienteService.listar` | `IngredienteControllerTest` (200) |
| 7 | POST | `/api/v1/recetas/buscar-por-ingredientes` | ⏳ | | |
| 8 | GET | `/api/v1/despensa` | ✅ | `DespensaService.listar` | `DespensaControllerTest` (200) |
| 9 | PUT | `/api/v1/despensa/{ingredienteId}` | ✅ | `DespensaService.guardar` | `DespensaControllerTest` (200, 204, 400, 404) |
| 10 | POST | `/api/v1/listas-compra/desde-receta` | ⏳ | | |
| 11 | PATCH | `/api/v1/listas-compra/{id}/cerrar` | ⏳ | | |
| 12 | GET | `/api/v1/colecciones` | ✅ | `ColeccionService.listar` | `ColeccionControllerTest` (200) |
| 13 | PUT | `/api/v1/colecciones/{nombre}/recetas/{recetaId}` | ✅ | `ColeccionService.guardarReceta` | `ColeccionControllerTest` (200, 409) |
| 14 | POST | `/api/v1/registros` | ✅ | `RegistroService.registrar` | `RegistroControllerTest` (201, 400, 409) · `RegistroServiceTest` (409, 422) |
| 15 | GET | `/api/v1/registros/semana?fecha=` | ⏳ | | |
| 16 | GET | `/api/v1/informes/recetas-populares?desde=&hasta=` | ✅ | `InformeService.recetasPopulares` | `InformeControllerTest` (200, 422) |
| Extra | GET | `/api/v1/ingredientes/{id}` | ✅ | `IngredienteService.buscarPorId` | `IngredienteControllerTest` (404) |
| Extra | POST | `/api/v1/ingredientes` | ✅ | `IngredienteService.crear` | `IngredienteControllerTest` (201, 400, 409) |

✅ hecho · ⏳ pendiente

## Tests

| Clase | Qué prueba | Necesita BD |
|---|---|---|
| `controladores/*ControllerTest` | Cada endpoint por su URL: ruta, código HTTP y JSON de respuesta (`@WebMvcTest`) | No |
| `servicios/RecetaServiceTest` | RN-01: pasos sin huecos e ingredientes sin repetir; si falla no se guarda nada | No |
| `servicios/RegistroServiceTest` | RN-07: un registro por día y momento; comida libre sin descripción; si falla no se guarda nada | No |
| `SmartChefApplicationTests` | Cada consulta de los repositorios contra PostgreSQL (una por endpoint) | Sí |
| `MapperTests` | Conversiones de MapStruct | Sí |
| `DtoValidationTests` | Validaciones de los DTO de entrada | No |

## Consultas de los repositorios

Todas las consultas se usan desde algún servicio.

| Formato | Consulta | Repositorio | La usa | Test |
|---|---|---|---|---|
| Q1 Derivada | `findByNombreContainingIgnoreCase` | `IngredienteRepository` | Endpoint 6 | `endpoint6_catalogoIngredientes` |
| Q1 Derivada | `existsByNombreIgnoreCase` | `IngredienteRepository` | Alta de ingrediente | `altaIngrediente_noDuplicados` |
| Q1 Derivada | `findByUsuarioIdOrderByCaducidadAsc` | `DespensaRepository` | Endpoint 8 | `endpoints8y9_despensa` |
| Q1 Derivada | `findByUsuarioIdAndIngredienteId` | `DespensaRepository` | Endpoint 9 | `endpoints8y9_despensa` |
| Q1 Derivada | `findByUsuarioId` | `ColeccionRepository` | Endpoint 12 | `endpoints12y13_colecciones` |
| Q1 Derivada | `findByUsuarioIdAndNombreIgnoreCase` | `ColeccionRepository` | Endpoint 13 | `endpoints12y13_colecciones` |
| Q1 Derivada | `existsByUsuarioIdAndFechaAndMomento` | `RegistroComidaRepository` | Endpoint 14 | `endpoint14_registroDuplicado` |
| Q2 JPQL (JOIN + COUNT + GROUP BY) | `findRecetasMasCocinadas` | `RecetaRepository` | Endpoint 16 | `endpoint16_informeRecetasPopulares` |
| Q2 JPQL (JOIN + COUNT + GROUP BY) | `findRecetasMasGuardadas` | `ColeccionRepository` | Endpoint 16 | `endpoint16_informeRecetasPopulares` |
| Q5 Proyección `SELECT new` a DTO | `RecetaPopularDto` en las dos consultas del informe | `RecetaRepository`, `ColeccionRepository` | Endpoint 16 | `endpoint16_informeRecetasPopulares` |
| Q6 Paginación | `Pageable` / `Page` | `RecetaRepository`, `IngredienteRepository` | Endpoints 1 y 6 | `endpoint1_catalogoConFiltros`, `endpoint6_catalogoIngredientes` |
| Q7 Specification | `RecetaSpecifications` (solo recetas activas, RN-09) | `RecetaRepository` | Endpoint 1 | `endpoint1_catalogoConFiltros` |
| Q8 Evitar N+1 | `findById` con `@EntityGraph` | `RecetaRepository` | Endpoint 2 | `endpoint2_detalleReceta` |

Pendientes: Q3 (2 consultas nativas), Q4 (`@Modifying`) y la proyección por interfaz de Q5. Irán con los endpoints 7, 15 y la tarea programada; el resumen semanal (endpoint 15) devolverá un DTO.

## Decisiones de diseño

- **Relaciones N:M sin datos propios** (`colecciones_recetas`, `usuarios_preferencias`, `ingredientes_preferencias`): se mapean con `@ManyToMany` + `@JoinTable`, **sin clase intermedia**.
- **Relaciones N:M con datos propios** (`recetas_ingredientes` guarda la cantidad; `despensas` guarda la cantidad y la caducidad): necesitan entidad, porque `@ManyToMany` no puede guardar columnas extra. El enunciado lo indica así: *"Receta–ingrediente es N:M con atributos (cantidad)"*. Tienen su propio `id` (más un `UNIQUE` en la pareja), así que no necesitan una clase Id aparte.
- **Preferencias como tabla** (`preferencias`) y no como columnas: así se puede añadir una nueva (por ejemplo SIN_LACTOSA) insertando una fila. Un ingrediente se relaciona con las preferencias que cumple, y una receta cumple una preferencia si todos sus ingredientes la cumplen (RN-02).
- **Enums** (`Rol`, `Dificultad`, `UnidadMedida`, `Categoria`, `Momento`, `TipoNotificacion`, `RecursoNotificacion`) guardados como texto con `@Enumerated(EnumType.STRING)` y un `CHECK` en la base de datos.
- **Informes y datos concretos:** se devuelven como DTO (`RecetaPopularDto`, `InformeRecetasPopularesDto`) construidos en la consulta con `SELECT new`, nunca como entidades ni `Object[]`.
- **Detalle de receta:** solo se cargan los ingredientes con `@EntityGraph`, porque cargar varias `List` a la vez provoca `MultipleBagFetchException`. Los pasos se cargan aparte, ordenados con `@OrderBy("numero")`.
- **Calorías y precio** de un ingrediente se guardan por unidad de medida (gramo, ml o unidad). El precio usa `NUMERIC(10,4)` porque un gramo puede costar 0,0025 €. El coste y las calorías de una receta se calculan: cantidad × valor, sumado y dividido entre las raciones.
