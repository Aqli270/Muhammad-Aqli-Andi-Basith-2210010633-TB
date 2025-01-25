package aplikasi_krs_pengelolaan;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class KelasMahasiswaDialog extends JDialog {
    private JTable tableKelas;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnEdit, btnHapus, btnClose;
    private Connection connection;
    private int mahasiswaId;

    public KelasMahasiswaDialog(JFrame parent, Connection connection, int mahasiswaId) {
        super(parent, "Data Kelas Mahasiswa", true);
        this.connection = connection;
        this.mahasiswaId = mahasiswaId;

        // Inisialisasi tabel
        String[] columnNames = {"ID Kelas", "Nama Kelas", "Jadwal", "Ruang"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableKelas = new JTable(tableModel);

        // Inisialisasi tombol
        btnTambah = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnClose = new JButton("Tutup");

        btnTambah.addActionListener(e -> tambahKelas());
        btnEdit.addActionListener(e -> editKelas());
        btnHapus.addActionListener(e -> hapusKelas());
        btnClose.addActionListener(e -> dispose());

        // Layout tombol
        JPanel panelBottom = new JPanel();
        panelBottom.add(btnTambah);
        panelBottom.add(btnEdit);
        panelBottom.add(btnHapus);
        panelBottom.add(btnClose);

        // Layout utama
        JScrollPane scrollPane = new JScrollPane(tableKelas);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        // Load data kelas berdasarkan mahasiswaId
        loadKelasData();

        // Konfigurasi dialog
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void loadKelasData() {
        try {
            String query = "SELECT k.id_kelas, k.nama_kelas, k.jadwal, k.ruang " +
                           "FROM kelas k " +
                           "JOIN registrasi_kelas rk ON k.id_kelas = rk.id_kelas " +
                           "WHERE rk.id_mahasiswa = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, mahasiswaId);
            ResultSet rs = stmt.executeQuery();

            // Clear data yang ada di tabel
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_kelas"));
                row.add(rs.getString("nama_kelas"));
                row.add(rs.getString("jadwal"));
                row.add(rs.getString("ruang"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data kelas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahKelas() {
        try {
            String idKelasStr = JOptionPane.showInputDialog(this, "Masukkan ID Kelas:");
            if (idKelasStr == null || idKelasStr.isEmpty()) return;
            int idKelas = Integer.parseInt(idKelasStr);

            String query = "INSERT INTO registrasi_kelas (id_kelas, id_mahasiswa, tanggal_registrasi) " +
                           "VALUES (?, ?, CURRENT_DATE)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idKelas);
            stmt.setInt(2, mahasiswaId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Kelas berhasil ditambahkan!");
            loadKelasData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat menambah kelas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Kelas harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editKelas() {
        int selectedRow = tableKelas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kelas yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idKelas = (int) tableModel.getValueAt(selectedRow, 0);

            // Input ID Kelas baru
            String newIdKelasStr = JOptionPane.showInputDialog(this, "Masukkan ID Kelas baru:", idKelas);
            if (newIdKelasStr == null || newIdKelasStr.isEmpty()) return;
            int newIdKelas = Integer.parseInt(newIdKelasStr);

            // Input Tanggal Registrasi baru
            String newTanggalRegistrasi = JOptionPane.showInputDialog(this, "Masukkan tanggal registrasi baru (format: yyyy-MM-dd):");
            if (newTanggalRegistrasi == null || newTanggalRegistrasi.isEmpty()) return;

            // Validasi format tanggal
            if (!newTanggalRegistrasi.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update data di database
            String query = "UPDATE registrasi_kelas SET id_kelas = ?, tanggal_registrasi = ? WHERE id_mahasiswa = ? AND id_kelas = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, newIdKelas);
            stmt.setString(2, newTanggalRegistrasi);
            stmt.setInt(3, mahasiswaId);
            stmt.setInt(4, idKelas);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Kelas berhasil diperbarui!");
            loadKelasData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat mengedit kelas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Kelas harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusKelas() {
        int selectedRow = tableKelas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kelas yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idKelas = (int) tableModel.getValueAt(selectedRow, 0);

            String query = "DELETE FROM registrasi_kelas WHERE id_kelas = ? AND id_mahasiswa = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idKelas);
            stmt.setInt(2, mahasiswaId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Kelas berhasil dihapus!");
            loadKelasData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat menghapus kelas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
