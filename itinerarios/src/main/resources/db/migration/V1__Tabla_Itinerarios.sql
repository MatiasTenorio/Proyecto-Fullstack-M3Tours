create table itinerarios (
    id                  integer         not null auto_increment,
    tour_id             integer,
    destino_id          integer,
    dia                 integer         not null,
    descripcion         varchar(255),
    hora_inicio         time,
    hora_fin            time,
    lugar               varchar(255)    not null,
    primary key (id)
)