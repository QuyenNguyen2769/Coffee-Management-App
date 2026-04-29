package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogDatBan extends JDialog {

    private JTextField txtSDT;
    private JLabel lblTenKhach;
    private JLabel lblCheck;

    public DialogDatBan(Window owner, String idBan, String trangThai, String sucChua, String khuVuc) {
        super(owner, "Đặt Bàn", ModalityType.APPLICATION_MODAL);
        setSize(480, 400); // Điều chỉnh lại chiều cao form
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlHeader.setBackground(new Color(111, 78, 55)); // Màu Nâu Cà phê
        JLabel lblTitle = new JLabel("Đặt bàn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // Body
        JPanel pnlBody = new JPanel();
        pnlBody.setLayout(null);
        pnlBody.setBackground(new Color(225, 228, 232));
        add(pnlBody, BorderLayout.CENTER);

        // Danh sách bàn tự động xếp
        JLabel lblBanCapTitle = new JLabel("Bàn cấp:");
        lblBanCapTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBanCapTitle.setBounds(30, 15, 80, 30);
        pnlBody.add(lblBanCapTitle);

        JTextField txtBanCap = new JTextField("Đang tính toán...");
        txtBanCap.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtBanCap.setForeground(new Color(34, 139, 34)); // Màu xanh lá
        txtBanCap.setEditable(false);
        txtBanCap.setBorder(null);
        txtBanCap.setOpaque(false);
        txtBanCap.setBounds(120, 15, 320, 30);
        pnlBody.add(txtBanCap);

        // Dòng 1: Khu vực & Sức chứa tổng
        JLabel lblKhuVucTitle = new JLabel("Khu vực:");
        lblKhuVucTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblKhuVucTitle.setBounds(30, 60, 80, 25);
        pnlBody.add(lblKhuVucTitle);

        JComboBox<String> cbKhuVuc = new JComboBox<>(new String[]{"Tầng 1", "Tầng 2", "Sân vườn", "VIP"});
        cbKhuVuc.setSelectedItem(khuVuc);
        cbKhuVuc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbKhuVuc.setBounds(120, 60, 100, 25);
        cbKhuVuc.setBackground(Color.WHITE);
        pnlBody.add(cbKhuVuc);

        JLabel lblSucChuaTitle = new JLabel("Sức chứa:");
        lblSucChuaTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSucChuaTitle.setBounds(260, 60, 80, 25);
        pnlBody.add(lblSucChuaTitle);

        JLabel lblSucChuaVal = new JLabel("0 người");
        lblSucChuaVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSucChuaVal.setForeground(Color.BLUE);
        lblSucChuaVal.setBounds(340, 60, 100, 25);
        pnlBody.add(lblSucChuaVal);

        // Dòng kẻ dọc
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setForeground(Color.DARK_GRAY);
        separator.setBounds(235, 65, 5, 50);
        pnlBody.add(separator);

        // Dòng 2: Số khách & Loại bàn
        JLabel lblSoKhach = new JLabel("Số khách:");
        lblSoKhach.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSoKhach.setBounds(30, 95, 80, 25);
        pnlBody.add(lblSoKhach);

        JSpinner spnSoKhach = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
        spnSoKhach.setBounds(120, 95, 60, 28);
        pnlBody.add(spnSoKhach);

        JLabel lblLoaiBanTitle = new JLabel("Loại bàn:");
        lblLoaiBanTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLoaiBanTitle.setBounds(260, 95, 80, 25);
        pnlBody.add(lblLoaiBanTitle);

        JLabel lblLoaiBanVal = new JLabel("Bàn Thường");
        lblLoaiBanVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLoaiBanVal.setBounds(340, 95, 100, 25);
        pnlBody.add(lblLoaiBanVal);

        // Giờ đến
        JLabel lblGioDen = new JLabel("Giờ đến:");
        lblGioDen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblGioDen.setBounds(30, 135, 80, 25);
        pnlBody.add(lblGioDen);

        String defaultTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(System.currentTimeMillis() + 3600000));
        JTextField txtGioDen = new JTextField(defaultTime);
        txtGioDen.setBounds(120, 135, 180, 28);
        pnlBody.add(txtGioDen);

        // SĐT Khách
        JLabel lblSDT = new JLabel("SĐT Khách:");
        lblSDT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSDT.setBounds(30, 175, 80, 25);
        pnlBody.add(lblSDT);

        txtSDT = new JTextField();
        txtSDT.setBounds(120, 175, 170, 28);
        pnlBody.add(txtSDT);

        JButton btnKiemTra = new JButton("Kiểm tra") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                g2.setColor(new Color(0, 122, 204)); // Blue border
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnKiemTra.setBounds(300, 175, 90, 28); 
        btnKiemTra.setForeground(new Color(0, 122, 204));
        btnKiemTra.setFocusPainted(false);
        btnKiemTra.setContentAreaFilled(false);
        btnKiemTra.setBorderPainted(false);
        pnlBody.add(btnKiemTra);

        // Sử dụng Icon vẽ tay (Graphics2D) để thay thế hoàn toàn ký tự Unicode, đảm bảo không bao giờ lỗi ô vuông
        class CheckIcon implements javax.swing.Icon {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(34, 139, 34)); // Xanh lá
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 2, y + 12, x + 8, y + 18);
                g2.drawLine(x + 8, y + 18, x + 18, y + 4);
                g2.dispose();
            }
            public int getIconWidth() { return 20; }
            public int getIconHeight() { return 24; }
        }

        javax.swing.Icon checkIconToUse = new CheckIcon();
        try {
            java.io.File fileTick = new java.io.File("images/tick_check.png");
            if (fileTick.exists()) {
                Image imgTick = new ImageIcon(fileTick.getPath()).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                checkIconToUse = new ImageIcon(imgTick);
            }
        } catch (Exception ex) {}

        lblCheck = new JLabel(checkIconToUse);
        lblCheck.setBounds(400, 175, 40, 28);
        lblCheck.setVisible(false); // Ẩn mặc định
        pnlBody.add(lblCheck);

        // Tên khách (Chuyển thành Label tĩnh)
        JLabel lblTen = new JLabel("Tên khách:");
        lblTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTen.setBounds(30, 215, 80, 25);
        pnlBody.add(lblTen);

        lblTenKhach = new JLabel("...");
        lblTenKhach.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTenKhach.setBounds(130, 215, 300, 28);
        pnlBody.add(lblTenKhach);

        // Footer buttons
        JButton btnQuayLai = new JButton("Quay lại") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnQuayLai.setBackground(new Color(0, 122, 204));
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnQuayLai.setBounds(30, 265, 100, 35);
        btnQuayLai.setFocusPainted(false);
        btnQuayLai.setContentAreaFilled(false);
        btnQuayLai.setBorderPainted(false);
        pnlBody.add(btnQuayLai);

        JButton btnDatBan = new JButton("Đặt bàn") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnDatBan.setBackground(new Color(34, 139, 34));
        btnDatBan.setForeground(Color.WHITE);
        btnDatBan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDatBan.setBounds(330, 265, 100, 35);
        btnDatBan.setFocusPainted(false);
        btnDatBan.setContentAreaFilled(false);
        btnDatBan.setBorderPainted(false);
        pnlBody.add(btnDatBan);

        // Sự kiện nút Đặt bàn (Luồng xử lý mới)
        btnDatBan.addActionListener(e -> {
            String banCap = txtBanCap.getText();
            if (banCap.equals("Không đủ bàn trống!") || banCap.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng xếp bàn hợp lệ trước khi đặt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!lblCheck.isVisible()) {
                JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra và xác nhận thông tin Khách hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Bước 1: Confirm đặt bàn (Hình 1)
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận đặt bàn " + banCap + "?", "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                
                // TODO: Gọi hàm lưu Phiếu Đặt Bàn xuống Database tại đây
                
                // Bước 2: Báo thành công và hỏi thêm dịch vụ (Hình 2)
                int goiDichVu = JOptionPane.showConfirmDialog(this, "Đặt bàn thành công - Bạn có muốn gọi món (thêm dịch vụ) không?", "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (goiDichVu == JOptionPane.YES_OPTION) {
                    // Bước 3: Mở form Gọi món (Hình 3)
                    this.dispose(); // Đóng form đặt bàn hiện tại
                    DialogGoiMon dialogGoiMon = new DialogGoiMon(SwingUtilities.getWindowAncestor(this), banCap);
                    dialogGoiMon.setVisible(true);
                } else {
                    this.dispose(); // Đóng form đặt bàn
                }
            }
        });

        // Hàm xử lý thuật toán xếp bàn tự động
        Runnable autoXepBan = () -> {
            int soKhach = (int) spnSoKhach.getValue();
            String currentKhuVuc = (String) cbKhuVuc.getSelectedItem();
            
            // Cập nhật loại bàn
            lblLoaiBanVal.setText(currentKhuVuc.equals("VIP") ? "Bàn VIP" : "Bàn Thường");

            // Parse thời gian từ txtGioDen
            java.time.LocalDateTime gioDenReq;
            try {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                gioDenReq = java.time.LocalDateTime.parse(txtGioDen.getText().trim(), formatter);
            } catch (Exception ex) {
                // Nếu lỗi format, mặc định lấy giờ hiện tại + 1 tiếng
                gioDenReq = java.time.LocalDateTime.now().plusHours(1);
            }

            // Lấy danh sách bàn thực tế từ Database thông qua DAO (Logic thông minh khóa +- 2 tiếng)
            dao.BanDAO banDAO = new dao.BanDAO();
            java.util.ArrayList<entity.Ban> dsBanTrong = banDAO.getBanTrong(currentKhuVuc, gioDenReq);
            
            int size = dsBanTrong.size();
            int[] capacities = new int[size];
            String[] ids = new String[size];
            
            for (int i = 0; i < size; i++) {
                capacities[i] = dsBanTrong.get(i).getSucChua();
                // Định dạng số bàn thành 3 chữ số (VD: 001, 002)
                ids[i] = String.format("%03d", dsBanTrong.get(i).getSoBan());
            }

            int soNguoiThieu = soKhach;
            String banCapStr = "";
            int tongSucChua = 0;
            
            boolean[] used = new boolean[capacities.length];
            
            while (soNguoiThieu > 0) {
                int bestIdx = -1;
                
                // Cố gắng tìm một bàn vừa đủ (bàn nhỏ nhất nhưng sức chứa >= số người thiếu)
                // Lưu ý: capacities đang sắp xếp giảm dần. Nếu duyệt từ cuối lên (từ nhỏ đến lớn), 
                // bàn đầu tiên thỏa mãn sẽ là bàn "vừa vặn" nhất.
                for (int i = capacities.length - 1; i >= 0; i--) {
                    if (!used[i] && capacities[i] >= soNguoiThieu) {
                        bestIdx = i;
                        break;
                    }
                }
                
                // Nếu chưa tìm được bàn nào đủ lớn (nghĩa là số người thiếu lớn hơn sức chứa của tất cả các bàn còn trống),
                // thì chọn bàn to nhất còn trống.
                if (bestIdx == -1) {
                    for (int i = 0; i < capacities.length; i++) {
                        if (!used[i]) {
                            bestIdx = i;
                            break;
                        }
                    }
                }
                
                // Nếu đã hết bàn trống
                if (bestIdx == -1) break;
                
                used[bestIdx] = true;
                banCapStr += ids[bestIdx] + ", ";
                tongSucChua += capacities[bestIdx];
                soNguoiThieu -= capacities[bestIdx];
            }
            
            if (soNguoiThieu > 0) {
                txtBanCap.setText("Không đủ bàn trống!");
                txtBanCap.setForeground(Color.RED);
                lblSucChuaVal.setText("0 người");
                btnDatBan.setEnabled(false);
            } else {
                if (banCapStr.endsWith(", ")) {
                    banCapStr = banCapStr.substring(0, banCapStr.length() - 2);
                }
                txtBanCap.setText(banCapStr);
                txtBanCap.setForeground(new Color(34, 139, 34));
                lblSucChuaVal.setText(tongSucChua + " người");
                btnDatBan.setEnabled(true);
            }
        };

        // Bắt sự kiện tự động xếp bàn
        cbKhuVuc.addActionListener(e -> autoXepBan.run());
        spnSoKhach.addChangeListener(e -> autoXepBan.run());
        txtGioDen.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                autoXepBan.run();
            }
        });
        
        // Gọi hàm xếp bàn lần đầu khi khởi tạo form
        autoXepBan.run();

        // Actions
        btnQuayLai.addActionListener(e -> dispose());
        
        btnKiemTra.addActionListener(e -> {
            String sdt = txtSDT.getText().trim();
            if(sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại cần kiểm tra!");
                lblCheck.setVisible(false);
                lblTenKhach.setText("...");
                return;
            }
            
            // Tìm trong DB
            dao.KhachHangDAO khDAO = new dao.KhachHangDAO();
            entity.KhachHang kh = khDAO.timKhachHangTheoSDT(sdt);
            
            if (kh != null) {
                // Đã có thông tin
                lblTenKhach.setText(kh.getHoTen());
                lblCheck.setVisible(true);
            } else {
                lblCheck.setVisible(false);
                lblTenKhach.setText("...");
                int choice = JOptionPane.showConfirmDialog(this, 
                    "Chưa có thông tin khách hàng này!\nBạn có muốn thêm mới khách hàng không?", 
                    "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    DialogThemKhachHang dialog = new DialogThemKhachHang(this, sdt);
                    dialog.setVisible(true);
                    
                    if (dialog.isAdded()) {
                        // Cố gắng gọi lại DB để chắc chắn dữ liệu đã vào
                        entity.KhachHang khMoi = khDAO.timKhachHangTheoSDT(sdt);
                        if (khMoi != null) {
                            lblTenKhach.setText(khMoi.getHoTen());
                            lblCheck.setVisible(true);
                        } else {
                            // Dự phòng cho chế độ demo không DB
                            lblTenKhach.setText(dialog.getAddedName());
                            lblCheck.setVisible(true);
                        }
                    }
                }
            }
        });

        btnDatBan.addActionListener(e -> {
            if(txtSDT.getText().trim().isEmpty() || lblTenKhach.getText().equals("...")) {
                JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra và xác nhận thông tin khách hàng trước khi đặt bàn!");
                return;
            }
            JOptionPane.showMessageDialog(this, "Đặt bàn thành công cho khách: " + lblTenKhach.getText());
            dispose();
        });
    }
}
