alter table reservation add column client_id bigint;

alter table reservation
add constraint fk_client_id
foreign key (client_id) references client(id) on delete cascade;