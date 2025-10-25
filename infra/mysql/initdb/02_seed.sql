-- Script para insertar datos de ejemplo en el esquema `users`

-- Usar el esquema `users`
USE users;

-- Insertar bancos
INSERT INTO bancos (nombre) VALUES
('Banco Galicia'),
('Banco Santander'),
('Banco Macro'),
('BBVA');

-- Insertar cuentas bancarias para propietarios
-- Asumimos que el ID 1 es para 'Juan Perez' y el ID 2 para 'Maria Rodriguez'
INSERT INTO cuentas_bancarias (numero_cuenta, cbu, alias, banco_id) VALUES
('123456-7', '0070123456789012345678', 'JUAN.PEREZ.MP', 1), -- Banco Galicia
('987654-3', '0720987654321098765432', 'MARIA.RODRIGUEZ.MP', 2); -- Banco Santander

-- Insertar usuarios
-- 2 Propietarios y 3 Huespedes
INSERT INTO usuarios (nombre, email, dni, telefono, tipo, fecha_nacimiento, cuenta_bancaria_id, hotel_id) VALUES
-- Propietario 1
('Juan Perez', 'juan.perez@example.com', '1122334455', '3415551234', 'PROPIETARIO', '1980-05-15', 1, NULL),
-- Propietario 2
('Maria Rodriguez', 'maria.rodriguez@example.com', '1133445566', '3415555678', 'PROPIETARIO', '1975-10-20', 2, NULL),
-- Huesped 1
('Carlos Gomez', 'carlos.gomez@example.com', '1144556677', '3415559012', 'HUESPED', '1992-08-01', NULL, NULL),
-- Huesped 2
('Ana Fernandez', 'ana.fernandez@example.com', '1155667788', '3415553456', 'HUESPED', '1995-03-30', NULL, NULL),
-- Huesped 3
('Martin Dominguez', 'martin.dominguez@example.com', '1166778899', '3415557890', 'HUESPED', '1988-12-10', NULL, NULL);

-- Insertar tarjetas de crédito para huéspedes
-- Asumimos que los IDs de usuario para huéspedes son 3, 4 y 5
INSERT INTO tarjetas_credito (numero_tarjeta, nombre_titular, fecha_vencimiento, codigo_seguridad, es_principal, usuario_id, banco_id) VALUES
('4517123456789012', 'Carlos Gomez', '1228', '123', 1, 3, 2), -- Santander, Principal
('5243123456789012', 'Carlos Gomez', '1027', '456', 0, 3, 3), -- Macro, No Principal
('4222123456789012', 'Ana Fernandez', '0529', '789', 1, 4, 4), -- BBVA, Principal
('4500123456789012', 'Martin Dominguez', '0826', '101', 1, 5, 1); -- Galicia, Principal
