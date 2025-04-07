-- V1__create_initial_schema.sql

-- Tabla para Skins
CREATE TABLE IF NOT EXISTS skins (
                                     id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    team VARCHAR(255),
    image TEXT,
    crates TEXT
    );

-- Tabla para Agents
CREATE TABLE IF NOT EXISTS agents (
                                      id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    team VARCHAR(255),
    image TEXT,
    crates TEXT
    );

-- Tabla para Crates
CREATE TABLE IF NOT EXISTS crates (
                                      id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image TEXT
    );

-- Tabla para Keys
CREATE TABLE IF NOT EXISTS keys (
                                    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    team VARCHAR(255),
    image TEXT,
    crates TEXT
    );
