package com.abbas.notsee.MysqlTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseManager {
    private Logger logger = Logger.getLogger("NotSee");

    public void initialize() {
        try (Connection connection = MySQLConnection.getConnection()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS bans ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "player_name VARCHAR(16) NOT NULL,"
                    + "reason TEXT NOT NULL,"
                    + "banned_by VARCHAR(16) NOT NULL,"
                    + "ban_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
                statement.execute();
            }
        } catch (SQLException e) {
            logger.severe("Failed to initialize database: " + e.getMessage());
        }
    }
}
