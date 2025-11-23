package gorselyapay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.ParseContext;

public class DocumentProcessor {

    /**
     * Belirtilen dosya yolundan ham metni çıkarmak için Apache Tika kullanır.
     * @param filePath İşlenecek dosyanın yolu.
     * @return Dosyanın içeriğindeki ham metin veya hata durumunda null.
     */
    public String extractTextFromFile(String filePath) {
        // Tika ile büyük dosyaları işlerken max 10MB limit koyarız
        BodyContentHandler handler = new BodyContentHandler(10 * 1024 * 1024); 
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();

        try (FileInputStream stream = new FileInputStream(new File(filePath))) {
            
            parser.parse(stream, handler, metadata, context);
            
            // Metin temizleme: Tika bazen gereksiz yeni satır veya karakterler ekler.
            String rawText = handler.toString();
            // Birden fazla boşluğu tek bir boşluğa, birden fazla yeni satırı ise 2 yeni satıra indiririz.
            return rawText.replaceAll("\\s+", " ").replaceAll("(\\s*\\n\\s*){3,}", "\n\n").trim();
            
        } catch (org.apache.tika.exception.TikaException e) {
            // Tika'nın dosya formatını tanımama hatası
            System.err.println("Tika Hatası: Dosya formatı desteklenmiyor veya bozuk.");
            return null;
        } catch (IOException e) {
            // Dosya bulunamadı veya okunamadı
            System.err.println("G/Ç Hatası: Dosya okunamadı: " + e.getMessage());
            return null;
        } catch (Exception e) {
             e.printStackTrace();
             return null;
        }
    }

    /**
     * Ham metinden yapay zeka ile özet çıkarma simülasyonu.
     * Gerçek projede GPT/Gemini API çağrısı bu metotta olurdu.
     * @param text Ham metin.
     * @return Özetlenmiş metin (Demo).
     */
    public String summarizeText(String text) {
        if (text == null || text.length() < 100) {
            return "Özetlenecek yeterli içerik bulunamadı (En az 100 karakter gerekli).";
        }
        
        // Simülasyon: Metnin ilk 200 karakterini alıp sonuna özet işareti koyarız.
        String firstSentence = text.substring(0, Math.min(text.length(), 200));
        
        return "🤖 YAPAY ZEKA ÖZETİ (Demo)\n\n" + 
               "Bu özet, içeriğin temel konularını kapsamaktadır. Gerçek bir model, bu metni anlamlandırıp daha akıcı bir özet çıkaracaktır.\n\n" + 
               "Temel Fikir: " + firstSentence + "... [ve kalan özet metni buraya gelirdi].";
    }
}