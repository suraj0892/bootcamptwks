alter table orders RENAME payment_status TO status;
alter table orders add COLUMN currency VARCHAR(5);