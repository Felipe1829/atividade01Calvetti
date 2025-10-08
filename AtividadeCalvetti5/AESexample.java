import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Base64;

public class AESexample extends JFrame {
    private SecretKey secretKey;
    private JTextArea inputArea;
    private JTextArea outputArea;

    public AESexample() {
        super("AES - Criptografia Simétrica (GUI)");
        initKey();
        initUI();
    }

    private void initKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            secretKey = keyGen.generateKey();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        inputArea = new JTextArea(5, 40);
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);

        JButton encryptBtn = new JButton("Criptografar (AES)");
        JButton decryptBtn = new JButton("Descriptografar (AES)");

        encryptBtn.addActionListener(this::onEncrypt);
        decryptBtn.addActionListener(this::onDecrypt);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createTitledBorder("Mensagem (entrada)"));
        top.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel middle = new JPanel(new FlowLayout());
        middle.add(encryptBtn);
        middle.add(decryptBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createTitledBorder("Resultado (Base64 para criptografado)"));
        bottom.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(middle, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        add(panel);
    }

    private void onEncrypt(ActionEvent e) {
        try {
            String plain = inputArea.getText();
            if (plain == null) plain = "";
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] ct = cipher.doFinal(plain.getBytes("UTF-8"));
            String ctB64 = Base64.getEncoder().encodeToString(ct);
            outputArea.setText(ctB64);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onDecrypt(ActionEvent e) {
        try {
            String ctB64 = outputArea.getText();
            if (ctB64 == null || ctB64.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cole ou gere um texto criptografado primeiro.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            byte[] ct = Base64.getDecoder().decode(ctB64);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] pt = cipher.doFinal(ct);
            String plain = new String(pt, "UTF-8");
            JOptionPane.showMessageDialog(this, "Texto descriptografado:\n" + plain, "Descriptografado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AESexample app = new AESexample();
            app.setVisible(true);
        });
    }
}


