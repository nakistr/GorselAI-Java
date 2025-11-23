package gorselyapay;

import java.awt.BorderLayout;
import java.awt.Color; // EKSİK OLAN IMPORT BU SATIRDI
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class DocumentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
    private ThemeManager themeManager;
    private UserManager userManager;
    private JFrame frame;
    private HomePanel homePanel; 
    
    private JPanel controlPanel;
    private JLabel categoryLabel;
    private JComboBox<String> categoryComboBox;
    private JButton selectFileButton;
    private JButton processButton;
    private JTextArea resultArea;
    private JButton backButton;
    
    private String selectedFilePath; 
    private final String[] categoryKeys = {"CAT_TEXT_TO_PDF", "CAT_PDF_SUMMARY"};

    private void updateTexts() {
        categoryLabel.setText(themeManager.getTranslation("LABEL_CATEGORY"));
        
        // Seçimi kaybetmeden combobox'ı güncelle
        int selectedIndex = categoryComboBox.getSelectedIndex();
        categoryComboBox.removeAllItems();
        categoryComboBox.addItem(themeManager.getTranslation(categoryKeys[0]));
        categoryComboBox.addItem(themeManager.getTranslation(categoryKeys[1]));
        if(selectedIndex != -1) categoryComboBox.setSelectedIndex(selectedIndex);
        
        String fileBtnText = selectedFilePath == null ? themeManager.getTranslation("BUTTON_SELECT_FILE") : new File(selectedFilePath).getName();
        selectFileButton.setText(fileBtnText);
        
        processButton.setText(themeManager.getTranslation("BUTTON_RUN_PROCESS"));
        backButton.setText(themeManager.getTranslation("BUTTON_BACK"));
        
        if (resultArea.getText().isEmpty() || resultArea.getText().equals("...")) {
             resultArea.setText(themeManager.getTranslation("RESULT_AREA_DEFAULT"));
        }
    }

    public DocumentPanel(JFrame frame, HomePanel homePanel, ThemeManager themeManager, UserManager userManager) {
        this.frame = frame;
        this.homePanel = homePanel;
        this.themeManager = themeManager;
        this.userManager = userManager;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- KONTROL PANELİ ---
        controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10)); 
        
        backButton = new JButton();
        controlPanel.add(backButton);
        
        categoryLabel = new JLabel();
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        controlPanel.add(categoryLabel);
        
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setPreferredSize(new Dimension(220, 35));
        controlPanel.add(categoryComboBox);
        
        selectFileButton = new JButton(); 
        selectFileButton.setPreferredSize(new Dimension(150, 35));
        controlPanel.add(selectFileButton);
        
        processButton = new JButton();
        processButton.setPreferredSize(new Dimension(150, 35));
        controlPanel.add(processButton);
        
        add(controlPanel, BorderLayout.NORTH);

        // --- SONUÇ ALANI ---
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        // Color.GRAY kullanımı için import eklendi, hata artık çıkmayacak.
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
        add(scrollPane, BorderLayout.CENTER);

        // --- LISTENERS ---
        backButton.addActionListener(e -> {
            frame.setContentPane(homePanel);
            frame.revalidate();
            frame.repaint();
        });

        selectFileButton.addActionListener(e -> selectFile());
        processButton.addActionListener(e -> startProcessing());
        
        refreshTheme();
    }
    
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            updateTexts(); 
        }
    }
    
    private void startProcessing() {
        if (selectedFilePath == null) {
            JOptionPane.showMessageDialog(frame, themeManager.getTranslation("MSG_FILE_REQUIRED"), "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String selectedCategoryName = (String) categoryComboBox.getSelectedItem();
        final String rawFileName = new File(selectedFilePath).getName();
        final String processType = selectedCategoryName;
        
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> resultArea.setText(themeManager.getTranslation("MSG_PROCESSING")));
                
                DocumentProcessor processor = new DocumentProcessor();
                String rawText = processor.extractTextFromFile(selectedFilePath);

                if (rawText == null || rawText.isEmpty()) return "Hata: Metin okunamadı.";

                String textToPdf = themeManager.getTranslation(categoryKeys[0]); 
                String finalResult;

                if (selectedCategoryName.equals(textToPdf)) {
                    finalResult = "--- METİN İÇERİĞİ ---\n\n" + rawText;
                } else {
                    finalResult = processor.summarizeText(rawText);
                }
                
                // Veritabanına Kayıt
                String contentToLog = finalResult.length() > 8000 ? finalResult.substring(0, 8000) + "..." : finalResult;
                userManager.logDocumentProcessing(userManager.getCurrentUser().getId(), rawFileName, processType, contentToLog);

                return finalResult;
            }

            @Override
            protected void done() {
                try {
                    resultArea.setText(get());
                } catch (Exception ex) {
                    resultArea.setText("Hata: " + ex.getMessage());
                }
                updateTexts();
            }
        };
        worker.execute();
    }

    private void refreshTheme() {
        updateTexts();
        // Sadece paneli boya, ThemeManager içindekiler halleder
        themeManager.applyTheme(this);
        
        if (controlPanel != null) themeManager.applyTheme(controlPanel);
    }
}