-- =====================================================================
-- SmartChef · data.sql · datos de prueba
-- Ejecutar DESPUÉS de schema.sql. Las fechas van relativas a hoy (CURRENT_DATE)
-- para que los casos límite (caducados, semana actual...) funcionen cualquier día.
-- Contraseña de TODOS los usuarios: password123 (cifrada con BCrypt)
-- =====================================================================

SET search_path TO smartchef;


-- ---------------------------------------------------------------------
-- 1. usuarios (10) · 1 ADMIN, 3 CHEF, 6 USUARIO
-- ---------------------------------------------------------------------
INSERT INTO usuarios (nombre, email, contrasena, rol) VALUES
('Equipo SmartChef', 'admin@smartchef.es',  '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'ADMIN'), -- 1
('María López',      'maria@smartchef.es',  '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'CHEF'), -- 2
('Andrés Ruiz',      'andres@smartchef.es', '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'CHEF'), -- 3
('Lucía Martín',     'lucia@smartchef.es',  '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'CHEF'), -- 4
('Julián Torres',    'julian@correo.es',    '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'USUARIO'), -- 5 el cliente piloto
('Elena Torres',     'elena@correo.es',     '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'USUARIO'), -- 6 la hermana celíaca
('Sergio Gil',       'sergio@correo.es',    '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'USUARIO'), -- 7 la pareja vegetariana
('Paula Sanz',       'paula@correo.es',     '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'USUARIO'), -- 8
('Diego Navarro',    'diego@correo.es',     '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'USUARIO'), -- 9
('Carmen Vidal',     'carmen@correo.es',    '$2a$10$kBwOehlscH1IDkoQ8omXp.m7ZOoBaYrOdln1Ds9RY.fAC8Sqgwbga', 'USUARIO'); -- 10


-- ---------------------------------------------------------------------
-- 2. ingredientes (22) · calorías y precio por unidad de medida
-- ---------------------------------------------------------------------
INSERT INTO ingredientes (nombre, unidad_medida, categoria, calorias, precio) VALUES
('Huevo',             'UNIDADES',   'LACTEOS',     78.00,  0.2500), -- 1
('Patata',            'GRAMOS',     'VERDULERIA',   0.77,  0.0012), -- 2
('Cebolla',           'GRAMOS',     'VERDULERIA',   0.40,  0.0015), -- 3
('Aceite de oliva',   'MILILITROS', 'DESPENSA',     8.84,  0.0090), -- 4
('Harina de trigo',   'GRAMOS',     'DESPENSA',     3.64,  0.0010), -- 5
('Leche entera',      'MILILITROS', 'LACTEOS',      0.61,  0.0010), -- 6
('Tomate',            'GRAMOS',     'VERDULERIA',   0.18,  0.0025), -- 7
('Pechuga de pollo',  'GRAMOS',     'CARNICERIA',   1.65,  0.0080), -- 8
('Arroz',             'GRAMOS',     'DESPENSA',     1.30,  0.0015), -- 9
('Espaguetis',        'GRAMOS',     'DESPENSA',     3.71,  0.0020), -- 10
('Lentejas',          'GRAMOS',     'DESPENSA',     1.16,  0.0025), -- 11
('Zanahoria',         'GRAMOS',     'VERDULERIA',   0.41,  0.0012), -- 12
('Pimiento rojo',     'GRAMOS',     'VERDULERIA',   0.31,  0.0030), -- 13
('Ajo',               'UNIDADES',   'VERDULERIA',   4.50,  0.1000), -- 14
('Queso rallado',     'GRAMOS',     'LACTEOS',      4.02,  0.0120), -- 15
('Merluza',           'GRAMOS',     'PESCADERIA',   0.90,  0.0120), -- 16
('Copos de avena',    'GRAMOS',     'DESPENSA',     3.89,  0.0030), -- 17
('Plátano',           'UNIDADES',   'VERDULERIA', 105.00,  0.2500), -- 18
('Pan rallado',       'GRAMOS',     'DESPENSA',     3.95,  0.0030), -- 19
('Calabacín',         'GRAMOS',     'VERDULERIA',   0.17,  0.0020), -- 20
('Sal',               'GRAMOS',     'DESPENSA',     0.00,  0.0005), -- 21
('Chorizo',           'GRAMOS',     'CARNICERIA',   4.55,  0.0120);

-- ---------------------------------------------------------------------
-- 2b. preferencias · catálogo. Para añadir una nueva basta con insertar una fila
-- ---------------------------------------------------------------------
INSERT INTO preferencias (nombre) VALUES
('VEGETARIANO'), -- 1
('SIN_GLUTEN'),  -- 2
('RAPIDO'),      -- 3 se calcula: 30 minutos o menos
('ECONOMICO');   -- 4 se calcula: 2,50 € o menos por ración


-- ---------------------------------------------------------------------
-- 2c. ingredientes_preferencias · qué ingredientes son aptos para cada preferencia
--     No son vegetarianos: pollo (8), merluza (16) y chorizo (22)
--     Tienen gluten: harina (5), espaguetis (10), avena (17) y pan rallado (19)
-- ---------------------------------------------------------------------
INSERT INTO ingredientes_preferencias (id_ingrediente, id_preferencia) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (9, 1), (10, 1), (11, 1),
(12, 1), (13, 1), (14, 1), (15, 1), (17, 1), (18, 1), (19, 1), (20, 1), (21, 1),
(1, 2), (2, 2), (3, 2), (4, 2), (6, 2), (7, 2), (8, 2), (9, 2), (11, 2), (12, 2),
(13, 2), (14, 2), (15, 2), (16, 2), (18, 2), (20, 2), (21, 2), (22, 2);


-- ---------------------------------------------------------------------
-- 2d. usuarios_preferencias · preferencias guardadas en el perfil (RN-03)
-- ---------------------------------------------------------------------
INSERT INTO usuarios_preferencias (id_usuario, id_preferencia) VALUES
(5, 3), (5, 4), -- Julián: rápido y económico
(6, 2),         -- Elena (celíaca): sin gluten
(7, 1),         -- Sergio: vegetariano
(9, 4);         -- Diego: económico
 -- 22


-- ---------------------------------------------------------------------
-- 3. recetas (12) · autores: chefs 2, 3 y 4
--    Casos límite: 11 está RETIRADA (activa = false), 12 no tiene foto de portada
-- ---------------------------------------------------------------------
INSERT INTO recetas (nombre, descripcion, dificultad, tiempo, raciones, id_autor, activa) VALUES
('Tortilla de patatas',   'La de toda la vida, jugosa por dentro.',          'MEDIA',   40, 4, 2, TRUE ), -- 1
('Tortitas de avena',     'Desayuno rápido y con fibra.',                    'FACIL',   15, 2, 2, TRUE ), -- 2
('Ensalada de tomate',    'Tomate, cebolla y buen aceite.',                  'FACIL',   10, 2, 3, TRUE ), -- 3
('Arroz con pollo',       'Un clásico para el táper.',                       'MEDIA',   45, 4, 3, TRUE ), -- 4
('Espaguetis con tomate', 'Pasta sencilla con salsa casera.',                'FACIL',   20, 2, 4, TRUE ), -- 5
('Lentejas con chorizo',  'Plato de cuchara de los de siempre.',             'MEDIA',   60, 4, 4, TRUE ), -- 6
('Merluza al horno',      'Merluza sobre cama de patatas.',                  'MEDIA',   35, 2, 2, TRUE ), -- 7
('Crema de calabacín',    'Cena ligera y suave.',                            'FACIL',   30, 4, 3, TRUE ), -- 8
('Pollo empanado',        'Crujiente por fuera y tierno por dentro.',        'FACIL',   25, 2, 4, TRUE ), -- 9
('Revuelto de calabacín', 'Cena rápida en una sartén.',                      'FACIL',   15, 2, 2, TRUE ), -- 10
('Lentejas con verduras', 'RETIRADA por el chef: se sustituye por la 6.',    'MEDIA',   50, 4, 3, FALSE), -- 11
('Pasta con queso',       'Sin foto de portada: no sale en el catálogo.',    'FACIL',   15, 1, 4, TRUE ); -- 12


-- ---------------------------------------------------------------------
-- 4. pasos · numerados 1, 2, 3... sin huecos (RN-01)
-- ---------------------------------------------------------------------
INSERT INTO pasos (id_receta, numero, descripcion) VALUES
(1, 1, 'Pelar y cortar las patatas y la cebolla en láminas finas.'),
(1, 2, 'Freírlas a fuego medio hasta que estén blandas y escurrir el aceite.'),
(1, 3, 'Batir los huevos con la sal y mezclar con las patatas.'),
(1, 4, 'Cuajar en la sartén por los dos lados.'),
(2, 1, 'Triturar la avena, el huevo, la leche y el plátano.'),
(2, 2, 'Hacer las tortitas en una sartén caliente, un cazo cada vez.'),
(3, 1, 'Cortar el tomate y la cebolla.'),
(3, 2, 'Aliñar con aceite y sal.'),
(4, 1, 'Dorar el pollo troceado con el ajo.'),
(4, 2, 'Añadir el pimiento y el tomate y sofreír.'),
(4, 3, 'Incorporar el arroz y el doble de agua; cocer 18 minutos.'),
(5, 1, 'Cocer los espaguetis en agua con sal.'),
(5, 2, 'Hacer la salsa con el tomate y el ajo.'),
(5, 3, 'Mezclar y servir con queso rallado.'),
(6, 1, 'Poner las lentejas en remojo la noche anterior.'),
(6, 2, 'Cocerlas con la zanahoria, la cebolla, el ajo y el chorizo durante 45 minutos.'),
(7, 1, 'Hornear las patatas en rodajas 15 minutos a 200 ºC.'),
(7, 2, 'Colocar la merluza encima con ajo y aceite y hornear 15 minutos más.'),
(8, 1, 'Pochar la cebolla y añadir el calabacín y la patata.'),
(8, 2, 'Cubrir de agua y cocer 20 minutos.'),
(8, 3, 'Triturar con la leche.'),
(9, 1, 'Pasar el pollo por huevo batido y pan rallado.'),
(9, 2, 'Freír en aceite caliente hasta que esté dorado.'),
(10, 1, 'Saltear el calabacín en dados.'),
(10, 2, 'Añadir los huevos batidos y remover hasta que cuajen.'),
(11, 1, 'Cocer las lentejas con las verduras.'),
(11, 2, 'Rectificar de sal y servir.'),
(12, 1, 'Cocer la pasta.'),
(12, 2, 'Mezclar con el queso rallado.');


-- ---------------------------------------------------------------------
-- 5. recetas_ingredientes · cantidad en la unidad del ingrediente
-- ---------------------------------------------------------------------
INSERT INTO recetas_ingredientes (id_receta, id_ingrediente, cantidad) VALUES
(1, 1, 6), (1, 2, 600), (1, 3, 150), (1, 4, 100), (1, 21, 5),
(2, 17, 100), (2, 1, 2), (2, 6, 200), (2, 18, 1),
(3, 7, 400), (3, 3, 50), (3, 4, 20), (3, 21, 2),
(4, 9, 320), (4, 8, 500), (4, 13, 150), (4, 7, 200), (4, 4, 40), (4, 14, 2),
(5, 10, 200), (5, 7, 300), (5, 14, 2), (5, 4, 20), (5, 15, 40),
(6, 11, 320), (6, 12, 150), (6, 3, 100), (6, 22, 150), (6, 14, 2),
(7, 16, 400), (7, 2, 300), (7, 4, 30), (7, 14, 2),
(8, 20, 800), (8, 2, 200), (8, 3, 100), (8, 4, 30), (8, 6, 100),
(9, 8, 300), (9, 1, 1), (9, 19, 80), (9, 4, 100),
(10, 20, 300), (10, 1, 4), (10, 4, 15), (10, 21, 2),
(11, 11, 300), (11, 12, 150), (11, 13, 100),
(12, 10, 100), (12, 15, 50);


-- ---------------------------------------------------------------------
-- 6. fotos_recetas · portada en todas menos en la 12 (caso límite) y 2 fotos extra en la 1
--    "clave" es el nombre del fichero en disco (UUID); los ficheros de prueba no existen
-- ---------------------------------------------------------------------
INSERT INTO fotos_recetas (id_receta, nombre_original, clave, tipo_contenido, tamano, orden, portada) VALUES
(1,  'tortilla.jpg',         '0b6e1f3a-1a2b-4c3d-8e9f-000000000001.jpg', 'image/jpeg', 245000, 0, TRUE ),
(1,  'tortilla-corte.jpg',   '0b6e1f3a-1a2b-4c3d-8e9f-000000000002.jpg', 'image/jpeg', 198000, 1, FALSE),
(1,  'tortilla-plato.webp',  '0b6e1f3a-1a2b-4c3d-8e9f-000000000003.webp','image/webp', 120000, 2, FALSE),
(2,  'tortitas.png',         '0b6e1f3a-1a2b-4c3d-8e9f-000000000004.png', 'image/png',  310000, 0, TRUE ),
(3,  'ensalada.jpg',         '0b6e1f3a-1a2b-4c3d-8e9f-000000000005.jpg', 'image/jpeg', 150000, 0, TRUE ),
(4,  'arroz-pollo.jpg',      '0b6e1f3a-1a2b-4c3d-8e9f-000000000006.jpg', 'image/jpeg', 280000, 0, TRUE ),
(5,  'espaguetis.jpg',       '0b6e1f3a-1a2b-4c3d-8e9f-000000000007.jpg', 'image/jpeg', 230000, 0, TRUE ),
(6,  'lentejas.jpg',         '0b6e1f3a-1a2b-4c3d-8e9f-000000000008.jpg', 'image/jpeg', 260000, 0, TRUE ),
(7,  'merluza.jpg',          '0b6e1f3a-1a2b-4c3d-8e9f-000000000009.jpg', 'image/jpeg', 240000, 0, TRUE ),
(8,  'crema.jpg',            '0b6e1f3a-1a2b-4c3d-8e9f-000000000010.jpg', 'image/jpeg', 175000, 0, TRUE ),
(9,  'pollo-empanado.jpg',   '0b6e1f3a-1a2b-4c3d-8e9f-000000000011.jpg', 'image/jpeg', 205000, 0, TRUE ),
(10, 'revuelto.jpg',         '0b6e1f3a-1a2b-4c3d-8e9f-000000000012.jpg', 'image/jpeg', 160000, 0, TRUE ),
(11, 'lentejas-verdura.jpg', '0b6e1f3a-1a2b-4c3d-8e9f-000000000013.jpg', 'image/jpeg', 220000, 0, TRUE );


-- ---------------------------------------------------------------------
-- 7. despensas (15) · casos límite para la tarea programada y la búsqueda (RN-04):
--    caducado hace más de 7 días (se borra), caducado hace 2 (no cuenta en búsquedas),
--    caduca mañana (aviso), sin caducidad (sal, aceite)
-- ---------------------------------------------------------------------
INSERT INTO despensas (id_usuario, id_ingrediente, cantidad, caducidad) VALUES
(5, 1,  6,    CURRENT_DATE + 5),   -- Julián: huevos
(5, 2,  1000, CURRENT_DATE + 20),  -- patatas
(5, 3,  300,  CURRENT_DATE + 10),  -- cebolla
(5, 4,  500,  NULL),               -- aceite (no caduca)
(5, 21, 500,  NULL),               -- sal (no caduca)
(5, 7,  500,  CURRENT_DATE + 1),   -- tomate: caduca MAÑANA → aviso
(5, 6,  1000, CURRENT_DATE - 10),  -- leche: caducada hace 10 días → la tarea la borra
(5, 15, 100,  CURRENT_DATE - 2),   -- queso: caducado hace 2 días → no cuenta en búsquedas
(5, 10, 500,  CURRENT_DATE + 180), -- espaguetis
(5, 14, 6,    CURRENT_DATE + 30),  -- ajos
(6, 9,  1000, CURRENT_DATE + 200), -- Elena: arroz
(6, 8,  500,  CURRENT_DATE + 2),   -- pollo: caduca en 2 días → aviso
(6, 13, 200,  CURRENT_DATE + 4),   -- pimiento
(7, 20, 800,  CURRENT_DATE + 6),   -- Sergio: calabacín
(7, 1,  12,   CURRENT_DATE + 12);  -- huevos


-- ---------------------------------------------------------------------
-- 8. listas_compra · Julián: una ABIERTA y una CERRADA; Elena: una ABIERTA
-- ---------------------------------------------------------------------
INSERT INTO listas_compra (id_usuario, estado, creada_en, cerrada_en) VALUES
(5, 'ABIERTA', CURRENT_TIMESTAMP - INTERVAL '1 day',  NULL),                                   -- 1
(5, 'CERRADA', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '9 days'), -- 2
(6, 'ABIERTA', CURRENT_TIMESTAMP - INTERVAL '2 days', NULL);                                   -- 3


-- ---------------------------------------------------------------------
-- 9. lineas_lista_compra · alguna ya marcada como comprada
-- ---------------------------------------------------------------------
INSERT INTO lineas_lista_compra (id_lista, id_ingrediente, cantidad, comprada) VALUES
(1, 13, 150, FALSE),
(1, 16, 400, TRUE ),
(1, 12, 150, FALSE),
(2, 1,  12,  TRUE ),
(2, 2,  1000, TRUE),
(3, 11, 320, FALSE),
(3, 22, 150, FALSE);


-- ---------------------------------------------------------------------
-- 10. colecciones · "Favoritas" para cada usuario (se crea al registrarse) + extras de Julián
-- ---------------------------------------------------------------------
INSERT INTO colecciones (id_usuario, nombre) VALUES
(1, 'Favoritas'), (2, 'Favoritas'), (3, 'Favoritas'), (4, 'Favoritas'), (5, 'Favoritas'),  -- 1 a 5
(6, 'Favoritas'), (7, 'Favoritas'), (8, 'Favoritas'), (9, 'Favoritas'), (10, 'Favoritas'), -- 6 a 10
(5, 'Para el táper'),  -- 11
(5, 'Cenas rápidas');  -- 12


-- ---------------------------------------------------------------------
-- 11. colecciones_recetas · la receta 11 (retirada) sigue en una colección (RN-09)
-- ---------------------------------------------------------------------
INSERT INTO colecciones_recetas (id_coleccion, id_receta) VALUES
(5, 1), (5, 3), (5, 11),
(11, 4), (11, 6),
(12, 3), (12, 10),
(6, 1), (6, 4),
(7, 8), (7, 10),
(8, 5),
(9, 1);


-- ---------------------------------------------------------------------
-- 12. registros_comida (14) · semana actual y anterior; uno sin receta (comida libre)
-- ---------------------------------------------------------------------
INSERT INTO registros_comida (id_usuario, id_receta, descripcion, fecha, momento) VALUES
(5, 2,    NULL,                      CURRENT_DATE,      'DESAYUNO'),
(5, 4,    NULL,                      CURRENT_DATE,      'COMIDA'),
(5, 3,    NULL,                      CURRENT_DATE - 1,  'CENA'),
(5, NULL, 'Bocadillo en el trabajo', CURRENT_DATE - 1,  'COMIDA'),  -- comida libre
(5, 1,    NULL,                      CURRENT_DATE - 2,  'CENA'),
(5, 10,   NULL,                      CURRENT_DATE - 3,  'CENA'),
(5, 1,    NULL,                      CURRENT_DATE - 8,  'COMIDA'),  -- semana anterior
(5, 6,    NULL,                      CURRENT_DATE - 9,  'COMIDA'),
(6, 4,    NULL,                      CURRENT_DATE,      'COMIDA'),
(6, 1,    NULL,                      CURRENT_DATE - 2,  'CENA'),
(7, 8,    NULL,                      CURRENT_DATE - 1,  'CENA'),
(7, 10,   NULL,                      CURRENT_DATE - 4,  'CENA'),
(8, 1,    NULL,                      CURRENT_DATE - 3,  'COMIDA'),
(9, 5,    NULL,                      CURRENT_DATE - 5,  'COMIDA');


-- ---------------------------------------------------------------------
-- 13. notificaciones (10) · leídas y sin leer
-- ---------------------------------------------------------------------
INSERT INTO notificaciones (id_usuario, tipo, mensaje, recurso, recurso_id, leida, creada_en) VALUES
(5, 'CADUCIDAD',         'Tu tomate caduca mañana. Prueba: Ensalada de tomate.',  'INGREDIENTE', 7,  FALSE, CURRENT_TIMESTAMP),
(6, 'CADUCIDAD',         'Tu pollo caduca en 2 días. Prueba: Arroz con pollo.',    'INGREDIENTE', 8,  FALSE, CURRENT_TIMESTAMP),
(5, 'RESUMEN_SEMANAL',   'La semana pasada registraste 2 comidas.',                NULL,          NULL, TRUE,  CURRENT_TIMESTAMP - INTERVAL '3 days'),
(6, 'RESUMEN_SEMANAL',   'La semana pasada no registraste comidas.',               NULL,          NULL, TRUE,  CURRENT_TIMESTAMP - INTERVAL '3 days'),
(7, 'RESUMEN_SEMANAL',   'La semana pasada registraste 1 comida.',                 NULL,          NULL, FALSE, CURRENT_TIMESTAMP - INTERVAL '3 days'),
(5, 'RECETA_RETIRADA',   'Se ha retirado "Lentejas con verduras" de tu colección Favoritas.', 'RECETA', 11, FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(5, 'LISTA_ACTUALIZADA', 'Se han añadido 3 ingredientes a tu lista de la compra.', 'LISTA_COMPRA', 1, TRUE,  CURRENT_TIMESTAMP - INTERVAL '1 day'),
(6, 'LISTA_ACTUALIZADA', 'Se han añadido 2 ingredientes a tu lista de la compra.', 'LISTA_COMPRA', 3, FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(8, 'RESUMEN_SEMANAL',   'La semana pasada registraste 1 comida.',                 NULL,          NULL, FALSE, CURRENT_TIMESTAMP - INTERVAL '3 days'),
(9, 'RESUMEN_SEMANAL',   'La semana pasada registraste 1 comida.',                 NULL,          NULL, TRUE,  CURRENT_TIMESTAMP - INTERVAL '3 days');
