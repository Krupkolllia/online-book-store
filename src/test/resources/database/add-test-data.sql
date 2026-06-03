INSERT INTO categories (id, name)
VALUES (1, 'Fantasy'),
       (2, 'Horror'),
       (3, 'History');

INSERT INTO books (id, title, author, isbn, price)
VALUES (1, 'Test title 1', 'Test author 1', '978-0134685991', 9.99),
       (2, 'Test title 2', 'Test author 2', '978-0132350884', 9.99),
       (3, 'Test title 3', 'Test author 3', '978-0131103627', 9.99);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 3),
       (3, 2),
       (3, 3);
