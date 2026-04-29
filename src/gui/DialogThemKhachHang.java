package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogThemKhachHang extends JDialog {

    private JTextField txtTenKhachHang;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JTextField txtDiaChi;
    private JComboBox<String> cbLoaiKhach;
    
    private boolean isAdded = false;
    private String addedName = "";

    public DialogThemKhachHang(Window owner, String sdtGoiY) {
        super(owner, "Thêm khách hàng", ModalityType.APPLICATION_MODAL);
        setSize(550, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setUndecorated(true); // Tắt thanh tiêu đề mặc định của Windows
        
        // Viền ngoài cho toàn bộ Dialog
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(111, 78, 55), 2)); // Viền nâu
        setContentPane(contentPane);

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(111, 78, 55)); // Màu Nâu Cà phê
        pnlHeader.setPreferredSize(new Dimension(0, 40));
        
        JLabel lblTitle = new JLabel("Thêm khách hàng", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        
        // Nút tắt (X) ở góc phải Header
        JButton btnClose = new JButton("X");
        btnClose.setFont(new Font("Arial", Font.BOLD, 16));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(new Color(111, 78, 55));
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setOpaque(true);
        btnClose.addActionListener(e -> dispose());
        // Hiệu ứng hover cho nút X
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClose.setBackground(Color.RED);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClose.setBackground(new Color(111, 78, 55));
            }
        });
        pnlHeader.add(btnClose, BorderLayout.EAST);
        
        contentPane.add(pnlHeader, BorderLayout.NORTH);

        // --- BODY (FORM) ---
        JPanel pnlBody = new JPanel();
        pnlBody.setLayout(null);
        pnlBody.setBackground(Color.WHITE);
        contentPane.add(pnlBody, BorderLayout.CENTER);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        int startX = 30, startY = 20, lblWidth = 120, txtWidth = 350, height = 30, gapY = 40;

        // Tên khách hàng
        JLabel lblTen = new JLabel("Tên khách hàng:");
        lblTen.setFont(labelFont);
        lblTen.setBounds(startX, startY, lblWidth, height);
        pnlBody.add(lblTen);

        txtTenKhachHang = new JTextField();
        txtTenKhachHang.setFont(textFont);
        txtTenKhachHang.setBounds(startX + lblWidth, startY, txtWidth, height);
        pnlBody.add(txtTenKhachHang);

        // SĐT
        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setFont(labelFont);
        lblSDT.setBounds(startX, startY + gapY, lblWidth, height);
        pnlBody.add(lblSDT);

        txtSDT = new JTextField(sdtGoiY);
        txtSDT.setFont(textFont);
        txtSDT.setBounds(startX + lblWidth, startY + gapY, txtWidth, height);
        txtSDT.setEditable(false);
        txtSDT.setBackground(new Color(240, 240, 240)); // Xám nhẹ vì không cho sửa
        pnlBody.add(txtSDT);

        // Email & Loại khách
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        lblEmail.setBounds(startX, startY + gapY * 2, lblWidth, height);
        pnlBody.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(textFont);
        txtEmail.setBounds(startX + lblWidth, startY + gapY * 2, 140, height);
        pnlBody.add(txtEmail);

        JLabel lblLoai = new JLabel("Loại khách:", SwingConstants.RIGHT);
        lblLoai.setFont(labelFont);
        lblLoai.setBounds(startX + lblWidth + 150, startY + gapY * 2, 80, height);
        pnlBody.add(lblLoai);

        cbLoaiKhach = new JComboBox<>(new String[]{"Khách mới", "Thường", "Thân thiết", "VIP"});
        cbLoaiKhach.setFont(textFont);
        cbLoaiKhach.setBounds(startX + lblWidth + 240, startY + gapY * 2, 110, height);
        cbLoaiKhach.setBackground(Color.WHITE);
        pnlBody.add(cbLoaiKhach);

        // Địa chỉ
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(labelFont);
        lblDiaChi.setBounds(startX, startY + gapY * 3, lblWidth, height);
        pnlBody.add(lblDiaChi);

        txtDiaChi = new JTextField();
        txtDiaChi.setFont(textFont);
        txtDiaChi.setBounds(startX + lblWidth, startY + gapY * 3, txtWidth, height);
        pnlBody.add(txtDiaChi);

        // --- Dòng Kẻ Dưới ---
        JSeparator sep = new JSeparator();
        sep.setBounds(10, 210, 530, 2);
        pnlBody.add(sep);

        // --- FOOTER BUTTONS ---
        JButton btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(211, 84, 0)); // Màu đỏ cam
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.setBounds(30, 230, 100, 35);
        btnHuy.setFocusPainted(false);
        btnHuy.setBorderPainted(false);
        btnHuy.setOpaque(true);
        pnlBody.add(btnHuy);

        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBackground(new Color(133, 193, 233)); // Xanh da trời lợt
        btnLamMoi.setForeground(Color.BLACK);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.setBounds(270, 230, 100, 35);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setBorderPainted(false);
        btnLamMoi.setOpaque(true);
        pnlBody.add(btnLamMoi);

        JButton btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(41, 128, 185)); // Xanh biển đậm
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThem.setBounds(380, 230, 100, 35);
        btnThem.setFocusPainted(false);
        btnThem.setBorderPainted(false);
        btnThem.setOpaque(true);
        pnlBody.add(btnThem);

        // --- ACTIONS ---
        btnHuy.addActionListener(e -> dispose());
        
        btnLamMoi.addActionListener(e -> {
            txtTenKhachHang.setText("");
            txtEmail.setText("");
            txtDiaChi.setText("");
            cbLoaiKhach.setSelectedIndex(0);
            txtTenKhachHang.requestFocus();
        });

        btnThem.addActionListener(e -> {
            String ten = txtTenKhachHang.getText().trim();
            String sdt = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();
            String loaiKhach = cbLoaiKhach.getSelectedItem().toString();
            String diaChi = txtDiaChi.getText().trim();

            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên khách hàng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtTenKhachHang.requestFocus();
                return;
            }
            
            // Khởi tạo Entity KhachHang (maKH = 0 vì DB sẽ tự tăng, diemTichLuy = 0)
            entity.KhachHang kh = new entity.KhachHang(0, ten, sdt, diaChi, 0, email, loaiKhach);
            
            // Gọi DAO để lưu xuống Database
            dao.KhachHangDAO khDAO = new dao.KhachHangDAO();
            int newMaKH = khDAO.themKhachHang(kh);
            
            if (newMaKH > 0) {
                isAdded = true;
                addedName = ten;
                // Có thể lưu thêm newMaKH vào biến nếu cần truyền qua form Đặt bàn
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!\nMã KH: " + newMaKH, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                // Nếu kết nối DB thất bại hoặc chưa có CSDL thực tế, ta vẫn cho pass ở chế độ Demo
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Không thể lưu vào Cơ sở dữ liệu (chưa kết nối SQL).\nTuy nhiên, bạn có muốn tiếp tục giữ tên khách hàng này cho form Đặt bàn (Chế độ Demo) không?", 
                    "Lỗi Database", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    isAdded = true;
                    addedName = ten;
                    dispose();
                }
            }
        });
    }

    public boolean isAdded() {
        return isAdded;
    }

    public String getAddedName() {
        return addedName;
    }
}
