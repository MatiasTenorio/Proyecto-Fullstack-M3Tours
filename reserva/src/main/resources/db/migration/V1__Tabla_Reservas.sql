CREATE TABLE reservas (
    id                  integer         not null auto_increment,
    tour_id             integer,
    numero_asiento      varchar(50),
    primary key (id)
);
 