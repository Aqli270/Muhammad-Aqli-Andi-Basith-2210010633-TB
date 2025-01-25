package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MahasiswaDashboard extends JFrame {
    private JLabel welcomeLabel;
    private int mahasiswaId; // Variabel untuk menyimpan ID mahasiswa
    private Connection connection; // Objek connection

    public MahasiswaDashboard(int mahasiswaId, String namaMahasiswa) {
        this.mahasiswaId = mahasiswaId; // Set ID mahasiswa dari login
        setTitle("Mahasiswa Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inisialisasi koneksi database
        try {
            // Ganti dengan konfigurasi database yang sesuai
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Koneksi database gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel headerLabel = new JLabel("Mahasiswa Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Welcome label
        welcomeLabel = new JLabel("Selamat datang, " + namaMahasiswa + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        headerPanel.add(welcomeLabel);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(7, 1, 10, 10)); // Tambahkan 7 elemen
        menuPanel.setBackground(new Color(240, 240, 240));

        JButton btnKRS = new JButton("KRS");
        JButton btnKelas = new JButton("Kelas");
        JButton btnJadwalKuliah = new JButton("Jadwal Kuliah");
        JButton btnMataKuliah = new JButton("Mata Kuliah");
        JButton btnLihatNilai = new JButton("Lihat Nilai"); // Tombol Lihat Nilai
        JButton btnLaporan = new JButton("Laporan"); // Tombol Laporan (untuk mahasiswa)
        JButton btnLogout = new JButton("Keluar");

        menuPanel.add(btnKRS);
        menuPanel.add(btnKelas);
        menuPanel.add(btnJadwalKuliah);
        menuPanel.add(btnMataKuliah);
        menuPanel.add(btnLihatNilai); // Menambahkan tombol Lihat Nilai
        menuPanel.add(btnLogout);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Action listener untuk tombol-tombol
        btnKRS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new KRSMahasiswaDialog(MahasiswaDashboard.this, mahasiswaId).setVisible(true);
            }
        });

        btnJadwalKuliah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JadwalKuliahDialog(MahasiswaDashboard.this, connection, mahasiswaId).setVisible(true);
            }
        });

        btnKelas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new KelasMahasiswaDialog(MahasiswaDashboard.this, connection, mahasiswaId).setVisible(true);
            }
        });

        btnMataKuliah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MataKuliahDialog(MahasiswaDashboard.this, connection).setVisible(true);
            }
        });

        btnLihatNilai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LihatNilaiDialog(MahasiswaDashboard.this, connection, mahasiswaId).setVisible(true);
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close dashboard
                new LoginPage().setVisible(true); // Go back to login
            }
        });
    }
}
