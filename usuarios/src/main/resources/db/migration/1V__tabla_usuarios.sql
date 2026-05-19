create table usuario (
    id integer not null auto_increment,
    usuario varchar(255) not null,
    nombre varchar(255) not null,
    apellido varchar(255) not null,
    email varchar(255) not null,
    rut varchar(10) not null,
    password varchar(255) not null,
    fecha_registro datetime not null,
    primary key (id)
)