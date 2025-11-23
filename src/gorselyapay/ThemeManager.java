package gorselyapay;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Collections;
import java.util.EnumMap; 
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory; 
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent; 
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.Border; 
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.JTextComponent; 

public class ThemeManager {
    private boolean isDarkTheme = false;

    // 1. Dilleri Tanımlayan Enum
    public enum Language {
        TR, EN 
    }

    private Language currentLanguage = Language.TR; 
    private final Map<Language, Map<String, String>> translations;

    // 2. MODERN RENK PALETİ
    
    // KARANLIK TEMA
    private Color darkBg = new Color(18, 18, 18);          
    private Color darkFg = new Color(230, 230, 240);       
    private Color darkBtnBg = new Color(0, 150, 120);      
    private Color darkBtnFg = Color.WHITE;                  
    private Color darkAccent = new Color(0, 255, 150);     
    private Color darkListSelectionBg = new Color(0, 100, 70); 
    private Color darkInputBg = new Color(30, 30, 40);     

    // AYDINLIK TEMA
    private Color lightBg = new Color(250, 250, 250);      
    private Color lightFg = new Color(30, 30, 30);         
    private Color lightBtnBg = new Color(0, 180, 180);     
    private Color lightBtnFg = Color.WHITE;                
    private Color lightAccent = new Color(0, 150, 150);    
    private Color lightListSelectionBg = new Color(180, 230, 230); 
    private Color lightInputBg = Color.WHITE;              
    
    // Modern Kenarlıklar
    private final Border modernLightBorder = BorderFactory.createLineBorder(new Color(200, 200, 205), 1);
    private final Border modernDarkBorder = BorderFactory.createLineBorder(new Color(60, 60, 65), 1);
    
