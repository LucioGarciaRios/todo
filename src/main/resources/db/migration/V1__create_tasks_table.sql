CREATE TABLE tasks (
                       id INT PRIMARY KEY IDENTITY(1,1),
                       title NVARCHAR(255) NOT NULL,
                       description NVARCHAR(500),
                       created_at DATETIME DEFAULT GETDATE(),
                       status NVARCHAR(50) CHECK (status IN ('pendente', 'em andamento', 'concluida'))
);
