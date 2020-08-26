--CREATE TABLE magic_eight_ball_languages(
--    id integer NOT NULL,
--    locale VARCHAR(8) NOT NULL,
--    name VARCHAR (24) NOT NULL,
--    PRIMARY KEY (id)
--) WITH (
--    OIDS = FALSE
--) TABLESPACE pg_default;


----------
CREATE TABLE magic_eight_ball_history(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id varchar(30),
    question VARCHAR(120) NOT NULL,
    truncated_question VARCHAR(120) NOT NULL,
    frequency integer NOT NULL,
    answer varchar(20) NOT NULL,
    sentiment varchar(20) NOT NULL,
    language_code VARCHAR(8) NOT NULL,
    created_date timestamp(6) not null
) TABLESPACE pg_default;