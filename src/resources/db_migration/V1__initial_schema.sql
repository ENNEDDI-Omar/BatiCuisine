-- Drop existing tables and types to prevent conflicts during creation
DROP TABLE IF EXISTS labor CASCADE;
DROP TABLE IF EXISTS material CASCADE;
DROP TABLE IF EXISTS component CASCADE;
DROP TABLE IF EXISTS quotes CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS client CASCADE;

DROP TYPE IF EXISTS profit_margin_type;
DROP TYPE IF EXISTS project_status_type;
DROP TYPE IF EXISTS quality_coefficient_type;
DROP TYPE IF EXISTS labor_type;
DROP TYPE IF EXISTS productivity_level_type;

-- Create enumerated types
CREATE TYPE profit_margin_type AS ENUM ('22%', '32%');
CREATE TYPE project_status_type AS ENUM ('in progress', 'completed', 'cancelled');
CREATE TYPE quality_coefficient_type AS ENUM ('standard', 'premium');
CREATE TYPE labor_type AS ENUM ('worker', 'specialist');
CREATE TYPE productivity_level_type AS ENUM ('normal', 'high');

-- Create the Client table
CREATE TABLE client (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) UNIQUE NOT NULL,
                        address VARCHAR(255),
                        phone VARCHAR(20),
                        is_professional BOOLEAN DEFAULT false
);

-- Create the Project table
CREATE TABLE project (
                         id SERIAL PRIMARY KEY,
                         project_name VARCHAR(255) UNIQUE NOT NULL,
                         profit_margin profit_margin_type,
                         total_cost DOUBLE PRECISION NOT NULL CHECK ( total_cost >= 0 ),
                         project_status project_status_type,
                         client_id INTEGER NOT NULL,
                         FOREIGN KEY (client_id) REFERENCES client(id)
);

-- Create the Quotes table
CREATE TABLE quotes (
                        id SERIAL PRIMARY KEY,
                        project_id INTEGER NOT NULL,
                        estimated_amount DOUBLE PRECISION NOT NULL CHECK ( estimated_amount >= 0 ),
                        issue_date DATE NOT NULL,
                        expiration_date DATE NOT NULL,
                        is_accepted BOOLEAN,
                        FOREIGN KEY (project_id) REFERENCES project(id)
);

-- Create the generic Component table for inheritance
CREATE TABLE component (
                           id SERIAL PRIMARY KEY,
                           project_id INTEGER NOT NULL,
                           name VARCHAR(255) UNIQUE NOT NULL,
                           tax DOUBLE PRECISION,
                           transport_cost DOUBLE PRECISION NOT NULL CHECK ( transport_cost >= 0 ),
                           FOREIGN KEY (project_id) REFERENCES project(id)
);

-- Create the Material table inheriting from Component
CREATE TABLE material (
                          unit_price DOUBLE PRECISION NOT NULL CHECK ( unit_price >= 0 ),
                          quantity DOUBLE PRECISION NOT NULL CHECK ( quantity >= 0 ),
                          quality_coefficient quality_coefficient_type
) INHERITS (component);

-- Create the Labor table inheriting from Component
CREATE TABLE labor (
                       type labor_type,
                       work_hours DOUBLE PRECISION NOT NULL CHECK ( work_hours >= 0 ),
                       productivity_level productivity_level_type
) INHERITS (component);



