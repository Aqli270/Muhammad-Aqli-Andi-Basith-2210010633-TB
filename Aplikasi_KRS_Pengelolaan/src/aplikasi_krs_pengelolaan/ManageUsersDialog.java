package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ManageUsersDialog extends JDialog {

    private Connection connection;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public ManageUsersDialog(JFrame parent) {
        super(parent, "Kelola Pengguna", true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Koneksi ke database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tabel data pengguna
        String[] columnNames = {"ID", "Nama Pengguna", "Email", "Peran", "Nama Lengkap", "Tanggal Dibuat"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

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

        // Load data pengguna dari database
        loadUsersData();

        // Action listeners untuk tombol CRUD
        btnAdd.addActionListener(e -> openAddUserDialog());
        btnEdit.addActionListener(e -> openEditUserDialog());
        btnDelete.addActionListener(e -> deleteUser());
    }

    // Fungsi untuk mengambil data pengguna dari database
    private void loadUsersData() {
        try {
            String query = "SELECT * FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            // Clear existing data
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("email"));
                row.add(rs.getString("role"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("created_at"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menambahkan pengguna baru
    private void openAddUserDialog() {
        // Form input pengguna baru
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField tfUsername = new JTextField();
        JTextField tfEmail = new JTextField();
        JTextField tfFullName = new JTextField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[] {"admin", "dosen", "mahasiswa"});
        JPasswordField tfPassword = new JPasswordField();

        panel.add(new JLabel("Nama Pengguna:"));
        panel.add(tfUsername);
        panel.add(new JLabel("Email:"));
        panel.add(tfEmail);
        panel.add(new JLabel("Nama Lengkap:"));
        panel.add(tfFullName);
        panel.add(new JLabel("Peran:"));
        panel.add(roleComboBox);
        panel.add(new JLabel("Password:"));
        panel.add(tfPassword);

        int option = JOptionPane.showConfirmDialog(this, panel, "Tambah Pengguna", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = tfUsername.getText();
            String email = tfEmail.getText();
            String fullName = tfFullName.getText();
            String role = (String) roleComboBox.getSelectedItem();
            String password = new String(tfPassword.getPassword());

            // Menyimpan data pengguna baru ke database
            try {
                String query = "INSERT INTO users (username, email, full_name, role, password, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, fullName);
                stmt.setString(4, role);
                stmt.setString(5, password);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Pengguna berhasil ditambahkan!");
                loadUsersData(); // Refresh data setelah penambahan
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Fungsi untuk mengedit pengguna yang dipilih
    private void openEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);
            String email = (String) tableModel.getValueAt(selectedRow, 2);
            String role = (String) tableModel.getValueAt(selectedRow, 3);
            String fullName = (String) tableModel.getValueAt(selectedRow, 4);

            // Form untuk edit pengguna
            JPanel panel = new JPanel(new GridLayout(5, 2));
            JTextField tfUsername = new JTextField(username);
            JTextField tfEmail = new JTextField(email);
            JTextField tfFullName = new JTextField(fullName);
            JComboBox<String> roleComboBox = new JComboBox<>(new String[] {"admin", "dosen", "mahasiswa"});
            roleComboBox.setSelectedItem(role);
            JPasswordField tfPassword = new JPasswordField();

            panel.add(new JLabel("Nama Pengguna:"));
            panel.add(tfUsername);
            panel.add(new JLabel("Email:"));
            panel.add(tfEmail);
            panel.add(new JLabel("Nama Lengkap:"));
            panel.add(tfFullName);
            panel.add(new JLabel("Peran:"));
            panel.add(roleComboBox);
            panel.add(new JLabel("Password (kosongkan jika tidak diubah):"));
            panel.add(tfPassword);

            int option = JOptionPane.showConfirmDialog(this, panel, "Edit Pengguna", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newUsername = tfUsername.getText();
                String newEmail = tfEmail.getText();
                String newFullName = tfFullName.getText();
                String newRole = (String) roleComboBox.getSelectedItem();
                String newPassword = new String(tfPassword.getPassword());

                // Update data pengguna ke database
                try {
                    String query = "UPDATE users SET username = ?, email = ?, full_name = ?, role = ?, password = ? WHERE user_id = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, newUsername);
                    stmt.setString(2, newEmail);
                    stmt.setString(3, newFullName);
                    stmt.setString(4, newRole);
                    stmt.setString(5, newPassword.isEmpty() ? null : newPassword); // Biarkan kosong jika password tidak diubah
                    stmt.setInt(6, userId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Pengguna berhasil diperbarui!");
                    loadUsersData(); // Refresh data setelah update
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih pengguna untuk diedit!");
        }
    }

    // Fungsi untuk menghapus pengguna yang dipilih
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM users WHERE user_id = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, userId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Pengguna berhasil dihapus!");
                loadUsersData(); // Refresh data setelah penghapusan
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih pengguna untuk dihapus!");
        }
    }
}
