package aplikasi_krs_pengelolaan;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class ViewCoursesDialog extends JDialog {

    private JTable tableCourses;
    private DefaultTableModel tableModel;
    private JButton btnClose;
    private Connection connection;
    private int dosenId;

    // Konstruktor yang menerima parameter dosenId
    public ViewCoursesDialog(JFrame parent, Connection connection, int dosenId) {
        super(parent, "Lihat Mata Kuliah Dosen", true);
        this.connection = connection;
        this.dosenId = dosenId;  // Menyimpan dosenId

        // Inisialisasi tabel
        String[] columnNames = {"ID Mata Kuliah", "Nama Mata Kuliah", "Jumlah SKS"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableCourses = new JTable(tableModel);

        // Inisialisasi tombol
        btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dispose());

        // Layout
        JScrollPane scrollPane = new JScrollPane(tableCourses);
        JPanel panelBottom = new JPanel();
        panelBottom.add(btnClose);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        // Load data mata kuliah berdasarkan dosenId
        loadCoursesData();

        // Konfigurasi dialog
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void loadCoursesData() {
        try {
            System.out.println("Mencoba mengambil data mata kuliah untuk dosen ID: " + dosenId);

            // Query untuk mengambil data mata kuliah berdasarkan dosenId
            String query = "SELECT m.id_mata_kuliah, m.nama_mata_kuliah, m.sks " +
                           "FROM mata_kuliah m " +
                           "JOIN mata_kuliah_dosen md ON m.id_mata_kuliah = md.id_mata_kuliah " +
                           "WHERE md.id_dosen = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, dosenId);
            ResultSet rs = stmt.executeQuery();

            // Clear data yang ada di tabel
            tableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_mata_kuliah"));
                row.add(rs.getString("nama_mata_kuliah"));
                row.add(rs.getInt("sks"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memuat data mata kuliah: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
