insert into users(name, username, email, password)
values ('kirill', 'isthatkirill', 'isthatkirill@yandex.ru', '$2a$12$m03nQbX3Xp4G04TGdXfcK.4XoiRqbMb0in0hZ/nFdrheFQU6svo6W'),
       ('anna', 'annushka', 'annushka@yandex.ru', '$2a$12$/HVpYIlpqnYvwU7sanp6XuMDDD0lfQYWkaQLSU8xasmP0LelYZ5Ii'),
       ('anastasia', 'stassy', 'stassy@yandex.ru', '$2a$12$lNIu6EReMAUDv51xHeDh1esFgq4JlimCu6KGYiG072pl3HTTDWjRC');

insert into user_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER');