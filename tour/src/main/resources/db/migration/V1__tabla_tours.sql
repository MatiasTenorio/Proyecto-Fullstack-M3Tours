create table tours (
    id integer not null auto_increment,
    operador_id integer,
    numero_reservas integer,
    ubicacion_inicial varchar(255),
    fecha_inicial date,
    primary key (id)
)