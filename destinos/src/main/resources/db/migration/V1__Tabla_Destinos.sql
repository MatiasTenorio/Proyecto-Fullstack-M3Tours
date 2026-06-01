create table destinos (
    id                  integer             not null auto_increment,
    nombre              varchar(255)        not null,
    pais                varchar(255)        not null,
    ciudad              varchar(255)        not null,
    descripcion         varchar(255),
    imagen_url          varchar(500),
    primary key (id)
)