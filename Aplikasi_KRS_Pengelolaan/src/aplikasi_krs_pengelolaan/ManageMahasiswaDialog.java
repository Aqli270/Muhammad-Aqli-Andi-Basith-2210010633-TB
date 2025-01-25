package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ManageMahasiswaDialog extends JDialog {

    private Connection connection;
    private JTable mahasiswaTable;
    private DefaultTableModel tableModel;

    public ManageMahasiswaDialog(JFrame parent) {
        super(parent, "Kelola Mahasiswa", true);
        setSize(700, 400);
        setLayout(new BorderLayout());

        // Koneksi ke database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tabel data mahasiswa
        String[] columnNames = {"ID Mahasiswa", "Nama Mahasiswa", "Program Studi", "Email", "Semester", "User ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        mahasiswaTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);

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

        // Load data mahasiswa dari database
        loadMahasiswaData();

        // Action listeners untuk tombol CRUD
        btnAdd.addActionListener(e -> openAddMahasiswaDialog());
        btnEdit.addActionListener(e -> openEditMahasiswaDialog());
        btnDelete.addActionListener(e -> deleteMahasiswa());
    }

    // Fungsi untuk mengambil data mahasiswa dari database
    private void loadMahasiswaData() {
        try {
            String query = "SELECT * FROM mahasiswa";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_mahasiswa"));
                row.add(rs.getString("nama_mahasiswa"));
                row.add(rs.getString("program_studi"));
                row.add(rs.getString("email"));
                row.add(rs.getString("semester"));
                row.add(rs.getInt("user_id")); // Tambahkan User ID
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menambahkan mahasiswa baru
    private void openAddMahasiswaDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2)); // Tambahkan satu baris untuk User ID
        JTextField tfNama = new JTextField();
        JTextField tfProgramStudi = new JTextField();
        JTextField tfEmail = new JTextField();
        JTextField tfSemester = new JTextField();
        JTextField tfUserId = new JTextField(); // Input untuk User ID

        panel.add(new JLabel("Nama Mahasiswa:"));
        panel.add(tfNama);
        panel.add(new JLabel("Program Studi:"));
        panel.add(tfProgramStudi);
        panel.add(new JLabel("Email:"));
        panel.add(tfEmail);
        panel.add(new JLabel("Semester:"));
        panel.add(tfSemester);
        panel.add(new JLabel("User ID:"));
        panel.add(tfUserId);

        int option = JOptionPane.showConfirmDialog(this, panel, "Tambah Mahasiswa", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nama = tfNama.getText();
            String programStudi = tfProgramStudi.getText();
            String email = tfEmail.getText();
            String semester = tfSemester.getText();
            int userId = Integer.parseInt(tfUserId.getText()); // Ambil User ID

            try {
                String query = "INSERT INTO mahasiswa (nama_mahasiswa, program_studi, email, semester, user_id) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, nama);
                stmt.setString(2, programStudi);
                stmt.setString(3, email);
                stmt.setString(4, semester);
                stmt.setInt(5, userId); // Simpan User ID
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil ditambahkan!");
                loadMahasiswaData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Fungsi untuk mengedit mahasiswa yang dipilih
    private void openEditMahasiswaDialog() {
        int selectedRow = mahasiswaTable.getSelectedRow();
        if (selectedRow != -1) {
            int idMahasiswa = (int) tableModel.getValueAt(selectedRow, 0);
            String nama = (String) tableModel.getValueAt(selectedRow, 1);
            String programStudi = (String) tableModel.getValueAt(selectedRow, 2);
            String email = (String) tableModel.getValueAt(selectedRow, 3);
            String semester = (String) tableModel.getValueAt(selectedRow, 4);
            int userId = (int) tableModel.getValueAt(selectedRow, 5);

            JPanel panel = new JPanel(new GridLayout(5, 2));
            JTextField tfNama = new JTextField(nama);
            JTextField tfProgramStudi = new JTextField(programStudi);
            JTextField tfEmail = new JTextField(email);
            JTextField tfSemester = new JTextField(semester);
            JTextField tfUserId = new JTextField(String.valueOf(userId));

            panel.add(new JLabel("Nama Mahasiswa:"));
            panel.add(tfNama);
            panel.add(new JLabel("Program Studi:"));
            panel.add(tfProgramStudi);
            panel.add(new JLabel("Email:"));
            panel.add(tfEmail);
            panel.add(new JLabel("Semester:"));
            panel.add(tfSemester);
            panel.add(new JLabel("User ID:"));
            panel.add(tfUserId);

            int option = JOptionPane.showConfirmDialog(this, panel, "Edit Mahasiswa", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newNama = tfNama.getText();
                String newProgramStudi = tfProgramStudi.getText();
                String newEmail = tfEmail.getText();
                String newSemester = tfSemester.getText();
                int newUserId = Integer.parseInt(tfUserId.getText());

                try {
                    String query = "UPDATE mahasiswa SET nama_mahasiswa = ?, program_studi = ?, email = ?, semester = ?, user_id = ? WHERE id_mahasiswa = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, newNama);
                    stmt.setString(2, newProgramStudi);
                    stmt.setString(3, newEmail);
                    stmt.setString(4, newSemester);
                    stmt.setInt(5, newUserId);
                    stmt.setInt(6, idMahasiswa);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Mahasiswa berhasil diperbarui!");
                    loadMahasiswaData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa untuk diedit!");
        }
    }

    // Fungsi untuk menghapus mahasiswa yang dipilih
    private void deleteMahasiswa() {
        int selectedRow = mahasiswaTable.getSelectedRow();
        if (selectedRow != -1) {
            int idMahasiswa = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM mahasiswa WHERE id_mahasiswa = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, idMahasiswa);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil dihapus!");
                loadMahasiswaData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa untuk dihapus!");
        }
    }
}