    private static final Font COMMON_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);


    public ThemeManager() {
        this.translations = createTranslationMap();
    }

    private Map<Language, Map<String, String>> createTranslationMap() {
         Map<Language, Map<String, String>> map = new EnumMap<>(Language.class);

        // --- TR Çevirileri ---
        Map<String, String> tr = new HashMap<>();
        tr.put("TITLE_MAIN_APP", "Görsel Yapay Zeka Uygulaması");
        tr.put("LABEL_USERNAME", "Kullanıcı Adı");
        tr.put("LABEL_PASSWORD", "Şifre");
        tr.put("LABEL_EMAIL", "E-Posta");
        tr.put("BUTTON_LOGIN", "Giriş Yap");
        tr.put("BUTTON_REGISTER", "Kaydol");
        tr.put("BUTTON_BACK", "Geri");
        tr.put("BUTTON_THEME_DARK", "Koyu Tema");
        tr.put("BUTTON_THEME_LIGHT", "Aydınlık Tema");
        tr.put("BUTTON_LOGOUT", "Çıkış Yap");
        tr.put("BUTTON_PROCESS_DOC", "Belge İşle");
        tr.put("BUTTON_SELECT_FILE", "Dosya Seç");
        tr.put("BUTTON_RUN_PROCESS", "İşlemi Başlat");
        
        tr.put("TITLE_APP", "Görsel Yapay Zeka Uygulaması");
        tr.put("MSG_LOGIN_SUCCESS", "Giriş Başarılı!");
        tr.put("MSG_LOGIN_ERROR", "Kullanıcı adı veya şifre yanlış.");
        tr.put("MSG_REG_SUCCESS", "Kayıt Başarılı! E-posta doğrulama kodu gönderildi.");
        tr.put("MSG_REG_ERROR_TITLE", "Kayıt Başarısız");
        tr.put("MSG_TAKEN_USER", "Bu kullanıcı adı zaten alınmış.");
        tr.put("MSG_TAKEN_EMAIL", "Bu e-posta adresi zaten kullanılıyor.");
        tr.put("MSG_INVALID_CODE", "Geçersiz doğrulama kodu.");
        tr.put("MSG_MAIL_ERROR", "Doğrulama maili gönderilirken bir hata oluştu.");
        tr.put("MSG_MAIL_ERROR_TITLE", "Mail Hatası");
        tr.put("TITLE_VERIFICATION", "E-Posta Doğrulama");
        tr.put("LABEL_VERIFICATION_CODE", "Doğrulama Kodu");
        tr.put("BUTTON_VERIFY", "Doğrula");
        tr.put("LABEL_WELCOME", "Hoş Geldin");
        tr.put("LABEL_CATEGORY", "İşlem Türü");
        tr.put("CAT_TEXT_TO_PDF", "Metin Çıkarma (TXT/PDF)");
        tr.put("CAT_PDF_SUMMARY", "PDF Özetleme (Demo)");
        tr.put("LABEL_FILE_INFO", "Seçili Dosya: Yok");
        tr.put("RESULT_AREA_DEFAULT", "İşlem sonucu burada görüntülenecektir...");
        tr.put("MSG_FILE_REQUIRED", "Lütfen bir dosya seçin.");
        tr.put("MSG_FILE_SELECT_ERROR", "Dosya seçimi iptal edildi.");
        tr.put("MSG_PROCESSING", "İşleniyor...");
        tr.put("LOADING_PANEL", "Başlatılıyor...");
        tr.put("EMAIL_SUBJECT", "Görsel Yapay Zeka Uygulaması - Doğrulama Kodunuz");
        tr.put("EMAIL_BODY_TEXT", "Doğrulama kodunuz:");
        tr.put("EMAIL_BODY_INSTRUCTION", "Kod 10 dakika geçerlidir.");
        
        tr.put("LABEL_EMAIL_REG", "E-Posta");
        tr.put("MSG_REQUIRED_FIELDS", "Tüm alanları doldurunuz.");
        tr.put("MSG_LOGIN_ERROR_TITLE", "Giriş Hatası");
        tr.put("MSG_INVALID_LOGIN", "Geçersiz kullanıcı adı veya şifre.");
        tr.put("MSG_INVALID_EMAIL_FORMAT", "Geçersiz E-posta formatı.");
        tr.put("MSG_USERNAME_TAKEN", "Kullanıcı adı kullanımda.");
        tr.put("MSG_EMAIL_TAKEN", "E-posta kullanımda.");
        tr.put("MSG_SENDING_MAIL", "Mail gönderiliyor...");
        tr.put("MSG_MAIL_SENDING_TITLE", "Mail Gönderimi");
        tr.put("MSG_CODE_INPUT_PROMPT", "Doğrulama Kodunu Giriniz:");
        tr.put("MSG_CODE_INPUT_TITLE", "Doğrulama");
        tr.put("MSG_REG_SUCCESS_TITLE", "Kayıt Başarılı");
        tr.put("MSG_DB_ERROR", "Veritabanı hatası.");

        map.put(Language.TR, tr);
        
        // --- EN Çevirileri ---
        Map<String, String> en = new HashMap<>();
        en.put("TITLE_MAIN_APP", "Visual AI Application");
        en.put("LABEL_USERNAME", "Username");
        en.put("LABEL_PASSWORD", "Password");
        en.put("LABEL_EMAIL", "Email");
        en.put("BUTTON_LOGIN", "Login");
        en.put("BUTTON_REGISTER", "Register");
        en.put("BUTTON_THEME_DARK", "Dark Theme");
        en.put("BUTTON_THEME_LIGHT", "Light Theme");
        en.put("BUTTON_LOGOUT", "Logout");
        en.put("BUTTON_BACK", "Back");
        en.put("BUTTON_PROCESS_DOC", "Process Document");
        en.put("BUTTON_SELECT_FILE", "Select File");
        en.put("BUTTON_RUN_PROCESS", "Start Process");
        en.put("TITLE_APP", "Visual AI Application");
        en.put("MSG_LOGIN_SUCCESS", "Login Successful!");
        en.put("MSG_LOGIN_ERROR", "Incorrect username or password.");
        en.put("MSG_REG_SUCCESS", "Registration Successful! Code sent.");
        en.put("MSG_REG_ERROR_TITLE", "Registration Failed");
        en.put("MSG_TAKEN_USER", "Username taken.");
        en.put("MSG_TAKEN_EMAIL", "Email taken.");
        en.put("MSG_INVALID_CODE", "Invalid code.");
        en.put("MSG_MAIL_ERROR", "Mail error.");
        en.put("MSG_MAIL_ERROR_TITLE", "Email Error");
        en.put("TITLE_VERIFICATION", "Email Verification");
        en.put("LABEL_VERIFICATION_CODE", "Verification Code");
        en.put("BUTTON_VERIFY", "Verify");
        en.put("LABEL_WELCOME", "Welcome");
        en.put("LABEL_CATEGORY", "Process Type");
        en.put("CAT_TEXT_TO_PDF", "Text Extraction");
        en.put("CAT_PDF_SUMMARY", "PDF Summarization");
        en.put("LABEL_FILE_INFO", "Selected File: None");
        en.put("RESULT_AREA_DEFAULT", "Result will be displayed here...");
        en.put("MSG_FILE_REQUIRED", "Please select a file.");
        en.put("MSG_FILE_SELECT_ERROR", "Selection canceled.");
        en.put("MSG_PROCESSING", "Processing...");
        en.put("LOADING_PANEL", "Initializing...");
        en.put("EMAIL_SUBJECT", "Verification Code");
        en.put("EMAIL_BODY_TEXT", "Your code:");
        en.put("EMAIL_BODY_INSTRUCTION", "Valid for 10 mins.");
        
        en.put("LABEL_EMAIL_REG", "Email");
        en.put("MSG_REQUIRED_FIELDS", "Fill all fields.");
        en.put("MSG_LOGIN_ERROR_TITLE", "Login Error");
        en.put("MSG_INVALID_LOGIN", "Invalid credentials.");
        en.put("MSG_INVALID_EMAIL_FORMAT", "Invalid email format.");
        en.put("MSG_USERNAME_TAKEN", "Username taken.");
        en.put("MSG_EMAIL_TAKEN", "Email taken.");
        en.put("MSG_SENDING_MAIL", "Sending mail...");
        en.put("MSG_MAIL_SENDING_TITLE", "Sending Mail");
        en.put("MSG_CODE_INPUT_PROMPT", "Enter Code:");
        en.put("MSG_CODE_INPUT_TITLE", "Verification");
        en.put("MSG_REG_SUCCESS_TITLE", "Registration Successful");
        en.put("MSG_DB_ERROR", "Database error.");

        map.put(Language.EN, en);

        return Collections.unmodifiableMap(map); 
    }

    public String getTranslation(String key) {
        Map<String, String> langMap = translations.get(currentLanguage);
        if (langMap == null) {
             langMap = translations.get(Language.TR);
        }
        String translation = langMap.get(key);
        if (translation == null) {
            return key;
        }
        return translation;
    }

    public void applyTheme(JComponent component) {
        Color bgColor = isDarkTheme ? darkBg : lightBg;
        Color fgColor = isDarkTheme ? darkFg : lightFg; 
        Color btnBgColor = isDarkTheme ? darkBtnBg : lightBtnBg; 
        Color btnFgColor = isDarkTheme ? darkBtnFg : lightBtnFg; 
        Color inputBgColor = isDarkTheme ? darkInputBg : lightInputBg;
        Color accentColor = isDarkTheme ? darkAccent : lightAccent;
        
        Color listSelBgColor = isDarkTheme ? darkListSelectionBg : lightListSelectionBg;
        Color listSelFgColor = Color.WHITE;
        
        Border activeBorder = isDarkTheme ? modernDarkBorder : modernLightBorder;

        component.setBackground(bgColor);
        component.setForeground(fgColor);
        component.setFont(COMMON_FONT);

        for (Component comp : component.getComponents()) {
            // KRİTİK DÜZELTME: Sadece JComponent olanları işle ve recursive çağır.
            if (comp instanceof JComponent) {
                JComponent jc = (JComponent) comp;
                
                if (jc instanceof JButton) {
                    JButton button = (JButton) jc;
                    button.setBackground(btnBgColor);
                    button.setForeground(btnFgColor);
                    button.setFont(BUTTON_FONT);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accentColor, 1),
                        BorderFactory.createEmptyBorder(8, 15, 8, 15)
                    ));
                } else if (jc instanceof JTextComponent) {
                    JTextComponent textComponent = (JTextComponent) jc;
                    textComponent.setBackground(inputBgColor);
                    textComponent.setForeground(fgColor);
                    textComponent.setCaretColor(fgColor);
                    textComponent.setFont(COMMON_FONT);
                    textComponent.setBorder(BorderFactory.createCompoundBorder(
                        activeBorder,
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                } else if (jc instanceof JLabel) {
                    jc.setForeground(fgColor);
                } else if (jc instanceof JScrollPane) {
                    jc.setBackground(bgColor);
                    jc.setForeground(fgColor);
                    JScrollPane scrollPane = (JScrollPane) jc;
                    scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
                    
                    JViewport view = scrollPane.getViewport();
                    if (view != null) {
                        view.setBackground(bgColor);
                        if (view.getView() instanceof JComponent) {
                             ((JComponent)view.getView()).setBackground(inputBgColor);
                             ((JComponent)view.getView()).setForeground(fgColor);
                        }
                    }
                } else if (jc instanceof JList) {
                    JList<?> list = (JList<?>) jc;
                    list.setBackground(inputBgColor);
                    list.setForeground(fgColor);
                    list.setSelectionBackground(listSelBgColor);
                    list.setSelectionForeground(listSelFgColor);
                } else if (jc instanceof JProgressBar) {
                    JProgressBar progressBar = (JProgressBar) jc;
                    progressBar.setBackground(isDarkTheme ? darkInputBg : lightBtnBg); 
                    progressBar.setForeground(accentColor);
                    progressBar.setBorderPainted(false); 
                } else if (jc instanceof JComboBox) {
                    JComboBox<?> comboBox = (JComboBox<?>) jc;
                    comboBox.setBackground(inputBgColor);
                    comboBox.setForeground(fgColor);
                    comboBox.setFont(COMMON_FONT);
                    comboBox.setBorder(activeBorder);
                    
                     try {
                         for (Component compItem : comboBox.getComponents()) {
                             if (compItem instanceof BasicArrowButton) {
                                 compItem.setBackground(btnBgColor); 
                             }
                         }
                     } catch (Exception ignore) {}
                }
                
                // KRİTİK DÜZELTME: Recursive çağrıyı sadece JComponent ise yap!
                // Bu sayede CellRendererPane gibi JComponent olmayan container'lara çarpıp hata vermez.
                applyTheme(jc);
            }
        }
    }

    public void applyTheme(LoginPanel panel) {
        applyTheme((JPanel) panel); 
    }
    
    public Color getDarkAccent() { return darkAccent; }
    public Color getLightAccent() { return lightAccent; }
    
    public Color getDarkBackground() { return darkBg; }
    public Color getLightBackground() { return lightBg; }

    public Language getCurrentLanguage() { return currentLanguage; }
    public void setLanguage(Language newLanguage) { this.currentLanguage = newLanguage; }
    public boolean isDarkTheme() { return isDarkTheme; }
    public void toggleTheme() { isDarkTheme = !isDarkTheme; }
}