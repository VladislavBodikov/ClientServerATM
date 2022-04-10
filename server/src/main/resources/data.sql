INSERT INTO USERS (id,first_name,last_name,status,role) VALUES
(1,'User','Userov','ACTIVE','USER');
INSERT INTO USERS (id,first_name,last_name,status,role) VALUES
(2,'Admin','Adminov','ACTIVE','ADMIN');
INSERT INTO ACCOUNTS (id,card_number,account_number,amount,pin_code,user_id) VALUES
(1,'1111222211112222','40800000000000000002','390.50','$2a$12$8oYHVHKlJVAdQMYWPfnhKuU/AS9P.isAL3XkZww1cf9tXClMA5H76',1);
INSERT INTO ACCOUNTS (id,card_number,account_number,amount,pin_code,user_id) VALUES
(2,'1111333311113333','40800000000000000003','4444.50','$2a$12$8oYHVHKlJVAdQMYWPfnhKuU/AS9P.isAL3XkZww1cf9tXClMA5H76',2);