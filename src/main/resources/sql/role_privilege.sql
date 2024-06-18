CREATE TABLE IF NOT EXISTS "role_privilege"(
    role_id INTEGER,
    privilege_id INTEGER,
    PRIMARY KEY (role_id,privilege_id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (privilege_id) REFERENCES privilege(id)
);