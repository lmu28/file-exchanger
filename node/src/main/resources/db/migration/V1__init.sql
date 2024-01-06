create table app_document
(
    id                bigserial not null,
    doc_name          varchar(255),
    file_size         bigint,
    mime_type         varchar(255),
    telegram_file_id  varchar(255),
    binary_content_id bigint,
    primary key (id)
) ;
create table app_photo
(
    id                bigserial not null,
    file_size         int      not null,
    telegram_file_id  varchar(255),
    binary_content_id bigint,
    primary key (id)
);
create table app_user
(
    id               bigserial not null,
    email            varchar(255),
    first_login_date timestamp,
    first_name       varchar(255),
    is_active        boolean   not null,
    last_name        varchar(255),
    state            varchar(255),
    telegram_user_id bigint      not null,
    username         varchar(255),
    primary key (id)
) ;
create table binary_content
(
    id                     bigserial not null,
    file_as_array_of_bytes bytea,
    primary key (id)
) ;
create table row_data
(
    id    bigserial not null,
    event jsonb,
    primary key (id)
) ;
alter table app_document
    add constraint fk_app_document_binary_content_id foreign key (binary_content_id) references binary_content;
alter table app_photo
    add constraint fk_app_photo_binary_content_id foreign key (binary_content_id) references binary_content;