package gorselyapay;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import gorselyapay.ThemeManager.Language; 

public class EmailSender {

    // --- BURAYI KESİNLİKLE KENDİ BİLGİLERİNİZLE DEĞİŞTİRİN ---
    private static final String GMAIL_ADRESIN = "nakismuhammedali@gmail.com"; 
    private static final String UYGULAMA_SIFREN = "hqpoiuvtwtlifijo"; 
    // --- DEĞİŞTİRİLECEK ALAN SONU ---
    
    public boolean sendVerificationEmail(String kimeGidecekEmail, String dogrulamaKodu, Language language) {
        
        ThemeManager tempThemeManager = new ThemeManager();
        tempThemeManager.setLanguage(language);
        
        String emailSubject = tempThemeManager.getTranslation("EMAIL_SUBJECT");
        String emailBodyText = tempThemeManager.getTranslation("EMAIL_BODY_TEXT");
        String emailBodyInstruction = tempThemeManager.getTranslation("EMAIL_BODY_INSTRUCTION");
        
        // 1. Mail sunucu ayarları (Gmail için standart)
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.port", "587"); 
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); 
        
        // 2. Oturum (Session) oluşturma
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_ADRESIN, UYGULAMA_SIFREN);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(GMAIL_ADRESIN));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(kimeGidecekEmail));
            message.setSubject(emailSubject);
            
            String mailMetni = emailBodyText + " <br><br>"
                             + "<h1 style='color:blue;'>" + dogrulamaKodu + "</h1>"
                             + "<br>" + emailBodyInstruction;
            
            message.setContent(mailMetni, "text/html; charset=utf-8");

            Transport.send(message);
            return true; 

        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }
}