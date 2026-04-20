-- ============================================================
-- data.sql - Datos iniciales del microservicio mascotas-ordenes
-- ============================================================

-- ============================================================
-- PRODUCTOS (6 registros)
-- ============================================================
INSERT INTO PRODUCTO (ID, NOMBRE, DESCRIPCION, PRECIO, CATEGORIA, STOCK, MARCA, IMAGEN)
VALUES (PRODUCTO_SEQ.NEXTVAL, 'Alimento Premium Perro Adulto',   'Croquetas balanceadas 15kg',          24990, 'PERRO', 50, 'ProPlan',  'https://cdn.ejemplo.cl/perro-premium.jpg');

INSERT INTO PRODUCTO (ID, NOMBRE, DESCRIPCION, PRECIO, CATEGORIA, STOCK, MARCA, IMAGEN)
VALUES (PRODUCTO_SEQ.NEXTVAL, 'Arena Sanitaria Gato',            'Arena aglomerante 10kg',              8990,  'GATO',  80, 'CatLife',  'https://cdn.ejemplo.cl/arena-gato.jpg');

INSERT INTO PRODUCTO (ID, NOMBRE, DESCRIPCION, PRECIO, CATEGORIA, STOCK, MARCA, IMAGEN)
VALUES (PRODUCTO_SEQ.NEXTVAL, 'Juguete Cuerda Perro',            'Juguete de cuerda trenzada',          4990,  'PERRO', 120,'PetPlay',  'https://cdn.ejemplo.cl/juguete-cuerda.jpg');

INSERT INTO PRODUCTO (ID, NOMBRE, DESCRIPCION, PRECIO, CATEGORIA, STOCK, MARCA, IMAGEN)
VALUES (PRODUCTO_SEQ.NEXTVAL, 'Comida Gato Salmón',              'Paté de salmón lata 85g',             1290,  'GATO',  200,'Whiskas',  'https://cdn.ejemplo.cl/gato-salmon.jpg');

INSERT INTO PRODUCTO (ID, NOMBRE, DESCRIPCION, PRECIO, CATEGORIA, STOCK, MARCA, IMAGEN)
VALUES (PRODUCTO_SEQ.NEXTVAL, 'Alpiste Canario',                 'Alimento premium para aves 1kg',      3490,  'AVE',   60, 'AviPlus',  'https://cdn.ejemplo.cl/alpiste.jpg');

INSERT INTO PRODUCTO (ID, NOMBRE, DESCRIPCION, PRECIO, CATEGORIA, STOCK, MARCA, IMAGEN)
VALUES (PRODUCTO_SEQ.NEXTVAL, 'Correa Retráctil',                'Correa retráctil 5m para paseo',      12990, 'PERRO', 30, 'WalkPro',  'https://cdn.ejemplo.cl/correa.jpg');

-- ============================================================
-- ORDENES DE COMPRA (3 registros, estados variados)
-- ============================================================
INSERT INTO ORDEN_COMPRA (ID, CLIENTE_NOMBRE, CLIENTE_EMAIL, FECHA, ESTADO, TOTAL, DIRECCION_ENVIO, METODO_PAGO)
VALUES (ORDEN_COMPRA_SEQ.NEXTVAL, 'María González', 'maria.gonzalez@mail.cl', '2026-04-15', 'CONFIRMADA', 33980,  'Av. Apoquindo 1234, Las Condes',       'WEBPAY');

INSERT INTO ORDEN_COMPRA (ID, CLIENTE_NOMBRE, CLIENTE_EMAIL, FECHA, ESTADO, TOTAL, DIRECCION_ENVIO, METODO_PAGO)
VALUES (ORDEN_COMPRA_SEQ.NEXTVAL, 'Carlos Muñoz',   'carlos.munoz@mail.cl',   '2026-04-16', 'PENDIENTE',  5780,   'Calle Providencia 567, Providencia',   'TRANSFERENCIA');

INSERT INTO ORDEN_COMPRA (ID, CLIENTE_NOMBRE, CLIENTE_EMAIL, FECHA, ESTADO, TOTAL, DIRECCION_ENVIO, METODO_PAGO)
VALUES (ORDEN_COMPRA_SEQ.NEXTVAL, 'Patricia López', 'patricia.lopez@mail.cl', '2026-04-17', 'ENTREGADA',  16480,  'Av. Vitacura 890, Vitacura',           'WEBPAY');

-- ============================================================
-- DETALLES DE ORDEN (7 registros)
-- Asumimos productos con IDs 1..6 y órdenes con IDs 1..3
-- ============================================================
INSERT INTO DETALLE_ORDEN (ID, ORDEN_ID, PRODUCTO_ID, PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES (DETALLE_ORDEN_SEQ.NEXTVAL, 1, 1, 'Alimento Premium Perro Adulto', 1, 24990, 24990);

INSERT INTO DETALLE_ORDEN (ID, ORDEN_ID, PRODUCTO_ID, PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES (DETALLE_ORDEN_SEQ.NEXTVAL, 1, 3, 'Juguete Cuerda Perro',          1, 4990,  4990);

INSERT INTO DETALLE_ORDEN (ID, ORDEN_ID, PRODUCTO_ID, PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES (DETALLE_ORDEN_SEQ.NEXTVAL, 1, 4, 'Comida Gato Salmón',            3, 1290,  3870);

INSERT INTO DETALLE_ORDEN (ID, ORDEN_ID, PRODUCTO_ID, PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES (DETALLE_ORDEN_SEQ.NEXTVAL, 2, 2, 'Arena Sanitaria Gato',           1, 8990,  8990);

INSERT INTO DETALLE_ORDEN (ID, ORDEN_ID, PRODUCTO_ID, PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES (DETALLE_ORDEN_SEQ.NEXTVAL, 2, 5, 'Alpiste Canario',                1, 3490,  3490);

INSERT INTO DETALLE_ORDEN (ID, ORDEN_ID, PRODUCTO_ID, PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES (DETALLE_ORDEN_SEQ.NEXTVAL, 3, 6, 'Correa Retráctil',               1, 12990, 12990);

INSERT INTO DETALLE_ORDEN (ID, ORDEN_ID, PRODUCTO_ID, PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES (DETALLE_ORDEN_SEQ.NEXTVAL, 3, 3, 'Juguete Cuerda Perro',           1, 4990,  4990);

COMMIT;
