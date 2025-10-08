import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAExample extends JFrame {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private JTextArea inputArea;
    private JTextArea outputArea;

    public RSAExample() {
        super("RSA - Criptografia Assimétrica");
        generateKeys();
        initUI();
    }

    private void generateKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);

        inputArea = new JTextArea(5, 50);
        outputArea = new JTextArea(8, 50);
        outputArea.setEditable(false);

        JButton encryptBtn = new JButton("Criptografar (com chave pública)");
        JButton decryptBtn = new JButton("Descriptografar (com chave privada)");

        encryptBtn.addActionListener(this::onEncrypt);
        decryptBtn.addActionListener(this::onDecrypt);

        JPanel panel = new JPanel(new BorderLayout(8,8));
        panel.add(new JScrollPane(inputArea), BorderLayout.NORTH);

        JPanel middle = new JPanel(new FlowLayout());
        middle.add(encryptBtn);
        middle.add(decryptBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        panel.add(middle, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createTitledBorder("RSA - digite a mensagem e criptografe"));
        add(panel);
    }

    private void onEncrypt(ActionEvent e) {
        try {
            String plain = inputArea.getText();
            if (plain == null) plain = "";
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
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
            RSAExample app = new RSAExample();
            app.setVisible(true);
        });
    }
}


