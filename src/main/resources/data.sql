INSERT INTO currency_type (id, name) VALUES
 (1, 'USD'),
 (2, 'CAD'),
 (3, 'HKD'),
 (4, 'AUD'),
 (5, 'GBP'),
 (6, 'EUR'),
 (7, 'JPY');

INSERT INTO account (id, balance, currency_type_id, created_at) VALUES
(1, 500.00, 1, '2018-03-01'),
(2, 650.00, 6, '2022-01-09'),
(3, 1100.00, 6, '2020-04-15'),
(4, 800.00, 5, '2020-11-24'),
(5, 1500.00, 6, '2017-10-29'),
(6, 1350.00, 2, '2019-06-05');

INSERT INTO transaction (source_account_id, target_account_id, amount, currency_type_id) VALUES
(1, 6, 50.00, 2),
(1, 6, 30.00, 2),
(1, 6, 80.00, 2),
(6, 1, 70.00, 1),
(1, 4, 30.00, 5),
(1, 2, 50.00, 6),
(3, 5, 150.00, 6),
(2, 3, 250.00, 6),
(5, 3, 90.00, 6);



