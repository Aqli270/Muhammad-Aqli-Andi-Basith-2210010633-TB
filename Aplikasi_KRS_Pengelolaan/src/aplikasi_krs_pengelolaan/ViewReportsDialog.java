package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;

public class ViewReportsDialog extends JDialog {

    private Connection connection;
    private JTable coursesTable;
    private JTable usersTable;
    private JTable dosenTable;
    private JTable mahasiswaTable;
    private JTable krsTable;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel usersTableModel;
    private DefaultTableModel dosenTableModel;
    private DefaultTableModel mahasiswaTableModel;
    private DefaultTableModel krsTableModel;

    public ViewReportsDialog(JFrame parent) {
        super(parent, "Laporan", true);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Koneksi ke database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tabel data mata kuliah
        String[] coursesColumnNames = {"ID Mata Kuliah", "Nama Mata Kuliah", "SKS"};
        coursesTableModel = new DefaultTableModel(coursesColumnNames, 0);
        coursesTable = new JTable(coursesTableModel);
        JScrollPane coursesScrollPane = new JScrollPane(coursesTable);

        // Tabel data users
        String[] usersColumnNames = {"User ID", "Username", "Role", "Full Name", "Email"};
        usersTableModel = new DefaultTableModel(usersColumnNames, 0);
        usersTable = new JTable(usersTableModel);
        JScrollPane usersScrollPane = new JScrollPane(usersTable);

        // Tabel data dosen
        String[] dosenColumnNames = {"ID Dosen", "Nama Dosen", "Fakultas", "Email"};
        dosenTableModel = new DefaultTableModel(dosenColumnNames, 0);
        dosenTable = new JTable(dosenTableModel);
        JScrollPane dosenScrollPane = new JScrollPane(dosenTable);

        // Tabel data mahasiswa
        String[] mahasiswaColumnNames = {"ID Mahasiswa", "Nama Mahasiswa", "Program Studi", "Email", "Semester"};
        mahasiswaTableModel = new DefaultTableModel(mahasiswaColumnNames, 0);
        mahasiswaTable = new JTable(mahasiswaTableModel);
        JScrollPane mahasiswaScrollPane = new JScrollPane(mahasiswaTable);

        // Tabel data KRS
        String[] krsColumnNames = {"ID KRS", "ID Mahasiswa", "ID Kelas", "Status"};
        krsTableModel = new DefaultTableModel(krsColumnNames, 0);
        krsTable = new JTable(krsTableModel);
        JScrollPane krsScrollPane = new JScrollPane(krsTable);

        // Tombol Ekspor PDF
        JPanel buttonPanel = new JPanel();
        JButton btnExport = new JButton("Ekspor PDF");
        buttonPanel.add(btnExport);

        // Tambahkan tabel dan tombol ke dialog
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(5, 1));
        tablePanel.add(coursesScrollPane);
        tablePanel.add(usersScrollPane);
        tablePanel.add(dosenScrollPane);
        tablePanel.add(mahasiswaScrollPane);
        tablePanel.add(krsScrollPane);

        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data dari database
        loadCoursesData();
        loadUsersData();
        loadDosenData();
        loadMahasiswaData();
        loadKrsData();

