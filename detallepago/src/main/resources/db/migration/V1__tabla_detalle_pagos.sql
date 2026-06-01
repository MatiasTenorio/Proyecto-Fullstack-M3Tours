CREATE TABLE detalle_pagos (
    id              Integer         NOT NULL AUTO_INCREMENT,
    numero_boleta   VARCHAR(100)    NOT NULL,
    tipo_pago       VARCHAR(50)     NOT NULL,
    estado          VARCHAR(50)     NOT NULL,
    nombre_tour     VARCHAR(100)    NOT NULL,
    numero_asiento  VARCHAR(50)     NOT NULL,
    subtotal        DOUBLE          NOT NULL,
    impuesto        DOUBLE          NOT NULL,
    total           DOUBLE          NOT NULL,
    primary key (id)
);