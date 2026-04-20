-- ============================================================
-- schema.sql - Microservicio mascotas-ordenes
-- Ejecutar en SQL Developer conectado al schema MASCOTAS_APP de Oracle ADB
-- ============================================================

-- Drop seguro de objetos previos (para re-ejecutar el script sin errores)

BEGIN EXECUTE IMMEDIATE 'DROP TABLE DETALLE_ORDEN CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE ORDEN_COMPRA CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE PRODUCTO CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE PRODUCTO_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE ORDEN_COMPRA_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE DETALLE_ORDEN_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- ============================================================
-- TABLA: PRODUCTO
-- ============================================================
CREATE TABLE PRODUCTO (
    ID          NUMBER(19)      NOT NULL,                       -- PK autoincremental (desde PRODUCTO_SEQ)
    NOMBRE      VARCHAR2(100)   NOT NULL,                       -- Nombre del producto
    DESCRIPCION VARCHAR2(300),                                  -- Descripción del producto (opcional)
    PRECIO      NUMBER(12,2)    NOT NULL,                       -- Precio unitario (NUMBER con 2 decimales)
    CATEGORIA   VARCHAR2(20)    NOT NULL,                       -- PERRO / GATO / AVE / OTROS
    STOCK       NUMBER(10)      NOT NULL,                       -- Unidades disponibles
    MARCA       VARCHAR2(50),                                   -- Marca comercial (opcional)
    IMAGEN      VARCHAR2(200),                                  -- URL/ruta de imagen (opcional)
    CONSTRAINT PK_PRODUCTO PRIMARY KEY (ID),                    -- Clave primaria
    CONSTRAINT CK_PRODUCTO_PRECIO CHECK (PRECIO > 0),           -- El precio debe ser positivo
    CONSTRAINT CK_PRODUCTO_STOCK  CHECK (STOCK >= 0)            -- El stock no puede ser negativo
);

CREATE SEQUENCE PRODUCTO_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- ============================================================
-- TABLA: ORDEN_COMPRA
-- ============================================================
CREATE TABLE ORDEN_COMPRA (
    ID              NUMBER(19)      NOT NULL,                   -- PK autoincremental (desde ORDEN_COMPRA_SEQ)
    CLIENTE_NOMBRE  VARCHAR2(100)   NOT NULL,                   -- Nombre del cliente
    CLIENTE_EMAIL   VARCHAR2(100)   NOT NULL,                   -- Email del cliente (validado en la app)
    FECHA           VARCHAR2(10)    NOT NULL,                   -- Fecha del pedido (yyyy-MM-dd)
    ESTADO          VARCHAR2(15)    NOT NULL,                   -- PENDIENTE / CONFIRMADA / ENVIADA / ENTREGADA / CANCELADA
    TOTAL           NUMBER(14,2)    NOT NULL,                   -- Total de la orden (calculado en el service)
    DIRECCION_ENVIO VARCHAR2(150)   NOT NULL,                   -- Dirección de envío
    METODO_PAGO     VARCHAR2(40)    NOT NULL,                   -- Método de pago (ej: WEBPAY, TRANSFERENCIA)
    CONSTRAINT PK_ORDEN_COMPRA PRIMARY KEY (ID),                -- Clave primaria
    CONSTRAINT CK_ORDEN_ESTADO CHECK (ESTADO IN ('PENDIENTE','CONFIRMADA','ENVIADA','ENTREGADA','CANCELADA')), -- Estados válidos
    CONSTRAINT CK_ORDEN_TOTAL  CHECK (TOTAL >= 0)               -- El total no puede ser negativo
);

CREATE SEQUENCE ORDEN_COMPRA_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- ============================================================
-- TABLA: DETALLE_ORDEN
-- ============================================================
CREATE TABLE DETALLE_ORDEN (
    ID                NUMBER(19)    NOT NULL,                   -- PK autoincremental (desde DETALLE_ORDEN_SEQ)
    ORDEN_ID          NUMBER(19)    NOT NULL,                   -- FK hacia ORDEN_COMPRA.ID (cascada desde el padre)
    PRODUCTO_ID       NUMBER(19)    NOT NULL,                   -- FK lógica hacia PRODUCTO.ID
    PRODUCTO_NOMBRE   VARCHAR2(100) NOT NULL,                   -- Snapshot del nombre al momento de la compra
    CANTIDAD          NUMBER(10)    NOT NULL,                   -- Cantidad comprada
    PRECIO_UNITARIO   NUMBER(12,2)  NOT NULL,                   -- Precio unitario al momento de la compra
    SUBTOTAL          NUMBER(14,2)  NOT NULL,                   -- cantidad * precio_unitario
    CONSTRAINT PK_DETALLE_ORDEN PRIMARY KEY (ID),               -- Clave primaria
    CONSTRAINT FK_DETALLE_ORDEN    FOREIGN KEY (ORDEN_ID)    REFERENCES ORDEN_COMPRA(ID) ON DELETE CASCADE, -- Cascada al borrar la orden
    CONSTRAINT FK_DETALLE_PRODUCTO FOREIGN KEY (PRODUCTO_ID) REFERENCES PRODUCTO(ID),                       -- FK física al producto
    CONSTRAINT CK_DETALLE_CANTIDAD CHECK (CANTIDAD >= 1),        -- Al menos 1 unidad por ítem
    CONSTRAINT CK_DETALLE_SUBTOTAL CHECK (SUBTOTAL >= 0)         -- Subtotal no puede ser negativo
);

CREATE SEQUENCE DETALLE_ORDEN_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Índices útiles para las consultas del repositorio
CREATE INDEX IX_ORDEN_ESTADO         ON ORDEN_COMPRA (ESTADO);
CREATE INDEX IX_ORDEN_CLIENTE_NOMBRE ON ORDEN_COMPRA (CLIENTE_NOMBRE);
CREATE INDEX IX_PRODUCTO_CATEGORIA   ON PRODUCTO (CATEGORIA);
