CREATE TABLE magic_eight_ball_languages(
    locale VARCHAR(8) NOT NULL,
    name VARCHAR (24) NOT NULL
) WITH (
    OIDS = FALSE
) TABLESPACE pg_default;


----------
CREATE TABLE magic_eight_ball_history(
    id integer NOT NULL,
    question VARCHAR(120) NOT NULL,
    frequency integer NOT NULL,
    language_code VARCHAR(8) NOT NULL,
    created_date timestamp(6) not null,
    PRIMARY KEY (id),
	FOREIGN KEY (language_code) REFERENCES magic_eight_ball_languages (locale)
) WITH (
    OIDS = FALSE
) TABLESPACE pg_default;