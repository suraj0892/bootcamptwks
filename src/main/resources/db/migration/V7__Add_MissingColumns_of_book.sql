ALTER TABLE books ADD COLUMN image_url varchar(255);
ALTER TABLE books ADD COLUMN small_image_url varchar(255);
ALTER TABLE books ADD COLUMN isbn varchar(25);
ALTER TABLE books ADD COLUMN original_publication_year integer ;
ALTER TABLE books ADD COLUMN original_title varchar(255);
ALTER TABLE books ADD COLUMN language_code varchar(25);
ALTER TABLE books ADD COLUMN average_rating varchar(25);