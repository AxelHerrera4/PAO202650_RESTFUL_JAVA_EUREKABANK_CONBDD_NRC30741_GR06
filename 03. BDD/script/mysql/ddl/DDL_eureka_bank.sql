-- Tabla: Sucursal (Para asociar cuentas y clientes)
CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- Tabla: Empleado (Autenticación y Auditoría)
CREATE TABLE empleado (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    rol ENUM('Cajero', 'Gerente', 'Superadmin Monster') NOT NULL
) ENGINE=InnoDB;

-- Tabla: Cliente
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    email VARCHAR(100)
) ENGINE=InnoDB;

-- Tabla: Cuenta
CREATE TABLE cuenta (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    saldo DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    id_cliente INT NOT NULL,
    id_sucursal INT NOT NULL,
    estado ENUM('Activa', 'Bloqueada') NOT NULL DEFAULT 'Activa',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
) ENGINE=InnoDB;

-- Tabla: Movimiento (Registro atómico y auditoría)
CREATE TABLE movimiento (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    numero_operacion VARCHAR(50) NOT NULL UNIQUE,
    numero_cuenta VARCHAR(20) NOT NULL,
    id_empleado INT NOT NULL,
    tipo ENUM('Deposito', 'Retiro') NOT NULL,
    monto DECIMAL(12, 2) NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta),
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
) ENGINE=InnoDB;