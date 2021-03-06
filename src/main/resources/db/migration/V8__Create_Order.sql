create table orders
(id bigint generated by default as identity,
 user_id bigint,
 book_id bigint,
 address_id bigint,
 count int,
 amount NUMERIC,
 currency varchar(5),
 status int,
 primary key (id),
 CONSTRAINT fk_book_orders FOREIGN KEY(book_id) references books(id),
 CONSTRAINT fk_address_orders FOREIGN KEY(address_id) references addresses(id))