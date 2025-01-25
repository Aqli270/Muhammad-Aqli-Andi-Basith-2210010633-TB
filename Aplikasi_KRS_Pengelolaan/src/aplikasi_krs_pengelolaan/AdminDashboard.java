package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    private JLabel welcomeLabel;

    public AdminDashboard(String role) {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel headerLabel = new JLabel("Admin Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + role + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        headerPanel.add(welcomeLabel);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(8, 1, 10, 10));
        menuPanel.setBackground(new Color(240, 240, 240));

        JButton btnManageUsers = new JButton("Kelola Pengguna");
        JButton btnManageCourses = new JButton("Kelola Mata Kuliah");
        JButton btnManageDosen = new JButton("Kelola Dosen");
        JButton btnManageMahasiswa = new JButton("Kelola Mahasiswa");
        JButton btnManageKRS = new JButton("Kelola KRS");
        JButton btnViewReports = new JButton("Laporan");
        JButton btnSettings = new JButton("Pengaturan");
        JButton btnLogout = new JButton("Keluar");

        menuPanel.add(btnManageUsers);
        menuPanel.add(btnManageCourses);
        menuPanel.add(btnManageDosen);
        menuPanel.add(btnManageMahasiswa);
        menuPanel.add(btnManageKRS);
        menuPanel.add(btnViewReports);
        menuPanel.add(btnSettings);
        menuPanel.add(btnLogout);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Action listeners
        btnManageUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageUsersDialog(AdminDashboard.this).setVisible(true);
            }
        });

        btnManageCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageCoursesDialog(AdminDashboard.this).setVisible(true);
            }
        });

        btnManageDosen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageDosenDialog(AdminDashboard.this).setVisible(true);
            }
        });

        btnManageMahasiswa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageMahasiswaDialog(AdminDashboard.this).setVisible(true);
            }
        });

        btnManageKRS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageKRSDialog(AdminDashboard.this).setVisible(true);
            }
        });

        btnViewReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewReportsDialog(AdminDashboard.this).setVisible(true);
            }
        });

        btnSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingsDialog(AdminDashboard.this).setVisible(true);
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
