-- Drop existing tables and types to prevent conflicts during creation
DROP TABLE IF EXISTS labors CASCADE;
DROP TABLE IF EXISTS materials CASCADE;
DROP TABLE IF EXISTS components CASCADE;
DROP TABLE IF EXISTS quotes CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS clients CASCADE;

DROP TYPE IF EXISTS profit_margin_type CASCADE;
DROP TYPE IF EXISTS project_status_type CASCADE;
DROP TYPE IF EXISTS component_type CASCADE;
DROP TYPE IF EXISTS quote_status_type CASCADE;
DROP TYPE IF EXISTS tax_rate_type CASCADE;
DROP TYPE IF EXISTS quality_coefficient_type CASCADE;
DROP TYPE IF EXISTS labor_type CASCADE;
DROP TYPE IF EXISTS productivity_level_type CASCADE;

-- Create enums types
CREATE TYPE profit_margin_type AS ENUM ('individual', 'company');
CREATE TYPE project_status_type AS ENUM ('created', 'started', 'in_progress', 'completed', 'cancelled');
CREATE TYPE component_type AS ENUM ('material', 'labor');
CREATE TYPE tax_rate_type AS ENUM ('material_tax_only', 'labor_tax_only', 'tax_combined');
CREATE TYPE quality_coefficient_type AS ENUM ('standard', 'premium');
CREATE TYPE labor_type AS ENUM ('worker', 'specialist');
CREATE TYPE productivity_level_type AS ENUM ('normal', 'high');
CREATE TYPE quote_status_type AS ENUM ('requested', 'accepted', 'refused', 'expired');

-- Create the Client table
CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         client_name VARCHAR(255) UNIQUE NOT NULL,
                         address VARCHAR(255),
                         phone VARCHAR(20),
                         is_professional BOOLEAN DEFAULT false
);

