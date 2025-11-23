package gorselyapay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import gorselyapay.ThemeManager.Language;

public class LoginPanel extends JPanel {
	private static final long serialVersionUID = 1L;
    private ThemeManager themeManager;
    private JFrame frame;
    private UserManager userManager;
    
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel emailLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton themeButton;
    private JPanel centerPanel;
    private JPanel topPanel;
    private JComboBox<String> languageComboBox; 
    
    // Sadece TR ve EN
    private final String[] languageNames = {"TR", "EN"}; 
    
    private void updateTexts() {
        titleLabel.setText(themeManager.getTranslation("TITLE_APP"));
        usernameLabel.setText(themeManager.getTranslation("LABEL_USERNAME"));
        passwordLabel.setText(themeManager.getTranslation("LABEL_PASSWORD"));
        emailLabel.setText(themeManager.getTranslation("LABEL_EMAIL"));
        loginButton.setText(themeManager.getTranslation("BUTTON_LOGIN"));
        registerButton.setText(themeManager.getTranslation("BUTTON_REGISTER"));
        themeButton.setText(themeManager.isDarkTheme() ? themeManager.getTranslation("BUTTON_THEME_LIGHT") : themeManager.getTranslation("BUTTON_THEME_DARK"));
    }

	public LoginPanel(JFrame frame, ThemeManager themeManager, UserManager userManager) {
        this.themeManager = themeManager;
        this.frame = frame;
        this.userManager = userManager;
        
        setLayout(new BorderLayout());

        // --- 1. ÜST PANEL ---
        topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false); // Gradient görünsün
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        
        themeButton = new JButton();
        themeButton.setPreferredSize(new Dimension(120, 30));
        
        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftWrapper.setOpaque(false);
        leftWrapper.add(themeButton);
        topPanel.add(leftWrapper, BorderLayout.WEST);

        languageComboBox = new JComboBox<>(languageNames); 
        languageComboBox.setSelectedIndex(themeManager.getCurrentLanguage().ordinal()); 
        languageComboBox.setPreferredSize(new Dimension(80, 30));
        topPanel.add(languageComboBox, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // --- 2. MERKEZ PANEL (FORM) ---
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Gradient için
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Başlık
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; 
        centerPanel.add(titleLabel, gbc);

        // Kullanıcı Adı
        usernameLabel = new JLabel();
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0; centerPanel.add(usernameLabel, gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1; centerPanel.add(usernameField, gbc);

        // Şifre
        passwordLabel = new JLabel();
        gbc.gridy = 2; gbc.gridx = 0; centerPanel.add(passwordLabel, gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1; centerPanel.add(passwordField, gbc);

        // Email
        emailLabel = new JLabel();
        gbc.gridy = 3; gbc.gridx = 0; centerPanel.add(emailLabel, gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1; centerPanel.add(emailField, gbc);

        // Butonlar
        loginButton = new JButton();
        loginButton.setPreferredSize(new Dimension(120, 40));
        gbc.gridy = 4; gbc.gridx = 0; gbc.insets = new Insets(25, 10, 10, 10); 
        centerPanel.add(loginButton, gbc);

        registerButton = new JButton();
        registerButton.setPreferredSize(new Dimension(120, 40));
        gbc.gridx = 1; 
        centerPanel.add(registerButton, gbc);
        
        add(centerPanel, BorderLayout.CENTER);

        // --- LISTENERS ---
        languageComboBox.addActionListener(e -> {
            String selected = (String) languageComboBox.getSelectedItem();
            themeManager.setLanguage(selected.equals("TR") ? Language.TR : Language.EN);
            refreshTheme(); 
        });

        loginButton.addActionListener(e -> attemptLogin());
        registerButton.addActionListener(e -> attemptRegister());

        themeButton.addActionListener(e -> {
            themeManager.toggleTheme();
            refreshTheme(); 
        });
        
        refreshTheme();
    }
    
    private void refreshTheme() {
        updateTexts();
        themeManager.applyTheme(this); 
        
        // Gradient arka plan için şeffaflık ayarları
        topPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        titleLabel.setForeground(themeManager.isDarkTheme() ? themeManager.getDarkAccent() : themeManager.getLightAccent());
        
        Language currentLang = themeManager.getCurrentLanguage();
        if (languageComboBox.getSelectedIndex() != currentLang.ordinal()) {
             languageComboBox.setSelectedIndex(currentLang.ordinal());
        }
        
        frame.repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = themeManager.isDarkTheme() ? themeManager.getDarkBackground().darker() : themeManager.getLightBackground();
        Color color2 = themeManager.isDarkTheme() ? new Color(35, 35, 40) : new Color(230, 240, 250);
        GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
             JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_REQUIRED_FIELDS"), "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userManager.login(username, password)) {
            HomePanel homePanel = new HomePanel(frame, themeManager, userManager); 
            frame.setContentPane(homePanel);
            frame.revalidate();
            frame.repaint();
        } else {
            JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_LOGIN_ERROR"), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void attemptRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_REQUIRED_FIELDS"), "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!userManager.isValidEmail(email)) {
             JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_INVALID_EMAIL_FORMAT"), "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (userManager.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_TAKEN_USER"), "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (userManager.isEmailTaken(email)) {
            JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_TAKEN_EMAIL"), "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "Kod gönderiliyor...", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                });
                EmailSender sender = new EmailSender();
                return sender.sendVerificationEmail(email, verificationCode, themeManager.getCurrentLanguage());
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        String inputCode = JOptionPane.showInputDialog(frame, themeManager.getTranslation("MSG_CODE_INPUT_PROMPT"), "Doğrulama", JOptionPane.QUESTION_MESSAGE);
                        if (inputCode != null && inputCode.trim().equals(verificationCode)) {
                            if (userManager.register(username, password, email)) {
                                userManager.verifyUser(username);
                                JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_REG_SUCCESS"), "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                                usernameField.setText(""); passwordField.setText(""); emailField.setText("");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Veritabanı hatası", "Hata", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_INVALID_CODE"), "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_MAIL_ERROR"), "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        }.execute();
    }
}