CREATE OR REPLACE FUNCTION gen_random_uuid7()
            RETURNS uuid AS $$
            DECLARE
                v_time timestamp with time zone = null;
                v_secs bigint;
                v_msec bigint;
                v_usec bigint;
                v_timestamp bigint;
                v_timestamp_hex varchar;
                v_rand bytea;
                v_rand_hex varchar;
                v_uuid uuid;
            BEGIN
                -- Get current timestamp
                v_time = clock_timestamp();
                v_secs = EXTRACT(EPOCH FROM v_time);
                v_msec = EXTRACT(MILLISECONDS FROM v_time);
                v_usec = EXTRACT(MICROSECONDS FROM v_time);
                
                -- Generate timestamp (first 48 bits)
                v_timestamp = (v_secs * 1000000 + v_usec) & ((1::bigint << 48) - 1);
                v_timestamp_hex = lpad(to_hex(v_timestamp), 12, '0');
                
                -- Generate random bytes for the rest
                v_rand = gen_random_bytes(10);
                v_rand_hex = encode(v_rand, 'hex');
                
                -- Construct UUID in PostgreSQL format: 8-4-4-4-12 hex digits
                v_uuid = (
                    substr(v_timestamp_hex, 1, 8) || '-' ||
                    substr(v_timestamp_hex, 9, 4) || '-' ||
                    '7' || substr(v_timestamp_hex, 13, 3) || '-' ||
                    substr(v_rand_hex, 1, 4) || '-' ||
                    substr(v_rand_hex, 5, 12)
                )::uuid;
                
                RETURN v_uuid;
            END;
            $$ LANGUAGE plpgsql;