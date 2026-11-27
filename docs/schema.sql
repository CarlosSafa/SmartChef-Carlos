-- =====================================================================
-- SmartChef · schema.sql
-- PostgreSQL 16 · Tablas en snake_case y plural (sección 2.4 del enunciado)
-- Los enums se guardan como texto con CHECK (se mapean con @Enumerated(EnumType.STRING))
-- =====================================================================

DROP SCHEMA IF EXISTS smartchef CASCADE;
CREATE SCHEMA smartchef;
SET search_path TO smartchef;


-- ---------------------------------------------------------------------
-- 1. usuarios · los tres roles del caso (ADMIN, CHEF, USUARIO)
--    Sus preferencias guardadas (RN-03) están en usuarios_preferencias
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id               SERIAL       PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    contrasena       VARCHAR(255) NOT NULL,
    rol              VARCHAR(10)  NOT NULL DEFAULT 'USUARIO'
                     CHECK (rol IN ('ADMIN', 'CHEF', 'USUARIO')),
    creado_en        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ---------------------------------------------------------------------
-- 2. ingredientes · catálogo común ("el tomate es siempre el mismo tomate")
--    calorias y precio son POR UNIDAD DE MEDIDA (por gramo, por ml o por unidad).
--    El precio por gramo necesita 4 decimales (0,0025 €/g), por eso NUMERIC(10,4).
-- ---------------------------------------------------------------------
CREATE TABLE ingredientes (
    id             SERIAL        PRIMARY KEY,
    nombre         VARCHAR(100)  NOT NULL UNIQUE,
    unidad_medida  VARCHAR(10)   NOT NULL
                   CHECK (unidad_medida IN ('GRAMOS', 'MILILITROS', 'UNIDADES')),
    categoria      VARCHAR(12)   NOT NULL
                   CHECK (categoria IN ('VERDULERIA', 'DESPENSA', 'CARNICERIA', 'PESCADERIA', 'LACTEOS')),
    calorias       NUMERIC(8, 2) NOT NULL CHECK (calorias >= 0),
    precio         NUMERIC(10, 4) NOT NULL CHECK (precio >= 0)
);


-- ---------------------------------------------------------------------
-- 2b. preferencias · catálogo (VEGETARIANO, SIN_GLUTEN, RAPIDO, ECONOMICO...)
--     Es una tabla y no columnas para poder añadir preferencias nuevas (SIN_LACTOSA...)
--     insertando una fila, sin cambiar la estructura de la base de datos.
-- ---------------------------------------------------------------------
CREATE TABLE preferencias (
    id      SERIAL      PRIMARY KEY,
    nombre  VARCHAR(30) NOT NULL UNIQUE
);


-- ---------------------------------------------------------------------
-- 2c. ingredientes_preferencias · qué preferencias CUMPLE cada ingrediente
--     N:M SIN atributos → @ManyToMany. RN-02: una receta es vegetariana (o sin gluten)
--     si TODOS sus ingredientes cumplen esa preferencia.
--     RAPIDO y ECONOMICO no se marcan aquí: se calculan con el tiempo y el coste de la receta.
-- ---------------------------------------------------------------------
CREATE TABLE ingredientes_preferencias (
    id_ingrediente  INT NOT NULL REFERENCES ingredientes(id) ON DELETE CASCADE,
    id_preferencia  INT NOT NULL REFERENCES preferencias(id) ON DELETE CASCADE,
    PRIMARY KEY (id_ingrediente, id_preferencia)
);


-- ---------------------------------------------------------------------
-- 2d. usuarios_preferencias · preferencias guardadas en el perfil (RN-03)
--     N:M SIN atributos → @ManyToMany
-- ---------------------------------------------------------------------
CREATE TABLE usuarios_preferencias (
    id_usuario      INT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    id_preferencia  INT NOT NULL REFERENCES preferencias(id) ON DELETE CASCADE,
    PRIMARY KEY (id_usuario, id_preferencia)
);


