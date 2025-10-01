-- Sample order to show list
INSERT INTO orders (user_id, created_at, status) VALUES (2, NOW(), 'PENDING');
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (1, 1, 1, 199.90);
