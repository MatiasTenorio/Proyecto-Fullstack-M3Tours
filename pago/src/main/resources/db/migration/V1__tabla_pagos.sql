CREATE TABLE pagos (
    id                  Integer         NOT NULL AUTO_INCREMENT,
    orden_compra        Integer         NOT NULL,
    usuario_id          Integer         NOT NULL,
    reserva_id          Integer         NOT NULL,
    costo               DOUBLE          NOT NULL,
    fecha_pago      DATE DEFAULT (CURRENT_DATE),
    fecha_emision   DATE DEFAULT (CURRENT_DATE),
    primary key (id)
);