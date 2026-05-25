create table categorias (
    id                  integer             not null auto_increment,
    nombre              varchar(255)        not null unique,
    descripcion         varchar(255),
    estado              varchar(50)         not null,
    primary key (id)
)