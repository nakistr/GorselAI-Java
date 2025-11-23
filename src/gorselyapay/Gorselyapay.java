package gorselyapay;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Gorselyapay {
    public static void main(String[] args) {
        
        // KRİTİK ADIM: Veritabanını kurar ve tabloları oluşturur
        DatabaseManager.initializeDatabase(); 
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Görsel Yapay Zeka Uygulaması");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);

            ThemeManager themeManager = new ThemeManager();
            UserManager userManager = new UserManager(); 
            // Gereksiz nesneler (ToolManager, StatisticsManager) KALDIRILDI.
            
            // LoginPanel sadece gerekli olan 3 parametreyi alıyor.
            frame.setContentPane(new LoginPanel(frame, themeManager, userManager));
            frame.setVisible(true);
        });
    }
}