-- Create the Project table
CREATE TABLE projects (
                          id SERIAL PRIMARY KEY,
                          project_name VARCHAR(255) UNIQUE NOT NULL,
                          surface DOUBLE PRECISION NOT NULL CHECK ( surface > 0 ), --!!!
                          profit_margin profit_margin_type,
                          total_cost DOUBLE PRECISION NOT NULL CHECK ( total_cost >= 0 ),
                          project_status project_status_type NOT NULL default 'created',
                          client_id INTEGER NOT NULL,
                          FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

-- Create the Quotes table
CREATE TABLE quotes (
                        id SERIAL PRIMARY KEY,
                        project_id INTEGER NOT NULL,
                        estimated_amount DOUBLE PRECISION NOT NULL CHECK ( estimated_amount >= 0 ),
                        issue_date DATE NOT NULL,
                        expiration_date DATE,
                        quote_status quote_status_type NOT NULL DEFAULT 'requested',
                        FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE ,

                        CHECK (expiration_date > issue_date),
    --CHECK (issue_date >= current_date),
                        CHECK (expiration_date <= issue_date + interval '10 days')
);


CREATE TABLE components (
                            id SERIAL PRIMARY KEY,
                            project_id INTEGER NOT NULL,
                            component_type component_type,
                            name VARCHAR(255) UNIQUE NOT NULL,
                            tax tax_rate_type,
                            cost DOUBLE PRECISION NOT NULL CHECK ( cost >= 0 ), --!!!
                            transport_cost DOUBLE PRECISION NOT NULL CHECK ( transport_cost >= 0 ),
                            FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);


CREATE TABLE materials (
                           unit_price DOUBLE PRECISION NOT NULL CHECK ( unit_price >= 0 ),
                           quantity DOUBLE PRECISION NOT NULL CHECK ( quantity >= 0 ),
                           quality_coefficient quality_coefficient_type
) INHERITS (components);


CREATE TABLE labors (
                       type labor_type,
                       work_hours DOUBLE PRECISION NOT NULL CHECK ( work_hours >= 0 ),
                       productivity_level productivity_level_type
) INHERITS (components);


---------------------------------L'isertion des données :------------------------------------

-- Insert multiple Clients
INSERT INTO clients (client_name, address, phone, is_professional) VALUES
                                                                       ('Alice Smith', '456 Main St', '0123456780', false),
                                                                       ('Bob Johnson', '789 Side Ave', '0123456781', true),
                                                                       ('Charlie Lee', '321 Back Rd', '0123456782', false),
                                                                       ('Diana Prince', '123 Paradise Island', '0123456790', true),
                                                                       ('Clark Kent', '321 Daily Planet Ave', '0123456791', false);

-- Insert multiple Projects
INSERT INTO projects (project_name, profit_margin, total_cost, project_status, client_id, surface) VALUES
                                                                                                       ('Small Bathroom Project', 'company', 15000, 'in_progress', 2, 25),
                                                                                                       ('Large Kitchen Project', 'individual', 75000, 'completed', 3, 45),
                                                                                                       ('Outdoor BBQ Area', 'individual', 30000, 'cancelled', 1, 50),
                                                                                                       ('Office Renovation', 'company', 45000, 'in_progress', 5, 75),
                                                                                                       ('Small Condo Remodel', 'individual', 18000, 'completed', 3, 30);



-- Insert multiple Quotes
INSERT INTO quotes (project_id, estimated_amount, issue_date, expiration_date, quote_status) VALUES
                                                                                                 (2, 14000, '2024-01-01', NULL, 'requested'),
                                                                                                 (3, 73000, '2024-03-01', '2024-03-11', 'accepted'),
                                                                                                 (4, 29500, '2024-05-01', '2024-05-11', 'refused'),
                                                                                                 (5, 44000, '2024-07-01', '2024-07-11', 'requested');


-- Insert multiple Materials
INSERT INTO materials (project_id, name, unit_price, quantity, quality_coefficient, tax, transport_cost, cost) VALUES
                                                                                                                   (2, 'Tile Flooring', 50, 100, 'standard', 'material_tax_only', 25, (50 * 100 + 25)),
                                                                                                                   (3, 'Marble Countertop', 300, 3, 'premium', 'material_tax_only', 18, (300 * 3 + 18)),
                                                                                                                   (4, 'Outdoor Bricks', 20, 50, 'standard', 'material_tax_only', 22, (20 * 50 + 22));



-- Insert multiple Labors
INSERT INTO labors (project_id, name, type, work_hours, productivity_level, tax, transport_cost, cost) VALUES
                                                                                                          (2, 'Tiling Team', 'worker', 40, 'normal', 'labor_tax_only', 11, (40 * 20 + 11)),
                                                                                                          (3, 'Marble Installation Team', 'specialist', 50, 'high', 'labor_tax_only', 15, (50 * 30 + 15)),
                                                                                                          (4, 'Bricklaying Team', 'worker', 60, 'normal', 'labor_tax_only', 8, (60 * 20 + 8));




--------------------------------------Test des Requetes normal et d'autres avec des jointures pour verifier le bon fonctionnement :---------------------------------

--Récupérer tous les projets avec les informations des clients associés :
SELECT p.project_name, p.total_cost, p.project_status, client_name, c.is_professional
FROM projects p
         JOIN clients c ON p.client_id = c.id;

-- Calcul du coût total en incluant matériaux et main-d'œuvre avec le profit margin correctement appliqué.
SELECT p.project_name,
       SUM(m.unit_price * m.quantity) AS total_material_cost,
       SUM(l.work_hours * CASE WHEN l.productivity_level = 'high' THEN 100 ELSE 70 END) AS total_labor_cost,
       (SUM(m.unit_price * m.quantity) + SUM(l.work_hours * CASE WHEN l.productivity_level = 'high' THEN 100 ELSE 70 END)) * (1 + CASE WHEN p.profit_margin = 'company' THEN 0.32 ELSE 0.22 END) AS total_project_cost
FROM projects p
         JOIN components c ON p.id = c.project_id
         LEFT JOIN materials m ON c.id = m.id
         LEFT JOIN labors l ON c.id = l.id
GROUP BY p.project_name, p.profit_margin;


-- Assurez-vous que les dates soient traitées correctement selon votre logique métier.
SELECT q.*, p.project_name
FROM quotes q
         JOIN projects p ON q.project_id = p.id
WHERE q.quote_status NOT IN ('accepted') AND q.expiration_date < CURRENT_DATE;


-- Détails de l'utilisation des matériaux et de la main-d'œuvre pour un projet spécifique.
SELECT p.project_name, c.name AS component_name, c.tax, c.transport_cost,
       CASE WHEN m.id IS NOT NULL THEN 'Material' ELSE 'Labor' END AS component_type,
       COALESCE(m.unit_price * m.quantity, l.work_hours * CASE WHEN l.productivity_level = 'high' THEN 100 ELSE 70 END) AS cost
FROM projects p
         LEFT JOIN components c ON p.id = c.project_id
         LEFT JOIN materials m ON c.id = m.id
         LEFT JOIN labors l ON c.id = l.id
WHERE p.project_name = 'Office Renovation';








