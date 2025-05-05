-- Create sequence
CREATE SEQUENCE IF NOT EXISTS position_number_seq START 1;

-- Create function
CREATE OR REPLACE FUNCTION set_position_number()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.position_number IS NULL THEN
        NEW.position_number := nextval('position_number_seq');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger
CREATE TRIGGER IF NOT EXISTS position_number_trigger
    BEFORE INSERT ON status
    FOR EACH ROW
EXECUTE FUNCTION set_position_number();