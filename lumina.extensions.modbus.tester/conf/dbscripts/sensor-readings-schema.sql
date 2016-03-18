DROP TABLE read_data_record;
DROP TABLE datapoint;
DROP TABLE binary_data_record;

CREATE TABLE datapoint (
    dpid text,
    uri text NOT NULL,
	descr text NOT NULL,
    PRIMARY KEY(dpid)
);


CREATE TABLE read_data_record (
	dpid text NOT NULL REFERENCES datapoint(dpid),			-- Data point ID
	ats bigint NOT NULL,						-- Acquisition timestamp
	rts bigint NOT NULL,						-- Read timestamp
	reading bigint NOT NULL,					-- Reading value or a pointer to a binary value (in the case of a binary datapoint)
	PRIMARY KEY(dpid,rts)
);

CREATE TABLE binary_data_record (
	bid bigint, 											-- BLOB ID
	data bytea,
    PRIMARY KEY(bid)	                       				-- BINARY
);

CREATE TABLE device (
	devid bigint,
	device_type  text,
	model text,
	description text,
	PRIMARY KEY(devid)
);

CREATE TABLE datapoint_metadata(
);