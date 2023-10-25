insert into tasks (title, description, priority, status, created_at, expires_at, notify, user_id)
values
    ('Buy the car', 'Buy a mercedes-benz', 'HIGH', 'DONE', NOW() - interval '2' day, NOW() - interval '1' day, false, 1), -- id=1
    ('Do laboratory programming', 'Do laboratory tests on logical and functional programming', 'MEDIUM', 'IN_PROGRESS', NOW() - interval '2' DAY, NOW() - interval '1' DAY, true, 1), -- id=2
    ('Make a cake', 'Cake with cherry', 'LOW', 'DONE', NOW() + interval '1' DAY, NOW() + interval '2' DAY, true, 1), -- id=3
    ('Clean the room', 'Throw away old books', 'MEDIUM', 'NEW', NOW() + interval '1' DAY, NOW() + interval '2' DAY, false, 1),  -- id=4
    ('Feed the god', 'Darcy always wants to eat...', 'HIGH', 'IN_PROGRESS', NOW() + interval '1' DAY, NOW() + interval '2' DAY, true, 1), -- id=5
    ('Buy a present for sister', 'Present for Veronika', 'LOW', 'DONE', NOW() + interval '1' DAY, NOW() + interval '2' DAY, false, 1), -- id=6
    ('Call Ruslan', 'Call and ask about headphones', 'LOW', 'NEW', NOW() + interval '1' DAY, NOW() + interval '2' DAY, false, 1), -- id=7
    ('Pay for internet', 'Nevalink', 'HIGH', 'NEW', NOW() + interval '1' DAY, NOW() + interval '2' DAY, true, 2), -- id=8
    ('Pay for university', '220k per year', 'MEDIUM', 'NEW', NOW() + interval '1' DAY, NOW() + interval '2' DAY, true, 2); -- id=9
