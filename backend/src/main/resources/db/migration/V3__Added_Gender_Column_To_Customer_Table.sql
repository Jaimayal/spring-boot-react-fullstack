ALTER TABLE customer
ADD COLUMN gender TEXT CHECK (gender IN ('male', 'female'));