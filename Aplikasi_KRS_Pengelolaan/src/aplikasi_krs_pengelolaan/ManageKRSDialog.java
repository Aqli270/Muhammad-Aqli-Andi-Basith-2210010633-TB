package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ManageKRSDialog extends JDialog {

    private Connection connection;
    private JTable krsTable;
    private DefaultTableModel tableModel;

    public ManageKRSDialog(JFrame parent) {
        super(parent, "Kelola KRS", true);
        setSize(700, 400);
        setLayout(new BorderLayout());

        // Koneksi ke database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tabel data KRS
        String[] columnNames = {"ID KRS", "ID Mahasiswa", "ID Kelas", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        krsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(krsTable);

        // Tombol CRUD
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Tambahkan ke dialog
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data KRS dari database
        loadKrsData();

        // Action listeners untuk tombol CRUD
        btnAdd.addActionListener(e -> openAddKrsDialog());
        btnEdit.addActionListener(e -> openEditKrsDialog());
        btnDelete.addActionListener(e -> deleteKrs());
    }

    // Fungsi untuk mengambil data KRS dari database
    private void loadKrsData() {
        try {
            String query = "SELECT * FROM krs";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_krs"));
                row.add(rs.getInt("id_mahasiswa"));
                row.add(rs.getInt("id_kelas"));
                row.add(rs.getString("status"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menambahkan KRS baru
    private void openAddKrsDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField tfIdMahasiswa = new JTextField();
        JTextField tfIdKelas = new JTextField();
        JTextField tfStatus = new JTextField();

        panel.add(new JLabel("ID Mahasiswa:"));
        panel.add(tfIdMahasiswa);
        panel.add(new JLabel("ID Kelas:"));
        panel.add(tfIdKelas);
        panel.add(new JLabel("Status:"));
        panel.add(tfStatus);

        int option = JOptionPane.showConfirmDialog(this, panel, "Tambah KRS", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int idMahasiswa = Integer.parseInt(tfIdMahasiswa.getText());
            int idKelas = Integer.parseInt(tfIdKelas.getText());
            String status = tfStatus.getText();

            try {
                String query = "INSERT INTO krs (id_mahasiswa, id_kelas, status) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, idMahasiswa);
                stmt.setInt(2, idKelas);
                stmt.setString(3, status);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "KRS berhasil ditambahkan!");
                loadKrsData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Fungsi untuk mengedit KRS yang dipilih
    private void openEditKrsDialog() {
        int selectedRow = krsTable.getSelectedRow();
        if (selectedRow != -1) {
            int idKrs = (int) tableModel.getValueAt(selectedRow, 0);
            int idMahasiswa = (int) tableModel.getValueAt(selectedRow, 1);
            int idKelas = (int) tableModel.getValueAt(selectedRow, 2);
            String status = (String) tableModel.getValueAt(selectedRow, 3);

            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField tfIdMahasiswa = new JTextField(String.valueOf(idMahasiswa));
            JTextField tfIdKelas = new JTextField(String.valueOf(idKelas));
            JTextField tfStatus = new JTextField(status);

            panel.add(new JLabel("ID Mahasiswa:"));
            panel.add(tfIdMahasiswa);
            panel.add(new JLabel("ID Kelas:"));
            panel.add(tfIdKelas);
            panel.add(new JLabel("Status:"));
            panel.add(tfStatus);

            int option = JOptionPane.showConfirmDialog(this, panel, "Edit KRS", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                int newIdMahasiswa = Integer.parseInt(tfIdMahasiswa.getText());
                int newIdKelas = Integer.parseInt(tfIdKelas.getText());
                String newStatus = tfStatus.getText();

                try {
                    String query = "UPDATE krs SET id_mahasiswa = ?, id_kelas = ?, status = ? WHERE id_krs = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, newIdMahasiswa);
                    stmt.setInt(2, newIdKelas);
                    stmt.setString(3, newStatus);
                    stmt.setInt(4, idKrs);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "KRS berhasil diperbarui!");
                    loadKrsData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih KRS untuk diedit!");
        }
    }

    // Fungsi untuk menghapus KRS yang dipilih
    private void deleteKrs() {
        int selectedRow = krsTable.getSelectedRow();
        if (selectedRow != -1) {
            int idKrs = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM krs WHERE id_krs = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, idKrs);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "KRS berhasil dihapus!");
                loadKrsData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih KRS untuk dihapus!");
        }
    }
}
