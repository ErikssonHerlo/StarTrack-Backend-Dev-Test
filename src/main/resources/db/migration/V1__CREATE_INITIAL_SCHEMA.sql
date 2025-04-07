
CREATE TABLE IF NOT EXISTS skin (
                                     id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    team TEXT,
    image TEXT,
    crates TEXT
    );

CREATE TABLE IF NOT EXISTS agent (
                                      id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    team TEXT,
    image TEXT
    );

CREATE TABLE IF NOT EXISTS crate (
                                      id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image TEXT
    );

CREATE TABLE IF NOT EXISTS key (
                                    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image TEXT,
    crates TEXT
    );
