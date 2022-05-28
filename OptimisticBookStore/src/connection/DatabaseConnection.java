package connection;
// import bumbu-bumbu yang diperlukan
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

// bisa juga diimpor sekaligus seperti ini:
//import java.sql.*;

public class DatabaseConnection {
    
    // Menyiapkan paramter JDBC untuk koneksi ke datbase
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/optimistic";
    static final String USER = "root";
    static final String PASS = "";
    
    // Menyiapkan objek yang diperlukan untuk mengelola database
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    static PreparedStatement preparedStatement;
    
    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            
            stmt = conn.createStatement();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public ResultSet executeQuery(String query) {
        ResultSet res = null;
        try {
            res=stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public void executeUpdate(String query) {
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}