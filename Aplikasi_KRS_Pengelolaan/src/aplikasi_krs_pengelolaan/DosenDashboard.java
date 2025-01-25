package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class DosenDashboard extends JFrame {
    private JLabel welcomeLabel;
    private int dosenId;
    private Connection connection;
    private String namaDosen;

    // Konstruktor dengan parameter dosenId dan connection
    public DosenDashboard(String namaDosen, int dosenId, Connection connection) {
        this.dosenId = dosenId;
        this.connection = connection;
        this.namaDosen = namaDosen;
        setTitle("Dashboard Dosen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.dosenId = dosenId;
        this.connection = connection;

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel headerLabel = new JLabel("Dashboard Dosen");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Welcome label
        welcomeLabel = new JLabel("Selamat datang, " + namaDosen + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        headerPanel.add(welcomeLabel);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(8, 1, 10, 10)); // Ubah jumlah baris menjadi 8
        menuPanel.setBackground(new Color(240, 240, 240));

        // Tombol menu
        JButton btnLihatMataKuliah = new JButton("Lihat Mata Kuliah");
        JButton btnDaftarMahasiswa = new JButton("Daftar Mahasiswa di Kelas");
        JButton btnRekapInputNilai = new JButton("Rekap dan Input Nilai");
        JButton btnValidasiKRS = new JButton("Validasi KRS");
        JButton btnStatistikMataKuliah = new JButton("Statistik Mata Kuliah");
        JButton btnProfilDosen = new JButton("Profil Dosen");
        JButton btnNotifikasi = new JButton("Notifikasi");
        JButton btnKeluar = new JButton("Keluar");

        menuPanel.add(btnLihatMataKuliah);
        menuPanel.add(btnDaftarMahasiswa);
        menuPanel.add(btnRekapInputNilai);
        menuPanel.add(btnValidasiKRS);
        menuPanel.add(btnStatistikMataKuliah);
        menuPanel.add(btnKeluar);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Action listeners
        btnLihatMataKuliah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewCoursesDialog(DosenDashboard.this, connection, dosenId).setVisible(true);
            }
        });

        btnDaftarMahasiswa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewStudentsDialog(DosenDashboard.this, connection, dosenId).setVisible(true);
            }
        });

        btnRekapInputNilai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InputGradesDialog(DosenDashboard.this, connection).setVisible(true);
            }
        });

        btnValidasiKRS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ValidateKRSDialog(DosenDashboard.this, connection, dosenId).setVisible(true);
            }
        });

        btnStatistikMataKuliah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StatistikMatkulDialog(DosenDashboard.this, connection).setVisible(true);
            }
        });

        btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close dashboard
                new LoginPage().setVisible(true); // Go back to login
            }
        });
    }
}