-- ---------------------------------------------------------------------
-- 3. recetas · RN-01 (tiempo > 0, de 1 a 12 raciones), RN-09 (baja lógica con activa)
--    Coste, calorías y preferencias NO se guardan: se calculan (RN-02)
--    creado_en / actualizado_en: campos de auditoría (@PrePersist / @PreUpdate)
-- ---------------------------------------------------------------------
CREATE TABLE recetas (
    id              SERIAL       PRIMARY KEY,
    nombre          VARCHAR(150) NOT NULL,
    descripcion     TEXT,
    dificultad      VARCHAR(10)  NOT NULL
                    CHECK (dificultad IN ('FACIL', 'MEDIA', 'DIFICIL')),
    tiempo          INT          NOT NULL CHECK (tiempo > 0),
    raciones        INT          NOT NULL CHECK (raciones BETWEEN 1 AND 12),
    id_autor        INT          NOT NULL REFERENCES usuarios(id) ON DELETE RESTRICT,
    activa          BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ---------------------------------------------------------------------
-- 4. pasos · relación 1:N con recetas. Numeración sin repetir dentro de una receta
-- ---------------------------------------------------------------------
CREATE TABLE pasos (
    id           SERIAL PRIMARY KEY,
    id_receta    INT    NOT NULL REFERENCES recetas(id) ON DELETE CASCADE,
    numero       INT    NOT NULL CHECK (numero > 0),
    descripcion  TEXT   NOT NULL,
    UNIQUE (id_receta, numero)
);


-- ---------------------------------------------------------------------
-- 5. recetas_ingredientes · N:M CON atributo (cantidad) → necesita entidad.
--    Clave propia (id) + UNIQUE: así la entidad no necesita una clase Id aparte.
-- ---------------------------------------------------------------------
CREATE TABLE recetas_ingredientes (
    id              SERIAL         PRIMARY KEY,
    id_receta       INT            NOT NULL REFERENCES recetas(id) ON DELETE CASCADE,
    id_ingrediente  INT            NOT NULL REFERENCES ingredientes(id) ON DELETE RESTRICT,
    cantidad        NUMERIC(10, 2) NOT NULL CHECK (cantidad > 0),
    UNIQUE (id_receta, id_ingrediente)
);


-- ---------------------------------------------------------------------
-- 6. fotos_recetas · solo metadatos; el fichero está en disco con nombre UUID (sección 3.7)
--    Una portada como mucho por receta (índice único parcial, más abajo)
-- ---------------------------------------------------------------------
CREATE TABLE fotos_recetas (
    id               SERIAL       PRIMARY KEY,
    id_receta        INT          NOT NULL REFERENCES recetas(id) ON DELETE CASCADE,
    nombre_original  VARCHAR(255) NOT NULL,
    clave            VARCHAR(100) NOT NULL UNIQUE,
    tipo_contenido   VARCHAR(20)  NOT NULL
                     CHECK (tipo_contenido IN ('image/jpeg', 'image/png', 'image/webp')),
    tamano           BIGINT       NOT NULL CHECK (tamano > 0 AND tamano <= 5242880),
    orden            INT          NOT NULL DEFAULT 0 CHECK (orden >= 0),
    portada          BOOLEAN      NOT NULL DEFAULT FALSE,
    subida_en        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ---------------------------------------------------------------------
-- 7. despensas · lo que tiene cada persona en casa (N:M usuario–ingrediente CON atributos)
--    Un ingrediente solo una vez por persona. Sin caducidad = no caduca (sal, aceite)
-- ---------------------------------------------------------------------
CREATE TABLE despensas (
    id              SERIAL         PRIMARY KEY,
    id_usuario      INT            NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    id_ingrediente  INT            NOT NULL REFERENCES ingredientes(id) ON DELETE RESTRICT,
    cantidad        NUMERIC(10, 2) NOT NULL CHECK (cantidad > 0),
    caducidad       DATE,
    UNIQUE (id_usuario, id_ingrediente)
);


-- ---------------------------------------------------------------------
-- 8. listas_compra · RN-05 (una sola lista ABIERTA por persona, índice más abajo), RN-06
-- ---------------------------------------------------------------------
CREATE TABLE listas_compra (
    id          SERIAL      PRIMARY KEY,
    id_usuario  INT         NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    estado      VARCHAR(10) NOT NULL DEFAULT 'ABIERTA'
                CHECK (estado IN ('ABIERTA', 'CERRADA')),
    creada_en   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cerrada_en  TIMESTAMP,
    -- Una lista cerrada tiene fecha de cierre y una abierta no
    CHECK ((estado = 'ABIERTA' AND cerrada_en IS NULL) OR (estado = 'CERRADA' AND cerrada_en IS NOT NULL))
);


-- ---------------------------------------------------------------------
-- 9. lineas_lista_compra · sin ingredientes repetidos en una lista (RN-05)
-- ---------------------------------------------------------------------
CREATE TABLE lineas_lista_compra (
    id              SERIAL         PRIMARY KEY,
    id_lista        INT            NOT NULL REFERENCES listas_compra(id) ON DELETE CASCADE,
    id_ingrediente  INT            NOT NULL REFERENCES ingredientes(id) ON DELETE RESTRICT,
    cantidad        NUMERIC(10, 2) NOT NULL CHECK (cantidad > 0),
    comprada        BOOLEAN        NOT NULL DEFAULT FALSE,
    UNIQUE (id_lista, id_ingrediente)
);


-- ---------------------------------------------------------------------
-- 10. colecciones · RN-08 (nombre único por persona; "Favoritas" se crea al registrarse)
-- ---------------------------------------------------------------------
CREATE TABLE colecciones (
    id          SERIAL       PRIMARY KEY,
    id_usuario  INT          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    nombre      VARCHAR(100) NOT NULL,
    UNIQUE (id_usuario, nombre)
);


-- ---------------------------------------------------------------------
-- 11. colecciones_recetas · N:M SIN atributos → @ManyToMany + @JoinTable, sin clase intermedia
--     La clave primaria doble impide guardar dos veces la misma receta (RN-08)
-- ---------------------------------------------------------------------
CREATE TABLE colecciones_recetas (
    id_coleccion  INT NOT NULL REFERENCES colecciones(id) ON DELETE CASCADE,
    id_receta     INT NOT NULL REFERENCES recetas(id) ON DELETE CASCADE,
    PRIMARY KEY (id_coleccion, id_receta)
);


-- ---------------------------------------------------------------------
-- 12. registros_comida · RN-07 (un registro por día y momento → UNIQUE)
--     id_receta es la RELACIÓN OPCIONAL (FK que admite NULL): comida libre con descripción
-- ---------------------------------------------------------------------
CREATE TABLE registros_comida (
    id           SERIAL       PRIMARY KEY,
    id_usuario   INT          NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    id_receta    INT          REFERENCES recetas(id) ON DELETE RESTRICT,
    descripcion  VARCHAR(255),
    fecha        DATE         NOT NULL,
    momento      VARCHAR(10)  NOT NULL
                 CHECK (momento IN ('DESAYUNO', 'COMIDA', 'CENA')),
    creado_en    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (id_usuario, fecha, momento),
    -- O tiene receta o tiene descripción (comida libre)
    CHECK (id_receta IS NOT NULL OR descripcion IS NOT NULL)
);


-- ---------------------------------------------------------------------
-- 13. notificaciones · sección 3.8 (destinatario, tipo, mensaje, recurso, leída, fecha)
-- ---------------------------------------------------------------------
CREATE TABLE notificaciones (
    id          SERIAL      PRIMARY KEY,
    id_usuario  INT         NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    tipo        VARCHAR(20) NOT NULL
                CHECK (tipo IN ('CADUCIDAD', 'RESUMEN_SEMANAL', 'RECETA_RETIRADA', 'LISTA_ACTUALIZADA')),
    mensaje     TEXT        NOT NULL,
    recurso     VARCHAR(20)
                CHECK (recurso IN ('INGREDIENTE', 'RECETA', 'LISTA_COMPRA', 'REGISTRO')),
    recurso_id  INT,
    leida       BOOLEAN     NOT NULL DEFAULT FALSE,
    creada_en   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================================
-- Índices creados a mano (sección 3.1)
-- =====================================================================

-- La tarea programada de las 09:00 busca en la despensa por fecha de caducidad
CREATE INDEX idx_despensas_caducidad ON despensas (caducidad);

-- "Mis notificaciones?leida=" se consulta constantemente
CREATE INDEX idx_notificaciones_usuario_leida ON notificaciones (id_usuario, leida);

-- El catálogo solo enseña recetas activas y se busca por nombre
CREATE INDEX idx_recetas_activa_nombre ON recetas (activa, nombre);

-- RN-05: como mucho UNA lista ABIERTA por persona (índice único parcial)
CREATE UNIQUE INDEX ux_listas_compra_una_abierta ON listas_compra (id_usuario) WHERE estado = 'ABIERTA';

-- Como mucho UNA foto de portada por receta
CREATE UNIQUE INDEX ux_fotos_recetas_una_portada ON fotos_recetas (id_receta) WHERE portada;
