package aplikasi_krs_pengelolaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class InputGradesDialog extends JDialog {
    private JTable tableMahasiswa;
    private DefaultTableModel tableModel;
    private JButton btnSimpan, btnClose, btnInputNilai;
    private Connection connection;
    private JComboBox<String> comboKelas;
    
    public InputGradesDialog(JFrame parent, Connection connection) {
        super(parent, "Input Nilai Mahasiswa", true);
        this.connection = connection;

        // Inisialisasi komponen UI
        comboKelas = new JComboBox<>();
        tableMahasiswa = new JTable();
        tableModel = new DefaultTableModel(new Object[] {"ID Mahasiswa", "Nama Mahasiswa", "Nilai"}, 0);
        tableMahasiswa.setModel(tableModel);
        
        btnSimpan = new JButton("Simpan Nilai");
        btnClose = new JButton("Tutup");
        btnInputNilai = new JButton("Input Nilai");

        // Action Listener untuk tombol Simpan
        btnSimpan.addActionListener(e -> simpanNilai());
        btnClose.addActionListener(e -> dispose());
        btnInputNilai.addActionListener(e -> inputNilai());

        // Layout komponen
        JPanel panelTop = new JPanel();
        panelTop.add(new JLabel("Pilih Kelas:"));
        panelTop.add(comboKelas);
        
        JScrollPane scrollPane = new JScrollPane(tableMahasiswa);
        
        JPanel panelBottom = new JPanel();
        panelBottom.add(btnSimpan);
        panelBottom.add(btnInputNilai);
        panelBottom.add(btnClose);
        
        setLayout(new BorderLayout());
        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        // Memuat data kelas
        loadKelasData();
        
        // Konfigurasi dialog
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void loadKelasData() {
        try {
            String query = "SELECT k.id_kelas, k.nama_kelas FROM kelas k";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Clear combo box
            comboKelas.removeAllItems();
            
            while (rs.next()) {
                String kelas = rs.getInt("id_kelas") + " - " + rs.getString("nama_kelas");
                comboKelas.addItem(kelas);
            }
            
            // Add listener for combo box selection
            comboKelas.addActionListener(e -> loadMahasiswaData());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data kelas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMahasiswaData() {
        try {
            String selectedKelas = (String) comboKelas.getSelectedItem();
            if (selectedKelas == null) return;

            // Extract id_kelas from selected kelas
            int idKelas = Integer.parseInt(selectedKelas.split(" - ")[0]);

            // Query untuk mengambil data mahasiswa terdaftar di kelas tersebut
            String query = "SELECT m.id_mahasiswa, m.nama_mahasiswa FROM mahasiswa m " +
                           "JOIN registrasi_kelas rk ON m.id_mahasiswa = rk.id_mahasiswa " +
                           "WHERE rk.id_kelas = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idKelas);
            ResultSet rs = stmt.executeQuery();
            
            // Clear tabel
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_mahasiswa"));
                row.add(rs.getString("nama_mahasiswa"));
                row.add("");  // Kolom nilai kosong untuk input
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data mahasiswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inputNilai() {
        // Menampilkan dialog untuk memasukkan nilai ke mahasiswa
        int selectedRow = tableMahasiswa.getSelectedRow();
        if (selectedRow != -1) {
            String nilai = JOptionPane.showInputDialog(this, "Masukkan Nilai untuk " +
                    tableMahasiswa.getValueAt(selectedRow, 1).toString());
            
            // Memasukkan nilai ke tabel
            if (nilai != null && !nilai.isEmpty()) {
                tableMahasiswa.setValueAt(nilai, selectedRow, 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa untuk input nilai", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void simpanNilai() {
        try {
            String selectedKelas = (String) comboKelas.getSelectedItem();
            if (selectedKelas == null) return;

            int idKelas = Integer.parseInt(selectedKelas.split(" - ")[0]);

            // Proses input nilai
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int idMahasiswa = (int) tableModel.getValueAt(i, 0);
                String nilai = (String) tableModel.getValueAt(i, 2); // Mengambil nilai dari kolom

                // Menyimpan nilai ke tabel 'nilai'
                if (!nilai.isEmpty()) {
                    String query = "INSERT INTO nilai (id_registrasi, nilai) " +
                                   "SELECT id_registrasi, ? FROM registrasi_kelas WHERE id_kelas = ? AND id_mahasiswa = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, nilai);
                    stmt.setInt(2, idKelas);
                    stmt.setInt(3, idMahasiswa);
                    stmt.executeUpdate();
                }
            }
            
            JOptionPane.showMessageDialog(this, "Nilai berhasil disimpan!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat menyimpan nilai: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
