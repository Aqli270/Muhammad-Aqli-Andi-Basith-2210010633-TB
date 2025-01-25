package aplikasi_krs_pengelolaan;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class SettingsDialog extends JDialog {

    public SettingsDialog(JFrame parent) {
        super(parent, "Pengaturan", true);
        setSize(400, 300);
        setLayout(new GridLayout(3, 2, 10, 10));

        // Load saved preferences
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        String savedTheme = prefs.get("theme", "Terang");
        String savedLanguage = prefs.get("language", "Indonesia");

        // Create combo boxes
        JLabel lblTheme = new JLabel("Tema:");
        JComboBox<String> themeComboBox = new JComboBox<>(new String[]{"Terang", "Gelap"});
        themeComboBox.setSelectedItem(savedTheme);

        JLabel lblLanguage = new JLabel("Bahasa:");
        JComboBox<String> languageComboBox = new JComboBox<>(new String[]{"Indonesia", "Inggris"});
        languageComboBox.setSelectedItem(savedLanguage);

        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");

        // Add components to dialog
        add(lblTheme);
        add(themeComboBox);
        add(lblLanguage);
        add(languageComboBox);
        add(btnSave);
        add(btnCancel);

        // Action listeners
        btnSave.addActionListener(e -> {
            // Save preferences
            prefs.put("theme", (String) themeComboBox.getSelectedItem());
            prefs.put("language", (String) languageComboBox.getSelectedItem());

            // Apply theme
            String theme = (String) themeComboBox.getSelectedItem();
            if ("Gelap".equals(theme)) {
                parent.getContentPane().setBackground(Color.DARK_GRAY);
            } else {
                parent.getContentPane().setBackground(Color.WHITE);
            }

            JOptionPane.showMessageDialog(this, "Pengaturan disimpan!");
        });

        btnCancel.addActionListener(e -> dispose());
    }
}
