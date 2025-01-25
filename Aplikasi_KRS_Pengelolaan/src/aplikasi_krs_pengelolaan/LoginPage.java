package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, backButton;

    public LoginPage() {
        setTitle("Login KRS");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Komponen form
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        backButton = new JButton("Kembali");

        // Layout
        setLayout(new java.awt.GridLayout(3, 2));
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(backButton);
        add(loginButton);

        // Event Listener Login
        loginButton.addActionListener(e -> login());

        // Event Listener Kembali
        backButton.addActionListener(e -> {
            dispose();
            new Aplikasi_KRS_Pengelolaan().setVisible(true);
        });
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            // Koneksi ke database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
            String query = "SELECT user_id, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");  // Ambil ID user
                String role = resultSet.getString("role");  // Ambil role dari database

                // Jika role adalah mahasiswa
                if (role.equals("mahasiswa")) {
                    int idMahasiswa = getIdMahasiswaFromDatabase(userId);
                    String namaMahasiswa = getNamaMahasiswaFromDatabase(idMahasiswa);
                    
                    if (namaMahasiswa != null) {
                        dispose();  
                        redirectToDashboard(role, idMahasiswa, namaMahasiswa); 
                    } else {
                        JOptionPane.showMessageDialog(this, "Data mahasiswa tidak ditemukan.");
                    }
                } else if (role.equals("dosen")) {
                    // Ambil nama dosen dan dosenId
                    String namaDosen = getNamaDosenFromDatabase(userId);
                    int dosenId = getDosenIdFromDatabase(userId);

                    if (namaDosen != null && dosenId != -1) {
                        dispose();
                        // Kirimkan namaDosen, dosenId, dan connection ke DosenDashboard
                        new DosenDashboard(namaDosen, dosenId, connection).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Data dosen tidak ditemukan.");
                    }
                } else {
                    dispose();
                    redirectToDashboard(role, userId, null); 
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal! Periksa username dan password.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Fungsi untuk mendapatkan id_mahasiswa berdasarkan user_id
    private int getIdMahasiswaFromDatabase(int userId) {
        int idMahasiswa = -1;  // Default jika tidak ditemukan
        try {
            // Query untuk mendapatkan id_mahasiswa berdasarkan user_id
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
            String query = "SELECT id_mahasiswa FROM mahasiswa WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idMahasiswa = resultSet.getInt("id_mahasiswa");
            }

            connection.close(); // Tutup koneksi
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        return idMahasiswa;
    }

    // Fungsi untuk mendapatkan nama mahasiswa berdasarkan id_mahasiswa
    private String getNamaMahasiswaFromDatabase(int idMahasiswa) {
        String namaMahasiswa = "";
        
        try {
            // Query untuk mendapatkan nama mahasiswa berdasarkan id_mahasiswa
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
            String query = "SELECT nama_mahasiswa FROM mahasiswa WHERE id_mahasiswa = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idMahasiswa);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                namaMahasiswa = resultSet.getString("nama_mahasiswa");
            }

            connection.close(); // Tutup koneksi
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        return namaMahasiswa;
    }

    // Fungsi untuk mendapatkan nama dosen berdasarkan user_id
    private String getNamaDosenFromDatabase(int userId) {
        String namaDosen = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
            String query = "SELECT nama_dosen FROM dosen WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                namaDosen = resultSet.getString("nama_dosen");
            }

            connection.close(); // Tutup koneksi
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
        return namaDosen;
    }


    // Fungsi untuk mendapatkan dosenId berdasarkan user_id
    private int getDosenIdFromDatabase(int userId) {
        int dosenId = -1;  // Default jika tidak ditemukan
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");
            String query = "SELECT id_dosen FROM dosen WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                dosenId = resultSet.getInt("id_dosen");
            }

            connection.close(); // Tutup koneksi
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
        return dosenId;
    }


    // Fungsi untuk mengarahkan user ke dashboard berdasarkan role
    private void redirectToDashboard(String role, int userId, String nama) {
        switch (role) {
            case "admin":
                // Untuk admin, cukup role admin
                new AdminDashboard(role).setVisible(true);
                break;

            case "mahasiswa":
                // Jika role mahasiswa, arahkan ke MahasiswaDashboard
                new MahasiswaDashboard(userId, nama).setVisible(true);
                dispose();  // Tutup form login
                break;

            case "dosen":
                try {
                    // Ambil namaDosen dan dosenId dari database
                    String namaDosen = getNamaDosenFromDatabase(userId); // Buat metode untuk mengambil nama
                    int dosenId = getDosenIdFromDatabase(userId); // Buat metode untuk mengambil ID
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistem_krs", "root", "");

                    if (namaDosen != null && dosenId != -1) {
                        new DosenDashboard(namaDosen, dosenId, connection).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Data dosen tidak ditemukan.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error koneksi: " + ex.getMessage());
                }
                break;


            default:
                JOptionPane.showMessageDialog(this, "Role tidak dikenal.");
                break;
        }
    }    
}
