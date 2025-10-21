
DROP TABLE IF EXISTS project;

create table project (
   id varchar(255) not null,
   region varchar(255) not null,
   access_key varchar(255) not null,
   secret_key varchar(255) not null,
   created_by varchar(255),
   enabled boolean not null,
   last_modified_by varchar(255),
   name varchar(255) not null,
   created_date timestamp(6),
   last_modified_date timestamp(6),
   
   primary key (id)
)