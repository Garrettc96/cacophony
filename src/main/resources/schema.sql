ALTER TABLE message ADD COLUMN IF NOT EXISTS fts_vector tsvector;

  -- Create a function to update the tsvector column
   CREATE OR REPLACE FUNCTION update_message_fts_vector() RETURNS trigger AS '
   BEGIN
       NEW.fts_vector :=
           to_tsvector(NEW.text);
       RETURN NEW;
   END;
   ' LANGUAGE plpgsql;

   -- Create a trigger to call the function before insert or update
   CREATE OR REPLACE TRIGGER tsvector_update BEFORE INSERT OR UPDATE
   ON message FOR EACH ROW EXECUTE FUNCTION update_message_fts_vector();

   -- Create a GIN index for performance
   CREATE INDEX IF NOT EXISTS message_fts_idx ON message USING gin(fts_vector);

   -- (Optional) Initialize the column for existing data
   UPDATE message SET fts_vector = to_tsvector(coalesce(text, ''));