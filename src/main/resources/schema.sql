-- Create sequence only if it doesn't exist
CREATE SEQUENCE IF NOT EXISTS position_number_seq START 1;

-- Create function only if it doesn't exist
CREATE OR REPLACE FUNCTION set_position_number()
    RETURNS trigger AS $$
BEGIN
    IF NEW.position_number IS NULL THEN
        NEW.position_number := nextval('position_number_seq');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger only if it doesn't exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_trigger
                       WHERE tgname = 'position_number_trigger') THEN
            CREATE TRIGGER position_number_trigger
                BEFORE INSERT
                ON status
                FOR EACH ROW
            EXECUTE FUNCTION set_position_number();
        END IF;
    END
$$;
