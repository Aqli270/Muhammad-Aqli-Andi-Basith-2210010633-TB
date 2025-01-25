package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ManageCoursesDialog extends JDialog {

    private Connection connection;
    private JTable coursesTable;
    private DefaultTableModel tableModel;

    public ManageCoursesDialog(JFrame parent) {
        super(parent, "Kelola Mata Kuliah", true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Koneksi ke database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tabel data mata kuliah
        String[] columnNames = {"ID", "Nama Mata Kuliah", "SKS"};
        tableModel = new DefaultTableModel(columnNames, 0);
        coursesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(coursesTable);

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

        // Load data mata kuliah dari database
        loadCoursesData();

        // Action listeners untuk tombol CRUD
        btnAdd.addActionListener(e -> openAddCourseDialog());
        btnEdit.addActionListener(e -> openEditCourseDialog());
        btnDelete.addActionListener(e -> deleteCourse());
    }

    // Fungsi untuk mengambil data mata kuliah dari database
    private void loadCoursesData() {
        try {
            String query = "SELECT * FROM mata_kuliah";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("id_mata_kuliah");
                row[1] = rs.getString("nama_mata_kuliah");
                row[2] = rs.getInt("sks");
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menambahkan mata kuliah baru
    private void openAddCourseDialog() {
        JTextField courseNameField = new JTextField();
        JTextField sksField = new JTextField();
        Object[] message = {
            "Nama Mata Kuliah:", courseNameField,
            "SKS:", sksField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Tambah Mata Kuliah", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String courseName = courseNameField.getText();
            String sks = sksField.getText();
            try {
                String query = "INSERT INTO mata_kuliah (nama_mata_kuliah, sks) VALUES (?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, courseName);
                stmt.setInt(2, Integer.parseInt(sks));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil ditambahkan!");
                loadCoursesData(); // Refresh data
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Fungsi untuk mengedit mata kuliah yang dipilih
    private void openEditCourseDialog() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow != -1) {
            int courseId = (int) tableModel.getValueAt(selectedRow, 0);
            String courseName = (String) tableModel.getValueAt(selectedRow, 1);
            int sks = (int) tableModel.getValueAt(selectedRow, 2);

            JTextField courseNameField = new JTextField(courseName);
            JTextField sksField = new JTextField(String.valueOf(sks));
            Object[] message = {
                "Nama Mata Kuliah:", courseNameField,
                "SKS:", sksField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Mata Kuliah", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String query = "UPDATE mata_kuliah SET nama_mata_kuliah = ?, sks = ? WHERE id_mata_kuliah = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, courseNameField.getText());
                    stmt.setInt(2, Integer.parseInt(sksField.getText()));
                    stmt.setInt(3, courseId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Mata kuliah berhasil diperbarui!");
                    loadCoursesData(); // Refresh data
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah untuk diedit!");
        }
    }

    // Fungsi untuk menghapus mata kuliah yang dipilih
    private void deleteCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow != -1) {
            int courseId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM mata_kuliah WHERE id_mata_kuliah = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, courseId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil dihapus!");
                loadCoursesData(); // Refresh data setelah penghapusan
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah untuk dihapus!");
        }
    }
}
