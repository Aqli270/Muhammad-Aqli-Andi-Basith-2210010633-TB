package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ManageDosenDialog extends JDialog {

    private Connection connection;
    private JTable dosenTable;
    private DefaultTableModel tableModel;

    public ManageDosenDialog(JFrame parent) {
        super(parent, "Kelola Dosen", true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Koneksi ke database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tabel data dosen
        String[] columnNames = {"ID Dosen", "Nama Dosen", "Fakultas", "Email", "User ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dosenTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dosenTable);

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

        // Load data dosen dari database
        loadDosenData();

        // Action listeners untuk tombol CRUD
        btnAdd.addActionListener(e -> openAddDosenDialog());
        btnEdit.addActionListener(e -> openEditDosenDialog());
        btnDelete.addActionListener(e -> deleteDosen());
    }

    // Fungsi untuk mengambil data dosen dari database
    private void loadDosenData() {
        try {
            String query = "SELECT * FROM dosen";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_dosen"));
                row.add(rs.getString("nama_dosen"));
                row.add(rs.getString("fakultas"));
                row.add(rs.getString("email"));
                row.add(rs.getInt("user_id"));  // Menambahkan user_id ke tabel
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menambahkan dosen baru
    private void openAddDosenDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2));  // Menambahkan input untuk User ID
        JTextField tfNama = new JTextField();
        JTextField tfFakultas = new JTextField();
        JTextField tfEmail = new JTextField();
        JTextField tfUserId = new JTextField();  // Kolom untuk User ID

        panel.add(new JLabel("Nama Dosen:"));
        panel.add(tfNama);
        panel.add(new JLabel("Fakultas:"));
        panel.add(tfFakultas);
        panel.add(new JLabel("Email:"));
        panel.add(tfEmail);
        panel.add(new JLabel("User ID:"));  // Label User ID
        panel.add(tfUserId);  // Input User ID

        int option = JOptionPane.showConfirmDialog(this, panel, "Tambah Dosen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String namaDosen = tfNama.getText();
            String fakultas = tfFakultas.getText();
            String email = tfEmail.getText();
            int userId = Integer.parseInt(tfUserId.getText());  // Mendapatkan nilai User ID

            try {
                String query = "INSERT INTO dosen (nama_dosen, fakultas, email, user_id) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, namaDosen);
                stmt.setString(2, fakultas);
                stmt.setString(3, email);
                stmt.setInt(4, userId);  // Menyisipkan User ID
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Dosen berhasil ditambahkan!");
                loadDosenData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Fungsi untuk mengedit dosen yang dipilih
    private void openEditDosenDialog() {
        int selectedRow = dosenTable.getSelectedRow();
        if (selectedRow != -1) {
            int idDosen = (int) tableModel.getValueAt(selectedRow, 0);
            String namaDosen = (String) tableModel.getValueAt(selectedRow, 1);
            String fakultas = (String) tableModel.getValueAt(selectedRow, 2);
            String email = (String) tableModel.getValueAt(selectedRow, 3);
            int userId = (int) tableModel.getValueAt(selectedRow, 4);  // Mendapatkan User ID

            JPanel panel = new JPanel(new GridLayout(5, 2));  // Menambahkan input untuk User ID
            JTextField tfNama = new JTextField(namaDosen);
            JTextField tfFakultas = new JTextField(fakultas);
            JTextField tfEmail = new JTextField(email);
            JTextField tfUserId = new JTextField(String.valueOf(userId));  // Menampilkan User ID

            panel.add(new JLabel("Nama Dosen:"));
            panel.add(tfNama);
            panel.add(new JLabel("Fakultas:"));
            panel.add(tfFakultas);
            panel.add(new JLabel("Email:"));
            panel.add(tfEmail);
            panel.add(new JLabel("User ID:"));  // Label User ID
            panel.add(tfUserId);  // Input User ID

            int option = JOptionPane.showConfirmDialog(this, panel, "Edit Dosen", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String query = "UPDATE dosen SET nama_dosen = ?, fakultas = ?, email = ?, user_id = ? WHERE id_dosen = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, tfNama.getText());
                    stmt.setString(2, tfFakultas.getText());
                    stmt.setString(3, tfEmail.getText());
                    stmt.setInt(4, Integer.parseInt(tfUserId.getText()));  // Menyisipkan User ID
                    stmt.setInt(5, idDosen);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Dosen berhasil diperbarui!");
                    loadDosenData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih dosen untuk diedit!");
        }
    }

    // Fungsi untuk menghapus dosen yang dipilih
    private void deleteDosen() {
        int selectedRow = dosenTable.getSelectedRow();
        if (selectedRow != -1) {
            int idDosen = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM dosen WHERE id_dosen = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, idDosen);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Dosen berhasil dihapus!");
                loadDosenData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih dosen untuk dihapus!");
        }
    }
}
