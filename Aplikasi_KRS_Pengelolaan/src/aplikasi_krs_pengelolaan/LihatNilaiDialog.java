package aplikasi_krs_pengelolaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LihatNilaiDialog extends JDialog {
    private JTable tableNilai;
    private DefaultTableModel tableModel;
    private Connection connection;
    private int mahasiswaId;

    public LihatNilaiDialog(JFrame parent, Connection connection, int mahasiswaId) {
        super(parent, "Lihat Nilai Mahasiswa", true);
        this.connection = connection;
        this.mahasiswaId = mahasiswaId;

        // Inisialisasi komponen UI
        tableNilai = new JTable();
        tableModel = new DefaultTableModel(new Object[] {"Mata Kuliah", "Nilai"}, 0);
        tableNilai.setModel(tableModel);

        // Layout komponen
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(tableNilai), BorderLayout.CENTER);

        JButton btnTutup = new JButton("Tutup");
        panel.add(btnTutup, BorderLayout.SOUTH);

        add(panel);

        // Memuat data nilai
        loadNilaiMahasiswa();

        // Aksi tombol tutup
        btnTutup.addActionListener(e -> dispose());

        // Konfigurasi dialog
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void loadNilaiMahasiswa() {
        try {
            String query = "SELECT mk.nama_mata_kuliah, n.nilai " +
                           "FROM nilai n " +
                           "JOIN registrasi_kelas rk ON n.id_registrasi = rk.id_registrasi " +
                           "JOIN kelas k ON rk.id_kelas = k.id_kelas " +  // Menggunakan id_kelas dari registrasi_kelas
                           "JOIN mata_kuliah mk ON k.id_mata_kuliah = mk.id_mata_kuliah " + // Menggunakan id_mata_kuliah dari kelas
                           "WHERE rk.id_mahasiswa = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, mahasiswaId);
            ResultSet rs = stmt.executeQuery();

            // Clear tabel
            tableModel.setRowCount(0);

            while (rs.next()) {
                String namaMataKuliah = rs.getString("nama_mata_kuliah");
                String nilai = rs.getString("nilai");
                tableModel.addRow(new Object[] {namaMataKuliah, nilai});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data nilai: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
