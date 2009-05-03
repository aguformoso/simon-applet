-- Database: simon

DROP DATABASE simon;

CREATE DATABASE simon;

CREATE TABLE results (
        	id      integer auto-increment primary key,
        	date_test   timestamp,
        	type      char(20),
        	ip_origin   inet,
        	ip_destination	 inet,
	 		number_probes	 integer,
			min_rtt	integer,
			max_rtt	integer,
			ave_rtt	integer,
			jitter	integer,
			loss_por	integer,
			country_origin	char(2),
			country_destination	char(2)
);