package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class ValidateKRSDialog extends JDialog {
    private Connection connection;
    private int dosenId; // ID dosen yang sedang login
    private JTable table;
    private DefaultTableModel tableModel;
    
    public ValidateKRSDialog(JFrame parent, Connection connection, int dosenId) {
        super(parent, "Validasi KRS", true);
        this.connection = connection;
        this.dosenId = dosenId;
        
        setSize(600, 400);
        setLocationRelativeTo(parent);
        
        // Table model to display the data
        tableModel = new DefaultTableModel(new String[]{"ID Registrasi", "Nama Kelas", "Nama Mahasiswa", "Tanggal Registrasi"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load data on dialog initialization
        loadData();
        
        // Button for Validasi
        JButton btnValidate = new JButton("Validasi");
        btnValidate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateKRS();
            }
        });
        
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
        btnPanel.add(btnValidate);
        btnPanel.add(btnClose);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    private void loadData() {
        // SQL query to retrieve the registrasi KRS for classes that the logged-in dosen teaches
        String query = "SELECT rk.id_registrasi, k.nama_kelas, m.nama_mahasiswa, rk.tanggal_registrasi " +
                       "FROM registrasi_kelas rk " +
                       "JOIN kelas k ON rk.id_kelas = k.id_kelas " +
                       "JOIN mata_kuliah mk ON k.id_mata_kuliah = mk.id_mata_kuliah " +
                       "JOIN mata_kuliah_dosen mkd ON mk.id_mata_kuliah = mkd.id_mata_kuliah " +
                       "JOIN dosen d ON mkd.id_dosen = d.id_dosen " +
                       "JOIN mahasiswa m ON rk.id_mahasiswa = m.id_mahasiswa " +
                       "WHERE d.id_dosen = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, dosenId);
            ResultSet rs = ps.executeQuery();
            
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Populate table with data
            while (rs.next()) {
                int idRegistrasi = rs.getInt("id_registrasi");
                String namaKelas = rs.getString("nama_kelas");
                String namaMahasiswa = rs.getString("nama_mahasiswa");
                String tanggalRegistrasi = rs.getString("tanggal_registrasi");
                
                // Add row to the table
                tableModel.addRow(new Object[]{idRegistrasi, namaKelas, namaMahasiswa, tanggalRegistrasi});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data KRS: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void validateKRS() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idRegistrasi = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Logic to validate KRS (You can add additional logic if needed)
            String query = "UPDATE registrasi_kelas SET status = 'VALID' WHERE id_registrasi = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, idRegistrasi);
                int rowsAffected = ps.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "KRS berhasil divalidasi.", "Validasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    loadData();  // Refresh the table data
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal melakukan validasi KRS.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saat validasi KRS: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih KRS yang ingin divalidasi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
}
