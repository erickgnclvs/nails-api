-- Create studios table
CREATE TABLE studios (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    logo_url VARCHAR(255),
    address VARCHAR(255),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(255),
    business_hours JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create studio_providers table for managing provider associations
CREATE TABLE studio_providers (
    studio_id BIGINT NOT NULL REFERENCES studios(id),
    provider_id BIGINT NOT NULL REFERENCES profiles(id),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (studio_id, provider_id)
);

-- Add studio_id to nail_services table to support studio services
ALTER TABLE nail_services
    ADD COLUMN studio_id BIGINT REFERENCES studios(id),
    -- Ensure a service belongs to either a provider or a studio, not both
    ADD CONSTRAINT service_ownership_check 
    CHECK ((provider_id IS NOT NULL AND studio_id IS NULL) OR 
           (provider_id IS NULL AND studio_id IS NOT NULL));

-- Create indexes for better performance
CREATE INDEX idx_studio_providers_studio_id ON studio_providers(studio_id);
CREATE INDEX idx_studio_providers_provider_id ON studio_providers(provider_id);
CREATE INDEX idx_nail_services_studio_id ON nail_services(studio_id);
