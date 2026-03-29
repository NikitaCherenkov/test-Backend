create schema if not exists purchase;

create type purchase.currency_code_type as enum ('RUB', 'USD', 'EUR');
create type purchase.nds_rate_type as enum ('Без НДС', '18%', '20%');

create table if not exists purchase.customer (
    id bigint generated always as identity primary key,
    customer_code varchar(100) not null unique,
    customer_name varchar(255) not null,
    customer_inn varchar(12) not null unique,
    customer_kpp varchar(9),
    customer_legal_address varchar(500),
    customer_postal_address varchar(500),
    customer_email varchar(255),
    customer_code_main varchar(100),
    is_organization boolean not null default false,
    is_person boolean not null default false,
    constraint fk_customer_code_main
        foreign key (customer_code_main)
        references purchase.customer (customer_code)
        on update cascade
        on delete set null,
    constraint chk_customer_type_only_one_true
        check ((is_organization and not is_person) or (is_person and not is_organization))
);

create table if not exists purchase.lot (
    id bigint generated always as identity primary key,
    lot_name varchar(255) not null,
    customer_code varchar(100) not null,
    price numeric(19,2) not null,
    currency_code purchase.currency_code_type not null,
    nds_rate purchase.nds_rate_type not null,
    place_delivery varchar(500) not null,
    date_delivery date not null,
    constraint fk_lot_customer_code
        foreign key (customer_code)
        references purchase.customer (customer_code)
        on update cascade
        on delete restrict,
    constraint check_price check (price >= 0)
);

create index idx_lot_customer_code on purchase.lot(customer_code);