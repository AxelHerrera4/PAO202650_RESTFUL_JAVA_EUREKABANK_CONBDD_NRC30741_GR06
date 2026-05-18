-- Inserción de Datos Iniciales (Seeders)
INSERT INTO sucursal (nombre, ciudad) VALUES ('Sucursal Central', 'Lima');
INSERT INTO sucursal (nombre, ciudad) VALUES ('Sucursal Norte', 'Trujillo');

INSERT INTO empleado (usuario, password, nombre_completo, rol) VALUES 
('monster', '$2a$10$Uwa//bofjciGLwvlLdd4hOqulvHsW1pNaH7SfObFuysKjFKsk5Jga', 'monster', 'Superadmin Monster'),
('cajero1', '$2a$10$cz/eBAj1cdHKgq5OG6oFye1PhAcZ8PfzRI1hipX7go.krWO.bABzO', 'Juan Perez', 'Cajero'),
('gerente1', '$2a$10$tx.i/Ue80NBdsvIcXATNm.qp0LEUgZoswk4jJ0Q9Ovb1kCHtfUPm.', 'Maria Lopez', 'Gerente');

INSERT INTO cliente (dni, nombre, apellido, direccion, telefono, email) VALUES 
('12345678', 'Carlos', 'Garcia', 'Av. Siempre Viva 123', '999888777', 'carlos@gmail.com'),
('87654321', 'Ana', 'Torres', 'Calle Luna 456', '999111222', 'ana@gmail.com');

INSERT INTO cuenta (numero_cuenta, saldo, id_cliente, id_sucursal, estado) VALUES 
('001-0001', 1000.00, 1, 1, 'Activa'),
('001-0002', 500.00, 2, 1, 'Activa');
