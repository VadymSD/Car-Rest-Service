INSERT INTO cars (make, year_column, model, category)
SELECT make, year_column, model, category
FROM CSVREAD('classpath:/db/migration/file.csv');


