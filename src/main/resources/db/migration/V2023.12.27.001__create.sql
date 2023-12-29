create table if not exists client (
  id bigint,
  first_name varchar(100),
  last_name varchar(100),
  phone_number varchar(100),
  primary key (id)
);

create table if not exists reservation (
  id bigserial,
  date date,
  time time without time zone,
  number_of_people varchar(100),
  primary key (id)
);

create unique index client_index on client(id);
create unique index reservation_index on reservation(id);

create table if not exists client_reservation (
  client_id bigint NOT NULL,
  reservation_id bigint NOT NULL,
  primary key (client_id, reservation_id),
  foreign key (client_id) references client(id) on delete cascade,
  foreign key (reservation_id) references reservation(id) on delete cascade
);