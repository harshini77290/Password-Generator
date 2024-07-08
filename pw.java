import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.security.SecureRandom;

class PWGApp {
    private static JTextField passwordField; // Declare as a class-level variable

    public static void main(String args[]) {
        JFrame f = new JFrame("Advanced Password Generator");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(new Color(230, 230, 250)); // Set lavender background color
        f.setLayout(null); // Use absolute positioning
        f.setSize(700, 700); // Set frame size to 700x700

        Font timesNewRomanBold = new Font("Times New Roman", Font.BOLD, 16);

        JLabel titleLabel = new JLabel("Advanced Password Generator");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        titleLabel.setBounds(200, 20, 500, 40);

        JLabel lengthLabel = new JLabel("Password Length");
        lengthLabel.setFont(timesNewRomanBold);
        lengthLabel.setBounds(50, 80, 150, 30);

        JSlider l = new JSlider(6, 20, 12);
        l.setMajorTickSpacing(2);
        l.setMinorTickSpacing(1);
        l.setPaintTicks(true);
        l.setPaintLabels(true);
        l.setSnapToTicks(true);
        l.setBounds(220, 80, 200, 50);
        l.setBackground(new Color(230, 230, 250)); // Set background color

        JLabel startTypeLabel = new JLabel("Select Starting Type");
        startTypeLabel.setFont(timesNewRomanBold);
        startTypeLabel.setBounds(50, 150, 200, 30);

        JComboBox<String> startTypeComboBox = new JComboBox<>(new String[]{"Lowercase", "Uppercase", "Digit", "Special Character"});
        startTypeComboBox.setFont(timesNewRomanBold);
        startTypeComboBox.setBounds(220, 150, 200, 30);

        JCheckBox uc = new JCheckBox("Include Uppercase");
        uc.setFont(timesNewRomanBold);
        uc.setBackground(new Color(230, 230, 250)); // Set background color
        uc.setBounds(50, 200, 200, 30);

        JCheckBox lc = new JCheckBox("Include Lowercase");
        lc.setFont(timesNewRomanBold);
        lc.setBackground(new Color(230, 230, 250)); // Set background color
        lc.setBounds(50, 250, 200, 30);

        JCheckBox dc = new JCheckBox("Include Digits");
        dc.setFont(timesNewRomanBold);
        dc.setBackground(new Color(230, 230, 250)); // Set background color
        dc.setBounds(50, 300, 200, 30);

        JCheckBox sc = new JCheckBox("Include Special Characters");
        sc.setFont(timesNewRomanBold);
        sc.setBackground(new Color(230, 230, 250)); // Set background color
        sc.setBounds(50, 350, 250, 30);

        JButton gB = new JButton("Generate Password");
        gB.setFont(timesNewRomanBold);
        gB.setBounds(50, 400, 500, 40);
        gB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int length = l.getValue();
                String charset = "";

                if (uc.isSelected()) {
                    charset += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                }
                if (lc.isSelected()) {
                    charset += "abcdefghijklmnopqrstuvwxyz";
                }
                if (dc.isSelected()) {
                    charset += "0123456789";
                }
                if (sc.isSelected()) {
                    charset += "!@#$%^&*()_-+=<>?/[]{}";
                }

                if (charset.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Select at least one type to generate the password.");
                    return;
                }

                // Get the selected starting type
                String startType = (String) startTypeComboBox.getSelectedItem();

                // Rearrange charset based on the selected starting type
                if ("Uppercase".equals(startType)) {
                    charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + charset;
                } else if ("Lowercase".equals(startType)) {
                    charset = "abcdefghijklmnopqrstuvwxyz" + charset;
                } else if ("Digit".equals(startType)) {
                    charset = "0123456789" + charset;
                } else if ("Special Character".equals(startType)) {
                    charset = "!@#$%^&*()_-+=<>?/[]{}" + charset;
                }

                SecureRandom random = new SecureRandom();
                StringBuilder password = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    int randomIndex = random.nextInt(charset.length());
                    password.append(charset.charAt(randomIndex));
                }

                // Display password in the text field
                passwordField.setText(password.toString());

                // Calculate and display password strength
                int strength = calculatePasswordStrength(password.toString());
                JOptionPane.showMessageDialog(f, "Password Strength: " + getStrengthLabel(strength));
            }
        });

        passwordField = new JTextField(20); // Initialize passwordField here
        passwordField.setEditable(false);
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        passwordField.setBounds(50, 450, 500, 30);

        JButton copy = new JButton("Copy to Clipboard");
        copy.setFont(new Font("Times New Roman", Font.BOLD, 16));
        copy.setBounds(50, 500, 500, 40);
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(passwordField.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                JOptionPane.showMessageDialog(f, "Password copied to clipboard.");
            }
        });

        f.add(titleLabel);
        f.add(lengthLabel);
        f.add(l);
        f.add(startTypeLabel);
        f.add(startTypeComboBox);
        f.add(uc);
        f.add(lc);
        f.add(dc);
        f.add(sc);
        f.add(gB);
        f.add(passwordField);
        f.add(copy);

        f.setVisible(true);
    }

    private static int calculatePasswordStrength(String password) {
        int strength = 0;
        int length = password.length();

        // Length-based strength
        strength += length * 2;

        // Mix of character types
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        if (hasLower) strength += 10;
        if (hasUpper) strength += 10;
        if (hasDigit) strength += 10;
        if (hasSpecial) strength += 10;

        // Avoiding repeated characters
        boolean hasRepeats = false;
        for (int i = 0; i < length - 1; i++) {
            if (password.charAt(i) == password.charAt(i + 1)) {
                hasRepeats = true;
                break;
            }
        }
        if (!hasRepeats) strength += 10;

        return strength;
    }

    private static String getStrengthLabel(int strength) {
        if (strength < 30) return "Weak";
        else if (strength < 50) return "Moderate";
        else if (strength < 70) return "Strong";
        else return "Very Strong";
    }
}
