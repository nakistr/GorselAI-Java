package gorselyapay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Gerekli olabilir
import java.sql.Timestamp; // logDocumentProcessing için gerekli
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt; // BCrypt Kütüphanesi

/**
 * YÖNETİCİ SINIFI: UserManager (Kullanıcı veritabanı işlemlerini yönetir)
 */
public class UserManager {
    
    private User currentUser;
    
    // Geçerli e-posta formatını kontrol etmek için regex
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    public UserManager() {
        // Constructor boş (Gerekli tüm işlemler metotlar içinde yapılıyor)
    }
    
    // ------------------------------------
    // --- TEMEL KULLANICI İŞLEMLERİ ---
    // ------------------------------------
    
    /**
     * Kullanıcı girişi yapar ve başarılıysa currentUser'ı ayarlar.
     * @param username Kullanıcı adı
     * @param password Şifre (düz metin)
     * @return Giriş başarılıysa true
     */
    public boolean login(String username, String password) {
        String sql = "SELECT id, username, password_hash, email, isVerified FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    
                    // Şifreyi BCrypt ile kontrol et
                    if (BCrypt.checkpw(password, storedHash)) {
                        currentUser = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getBoolean("isVerified")
                        );
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Yeni bir kullanıcı kaydeder.
     * @param username Kullanıcı adı
     * @param password Şifre (düz metin)
     * @param email E-posta adresi
     * @return Kayıt başarılıysa true
     */
    public boolean register(String username, String password, String email) {
        // Şifreyi hashle
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        
        // isVerified varsayılan olarak 0 (false)
        String sql = "INSERT INTO users (username, password_hash, email, isVerified) VALUES (?, ?, ?, 0)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, hashed);
            pstmt.setString(3, email);
            
            // Satır eklendi mi?
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kullanıcının e-posta doğrulamasını yapar.
     * @param username Kullanıcı adı
     */
    public void verifyUser(String username) {
        String sql = "UPDATE users SET isVerified = 1 WHERE username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            
            // Eğer o anki kullanıcı doğrulanıyorsa objeyi güncelle
            if (currentUser != null && currentUser.getUsername().equals(username)) {
                currentUser.setVerified(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // ------------------------------------
    // --- LOG İŞLEMLERİ ---
    // ------------------------------------
    
    /**
     * Belge işleme aktivitesini 'processed_documents' tablosuna kaydeder.
     */
    public void logDocumentProcessing(int userId, String fileName, String processType, String processedContent) {
        String sql = "INSERT INTO processed_documents (user_id, file_name, process_type, processed_content, process_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, fileName);
            pstmt.setString(3, processType);
            // Content'in NVARCHAR(MAX) olacağını varsayarak
            pstmt.setString(4, processedContent);
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // --- YARDIMCI METOTLAR ---
    // ------------------------------------
    
    /**
     * Kullanıcı adının veritabanında kayıtlı olup olmadığını kontrol eder.
     */
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { return rs.getInt(1) > 0; }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    /**
     * E-posta adresinin veritabanında kayıtlı olup olmadığını kontrol eder.
     */
    public boolean isEmailTaken(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { return rs.getInt(1) > 0; }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    /**
     * E-posta formatının geçerliliğini kontrol eder (Regex).
     */
    public boolean isValidEmail(String email) {
        if (email == null) { return false; }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Giriş yapmış kullanıcı objesini döndürür.
     */
    public User getCurrentUser() { return currentUser; }
    
    /**
     * Kullanıcının oturumunu kapatır.
     */
    public void logout() {
        currentUser = null;
    }
}