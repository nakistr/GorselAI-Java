package gorselyapay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HomePanel extends JPanel {
	private static final long serialVersionUID = 1L;
    private ThemeManager themeManager;
    private UserManager userManager;
    private JFrame frame;
    
    // Global Bileşenler
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JButton themeButton;
    private JButton processButton; 
    private JPanel topPanel;
    private JPanel buttonPanel;
    private JPanel centerContent;
    private JLabel infoLabel;

    // Metin Güncelleme
    private void updateTexts() {
        String username = userManager.getCurrentUser() != null ? userManager.getCurrentUser().getUsername() : "User";
        welcomeLabel.setText(themeManager.getTranslation("LABEL_WELCOME") + ", " + username);
        
        logoutButton.setText(themeManager.getTranslation("BUTTON_LOGOUT"));
        processButton.setText(themeManager.getTranslation("BUTTON_PROCESS_DOC"));
        themeButton.setText(themeManager.isDarkTheme() ? themeManager.getTranslation("BUTTON_THEME_LIGHT") : themeManager.getTranslation("BUTTON_THEME_DARK"));
        infoLabel.setText(themeManager.getTranslation("RESULT_AREA_DEFAULT"));
    }

    public HomePanel(JFrame frame, ThemeManager themeManager, UserManager userManager) {
        this.frame = frame;
        this.themeManager = themeManager;
        this.userManager = userManager;
        
        setLayout(new BorderLayout());

        // --- 1. ÜST PANEL ---
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        welcomeLabel = new JLabel("", SwingConstants.LEFT); 
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        // --- 2. BUTON PANELİ (Sağ Üst) ---
        buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Yan yana 3 buton
        
        processButton = new JButton(); // Belge İşle
        processButton.setPreferredSize(new Dimension(120, 40));
        
        themeButton = new JButton(); // Tema
        logoutButton = new JButton(); // Çıkış

        buttonPanel.add(processButton); 
        buttonPanel.add(themeButton);
        buttonPanel.add(logoutButton);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- 3. MERKEZ İÇERİK (Modern Kart Görünümü) ---
        centerContent = new JPanel(new GridBagLayout());
        
        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        centerContent.add(infoLabel, new GridBagConstraints());
        add(centerContent, BorderLayout.CENTER);

        // --- LISTENERS ---
        processButton.addActionListener(e -> {
            frame.setContentPane(new DocumentPanel(frame, this, themeManager, userManager));
            frame.revalidate();
            frame.repaint();
        });
        
        themeButton.addActionListener(e -> {
            themeManager.toggleTheme();
            refreshTheme(); 
        });

        logoutButton.addActionListener(e -> {
            userManager.logout();
            frame.setContentPane(new LoginPanel(frame, themeManager, userManager));
            frame.revalidate();
            frame.repaint();
        });

        refreshTheme();
    }
    
    private void refreshTheme() {
        updateTexts();
        
        // Tüm paneli boya
        themeManager.applyTheme(this);
        
        // Özel renklendirmeler
        if (welcomeLabel != null) {
            welcomeLabel.setForeground(themeManager.isDarkTheme() ? themeManager.getDarkAccent() : themeManager.getLightAccent());
        }
        
        // Alt panelleri manuel güncellemeye gerek yok, applyTheme recursive çalışıyor ama emin olmak için:
        topPanel.setBackground(themeManager.isDarkTheme() ? themeManager.getDarkBackground() : themeManager.getLightBackground());
        buttonPanel.setBackground(themeManager.isDarkTheme() ? themeManager.getDarkBackground() : themeManager.getLightBackground());
        centerContent.setBackground(themeManager.isDarkTheme() ? themeManager.getDarkBackground() : themeManager.getLightBackground());

        frame.revalidate();
        frame.repaint();
    }
}