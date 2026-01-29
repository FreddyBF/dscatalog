-- V2__create_roles_table.sql
CREATE TABLE tb_role (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    authority VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

-- Tabela de associação User x Role (many-to-many)
CREATE TABLE tb_user_role (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES tb_role (id) ON DELETE CASCADE
);
