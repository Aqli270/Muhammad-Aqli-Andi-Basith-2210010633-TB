/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;

public class Aplikasi_KRS_Pengelolaan extends JFrame {
    private JButton btnLogin, btnRegister;

    public Aplikasi_KRS_Pengelolaan() {
        setTitle("Aplikasi KRS");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout Manager
        setLayout(new BorderLayout());

        // Panel untuk tombol-tombol
        JPanel panelButtons = new JPanel();
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        panelButtons.add(btnLogin);
        panelButtons.add(btnRegister);

        // Menambahkan panel tombol ke bagian bawah
        add(panelButtons, BorderLayout.SOUTH);

        // Action button
        btnLogin.addActionListener(e -> {
            // Action untuk login
            showLoginForm();
        });

        btnRegister.addActionListener(e -> {
            // Action untuk register
            showRegisterForm();
        });
    }

    private void showLoginForm() {
        // Create and show the login form
        new LoginPage().setVisible(true);
        this.setVisible(false); // Optionally hide the main window
    }

    private void showRegisterForm() {
       // Create and show the register form
       new RegisterPage().setVisible(true);
       this.setVisible(false); // Optionally hide the main window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Aplikasi_KRS_Pengelolaan().setVisible(true);
        });
    }
}
