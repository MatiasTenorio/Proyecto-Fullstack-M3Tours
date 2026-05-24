create table operadores (
    id                  integer             not null auto_increment,
    empresa_id          integer,
    nombre              varchar(255)        not null,
    apellido            varchar(255)        not null,
    rut                 varchar(10)         not null unique,
    email               varchar(255)        not null unique,
    telefono            integer(15)
    fecha_registro      datetime            not null, 
    primary key (id)
) 