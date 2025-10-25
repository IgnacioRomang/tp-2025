-- Insertar datos de ejemplo en el esquema tp_dan

-- Insertar Hoteles
-- Usamos ON CONFLICT para evitar errores si los datos ya existen, útil para ejecuciones repetidas.
INSERT INTO tp_dan.hotel (id, nombre, cuit, domicilio, latitud, longitud, telefono, hotel_status, status_date, correo_contacto, categoria) VALUES
(1, 'Hotel Panamericano Buenos Aires', '30-11223344-5', 'Av. 9 de Julio 1020, CABA', -34.6037, -58.3816, '011-4348-5000', 'ABIERTO', '2024-01-01 00:00:00', 'contacto@panamericano.com', 5),
(2, 'Ibis Hotel Obelisco', '30-55667788-9', 'Av. Corrientes 1344, CABA', -34.6042, -58.3845, '011-4370-9100', 'ABIERTO', '2024-01-01 00:00:00', 'reservas@ibisobelisco.com', 3)
ON CONFLICT (id) DO NOTHING;

-- Reiniciar la secuencia del hotel al valor máximo + 1 para evitar colisiones con inserciones manuales
SELECT setval('tp_dan.hotel_id_seq', (SELECT MAX(id) FROM tp_dan.hotel));

-- Insertar Amenities para los Hoteles
-- Hotel 1: Panamericano
INSERT INTO tp_dan.amenity_hotel (id_hotel, amenity) VALUES
(1, 'Piscina Climatizada'),
(1, 'Gimnasio 24hs'),
(1, 'Spa y Sauna'),
(1, 'Wi-Fi de alta velocidad'),
(1, 'Estacionamiento Valet Parking');

-- Hotel 2: Ibis Obelisco
INSERT INTO tp_dan.amenity_hotel (id_hotel, amenity) VALUES
(2, 'Wi-Fi de alta velocidad'),
(2, 'Desayuno Buffet'),
(2, 'Bar 24hs'),
(2, 'Estacionamiento');

-- Insertar Tarifas para Tipos de Habitación
-- Se asume que los tipos de habitación ya fueron insertados por 01_schema.sql
-- Tarifas para todo el año 2024
INSERT INTO tp_dan.tarifa (fecha_inicio, fecha_fin, id_tipo_habitacion, precio_noche) VALUES
('2024-01-01', '2025-12-31', 1, 15000.00), -- SINGLE
('2024-01-01', '2025-12-31', 3, 25000.00), -- DOBLE
('2024-01-01', '2025-12-31', 5, 32000.00), -- TRIPLE
('2024-01-01', '2025-12-31', 7, 28000.00); -- DOBLE SUPERIOR

-- Insertar Habitaciones
-- Hotel 1: Panamericano
INSERT INTO tp_dan.habitacion (numero, piso, id_tipo, id_hotel) VALUES
(101, 1, 1, 1), -- Single
(102, 1, 1, 1), -- Single
(205, 2, 3, 1), -- Doble
(206, 2, 7, 1); -- Doble Superior

-- Hotel 2: Ibis Obelisco
INSERT INTO tp_dan.habitacion (numero, piso, id_tipo, id_hotel) VALUES
(501, 5, 1, 2), -- Single
(502, 5, 3, 2), -- Doble
(810, 8, 3, 2); -- Doble