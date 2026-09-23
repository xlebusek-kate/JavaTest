ALTER TABLE student ADD CONSTRAINT age_constraint CHECK (age >= 16 );
ALTER TABLE student ADD CONSTRAINT name_constraint_check check ( name!= 0 );
ALTER TABLE student add CONSTRAINT  name_constraint_unique unique(name);
ALTER TABLE faculty add CONSTRAINT  name_color_constraint unique (name, color);
ALTER TABLE student ALTER COLUMN name SET DEFAULT 20;