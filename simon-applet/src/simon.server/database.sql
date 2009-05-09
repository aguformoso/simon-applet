--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: simon
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET search_path = public, pg_catalog;

CREATE ROLE simon LOGIN
  ENCRYPTED PASSWORD 'md51afd06fdc7fec1a9c7662db4863e8615'
  NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;

--
-- Name: results_id_seq; Type: SEQUENCE; Schema: public; Owner: simon
--

CREATE SEQUENCE results_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.results_id_seq OWNER TO simon;

--
-- Name: results_id_seq; Type: SEQUENCE SET; Schema: public; Owner: simon
--

SELECT pg_catalog.setval('results_id_seq', 1, true);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: results; Type: TABLE; Schema: public; Owner: simon; Tablespace: 
--

CREATE TABLE results (
    id integer DEFAULT nextval('results_id_seq'::regclass) NOT NULL,
    date_test timestamp with time zone,
    version integer,
    ip_origin inet,
    ip_destination inet,
    testtype character varying(20),
    number_probes integer,
    min_rtt integer,
    max_rtt integer,
    ave_rtt integer,
    dev_rtt integer,
    median_rtt integer,
    packet_loss integer,
    country_origin character(2),
    country_destination character(2)
);


ALTER TABLE public.results OWNER TO simon;

--
-- Name: testpointid_seq; Type: SEQUENCE; Schema: public; Owner: simon
--

CREATE SEQUENCE testpointid_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.testpointid_seq OWNER TO simon;

--
-- Name: testpointid_seq; Type: SEQUENCE SET; Schema: public; Owner: simon
--

SELECT pg_catalog.setval('testpointid_seq', 1, false);


--
-- Name: test_points; Type: TABLE; Schema: public; Owner: simon; Tablespace: 
--

CREATE TABLE test_points (
    testpointid integer DEFAULT nextval('testpointid_seq'::regclass) NOT NULL,
    description text,
    testtype character varying(20),
    ip_address inet,
    country character varying(2),
    enabled boolean,
    date_created timestamp without time zone
);


ALTER TABLE public.test_points OWNER TO simon;


--
-- Data for Name: test_points; Type: TABLE DATA; Schema: public; Owner: simon
--

COPY test_points (testpointid, description, testtype, ip_address, country, enabled, date_created) FROM stdin;
\.


--
-- Name: results_pkey; Type: CONSTRAINT; Schema: public; Owner: simon; Tablespace: 
--

ALTER TABLE ONLY results
    ADD CONSTRAINT results_pkey PRIMARY KEY (id);


ALTER INDEX public.results_pkey OWNER TO simon;

--
-- Name: test_points_pkey; Type: CONSTRAINT; Schema: public; Owner: simon; Tablespace: 
--

ALTER TABLE ONLY test_points
    ADD CONSTRAINT test_points_pkey PRIMARY KEY (testpointid);


ALTER INDEX public.test_points_pkey OWNER TO simon;

--
-- Name: public; Type: ACL; Schema: -; Owner: simon
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM simon;
GRANT ALL ON SCHEMA public TO simon;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: results_id_seq; Type: ACL; Schema: public; Owner: simon
--

REVOKE ALL ON TABLE results_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE results_id_seq FROM simon;
GRANT SELECT,UPDATE ON TABLE results_id_seq TO simon;


--
-- Name: results; Type: ACL; Schema: public; Owner: simon
--

REVOKE ALL ON TABLE results FROM PUBLIC;
REVOKE ALL ON TABLE results FROM simon;
GRANT INSERT,SELECT,UPDATE,DELETE,REFERENCES,TRIGGER ON TABLE results TO simon;


--
-- Name: testpointid_seq; Type: ACL; Schema: public; Owner: simon
--

REVOKE ALL ON TABLE testpointid_seq FROM PUBLIC;
REVOKE ALL ON TABLE testpointid_seq FROM simon;
GRANT SELECT,UPDATE ON TABLE testpointid_seq TO simon;


--
-- Name: test_points; Type: ACL; Schema: public; Owner: simon
--

REVOKE ALL ON TABLE test_points FROM PUBLIC;
REVOKE ALL ON TABLE test_points FROM simon;
GRANT INSERT,SELECT,UPDATE,DELETE,REFERENCES,TRIGGER ON TABLE test_points TO simon;


--
-- PostgreSQL database dump complete
--



