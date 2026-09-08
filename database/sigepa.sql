CREATE DATABASE IF NOT EXISTS sigepa_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE sigepa_db;

-- ==========================================
-- ROLES
-- ==========================================

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ==========================================
-- USUARIOS
-- ==========================================

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES roles(id_rol)
);

-- ==========================================
-- CLIENTES
-- ==========================================

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    documento VARCHAR(30),
    telefono VARCHAR(40),
    email VARCHAR(120),
    direccion VARCHAR(180),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ==========================================
-- PRODUCTOS
-- ==========================================

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(12,2) NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ==========================================
-- PEDIDOS
-- ==========================================

CREATE TABLE pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT,
    total DECIMAL(12,2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
);

-- ==========================================
-- DETALLE DE PEDIDOS
-- ==========================================

CREATE TABLE detalle_pedidos (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad DECIMAL(12,2) NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,

    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedidos(id_pedido),

    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
);

-- ==========================================
-- MATERIALES
-- ==========================================

CREATE TABLE materiales (
    id_material INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    unidad_medida VARCHAR(30) NOT NULL,
    stock_minimo DECIMAL(12,2) NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ==========================================
-- STOCK
-- ==========================================

CREATE TABLE stock (
    id_stock INT AUTO_INCREMENT PRIMARY KEY,
    id_material INT NOT NULL UNIQUE,
    cantidad DECIMAL(12,2) NOT NULL DEFAULT 0,
    ubicacion VARCHAR(100),

    CONSTRAINT fk_stock_material
        FOREIGN KEY (id_material)
        REFERENCES materiales(id_material)
);

-- ==========================================
-- MOVIMIENTOS DE STOCK
-- ==========================================

CREATE TABLE movimientos_stock (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_material INT NOT NULL,
    id_usuario INT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    cantidad DECIMAL(12,2) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(255),

    CONSTRAINT fk_movimiento_material
        FOREIGN KEY (id_material)
        REFERENCES materiales(id_material),

    CONSTRAINT fk_movimiento_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
);

-- ==========================================
-- ÓRDENES DE PRODUCCIÓN
-- ==========================================

CREATE TABLE ordenes_produccion (
    id_orden INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT,

    CONSTRAINT fk_orden_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedidos(id_pedido)
);

-- ==========================================
-- ENTREGAS
-- ==========================================

CREATE TABLE entregas (
    id_entrega INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responsable VARCHAR(120),
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT,

    CONSTRAINT fk_entrega_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedidos(id_pedido)
);





INSERT INTO roles (nombre) VALUES
('ADMINISTRADOR'),
('ADMINISTRATIVO'),
('DEPOSITO'),
('PRODUCCION'),
('ENTREGAS');

INSERT INTO usuarios
(id_rol, nombre, apellido, usuario, password)
VALUES
(1, 'Administrador', 'General', 'admin', 'admin123'),
(2, 'Laura', 'Gómez', 'administrativo', 'admin123'),
(3, 'Carlos', 'Pérez', 'deposito', 'admin123'),
(4, 'Miguel', 'Sosa', 'produccion', 'admin123'),
(5, 'Ana', 'Torres', 'entregas', 'admin123');

INSERT INTO clientes
(nombre, documento, telefono, email, direccion)
VALUES
(
    'Constructora del Sur',
    '30-71234567-8',
    '358-456789',
    'contacto@constructora.test',
    'Av. España 1250'
),
(
    'María Fernández',
    '28.456.789',
    '358-412345',
    'maria@test.com',
    'San Martín 840'
);

INSERT INTO productos
(nombre, descripcion, precio)
VALUES
(
    'Ventana corrediza aluminio',
    'Ventana de aluminio blanco 120x100',
    185000
),
(
    'Puerta exterior aluminio',
    'Puerta de aluminio reforzada 80x200',
    320000
),
(
    'Ventana paño fijo',
    'Paño fijo de aluminio 80x60',
    95000
);

INSERT INTO materiales
(nombre, unidad_medida, stock_minimo)
VALUES
('Perfil aluminio blanco', 'metro', 50),
('Vidrio 4 mm', 'm2', 20),
('Herrajes', 'unidad', 30),
('Burlete', 'metro', 100);

INSERT INTO stock
(id_material, cantidad, ubicacion)
VALUES
(1, 180, 'Depósito A'),
(2, 75, 'Depósito A'),
(3, 120, 'Depósito B'),
(4, 250, 'Depósito B');



