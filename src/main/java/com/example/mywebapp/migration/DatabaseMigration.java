package com.example.mywebapp.migration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Statement;

@Component
public class DatabaseMigration implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (var conn = dataSource.getConnection(); 
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS notes (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "title VARCHAR(255) NOT NULL, " +
                         "content TEXT NOT NULL, " +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            stmt.execute(sql);
            System.out.println("Database migration completed successfully");
        }
    }
}
