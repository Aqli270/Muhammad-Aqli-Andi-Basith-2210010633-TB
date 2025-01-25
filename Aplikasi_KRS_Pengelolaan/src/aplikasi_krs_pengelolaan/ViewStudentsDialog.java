package aplikasi_krs_pengelolaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewStudentsDialog extends JDialog {
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private Connection connection;
    private int dosenId;

    public ViewStudentsDialog(JFrame parent, Connection connection, int dosenId) {
        super(parent, "Daftar Mahasiswa di Kelas", true);
        this.connection = connection;
        this.dosenId = dosenId;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel headerLabel = new JLabel("Daftar Mahasiswa di Kelas");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID Mahasiswa", "Nama Mahasiswa", "Program Studi", "Semester", "Tanggal Registrasi"}, 0);
        studentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnKembali = new JButton("Kembali");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnKembali);

        // Add panels to dialog
        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadStudentData();

        // Button actions
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudentData();
            }
        });

        btnKembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadStudentData() {
        // Clear table
        tableModel.setRowCount(0);

        // Query to get students from classes taught by a specific dosen
        String query = "SELECT m.id_mahasiswa, m.nama_mahasiswa, m.program_studi, m.semester, r.tanggal_registrasi " +
                       "FROM registrasi_kelas r " +
                       "JOIN mahasiswa m ON r.id_mahasiswa = m.id_mahasiswa " +
                       "JOIN kelas k ON r.id_kelas = k.id_kelas " +
                       "JOIN dosen_kelas dk ON k.id_kelas = dk.id_kelas " +  // Join with dosen_kelas to filter by dosen
                       "WHERE dk.id_dosen = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, dosenId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idMahasiswa = rs.getInt("id_mahasiswa");
                String namaMahasiswa = rs.getString("nama_mahasiswa");
                String programStudi = rs.getString("program_studi");
                String semester = rs.getString("semester");
                String tanggalRegistrasi = rs.getString("tanggal_registrasi");

                tableModel.addRow(new Object[]{idMahasiswa, namaMahasiswa, programStudi, semester, tanggalRegistrasi});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data mahasiswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
