package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class JadwalKuliahDialog extends JDialog {

    private Connection connection;  // Koneksi yang akan digunakan
    private int mahasiswaId;

    // Konstruktor menerima connection sebagai parameter
    public JadwalKuliahDialog(Frame parent, Connection connection, int mahasiswaId) {
        super(parent, "Pilih Jadwal Kuliah", true);
        this.connection = connection;  // Menggunakan connection yang diberikan
        this.mahasiswaId = mahasiswaId;
        initUI();
    }

    private void initUI() {
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Layout untuk panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Tabel untuk menampilkan jadwal kuliah
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button untuk memilih jadwal kuliah
        JButton btnPilihJadwal = new JButton("Pilih Jadwal Kuliah");
        panel.add(btnPilihJadwal, BorderLayout.SOUTH);

        // Tambahkan listener untuk button
        btnPilihJadwal.addActionListener(e -> pilihJadwal(table));

        // Load data jadwal kuliah
        loadJadwalData(table);

        add(panel);
    }

    private void loadJadwalData(JTable table) {
        try {
            String query = "SELECT * FROM jadwal";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            // Set the table model with data from result set
            table.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void pilihJadwal(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int jadwalId = (Integer) table.getValueAt(selectedRow, 0);  // Ambil id_jadwal dari baris yang dipilih
            // Simpan jadwal yang dipilih ke tabel KRS
            tambahJadwalKRS(jadwalId);
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih jadwal kuliah terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void tambahJadwalKRS(int jadwalId) {
        try {
            String query = "INSERT INTO krs (id_mahasiswa, id_kelas, status) " +
                    "SELECT ?, id_kelas, ? FROM jadwal WHERE id_jadwal = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, mahasiswaId);
            stmt.setString(2, "Pending");  // Status default untuk KRS baru
            stmt.setInt(3, jadwalId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Jadwal kuliah berhasil ditambahkan ke KRS!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menambahkan jadwal kuliah ke KRS!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method untuk membuat TableModel dari ResultSet
    public static TableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        // Get column names
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Get rows data
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
