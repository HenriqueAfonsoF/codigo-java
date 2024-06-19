import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Criação de voucher de agendamento");
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().setBackground(new Color(15, 15, 15));

        ArrayList<JTextField> textFields = new ArrayList<>();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(15, 15, 15));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("C:\\Users\\henri\\OneDrive\\Imagens\\Capturas de tela\\Logo sem fundo retangular melhor.png"));
            int newWidth = (int) (img.getWidth() * 0.4);
            int newHeight = (int) (img.getHeight() * 0.4);
            Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImg);
            JLabel picLabel = new JLabel(icon);
            picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(picLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel titleLabel = new JLabel("Criação de voucher de agendamento");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(new Color(15, 15, 15));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addField(fieldsPanel, "Nome do Passageiro:", textFields);
        addField(fieldsPanel, "Endereço de Partida:", textFields);
        addField(fieldsPanel, "Horário de Partida:", textFields);
        addField(fieldsPanel, "Endereço de Chegada:", textFields);
        addField(fieldsPanel, "Número do Voo (Opcional):", textFields);
        addField(fieldsPanel, "Nome do Motorista:", textFields);
        addField(fieldsPanel, "Modelo do Veículo:", textFields);
        addField(fieldsPanel, "Placa do Veículo:", textFields);
        addField(fieldsPanel, "Valor da Corrida:", textFields);

        mainPanel.add(fieldsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        buttonPanel.setBackground(new Color(15, 15, 15));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundButton generatePDFButton = new RoundButton("GERAR PDF");
        generatePDFButton.setPreferredSize(new Dimension(200, 50));
        generatePDFButton.setBackground(new Color(248, 92, 7));
        generatePDFButton.setForeground(Color.BLACK);
        generatePDFButton.setFocusPainted(false);

        generatePDFButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                generatePDFButton.setBackground(Color.WHITE);
                Dimension originalSize = generatePDFButton.getSize();
                Dimension newSize = new Dimension((int) (originalSize.getWidth() * 1.1), (int) (originalSize.getHeight() * 1.1));
                generatePDFButton.setPreferredSize(newSize);
            }

            public void mouseExited(MouseEvent evt) {
                generatePDFButton.setBackground(new Color(248, 92, 7));
                Dimension originalSize = generatePDFButton.getSize();
                Dimension newSize = new Dimension((int) (originalSize.getWidth() / 1.1), (int) (originalSize.getHeight() / 1.1));
                generatePDFButton.setPreferredSize(newSize);
            }
        });

        generatePDFButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> fieldValues = new ArrayList<>();
                for (JTextField textField : textFields) {
                    fieldValues.add(textField.getText());
                }
                try {
                    String outputPath = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\data\\trabalho.sql";
                    createPDF(fieldValues, outputPath);
                    JOptionPane.showMessageDialog(generatePDFButton, "PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(generatePDFButton, "Erro ao gerar PDF.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(generatePDFButton);
        mainPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        jFrame.add(scrollPane);

        jFrame.setVisible(true);
    }

    private static void addField(JPanel panel, String labelText, ArrayList<JTextField> textFields) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(new Color(15, 15, 15));

        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(new Color(15, 15, 15));
        textField.setForeground(Color.WHITE);
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(248, 92, 7));
        textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), bottomBorder));

        fieldPanel.add(label);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldPanel.add(textField);

        panel.add(fieldPanel);

        textFields.add(textField);
    }

    private static void createPDF(ArrayList<String> fieldValues, String outputPath) throws IOException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(outputPath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        float pageWidth = PageSize.A4.getWidth();
        float marginLeft = (pageWidth - 200) / 2;
        Paragraph title = new Paragraph("Voucher de Agendamento")
                .setFontSize(20)
                .setFontColor(new DeviceRgb(255, 255, 255))
                .setMarginLeft(marginLeft
                );
        document.add(title);

        for (String fieldValue : fieldValues) {
            Paragraph info = new Paragraph(fieldValue)
                    .setFontSize(12)
                    .setFontColor(new DeviceRgb(255, 255, 255));
            document.add(info);
        }

        document.close();
    }

    // Adiciona JDBC e integração com banco de dados
    private static void addToDatabase(ArrayList<String> fieldValues) {
        try {
            // Estabelece a conexão com o banco de dados MySQL
            String url = "jdbc:mysql://localhost:3306/nome_do_banco";
            String username = "seu_nome_de_usuário";
            String password = "sua_senha";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Define a consulta SQL para inserir os dados na tabela do banco de dados
            String query = "INSERT INTO nome_da_tabela (nome_passageiro, endereco_partida, horario_partida, endereco_chegada, numero_voo, nome_motorista, modelo_veiculo, placa_veiculo, valor_corrida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Define os valores dos parâmetros na consulta SQL
            for (int i = 0; i < fieldValues.size(); i++) {
                preparedStatement.setString(i + 1, fieldValues.get(i));
            }

            // Executa a consulta SQL para inserir os dados no banco de dados
            preparedStatement.executeUpdate();

            // Fecha a conexão com o banco de dados
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RoundButton extends JButton {
    public RoundButton(String label) {
        super(label);
        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 20, 20);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 20, 20);
    }
}
