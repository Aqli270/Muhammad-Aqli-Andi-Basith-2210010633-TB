package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends JFrame {
    private JTextField txtUsername, txtFullName, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnRegister, btnBack;
    private Users users;

    public RegisterPage() {
        users = new Users(); // Initialize Users class to handle database operations

        setTitle("Registrasi Pengguna");
        setLayout(null);
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(30, 30, 100, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(120, 30, 150, 25);
        add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(30, 70, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(120, 70, 150, 25);
        add(txtPassword);

        JLabel lblFullName = new JLabel("Nama Lengkap:");
        lblFullName.setBounds(30, 110, 100, 25);
        add(lblFullName);

        txtFullName = new JTextField();
        txtFullName.setBounds(120, 110, 150, 25);
        add(txtFullName);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 150, 100, 25);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(120, 150, 150, 25);
        add(txtEmail);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(30, 190, 100, 25);
        add(lblRole);

        cmbRole = new JComboBox<>(new String[] {"admin", "dosen", "mahasiswa"});
        cmbRole.setBounds(120, 190, 150, 25);
        add(cmbRole);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(90, 230, 100, 30);
        add(btnRegister);

        btnBack = new JButton("Kembali");
        btnBack.setBounds(90, 270, 100, 30);  // Position it below the register button
        add(btnBack);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String fullName = txtFullName.getText();
                String email = txtEmail.getText();
                String role = cmbRole.getSelectedItem().toString();

                if (users.checkUsernameExists(username)) {
                    JOptionPane.showMessageDialog(null, "Username sudah terdaftar!");
                } else {
                    boolean success = users.addUser(username, password, role, fullName, email);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Pendaftaran berhasil!");
                        dispose();  // Menutup halaman registrasi
                        new LoginPage().setVisible(true);  // Menampilkan halaman login
                    } else {
                        JOptionPane.showMessageDialog(null, "Pendaftaran gagal!");
                    }
                }
            }
        });

        // ActionListener untuk tombol kembali
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Menutup halaman registrasi
                new Aplikasi_KRS_Pengelolaan().setVisible(true);  // Menampilkan halaman utama
            }
        });
    }
}
