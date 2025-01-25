package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class KRSMahasiswaDialog extends JDialog {

    private Connection connection;
    private JTable krsTable;
    private DefaultTableModel tableModel;
    private int idMahasiswa; // ID mahasiswa yang login

    // Konstruktor untuk menerima ID Mahasiswa yang login
    public KRSMahasiswaDialog(JFrame parent, int idMahasiswa) {
        super(parent, "Kelola KRS", true);
        this.idMahasiswa = idMahasiswa; // Set ID mahasiswa
        setSize(700, 400);
        setLayout(new BorderLayout());

        // Koneksi ke database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
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

        // Load data KRS untuk mahasiswa tertentu
        loadKrsData();

        // Action listeners untuk tombol CRUD
        btnAdd.addActionListener(e -> openAddKrsDialog());
        btnEdit.addActionListener(e -> openEditKrsDialog());
        btnDelete.addActionListener(e -> deleteKrs());
    }

    // Fungsi untuk mengambil data KRS dari database untuk mahasiswa tertentu
    private void loadKrsData() {
        try {
            // Debugging: Pastikan idMahasiswa yang diteruskan benar
            System.out.println("ID Mahasiswa yang login: " + idMahasiswa);
            
            // Query untuk mengambil data KRS berdasarkan id_mahasiswa yang login
            String query = "SELECT * FROM krs WHERE id_mahasiswa = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idMahasiswa); // Pastikan ID Mahasiswa yang login digunakan
            ResultSet rs = stmt.executeQuery();

            // Clear existing data
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                int idKrs = rs.getInt("id_krs");
                int idMahasiswa = rs.getInt("id_mahasiswa");
                int idKelas = rs.getInt("id_kelas");
                String status = rs.getString("status");

                // Debug: Periksa data yang diambil
                System.out.println("ID KRS: " + idKrs + ", ID Mahasiswa: " + idMahasiswa + ", ID Kelas: " + idKelas + ", Status: " + status);

                Vector<Object> row = new Vector<>();
                row.add(idKrs);
                row.add(idMahasiswa);
                row.add(idKelas);
                row.add(status);
                tableModel.addRow(row);
            }

            tableModel.fireTableDataChanged(); // Memastikan tabel ter-update

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menambahkan KRS baru
    private void openAddKrsDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel lblIdMahasiswa = new JLabel("ID Mahasiswa: " + idMahasiswa);
        JTextField tfIdKelas = new JTextField();
        JTextField tfStatus = new JTextField();

        panel.add(lblIdMahasiswa); // Menampilkan ID mahasiswa yang tetap
        panel.add(new JLabel());
        panel.add(new JLabel("ID Kelas:"));
        panel.add(tfIdKelas);
        panel.add(new JLabel("Status:"));
        panel.add(tfStatus);

        int option = JOptionPane.showConfirmDialog(this, panel, "Tambah KRS", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int idKelas = Integer.parseInt(tfIdKelas.getText());
            String status = tfStatus.getText();

            try {
                String query = "INSERT INTO krs (id_mahasiswa, id_kelas, status) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, idMahasiswa); // Menggunakan ID mahasiswa yang login
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
            int idKelas = (int) tableModel.getValueAt(selectedRow, 2);
            String status = (String) tableModel.getValueAt(selectedRow, 3);

            JPanel panel = new JPanel(new GridLayout(2, 2));
            JLabel lblIdMahasiswa = new JLabel("ID Mahasiswa: " + idMahasiswa);
            JTextField tfIdKelas = new JTextField(String.valueOf(idKelas));
            JTextField tfStatus = new JTextField(status);

            panel.add(lblIdMahasiswa); // Menampilkan ID mahasiswa yang tetap
            panel.add(new JLabel());
            panel.add(new JLabel("ID Kelas:"));
            panel.add(tfIdKelas);
            panel.add(new JLabel("Status:"));
            panel.add(tfStatus);

            int option = JOptionPane.showConfirmDialog(this, panel, "Edit KRS", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                int newIdKelas = Integer.parseInt(tfIdKelas.getText());
                String newStatus = tfStatus.getText();

                try {
                    String query = "UPDATE krs SET id_kelas = ?, status = ? WHERE id_krs = ? AND id_mahasiswa = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, newIdKelas);
                    stmt.setString(2, newStatus);
                    stmt.setInt(3, idKrs);
                    stmt.setInt(4, idMahasiswa); // Pastikan hanya mengupdate milik mahasiswa yang login
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
                String query = "DELETE FROM krs WHERE id_krs = ? AND id_mahasiswa = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, idKrs);
                stmt.setInt(2, idMahasiswa); // Pastikan hanya menghapus milik mahasiswa yang login
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
