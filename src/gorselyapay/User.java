package gorselyapay;

// MODEL SINIFI: User
public class User {
    private int id; 
    private String username;
    private String email; 
    private boolean isVerified; 
    
    // BCrypt kullandığımız için şifre bilgisini burada tutmuyoruz
    public User(int id, String username, String email, boolean isVerified) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isVerified = isVerified; 
    }
    
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; } 
    public boolean isVerified() { return isVerified; } 
    public void setVerified(boolean verified) { this.isVerified = verified; } 
}