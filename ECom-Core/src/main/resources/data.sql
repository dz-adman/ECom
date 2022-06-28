INSERT INTO ECOM_USER (id, first_name, last_name, login_id, email, password, role, locked, enabled, deleted) VALUES
(1, 'A', 'D', 'adman', 'amdan@gmail.com', '$2a$10$hhRtjhjghLZq.XiiHMs.oOPE3bQVVa3YeHTpxHhJacVWUCtxQBOOC', 0, 0, 1, 0),
(2, 'A', 'D', 'user1', 'user1@gmail.com', '$2a$10$hhRtjhjghLZq.XiiHMs.oOPE3bQVVa3YeHTpxHhJacVWUCtxQBOOC', 1, 0, 1, 0),
(3, 'A', 'D', 'user2', 'user2@gmail.com', '$2a$10$hhRtjhjghLZq.XiiHMs.oOPE3bQVVa3YeHTpxHhJacVWUCtxQBOOC', 1, 0, 1, 0),
(4, 'A', 'D', 'seller1', 'seller1@gmail.com', '$2a$10$hhRtjhjghLZq.XiiHMs.oOPE3bQVVa3YeHTpxHhJacVWUCtxQBOOC', 2, 0, 1, 0),
(5, 'A', 'D', 'seller2', 'seller2@gmail.com', '$2a$10$hhRtjhjghLZq.XiiHMs.oOPE3bQVVa3YeHTpxHhJacVWUCtxQBOOC', 2, 0, 1, 0);

INSERT INTO ECOM_USER_ADDRESS (id, user_id, address_type, default_address, address, city, state, country, pin_code, landmark) VALUES
(1, 2, 0, 1, "#142/1 DSN", "PATIALA", "PUNJAB", "INDIA", 147004, null),
(2, 3, 0, 1, "#141/1 DSN", "PATIALA", "PUNJAB", "INDIA", 147004, null);

INSERT INTO ECOM_PRODUCTS (id, product_id, name, category, sub_category, brand, price, stock, stock_unit, product_owner_id, status) VALUES
(1, "PRDCT_00001", "Brush", 1, 5, 2, 25.00, 10, 0, 1, 1),
(2, "PRDCT_00002", "Towel", 1, 5, 2, 250.00, 10, 0, 1, 1),
(3, "PRDCT_00003", "Hair Oil", 1, 5, 2, 50.00, 10, 0, 4, 1),
(4, "PRDCT_00004", "Men's Jacket", 1, 5, 1, 7500.00, 10, 0, 4, 1),
(5, "PRDCT_00005", "Cap", 1, 5, 1, 335.00, 10, 0, 4, 1),
(6, "PRDCT_00006", "A-to-C Cable", 0, 5, 1, 200.00, 10, 0, 5, 1),
(7, "PRDCT_00007", "Chain Lubricant", 2, 5, 0, 500.00, 10, 0, 5, 1),
(8, "PRDCT_00008", "Engine Oil", 2, 5, 0, 1250.00, 10, 0, 5, 1);

INSERT INTO ECOM_PRDCT_DISCNT (id, code, name, valid_from, valid_to, percentage_value, status) VALUES
(1, "DISCNT_00001", "NEW YEAR", '2022-01-01', '2022-01-7', 2, 1),
(2, "DISCNT_00002", "ALL TIME", '2022-01-01', '2022-12-31', 5, 1),
(3, "DISCNT_00003", "ALL TIME", '2022-01-01', '2022-01-31', 2, 1),
(4, "DISCNT_00004", "BACK_DATED", DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), 2, 0);

INSERT INTO ECOM_PRDCT_DISCNT_SUBS (id, product_id, discount_id, status) VALUES
(1, 1, 1, 1),
(2, 2, 2, 1),
(3, 3, 4, 1),
(4, 4, 3, 1),
(5, 5, 4, 1),
(6, 6, 2, 1),
(7, 7, 1, 1),
(8, 8, 3, 1);