        // Action listener untuk tombol ekspor
        btnExport.addActionListener(e -> exportToPDF());
    }

    private void loadCoursesData() {
        try {
            String query = "SELECT * FROM mata_kuliah";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            coursesTableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel courses
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_mata_kuliah"));
                row.add(rs.getString("nama_mata_kuliah"));
                row.add(rs.getInt("sks"));
                coursesTableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersData() {
        try {
            String query = "SELECT * FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            usersTableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel users
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("role"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("email"));
                usersTableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDosenData() {
        try {
            String query = "SELECT * FROM dosen";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            dosenTableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel dosen
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_dosen"));
                row.add(rs.getString("nama_dosen"));
                row.add(rs.getString("fakultas"));
                row.add(rs.getString("email"));
                dosenTableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMahasiswaData() {
        try {
            String query = "SELECT * FROM mahasiswa";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            mahasiswaTableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel mahasiswa
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_mahasiswa"));
                row.add(rs.getString("nama_mahasiswa"));
                row.add(rs.getString("program_studi"));
                row.add(rs.getString("email"));
                row.add(rs.getString("semester"));  // Menambahkan data semester
                mahasiswaTableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadKrsData() {
        try {
            String query = "SELECT * FROM krs";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Clear existing data
            krsTableModel.setRowCount(0);

            // Menambahkan data ke dalam tabel krs
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_krs"));
                row.add(rs.getInt("id_mahasiswa"));
                row.add(rs.getInt("id_kelas"));
                row.add(rs.getString("status"));
                krsTableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exportToPDF() {
        try {
            // Let the user choose where to save the file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF File");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                
                if (!filePath.endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // Create the PDF document
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Adding the title
                document.add(new Paragraph("Laporan Mata Kuliah, Pengguna, Dosen, Mahasiswa, dan KRS"));

                // Add courses table to the PDF
                document.add(new Paragraph("\nMata Kuliah:"));
                PdfPTable coursesPdfTable = new PdfPTable(3); // 3 columns
                coursesPdfTable.addCell("ID Mata Kuliah");
                coursesPdfTable.addCell("Nama Mata Kuliah");
                coursesPdfTable.addCell("SKS");

                // Fill courses data
                for (int i = 0; i < coursesTableModel.getRowCount(); i++) {
                    coursesPdfTable.addCell(coursesTableModel.getValueAt(i, 0).toString());
                    coursesPdfTable.addCell(coursesTableModel.getValueAt(i, 1).toString());
                    coursesPdfTable.addCell(coursesTableModel.getValueAt(i, 2).toString());
                }
                document.add(coursesPdfTable);

                // Add users table to the PDF
                document.add(new Paragraph("\nPengguna:"));
                PdfPTable usersPdfTable = new PdfPTable(5); // 5 columns
                usersPdfTable.addCell("User ID");
                usersPdfTable.addCell("Username");
                usersPdfTable.addCell("Role");
                usersPdfTable.addCell("Full Name");
                usersPdfTable.addCell("Email");

                // Fill users data
                for (int i = 0; i < usersTableModel.getRowCount(); i++) {
                    usersPdfTable.addCell(usersTableModel.getValueAt(i, 0).toString());
                    usersPdfTable.addCell(usersTableModel.getValueAt(i, 1).toString());
                    usersPdfTable.addCell(usersTableModel.getValueAt(i, 2).toString());
                    usersPdfTable.addCell(usersTableModel.getValueAt(i, 3).toString());
                    usersPdfTable.addCell(usersTableModel.getValueAt(i, 4).toString());
                }
                document.add(usersPdfTable);

                // Add dosen table to the PDF
                document.add(new Paragraph("\nDosen:"));
                PdfPTable dosenPdfTable = new PdfPTable(4); // 4 columns
                dosenPdfTable.addCell("ID Dosen");
                dosenPdfTable.addCell("Nama Dosen");
                dosenPdfTable.addCell("Fakultas");
                dosenPdfTable.addCell("Email");

                // Fill dosen data
                for (int i = 0; i < dosenTableModel.getRowCount(); i++) {
                    dosenPdfTable.addCell(dosenTableModel.getValueAt(i, 0).toString());
                    dosenPdfTable.addCell(dosenTableModel.getValueAt(i, 1).toString());
                    dosenPdfTable.addCell(dosenTableModel.getValueAt(i, 2).toString());
                    dosenPdfTable.addCell(dosenTableModel.getValueAt(i, 3).toString());
                }
                document.add(dosenPdfTable);

                // Add mahasiswa table to the PDF
                document.add(new Paragraph("\nMahasiswa:"));
                PdfPTable mahasiswaPdfTable = new PdfPTable(5); // 5 columns, add semester
                mahasiswaPdfTable.addCell("ID Mahasiswa");
                mahasiswaPdfTable.addCell("Nama Mahasiswa");
                mahasiswaPdfTable.addCell("Program Studi");
                mahasiswaPdfTable.addCell("Email");
                mahasiswaPdfTable.addCell("Semester"); // Add Semester column header

                // Fill mahasiswa data
                for (int i = 0; i < mahasiswaTableModel.getRowCount(); i++) {
                    mahasiswaPdfTable.addCell(mahasiswaTableModel.getValueAt(i, 0).toString());
                    mahasiswaPdfTable.addCell(mahasiswaTableModel.getValueAt(i, 1).toString());
                    mahasiswaPdfTable.addCell(mahasiswaTableModel.getValueAt(i, 2).toString());
                    mahasiswaPdfTable.addCell(mahasiswaTableModel.getValueAt(i, 3).toString());
                    mahasiswaPdfTable.addCell(mahasiswaTableModel.getValueAt(i, 4).toString()); // Add Semester data
                }
                document.add(mahasiswaPdfTable);

                // Add krs table to the PDF
                document.add(new Paragraph("\nKRS:"));
                PdfPTable krsPdfTable = new PdfPTable(4); // 4 columns
                krsPdfTable.addCell("ID KRS");
                krsPdfTable.addCell("ID Mahasiswa");
                krsPdfTable.addCell("ID Kelas");
                krsPdfTable.addCell("Status");

                // Fill krs data
                for (int i = 0; i < krsTableModel.getRowCount(); i++) {
                    krsPdfTable.addCell(krsTableModel.getValueAt(i, 0).toString());
                    krsPdfTable.addCell(krsTableModel.getValueAt(i, 1).toString());
                    krsPdfTable.addCell(krsTableModel.getValueAt(i, 2).toString());
                    krsPdfTable.addCell(krsTableModel.getValueAt(i, 3).toString());
                }
                document.add(krsPdfTable);

                document.close();
                JOptionPane.showMessageDialog(this, "Laporan berhasil diekspor ke PDF!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
