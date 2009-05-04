DROP ROLE simon;

CREATE ROLE simon LOGIN
  ENCRYPTED PASSWORD 'md570df7ab0acf02a3fa119cb4bca37b868'
  NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;	

DROP DATABASE simon;

CREATE DATABASE simon WITH OWNER simon;

DROP TABLE results;

DROP SEQUENCE results_id_seq;

CREATE SEQUENCE results_id_seq;

CREATE TABLE results (
        	id integer unique primary key,
        	date_test   timestamp,
        	version    integer,
        	ip_origin    inet,
        	ip_destination   inet,
        	testtype	 varchar(20),
        	number_probes   integer,
        	min_rtt	 integer,
        	max_rtt	 integer,
        	ave_rtt	 integer,
        	dev_rtt	 integer,
        	packet_loss 	integer,
        	country_origin	  char(2),
        	country_destination	  char(2)
);

ALTER TABLE results
    ALTER COLUMN id 
        SET DEFAULT NEXTVAL('results_id_seq');
        
DROP TABLE test_points;

DROP SEQUENCE testpointid_seq;
        
CREATE SEQUENCE testpointid_seq;

CREATE TABLE test_points (
      	testpointid integer unique primary key,
      	description text,
      	testtype    varchar(20),
      	ip_address	 inet,
      	country	  varchar(2),
      	enabled   boolean,
      	date_created    timestamp
);

ALTER TABLE test_points
    ALTER COLUMN testpointid
        SET DEFAULT NEXTVAL('testpointid_seq');

GRANT ALL PRIVILEGES on results to simon;
GRANT ALL PRIVILEGES on test_points to simon;