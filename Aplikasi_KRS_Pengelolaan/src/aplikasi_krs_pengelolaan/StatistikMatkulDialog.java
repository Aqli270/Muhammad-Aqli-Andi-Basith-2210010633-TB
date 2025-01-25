package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StatistikMatkulDialog extends JDialog {
    private Connection connection;
    private JTable table;
    private DefaultTableModel tableModel;

    public StatistikMatkulDialog(JFrame parent, Connection connection) {
        super(parent, "Statistik Mata Kuliah", true);
        this.connection = connection;

        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Table model to display the data
        tableModel = new DefaultTableModel(new String[]{"Nama Mata Kuliah", "Jumlah Mahasiswa"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load data on dialog initialization
        loadStatistics();

        // Button to close the dialog
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnClose);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadStatistics() {
        // SQL query to retrieve the statistics for each mata kuliah and count the number of students registered
        String query = "SELECT mk.nama_mata_kuliah, COUNT(rk.id_registrasi) AS jumlah_mahasiswa " +
                       "FROM mata_kuliah mk " +
                       "LEFT JOIN kelas k ON mk.id_mata_kuliah = k.id_mata_kuliah " +
                       "LEFT JOIN registrasi_kelas rk ON k.id_kelas = rk.id_kelas " +
                       "GROUP BY mk.id_mata_kuliah";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            // Clear existing table data
            tableModel.setRowCount(0);

            // Populate table with mata kuliah statistics
            while (rs.next()) {
                String namaMataKuliah = rs.getString("nama_mata_kuliah");
                int jumlahMahasiswa = rs.getInt("jumlah_mahasiswa");

                // Add row to the table
                tableModel.addRow(new Object[]{namaMataKuliah, jumlahMahasiswa});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data statistik: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
