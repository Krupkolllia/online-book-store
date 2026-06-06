INSERT INTO categories (id, name)
VALUES (1, 'Fantasy'),
       (2, 'Horror'),
       (3, 'History');

INSERT INTO books (id, title, author, isbn, price)
VALUES (1, 'Test title 1', 'Test author 1', '978-0134685991', 9.99),
       (2, 'Test title 2', 'Test author 2', '978-0132350884', 9.99),
       (3, 'Test title 3', 'Test author 3', '978-0131103627', 9.99),
       (4, 'Test title 4', 'Test author 4', '978-0596517748', 9.99);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 3),
       (3, 2),
       (3, 3);

INSERT INTO users (id, email, password, first_name, last_name)
VALUES (987, 'test_user@mail.com', 'test', 'test', 'test'),
       (988, 'test_admin@mail.com', 'test', 'test', 'test');

INSERT INTO users_roles (user_id, role_id)
VALUES (987, 1),
       (988, 2);

INSERT INTO shopping_carts (user_id)
VALUES (987);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (10, 987, 1, 2),
       (11, 987, 2, 1),
       (12, 987, 3, 3);