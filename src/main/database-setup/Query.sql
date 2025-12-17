
-- Alle volledige recipe
SELECT * FROM recipes;

-- Easy recipes
SELECT * FROM recipes
    WHERE Difficulty = 'EASY';

-- Medium recipes
SELECT * FROM recipes
WHERE Difficulty = 'MEDIUM';

-- Hard Recipes
SELECT * FROM recipes
WHERE Difficulty = 'HARD';

/*Instruction needed filter
SELECT * FROM recipes
WHERE instructions != 'instructions needed';
*/

-- recipe search request
SELECT
    R.title,
    R.difficulty,
    R.mealtype,
    R.kitchenstyle,
    MAX(R.cookingtime) AS cookingtime,

    -- ::TEXT maakt diet_id van INT naar STRING.
    STRING_AGG(DISTINCT RD.diet_id::TEXT, ', ') AS diets,
    STRING_AGG(DISTINCT IU.ingredient_name, ', ') AS Ingredients,

    -- COALESCE = Als Allergen_ID NULL is > 'No Allergens' (Elvis Operator).
    COALESCE(
    STRING_AGG(DISTINCT RA.allergen_id::TEXT, ', '),
    'No Allergens' ) AS allergens

FROM recipes AS R
         LEFT JOIN recipe_allergens AS RA
                   ON R.recipe_id = RA.recipe_id
         LEFT JOIN recipe_diets AS RD
                   ON R.recipe_id = RD.recipe_id
         LEFT JOIN ingredient_units AS IU
                    ON RD.recipe_id = IU.recipe_id
GROUP BY
    R.title,
    R.difficulty,
    R.mealtype,
    R.kitchenstyle;

-- recipe searchresult + Match score
SELECT

R.recipe_id,
R.title,
R.description,
R.cookingTime,
RI.image_url


--R.Ingredients / (Ingredients Needed) AS Score

FROM recipe_images AS RI
LEFT JOIN recipes AS R
    ON RI.recipe_id = R.recipe_id

