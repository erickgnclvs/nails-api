-- Create service categories table
CREATE TABLE service_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create nail services table
CREATE TABLE nail_services (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT NOT NULL REFERENCES profiles(id),
    category_id BIGINT REFERENCES service_categories(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    duration INTEGER NOT NULL, -- Duration in minutes
    price DECIMAL(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create service images table for multiple images per service
CREATE TABLE service_images (
    id BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL REFERENCES nail_services(id),
    image_url VARCHAR(255) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_nail_services_provider ON nail_services(provider_id);
CREATE INDEX idx_nail_services_category ON nail_services(category_id);
CREATE INDEX idx_service_images_service ON service_images(service_id);

-- Insert default service categories
INSERT INTO service_categories (name, description) VALUES
    ('Manicure', 'Basic to luxury nail care for hands'),
    ('Pedicure', 'Foot and toenail care services'),
    ('Nail Art', 'Decorative and artistic nail designs'),
    ('Nail Extensions', 'Acrylic, gel, and other nail extension services'),
    ('Nail Repair', 'Fixing broken or damaged nails');
