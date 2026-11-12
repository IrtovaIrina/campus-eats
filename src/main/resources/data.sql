INSERT INTO users (email, password_hash, full_name, phone, student_group, role, created_at)
VALUES ('a.d.sokolov@campus.edu', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b',
        'Соколов Алексей Дмитриевич', '+7 (912) 640-18-22', 'ПИ-301', 'STUDENT', CURRENT_TIMESTAMP),
       ('m.v.morozova@campus.edu', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b',
        'Морозова Мария Владиславовна', '+7 (922) 115-43-90', 'ИВТ-204', 'STUDENT', CURRENT_TIMESTAMP),
       ('canteen.eng@campus.edu', 'a905173864f9596244140d5a468c8b6d5a349a4091490f2005593b6f76a9782a',
        'Карпов Сергей Викторович', '+7 (904) 380-55-10', 'Инженерный корпус', 'CANTEEN', CURRENT_TIMESTAMP),
       ('canteen.main@campus.edu', 'a905173864f9596244140d5a468c8b6d5a349a4091490f2005593b6f76a9782a',
        'Елисеева Ольга Николаевна', '+7 (908) 911-27-64', 'Главный корпус', 'CANTEEN', CURRENT_TIMESTAMP),
       ('admin@campus.edu', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
        'Воронцов Дмитрий Игоревич', '+7 (900) 375-44-44', 'Администрация', 'ADMIN', CURRENT_TIMESTAMP),
       ('canteen.hum@campus.edu', 'a905173864f9596244140d5a468c8b6d5a349a4091490f2005593b6f76a9782a',
        'Смирнов Павел Андреевич', '+7 (902) 555-19-80', 'Гуманитарный корпус', 'CANTEEN', CURRENT_TIMESTAMP);

INSERT INTO canteens (name, building, floor, opening_hours, description, image_url, rating, reviews_count, is_open,
                      is_verified, manager_id)
VALUES ('Буфет Инженерного корпуса «ТехноЛанч»', 'Инженерный корпус, блок Б', 1, '08:30 - 18:30',
        'Боулы, воки, горячие обеды и зерновой кофе',
        'https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=900&q=80', 4.9, 3, TRUE, TRUE,
        3),
       ('Столовая Главного корпуса «Академическая»', 'Главный учебный корпус', 1, '08:00 - 19:00',
        'Супы, горячие блюда на пару и выпечка',
        'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=900&q=80', 4.8, 1, TRUE, TRUE,
        4),
       ('Кофейня Гуманитарного корпуса «Атриум»', 'Гуманитарный корпус', 2, '09:00 - 18:00',
        'Сэндвичи, салаты, десерты и смузи',
        'https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&w=900&q=80', 4.7, 0, TRUE,
        TRUE, 6);

INSERT INTO categories (name, slug, display_order)
VALUES ('Боулы и салаты', 'bowls-and-salads', 1),
       ('Супы', 'soups', 2),
       ('Горячие блюда', 'hot-dishes', 3),
       ('Завтраки и выпечка', 'breakfast-and-bakery', 4),
       ('Напитки', 'drinks', 5);

INSERT INTO dietary_tags (name, code, badge_color)
VALUES ('Вегетарианское', 'VEGAN', 'emerald'),
       ('Без глютена', 'GLUTEN_FREE', 'amber'),
       ('Высокий белок', 'HIGH_PROTEIN', 'indigo'),
       ('Без сахара', 'SUGAR_FREE', 'teal'),
       ('Халяль', 'HALAL', 'sky');

INSERT INTO dishes (canteen_id, category_id, name, composition, weight_grams, calories, proteins, fats, carbs, price,
                    image_url, prep_time_minutes, is_available, rating)
