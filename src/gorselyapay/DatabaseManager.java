package gorselyapay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseManager {
 

    private static final String SERVER_URL = "jdbc:sqlserver://NAKıS\\SQLEXPRESS;trustServerCertificate=true"; 
    private static final String DB_NAME = "GorselYapayDB"; 
    private static final String DB_USER = "GorselYapayUser"; 
    private static final String DB_PASS = "password";
    
    // Bağlantı URL'leri
    private static final String DATABASE_URL = SERVER_URL + ";databaseName=" + DB_NAME + ";user=" + DB_USER + ";password=" + DB_PASS;
    private static final String MASTER_URL = SERVER_URL + ";databaseName=master;user=" + DB_USER + ";password=" + DB_PASS;


    public static void initializeDatabase() {
        
  
        try (Connection conn = DriverManager.getConnection(MASTER_URL)) {
             Statement stmt = conn.createStatement();
             String sqlCreateDB = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = '" + DB_NAME + "') " +
                                  "CREATE DATABASE [" + DB_NAME + "];";
             stmt.executeUpdate(sqlCreateDB);
             
        } catch (SQLException e) {
             e.printStackTrace();
             JOptionPane.showMessageDialog(null, "KRİTİK HATA: SQL Server sunucusuna bağlanılamadı veya DB oluşturma izni yok.\n" +
                                                 "Hata: " + e.getMessage(), "Veritabanı Başlatma Hatası", JOptionPane.ERROR_MESSAGE);
             System.exit(1); 
        }
        
    
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement()) {
            
            
             String sqlUsers = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U') " +
                              "CREATE TABLE users (" +
                              " id INT PRIMARY KEY IDENTITY(1,1)," +
                              " username NVARCHAR(100) NOT NULL UNIQUE," +
                              " password_hash NVARCHAR(255) NOT NULL," + // Güvenli Hash
                              " email NVARCHAR(100) NOT NULL UNIQUE," +
                              " isVerified BIT NOT NULL DEFAULT 0," +
                              " register_date DATETIME NOT NULL DEFAULT GETDATE()" +
                              ");";
             
            
             String sqlDocuments = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='processed_documents' AND xtype='U') " +
                                   "CREATE TABLE processed_documents (" +
                                   " id INT PRIMARY KEY IDENTITY(1,1)," +
                                   " user_id INT FOREIGN KEY REFERENCES users(id) ON DELETE CASCADE," +
                                   " file_name NVARCHAR(255) NOT NULL," +
                                   " process_type NVARCHAR(50) NOT NULL," + 
                                   " processed_content NVARCHAR(MAX)," + 
                                   " process_date DATETIME NOT NULL DEFAULT GETDATE()" +
                                   ");";

             stmt.execute(sqlUsers);
             stmt.execute(sqlDocuments);
             
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Tablo Oluşturma Hatası: " + e.getMessage());
        }
    }
 
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}