insert into users (name, email, password, role) values
    ('Alice Johnson',   'alice@example.com',   'pass123',    'user'),
    ('Bob Smith',       'bob@example.com',     'secret456',  'user'),
    ('Charlie Green',   'charlie@example.com', 'charlie789', 'user'),
    ('Diana Carter',    'diana@example.com',   'diana111',   'user'),
    ('Ethan Brooks',    'ethan@example.com',   'ethan222',   'user'),
    ('Fiona White',     'fiona@example.com',   'fiona333',   'user'),
    ('George Hill',     'george@example.com',  'george444',  'user'),
    ('Hannah Reed',     'hannah@example.com',  'hannah555',  'user'),
    ('Ian Black',       'ian@example.com',     'ian666',     'user'),
    ('Julia Stone',     'julia@example.com',   'julia777',   'user'),
    ('Bart',     'bart@example.com',   'admin123',   'admin'),
    ('Fauve',     'fauve@example.com',   'admin123',   'admin'),
    ('Richard',     'richard@example.com',   'admin123',   'admin'),
    ('Yoran',     'yoran@example.com',   'admin123',   'admin');

insert into recipes (recipe_id, title, description, preptime, cookingtime, difficulty, image, mealtype, kitchenstyle) values
    (1, 'Spaghetti Bolognese', 'Classic Italian pasta with meat sauce.', 15, 45, 'medium', NULL, 'dinner', 'Italian'),
    (2, 'Vegetable Stir Fry',  'Quick and healthy mixed vegetable stir fry.', 10, 15, 'easy', NULL, 'lunch', 'Asian'),
    (3, 'Chicken Curry',       'Aromatic curry with chicken and basmati rice.', 20, 40, 'medium', NULL, 'dinner', 'Indian'),
    (4, 'Avocado Toast',       'Simple toast topped with fresh avocado.', 5, 5, 'easy', NULL, 'breakfast', 'American'),
    (5, 'Beef Tacos',          'Seasoned beef with fresh taco toppings.', 20, 10, 'easy', NULL, 'dinner', 'Mexican'),
    (6, 'Greek Salad',         'Fresh salad with feta cheese and olives.', 10, 0, 'easy', NULL, 'lunch', 'Greek'),
    (7, 'Pancakes',            'Fluffy breakfast pancakes with syrup.', 10, 15, 'easy', NULL, 'breakfast', 'American'),
    (8, 'Sushi Rolls',         'Rice rolls with fish and vegetables.', 25, 0, 'hard', NULL, 'dinner', 'Japanese'),
    (9, 'Lentil Soup',         'Hearty vegetarian lentil soup.', 15, 35, 'easy', NULL, 'dinner', 'Mediterranean'),
    (10,'Salmon Teriyaki',     'Grilled salmon with teriyaki glaze.', 15, 20, 'medium', NULL, 'dinner', 'Japanese');

insert into ingredients (name, description) values
    ('Tomato', 'Fresh red tomatoes.'),
    ('Ground Beef', 'Lean minced beef.'),
    ('Spaghetti', 'Dry pasta noodles.'),
    ('Soy Sauce', 'Fermented soy seasoning.'),
    ('Broccoli', 'Fresh broccoli.'),
    ('Chicken Breast', 'Boneless chicken breast.'),
    ('Curry Powder', 'Aromatic curry mix.'),
    ('Rice', 'Basmati white rice.'),
    ('Avocado', 'Ripe avocado.'),
    ('Bread', 'Whole grain bread.'),
    ('Beef', 'Seasoned beef.'),
    ('Tortilla', 'Corn or wheat tortilla.'),
    ('Feta Cheese', 'Greek white cheese.'),
    ('Cucumber', 'Fresh cucumber.'),
    ('Pancake Mix', 'Flour-based mix.'),
    ('Eggs', 'Chicken eggs.'),
    ('Salmon', 'Fresh salmon fillet.'),
    ('Teriyaki Sauce', 'Sweet soy glaze.'),
    ('Lentils', 'Dried lentils.'),
    ('Carrot', 'Fresh carrot.');

insert into allergens (allergen_id, name, displayname, description) values
    (1,  'gluten',       'Gluten',        'Found in wheat, barley, rye.'),
    (2,  'crustaceans',  'Crustaceans',   'Shrimp, crab, lobster.'),
    (3,  'eggs',         'Eggs',          'Egg proteins.'),
    (4,  'fish',         'Fish',          'Fish proteins.'),
    (5,  'peanuts',      'Peanuts',       'Peanut allergy.'),
    (6,  'soy',          'Soy',           'Soy products.'),
    (7,  'milk',         'Milk',          'Dairy & lactose.'),
    (8,  'tree_nuts',    'Tree Nuts',     'Almonds, walnuts, hazelnuts.'),
    (9,  'celery',       'Celery',        'Celery and root.'),
    (10, 'mustard',      'Mustard',       'Mustard seeds.'),
    (11, 'sesame',       'Sesame',        'Sesame seeds and oils.'),
    (12, 'sulphites',    'Sulphites',     'Sulphur dioxide preservatives.'),
    (13, 'lupin',        'Lupin',         'Lupin flour and beans.'),
    (14, 'molluscs',     'Molluscs',      'Clams, mussels, oysters.'),
    (15, 'corn',         'Corn',          'Corn and derivatives.');

