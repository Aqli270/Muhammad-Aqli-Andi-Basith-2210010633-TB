package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MataKuliahDialog extends JDialog {

    private Connection connection; // Koneksi database
    private JTable table; // Tabel untuk menampilkan data mata kuliah

    public MataKuliahDialog(Frame parent, Connection connection) {
        super(parent, "Daftar Mata Kuliah", true);
        this.connection = connection; // Menggunakan connection yang diberikan dari parameter
        
        initUI();
        loadMataKuliahData(); // Memuat data mata kuliah dari database
    }

    private void initUI() {
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel utama
        JPanel panel = new JPanel(new BorderLayout());

        // Tabel untuk menampilkan mata kuliah
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Tambahkan panel ke dialog
        add(panel);
    }

    private void loadMataKuliahData() {
        try {
            String query = "SELECT id_mata_kuliah AS 'ID', nama_mata_kuliah AS 'Mata Kuliah', sks AS 'SKS' FROM mata_kuliah";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Set data ke tabel
            table.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data mata kuliah!", "Error", JOptionPane.ERROR_MESSAGE);
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