VALUES (1, 1, 'Поке-боул с лососем, киноа и авокадо',
        'Филе лосося слабосолёное, киноа отварная, авокадо, эдамаме, огурец, чука, кунжутный соус', 310, 420, 25.4,
        18.2, 38.0, 340.00, 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=600&q=80',
        6, TRUE, 4.9),
       (1, 2, 'Том Ям с креветками и рисом', 'Бульон на кокосовом молоке, креветки, шампиньоны, черри, кинза, рис', 380,
        365, 19.8, 16.5, 34.2, 320.00,
        'https://images.unsplash.com/photo-1548943487-a2e4e43b4853?auto=format&fit=crop&w=600&q=80', 8, TRUE, 4.9),
       (1, 3, 'Вок-удон с цыплёнком терияки',
        'Лапша удон, филе куриного бедра в соусе терияки, болгарский перец, цукини, морковь, кунжут', 330, 510, 28.0,
        14.6, 65.4, 270.00,
        'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80', 9, TRUE, 4.8),
       (1, 4, 'Сырники на рисовой муке', 'Творог 5%, мука рисовая, яйцо, эритрит, ваниль, ягодный мусс', 220, 295, 24.0,
        11.5, 23.8, 210.00,
        'https://images.unsplash.com/photo-1565299543923-37dd37887442?auto=format&fit=crop&w=600&q=80', 7, TRUE, 5.0),
       (1, 5, 'Айс Матча Латте', 'Японский чай матча, кокосовое молоко, лёд', 300, 110, 2.4, 5.2, 12.8, 190.00,
        'https://images.unsplash.com/photo-1536256263959-770b48d82b0a?auto=format&fit=crop&w=600&q=80', 4, TRUE, 4.8),
       (2, 3, 'Паровые котлеты из индейки с гречкой', 'Филе индейки, гречка на пару, брокколи, оливковое масло', 320,
        380, 32.5, 9.8, 41.0, 240.00,
        'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=600&q=80', 6, TRUE, 4.8),
       (2, 2, 'Крем-суп из тыквы', 'Тыква, овощной бульон, кокосовые сливки, тыквенные семечки', 300, 215, 5.2, 11.0,
        24.5, 175.00, 'https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?auto=format&fit=crop&w=600&q=80', 5,
        TRUE, 4.9),
       (2, 2, 'Борщ с говядиной и сметаной', 'Говядина, свекла, капуста, картофель, томаты, зелень, сметана 15%', 350,
        290, 16.4, 13.2, 25.0, 195.00,
        'https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&fit=crop&w=600&q=80', 5, TRUE, 4.7),
       (2, 1, 'Фалафель-боул с хумусом', 'Нутовый фалафель, хумус, томаты, огурцы, булгур с зеленью', 310, 410, 14.8,
        16.0, 51.2, 250.00,
        'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=600&q=80', 6, TRUE, 4.8),
       (3, 4, 'Чиабатта с индейкой и песто', 'Чиабатта, филе индейки гриль, моцарелла, соус песто, томаты, руккола',
        240, 460, 26.0, 19.5, 44.0, 260.00,
        'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=600&q=80', 6, TRUE, 4.7),
       (3, 4, 'Чиа-пудинг с манго', 'Семена чиа, миндальное молоко, пюре манго, миндальные лепестки', 210, 230, 6.5,
        12.0, 24.0, 220.00,
        'https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&w=600&q=80', 3, TRUE, 4.9),
       (3, 5, 'Ягодный протеиновый смузи', 'Черника, малина, банан, сывороточный протеин, овсяное молоко', 330, 245,
        22.0, 3.5, 31.0, 230.00,
        'https://images.unsplash.com/photo-1553530666-ba11a7da3888?auto=format&fit=crop&w=600&q=80', 4, TRUE, 4.8);

INSERT INTO dish_dietary_tags (dish_id, tag_id)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (2, 2),
       (2, 3),
       (3, 3),
       (3, 5),
       (4, 2),
       (4, 3),
       (4, 4),
       (5, 1),
       (5, 2),
       (5, 4),
       (6, 2),
       (6, 3),
       (6, 4),
       (6, 5),
       (7, 1),
       (7, 2),
       (7, 4),
       (8, 2),
       (8, 3),
       (9, 1),
       (9, 4),
       (9, 5),
       (10, 3),
       (10, 5),
       (11, 1),
       (11, 2),
       (11, 4),
       (12, 3),
       (12, 4);

INSERT INTO favorite_dishes (user_id, dish_id, added_at)
VALUES (1, 1, CURRENT_TIMESTAMP),
       (1, 4, CURRENT_TIMESTAMP),
       (1, 7, CURRENT_TIMESTAMP),
       (2, 2, CURRENT_TIMESTAMP);

INSERT INTO cart_items (user_id, dish_id, quantity, special_request, added_at)
VALUES (1, 1, 1, 'Соус отдельно', CURRENT_TIMESTAMP),
       (1, 5, 1, 'Без льда', CURRENT_TIMESTAMP);

INSERT INTO food_orders (order_number, user_id, canteen_id, pickup_time_slot, total_amount, total_