insert into diets (diet_id, displayname, description) values
    (1,  'Veganistisch',   'Geen dierlijke producten.'),
    (2,  'Vegetarisch',    'Geen vlees of vis.'),
    (3,  'Glutenvrij',     'Geen glutenbevattende granen.'),
    (4,  'Lactosevrij',    'Geen lactose.'),
    (5,  'Notenvrij',      'Geen noten of sporen.'),
    (6,  'Zuivelvrij',     'Geen melkproducten.'),
    (7,  'Suikerarm',      'Weinig toegevoegde suikers.'),
    (8,  'Zoutarm',        'Weinig natrium.'),
    (9,  'Halal',          'Voldoet aan islamitische voorschriften.'),
    (10, 'Kosher',         'Voldoet aan joodse voorschriften.'),
    (11, 'Paleo',          'Geen granen, zuivel of bewerkt voedsel.'),
    (12, 'Keto',           'Laag in koolhydraten, hoog in vetten.'),
    (13, 'Raw food',       'Rauw of minimaal verhit.'),
    (14, 'Flexitarisch',   'Overwegend vegetarisch.');

insert into user_fridge (user_id, ingredient_name) values
    ('alice@example.com', 'Tomato'),
    ('alice@example.com', 'Spaghetti'),
    ('bob@example.com', 'Chicken Breast'),
    ('charlie@example.com', 'Broccoli'),
    ('diana@example.com', 'Avocado'),
    ('ethan@example.com', 'Rice'),
    ('fiona@example.com', 'Feta Cheese'),
    ('george@example.com', 'Beef'),
    ('hannah@example.com', 'Eggs'),
    ('julia@example.com', 'Salmon');

insert into user_favourites (user_id, recipe_id) values
    ('alice@example.com', 1),
    ('bob@example.com', 10),
    ('charlie@example.com', 2),
    ('diana@example.com', 4),
    ('ethan@example.com', 7),
    ('fiona@example.com', 6),
    ('george@example.com', 5),
    ('hannah@example.com', 9),
    ('ian@example.com', 3),
    ('julia@example.com', 8);

insert into user_allergens (user_id, allergen_id) values
    ('alice@example.com', 6),
    ('charlie@example.com', 1),
    ('diana@example.com', 8),
    ('george@example.com', 1),
    ('julia@example.com', 4);

insert into user_diets (user_id, diet_id) values
    ('alice@example.com', 2),
    ('charlie@example.com', 3),
    ('diana@example.com', 1),
    ('ethan@example.com', 12),
    ('fiona@example.com', 2),
    ('hannah@example.com', 3);

insert into recipe_diets (recipe_id, diet_id) values
    -- 1 Spaghetti Bolognese
    (1, 7), (1, 14), (1, 9), (1, 10),

    -- 2 Vegetable Stir Fry
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7),

    -- 3 Chicken Curry
    (3, 4), (3, 5), (3, 6), (3, 7), (3, 9), (3, 14),

    -- 4 Avocado Toast
    (4, 2), (4, 7), (4, 14), (4, 10),

    -- 5 Beef Tacos
    (5, 7), (5, 14), (5, 9), (5, 10),

    -- 6 Greek Salad
    (6, 2), (6, 7), (6, 14),

    -- 7 Pancakes
    (7, 7), (7, 14),

    -- 8 Sushi Rolls
    (8, 3), (8, 4), (8, 5), (8, 6), (8, 7), (8, 9), (8, 10),

    -- 9 Lentil Soup
    (9, 1), (9, 2), (9, 3), (9, 4), (9, 5), (9, 6), (9, 7),

    -- 10 Salmon Teriyaki
    (10, 4), (10, 5), (10, 6), (10, 7), (10, 9), (10, 10), (10, 14);


insert into ingredient_units (recipe_id, ingredient_name, amount, unittype) values
    -- 1 Spaghetti Bolognese
    (1, 'Tomato', 3, 'pieces'),
    (1, 'Ground Beef', 250, 'grams'),
    (1, 'Spaghetti', 200, 'grams'),

    -- 2 Stir Fry
    (2, 'Broccoli', 150, 'grams'),
    (2, 'Soy Sauce', 2, 'tbsp'),

    -- 3 Chicken Curry
    (3, 'Chicken Breast', 300, 'grams'),
    (3, 'Curry Powder', 2, 'tbsp'),
    (3, 'Rice', 200, 'grams'),

    -- 4 Avocado Toast
    (4, 'Avocado', 1, 'piece'),
    (4, 'Bread', 2, 'slices'),

    -- 5 Beef Tacos
    (5, 'Beef', 200, 'grams'),
    (5, 'Tortilla', 3, 'pieces'),

    -- 6 Greek Salad
    (6, 'Feta Cheese', 100, 'grams'),
    (6, 'Cucumber', 1, 'piece'),
    (6, 'Tomato', 2, 'pieces'),

    -- 7 Pancakes
    (7, 'Pancake Mix', 150, 'grams'),
    (7, 'Eggs', 2, 'pieces'),

    -- 8 Sushi Rolls
    (8, 'Rice', 200, 'grams'),
    (8, 'Salmon', 100, 'grams'),
    (8, 'Cucumber', 1, 'piece'),

    -- 9 Lentil Soup
    (9, 'Lentils', 200, 'grams'),
    (9, 'Carrot', 2, 'pieces'),
    (9, 'Tomato', 2, 'pieces'),

    -- 10 Salmon Teriyaki
    (10, 'Salmon', 200, 'grams'),
    (10, 'Teriyaki Sauce', 3, 'tbsp'),
    (10, 'Rice', 150, 'grams');

insert into ingredient_allergens (ingredient_name, allergen_id) values
    ('Spaghetti', 1),
    ('Bread', 1),
    ('Soy Sauce', 6),
    ('Feta Cheese', 7),
    ('Eggs', 3),
    ('Salmon', 4),
    ('Tortilla', 1),
    ('Pancake Mix', 1);