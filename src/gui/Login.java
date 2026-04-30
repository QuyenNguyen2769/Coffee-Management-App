package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.geom.RoundRectangle2D;

public class Login extends JFrame {

    public Login() {
        setTitle("Hệ Thống Quản Lý Quán Cà Phê");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Tấm nền chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // --- Phần bên trái  ---
        JPanel DarkPanel = new JPanel();
        DarkPanel.setBackground(new Color(10, 35, 50));
        DarkPanel.setPreferredSize(new Dimension(500, 400));
        DarkPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcBlue = new GridBagConstraints();
        gbcBlue.gridx = 0;
        gbcBlue.anchor = GridBagConstraints.CENTER;

        // Khung logo 
        JPanel logoFrame = new JPanel(new BorderLayout());
        logoFrame.setBackground(new Color(242, 235, 225)); // Đổi màu giống hình 1
        logoFrame.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // logo - tìm trong thư mục images/ của project
        String[] logoPaths = {
            "images/coffee_logo.jpg",
            "images/logo_cafe.png",
            "images/logo_cafe.jpg",
            "images/coffee_logo.png"
        };
        java.io.File logoFile = null;
        for (String path : logoPaths) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) { logoFile = f; break; }
        }
        if (logoFile != null) {
            ImageIcon originalIcon = new ImageIcon(logoFile.getAbsolutePath());
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel coffeeLogoLabel = new JLabel(new ImageIcon(scaledImage));
            logoFrame.add(coffeeLogoLabel, BorderLayout.CENTER);
        }

        gbcBlue.gridy = 0;
        DarkPanel.add(logoFrame, gbcBlue);

        // Văn bản tiêu đề
        JLabel titleLabel = new JLabel("QUẢN LÝ QUÁN CÀ PHÊ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbcBlue.gridy = 1;
        gbcBlue.insets = new Insets(25, 0, 5, 0);
        DarkPanel.add(titleLabel, gbcBlue);

        // Slogan
        JLabel sloganLabel = new JLabel("Hương vị nồng nàn - Phục vụ tận tình");
        sloganLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        sloganLabel.setForeground(new Color(200, 200, 200));
        gbcBlue.gridy = 2;
        gbcBlue.insets = new Insets(0, 0, 20, 0);
        DarkPanel.add(sloganLabel, gbcBlue);

        mainPanel.add(DarkPanel, BorderLayout.WEST);

        // --- Phần bên phải ---
        JPanel whitePanel = new JPanel();
        whitePanel.setBackground(new Color(242, 235, 225)); // Đổi màu giống hình 1
        whitePanel.setLayout(new GridBagLayout());
        // Bỏ viền phân cách đen, trả lại EmptyBorder cũ
        whitePanel.setBorder(new EmptyBorder(50, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        Font modernFont = new Font("Arial", Font.PLAIN, 16);
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        JLabel loginTitle = new JLabel("ĐĂNG NHẬP");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 32));
        loginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        whitePanel.add(loginTitle, gbc);

        JLabel subTitle = new JLabel("Vui lòng đăng nhập để tiếp tục");
        subTitle.setFont(modernFont);
        subTitle.setForeground(new Color(150, 150, 150));
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        whitePanel.add(subTitle, gbc);

        gbc.gridwidth = 1;
        
        // ID Field
        JLabel idLabel = new JLabel("Mã Tài Khoản / Số Điện Thoại:");
        idLabel.setFont(labelFont);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        whitePanel.add(idLabel, gbc);

        JTextField idField = new JTextField();
        idField.setFont(modernFont);
        idField.setPreferredSize(new Dimension(300, 45));
        idField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 5, 15, 5);
        whitePanel.add(idField, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Mật Khẩu:");
        passwordLabel.setFont(labelFont);
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        whitePanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(modernFont);
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        passwordField.setEchoChar('•');
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 5, 10, 5);
        whitePanel.add(passwordField, gbc);

        // Show Password Checkbox
        JCheckBox showPasswordCheckbox = new JCheckBox("Hiện mật khẩu");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 13));
        showPasswordCheckbox.setOpaque(false);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        whitePanel.add(showPasswordCheckbox, gbc);

        // Nút Đăng Nhập 
        RoundedButton loginButton = new RoundedButton("ĐĂNG NHẬP", 20);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(45, 23, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 50));
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 5, 10, 5);
        whitePanel.add(loginButton, gbc);

        // Nút Thoát 
        RoundedButton exitButton = new RoundedButton("THOÁT", 20);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBackground(new Color(230, 230, 230));
        exitButton.setForeground(Color.BLACK);
        exitButton.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = 8;
        whitePanel.add(exitButton, gbc);

        JLabel copyrightLabel = new JLabel("© 2026 Coffee Management System");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(180, 180, 180));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 9;
        gbc.insets = new Insets(40, 0, 0, 0);
        whitePanel.add(copyrightLabel, gbc);

        mainPanel.add(whitePanel, BorderLayout.CENTER);

        // --- Xử lý sự kiện ---
        
        exitButton.addActionListener(e -> System.exit(0));
        showPasswordCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        // Xử lý Đăng Nhập
        loginButton.addActionListener(e -> {
            String username = idField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Gọi DAO để kiểm tra trong Database
            dao.TaiKhoanDAO tkDAO = new dao.TaiKhoanDAO();
            entity.TaiKhoan tk = tkDAO.checkLogin(username, password);

            if (tk != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                new TrangChu().setVisible(true);
                this.dispose();
            } else if (username.equals("Admin") && password.equals("123")) {
                // Backup cứng phòng trường hợp Database chưa có dữ liệu
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công (Tài khoản dự phòng)!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                new TrangChu().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String label, int radius) {
            super(label);
            this.radius = radius;
            setContentAreaFilled(false); 
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
