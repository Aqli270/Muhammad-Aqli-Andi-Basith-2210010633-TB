import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotifikasiDialog extends JDialog {
    private JTable notificationTable;
    private DefaultTableModel tableModel;
    private JButton closeButton;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sistem_krs";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public NotifikasiDialog(JFrame parent, int mahasiswaId) {
        super(parent, "Notifikasi KRS", true);
        initializeUI(mahasiswaId);
    }

    private void initializeUI(int mahasiswaId) {
        // Table setup
        String[] columnNames = {"ID KRS", "Nama Kelas", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        notificationTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(notificationTable);

        // Close button setup
        closeButton = new JButton("Tutup");
        closeButton.addActionListener(e -> dispose());

        // Layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        getContentPane().add(scrollPane, "Center");
        getContentPane().add(buttonPanel, "South");

        setSize(600, 400);
        setLocationRelativeTo(null);

        loadNotifications(mahasiswaId);
    }

    private void loadNotifications(int mahasiswaId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = """
                SELECT krs.id_krs, kelas.nama_kelas, krs.status
                FROM krs
                JOIN kelas ON krs.id_kelas = kelas.id_kelas
                WHERE krs.id_mahasiswa = ?
            """;

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, mahasiswaId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int idKrs = resultSet.getInt("id_krs");
                        String namaKelas = resultSet.getString("nama_kelas");
                        String status = resultSet.getString("status");

                        tableModel.addRow(new Object[]{idKrs, namaKelas, status});
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NotifikasiDialog.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
