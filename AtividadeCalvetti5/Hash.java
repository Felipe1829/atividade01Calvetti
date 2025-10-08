import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.MessageDigest;

public class Hash extends JFrame {
    private JTextArea inputArea;
    private JTextField outputField;

    public Hash() {
        super("SHA-256 - Função Hash");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);

        inputArea = new JTextArea(5, 40);
        outputField = new JTextField();
        outputField.setEditable(false);

        JButton hashBtn = new JButton("Gerar Hash (SHA-256)");
        hashBtn.addActionListener(this::onHash);

        JPanel panel = new JPanel(new BorderLayout(8,8));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createTitledBorder("Mensagem / Senha"));
        top.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel middle = new JPanel(new FlowLayout());
        middle.add(hashBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createTitledBorder("Hash (hex)"));
        bottom.add(outputField, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(middle, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        add(panel);
    }

    private void onHash(ActionEvent e) {
        try {
            String msg = inputArea.getText();
            if (msg == null) msg = "";
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(msg.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            outputField.setText(hex.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Hash app = new Hash();
            app.setVisible(true);
        });
    }
}

