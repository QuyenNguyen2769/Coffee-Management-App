package gui;

import dao.KhachHangDAO;
import dao.KhuyenMaiDAO;
import dao.SanPhamDAO;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DialogGoiMon extends JDialog {
    private String banHienTai;
    
    // Left side (Menu)
    private JTextField txtTenMon;
    private JTextField txtMaMon;
    private JTable tblMenu;
    private DefaultTableModel modelMenu;
    
    // Right side (Order)
    private JTable tblOrder;
    private DefaultTableModel modelOrder;
    private JLabel lblTienHang;
    private JLabel lblVAT;
    private JLabel lblGiamGia;
    private JComboBox<String> cbKhuyenMai;
    private ArrayList<KhuyenMai> listKMHienTai;
    private JLabel lblGoiYUpsell;
    
    private JLabel lblTongTien;
    private JTextField txtKhachDua;
    private JLabel lblTienThua;
    private JCheckBox chkInHoaDon;
    
    // Customer Info
    private JTextField txtSDTKhach;
    private JLabel lblInfo;
    private KhachHang khachHangHienTai = null;
    
    private DecimalFormat df = new DecimalFormat("#,### VND");
    private double tienHang = 0;
    private double thueVAT = 0;
    private double tienGiamGia = 0;
    private double khachCanTra = 0;
    private double tongTien = 0;
    private Integer maKMDuocChon = null; // Fix 4: lưu maKM được chọn để lưu DB

    public DialogGoiMon(Window owner, String banHienTai) {
        super(owner, "Cập nhật Order", ModalityType.APPLICATION_MODAL);
        this.banHienTai = banHienTai;
        setSize(1100, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header - Màu Nâu Cà Phê
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlHeader.setBackground(new Color(111, 78, 55)); // Coffee Brown
        JLabel lblTitle = new JLabel("Cập nhật Order - Bàn " + banHienTai);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Content - Split 2 panels
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlCenter.setBackground(new Color(225, 230, 235));
        pnlCenter.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ================= LEFT PANEL: THỰC ĐƠN =================
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setOpaque(false);
        TitledBorder leftBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Thực đơn (Danh sách món)");
        leftBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlLeft.setBorder(BorderFactory.createCompoundBorder(leftBorder, new EmptyBorder(10, 10, 10, 10)));

        // Left Top: Search filters
        JPanel pnlLeftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLeftTop.setOpaque(false);
        
        pnlLeftTop.add(new JLabel("Tên món:"));
        txtTenMon = new JTextField(12);
        pnlLeftTop.add(txtTenMon);
        
        pnlLeftTop.add(new JLabel("Mã món:"));
        txtMaMon = new JTextField(8);
        pnlLeftTop.add(txtMaMon);

        JButton btnTim = createButton("Tìm", new Color(0, 51, 153), 70, 30);
        JButton btnLamMoiMenu = createButton("Làm mới", new Color(20, 140, 140), 90, 30);
        pnlLeftTop.add(btnTim);
        pnlLeftTop.add(btnLamMoiMenu);
        pnlLeft.add(pnlLeftTop, BorderLayout.NORTH);

        // Left Center: Table Menu
        String[] colsMenu = {"Mã món", "Tên món", "Giá bán", "ĐVT", "+"};
        modelMenu = new DefaultTableModel(colsMenu, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMenu = new JTable(modelMenu);
        styleTable(tblMenu);
        tblMenu.getColumnModel().getColumn(4).setMaxWidth(40); // Cột dấu +
        
        JScrollPane scrollMenu = new JScrollPane(tblMenu);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(100);
        pnlLeft.add(scrollMenu, BorderLayout.CENTER);
        
        pnlCenter.add(pnlLeft);

        // ================= RIGHT PANEL: CHI TIẾT ORDER =================
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setOpaque(false);
        TitledBorder rightBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Chi tiết Order");
        rightBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlRight.setBorder(BorderFactory.createCompoundBorder(rightBorder, new EmptyBorder(10, 10, 10, 10)));

        // Right Top: Info
        JPanel pnlRightTop = new JPanel(new GridLayout(2, 1, 0, 8));
        pnlRightTop.setOpaque(false);
        
        JPanel pnlBanInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBanInfo.setOpaque(false);
        JLabel lblBan = new JLabel("Bàn số: " + banHienTai);
        lblBan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBan.setForeground(new Color(34, 139, 34)); // Xanh lá
        pnlBanInfo.add(lblBan);
        
        JPanel pnlKhachInfo = new JPanel(new BorderLayout(5, 0));
        pnlKhachInfo.setOpaque(false);
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearch.setOpaque(false);
        pnlSearch.add(new JLabel("SĐT: "));
        txtSDTKhach = new JTextField(10);
        txtSDTKhach.setToolTipText("Nhập SĐT và nhấn Enter");
        pnlSearch.add(txtSDTKhach);
        pnlSearch.add(new JLabel(" | "));
        
        lblInfo = new JLabel("Khách lẻ");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfo.setForeground(new Color(192, 57, 43));
        
        pnlKhachInfo.add(pnlSearch, BorderLayout.WEST);
        pnlKhachInfo.add(lblInfo, BorderLayout.CENTER);
        
        pnlRightTop.add(pnlBanInfo);
        pnlRightTop.add(pnlKhachInfo);
        pnlRight.add(pnlRightTop, BorderLayout.NORTH);
        
        // Sự kiện tìm khách (Nhấn Enter trên ô SĐT)
        txtSDTKhach.addActionListener(e -> {
            String sdt = txtSDTKhach.getText().trim();
            if(!sdt.isEmpty()) {
                KhachHangDAO khDAO = new KhachHangDAO();
                khachHangHienTai = khDAO.timKhachHangTheoSDT(sdt);
                if(khachHangHienTai != null) {
                    lblInfo.setText(khachHangHienTai.getHoTen() + " (" + khachHangHienTai.getDiemTichLuy() + " đ)");
                    lblInfo.setForeground(new Color(34, 139, 34));
                } else {
                    lblInfo.setText("Không tìm thấy");
                    lblInfo.setForeground(Color.RED);
                    khachHangHienTai = null;
                }
                updateTotal(); // Tính lại lỡ có mã VIP
            }
        });

        // Right Center: Table Order
        String[] colsOrder = {"Mã", "Tên món", "Đơn giá", "SL", "Thành tiền", "+", "-", "x"};
        modelOrder = new DefaultTableModel(colsOrder, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 3; } // Chỉ cho phép sửa cột SL (cột số 3)
        };
        tblOrder = new JTable(modelOrder);
        styleTable(tblOrder);
        tblOrder.getColumnModel().getColumn(3).setMaxWidth(40); // SL
        tblOrder.getColumnModel().getColumn(5).setMaxWidth(40); // +
        tblOrder.getColumnModel().getColumn(6).setMaxWidth(40); // -
        tblOrder.getColumnModel().getColumn(7).setMaxWidth(40); // x
        
        JScrollPane scrollOrder = new JScrollPane(tblOrder);
        scrollOrder.getVerticalScrollBar().setUnitIncrement(100);
        pnlRight.add(scrollOrder, BorderLayout.CENTER);

        // Right Bottom: Total & Payment Block
        JPanel pnlRightBot = new JPanel(new GridLayout(6, 2, 10, 5));
        pnlRightBot.setOpaque(false);
        pnlRightBot.setBorder(new EmptyBorder(10, 20, 0, 0)); // Đẩy sang phải
        
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
        
        pnlRightBot.add(new JLabel("Tiền hàng:", SwingConstants.RIGHT));
        lblTienHang = new JLabel("0 VND", SwingConstants.RIGHT);
        lblTienHang.setFont(boldFont);
        pnlRightBot.add(lblTienHang);
        
        // Khuyến mãi Combo box
        JPanel pnlKM = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlKM.setOpaque(false);
        pnlKM.add(new JLabel("Khuyến mãi: "));
        cbKhuyenMai = new JComboBox<>(new String[]{"Không có"});
        cbKhuyenMai.setPreferredSize(new Dimension(103, 22));
        pnlKM.add(cbKhuyenMai);
        pnlRightBot.add(pnlKM);
        
        lblGiamGia = new JLabel("-0 VND", SwingConstants.RIGHT);
        lblGiamGia.setFont(boldFont);
        lblGiamGia.setForeground(new Color(230, 126, 34)); // Cam
        pnlRightBot.add(lblGiamGia);
        
        pnlRightBot.add(new JLabel("Thuế VAT (10%):", SwingConstants.RIGHT));
        lblVAT = new JLabel("0 VND", SwingConstants.RIGHT);
        lblVAT.setFont(boldFont);
        pnlRightBot.add(lblVAT);
        
        JLabel lblKCT = new JLabel("Khách cần trả:", SwingConstants.RIGHT);
        lblKCT.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlRightBot.add(lblKCT);
        
        lblTongTien = new JLabel("0 VND", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongTien.setForeground(new Color(192, 57, 43)); // Đỏ
        pnlRightBot.add(lblTongTien);
        
        pnlRightBot.add(new JLabel("Tiền khách đưa:", SwingConstants.RIGHT));
        txtKhachDua = new JTextField("0");
        txtKhachDua.setHorizontalAlignment(JTextField.RIGHT);
        txtKhachDua.setFont(boldFont);
        pnlRightBot.add(txtKhachDua);
        
        pnlRightBot.add(new JLabel("Tiền thừa:", SwingConstants.RIGHT));
        lblTienThua = new JLabel("0 VND", SwingConstants.RIGHT);
        lblTienThua.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTienThua.setForeground(new Color(34, 139, 34)); // Xanh lá
        pnlRightBot.add(lblTienThua);
        
        // Sự kiện khi đổi Khuyến mãi
        cbKhuyenMai.addActionListener(e -> tinhToanKhuyenMai());
        
        lblGoiYUpsell = new JLabel(" ");
        lblGoiYUpsell.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblGoiYUpsell.setForeground(new Color(231, 76, 60)); // Đỏ nhạt
        lblGoiYUpsell.setHorizontalAlignment(SwingConstants.RIGHT);
        lblGoiYUpsell.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JPanel pnlTotalWrapper = new JPanel(new BorderLayout());
        pnlTotalWrapper.setOpaque(false);
        pnlTotalWrapper.add(lblGoiYUpsell, BorderLayout.NORTH);
        pnlTotalWrapper.add(pnlRightBot, BorderLayout.CENTER);
        
        pnlRight.add(pnlTotalWrapper, BorderLayout.SOUTH);

        pnlCenter.add(pnlRight);
        
        add(pnlCenter, BorderLayout.CENTER);

        // ================= FOOTER BUTTONS =================
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(225, 230, 235));
        pnlFooter.setBorder(new EmptyBorder(0, 20, 15, 20));

        JButton btnQuayLai = createButton("Quay lại", new Color(30, 60, 200), 110, 40);
        JPanel pnlFooterRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooterRight.setOpaque(false);
        
        chkInHoaDon = new JCheckBox("Xuất hóa đơn", true);
        chkInHoaDon.setOpaque(false);
        chkInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JButton btnLuu = createButton("Lưu Order", new Color(39, 174, 96), 130, 40);
        JButton btnThanhToan = createButton("Thanh toán ngay", new Color(230, 126, 34), 160, 40);
        
        pnlFooter.add(btnQuayLai, BorderLayout.WEST);
        pnlFooterRight.add(chkInHoaDon);
        pnlFooterRight.add(btnLuu);
        pnlFooterRight.add(btnThanhToan);
        pnlFooter.add(pnlFooterRight, BorderLayout.EAST);
        
        add(pnlFooter, BorderLayout.SOUTH);

        // ================= EVENTS & LOGIC =================
        loadMenuData("");
        
        dao.HoaDonDAO hdDAO = new dao.HoaDonDAO();
        int maBanInt = Integer.parseInt(banHienTai);
        int maHD = hdDAO.getHoaDonHienTai(maBanInt);
        if (maHD != -1) {
            hdDAO.loadOrderDetail(maHD, modelOrder, df);
            updateTotal();
        }

        btnQuayLai.addActionListener(e -> dispose());
        btnTim.addActionListener(e -> loadMenuData(txtTenMon.getText().trim()));
        txtTenMon.addActionListener(e -> btnTim.doClick());
        btnLamMoiMenu.addActionListener(e -> {
            txtTenMon.setText("");
            txtMaMon.setText("");
            loadMenuData("");
        });
        
        // Cập nhật tiền thừa khi nhập tiền khách đưa
        txtKhachDua.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calculateTienThua(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calculateTienThua(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calculateTienThua(); }
        });

        // Bắt sự kiện khi người dùng gõ trực tiếp vào cột số lượng (SL)
        modelOrder.addTableModelListener(new javax.swing.event.TableModelListener() {
            private boolean isUpdating = false;
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                if (isUpdating) return;
                // Nếu cột SL bị thay đổi
                if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 3) {
                    isUpdating = true;
                    int row = e.getFirstRow();
                    if (row >= 0 && row < modelOrder.getRowCount()) {
                        try {
                            String slStr = modelOrder.getValueAt(row, 3).toString().trim();
                            int sl = Integer.parseInt(slStr);
                            if (sl <= 0) {
                                sl = 1; // Nếu nhập <= 0, reset về 1
                                modelOrder.setValueAt(sl, row, 3);
                            }
                            // Cập nhật lại thành tiền
                            String giaStr = modelOrder.getValueAt(row, 2).toString().replaceAll("[^0-9]", "");
                            double gia = Double.parseDouble(giaStr);
                            modelOrder.setValueAt(df.format(gia * sl), row, 4);
                            updateTotal();
                        } catch (Exception ex) {
                            // Nhập sai định dạng (chữ cái), reset về 1
                            modelOrder.setValueAt(1, row, 3);
                            String giaStr = modelOrder.getValueAt(row, 2).toString().replaceAll("[^0-9]", "");
                            double gia = Double.parseDouble(giaStr);
                            modelOrder.setValueAt(df.format(gia), row, 4);
                            updateTotal();
                        }
                    }
                    isUpdating = false;
                }
            }
        });

        // Click on Menu Table to Add Item
        tblMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblMenu.rowAtPoint(e.getPoint());
                int col = tblMenu.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) { // Cột dấu +
                    String ma = modelMenu.getValueAt(row, 0).toString();
                    String ten = modelMenu.getValueAt(row, 1).toString();
                    String giaStr = modelMenu.getValueAt(row, 2).toString().replaceAll("[^0-9]", "");
                    double gia = Double.parseDouble(giaStr);
                    addItemToOrder(ma, ten, gia);
                }
            }
        });

        // Click on Order Table to Modify Quantity
        tblOrder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblOrder.rowAtPoint(e.getPoint());
                int col = tblOrder.columnAtPoint(e.getPoint());
                if (row >= 0) {
                    if (col == 5) { // Cột +
                        changeQuantity(row, 1);
                    } else if (col == 6) { // Cột -
                        changeQuantity(row, -1);
                    } else if (col == 7) { // Cột x
                        modelOrder.removeRow(row);
                        updateTotal();
                    }
                }
            }
        });

        btnLuu.addActionListener(e -> {
            if (modelOrder.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Order đang trống!");
                return;
            }
            dao.HoaDonDAO hd = new dao.HoaDonDAO();
            int maBan = Integer.parseInt(banHienTai);
            int idHD = hd.getHoaDonHienTai(maBan);
            if (idHD == -1) {
                idHD = hd.taoHoaDon(maBan, 1);
            }
            if (idHD != -1) {
                hd.luuOrderVoiKM(idHD, modelOrder, tongTien, maKMDuocChon); // Fix 4
                JOptionPane.showMessageDialog(this, "Đã lưu Order thành công (Khách chưa thanh toán)!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi tạo hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnThanhToan.addActionListener(e -> {
            if (modelOrder.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Order đang trống, không thể thanh toán!");
                return;
            }
            dao.HoaDonDAO hd = new dao.HoaDonDAO();
            int maBan = Integer.parseInt(banHienTai);
            int idHD = hd.getHoaDonHienTai(maBan);
            if (idHD == -1) {
                idHD = hd.taoHoaDon(maBan, 1);
            }
            if (idHD != -1) {
                hd.luuOrderVoiKM(idHD, modelOrder, tongTien, maKMDuocChon); // Fix 4: lưu kèm maKM
                hd.thanhToanHoaDon(idHD);
                
                String tenKM = (maKMDuocChon != null && listKMHienTai != null)
                    ? listKMHienTai.stream()
                        .filter(k -> Integer.parseInt(k.getMaKM()) == maKMDuocChon)
                        .findFirst().map(k -> k.getTenKM()).orElse("")
                    : "";
                
                String msg = "Đã thanh toán thành công!";
                if (!tenKM.isEmpty()) msg += "\n✔ KM áp dụng: " + tenKM + " - Giảm " + df.format(tienGiamGia);
                JOptionPane.showMessageDialog(this, msg);

                if (chkInHoaDon.isSelected()) {
                    // Gọi Dialog hóa đơn tập trung mới
                    DialogHoaDon dlgHD = new DialogHoaDon(
                        (Frame)SwingUtilities.getWindowAncestor(this), 
                        "HD" + idHD, "admin", 
                        (khachHangHienTai != null ? khachHangHienTai.getHoTen() : "Khách lẻ"), 
                        banHienTai, modelOrder, tongTien
                    );
                    dlgHD.setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi tạo hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadMenuData(String keyword) {
        modelMenu.setRowCount(0);
        SanPhamDAO spDAO = new SanPhamDAO();
        ArrayList<SanPham> list = keyword.isEmpty() ? spDAO.getAllSanPham() : spDAO.timSanPhamTheoTen(keyword);
        
        for (SanPham sp : list) {
            boolean matchMa = txtMaMon.getText().isEmpty() || sp.getMaSP().contains(txtMaMon.getText());
            if (matchMa) {
                modelMenu.addRow(new Object[]{
                    sp.getMaSP(), 
                    sp.getTenSP(), 
                    df.format(sp.getGiaBan()), 
                    sp.getDonViTinh(), 
                    "+"
                });
            }
        }
    }

    private void addItemToOrder(String ma, String ten, double gia) {
        // Check if item already exists in order
        for (int i = 0; i < modelOrder.getRowCount(); i++) {
            if (modelOrder.getValueAt(i, 0).toString().equals(ma)) {
                changeQuantity(i, 1);
                return;
            }
        }
        // If not exists, add new row
        modelOrder.addRow(new Object[]{
            ma, ten, df.format(gia), 1, df.format(gia), "+", "-", "x"
        });
        updateTotal();
    }

    private void changeQuantity(int row, int delta) {
        int sl = Integer.parseInt(modelOrder.getValueAt(row, 3).toString());
        sl += delta;
        if (sl <= 0) {
            modelOrder.removeRow(row);
            updateTotal();
        } else {
            // Khi set giá trị mới vào đây, TableModelListener ở trên sẽ tự động 
            // tính lại "Thành tiền" và "Tổng tiền"
            modelOrder.setValueAt(sl, row, 3);
        }
    }

    private void updateTotal() {
        tienHang = 0;
        for (int i = 0; i < modelOrder.getRowCount(); i++) {
            String ttStr = modelOrder.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
            tienHang += Double.parseDouble(ttStr);
        }
        lblTienHang.setText(df.format(tienHang));
        
        // Tải lại danh sách khuyến mãi hợp lệ mỗi khi thêm/bớt món
        loadKhuyenMaiHopLe();
        tinhToanKhuyenMai();
    }
    
    private void loadKhuyenMaiHopLe() {
        cbKhuyenMai.removeAllItems();
        cbKhuyenMai.addItem("Không có");
        lblGoiYUpsell.setText(" "); // Xóa dòng nhắc nhở
        maKMDuocChon = null; // Reset mã KM
        
        KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
        ArrayList<KhuyenMai> allActiveKMs = kmDAO.getKhuyenMaiHopLe(0); // Lấy TẤT CẢ KM đang diễn ra
        listKMHienTai = new ArrayList<>();
        
        // Fix 2: Dùng loaiKhach thay vì điểm tích lũy
        boolean isVIP = (khachHangHienTai != null && khachHangHienTai.isVIP());
        KhuyenMai kmVIPTiemNang = null;
        int autoSelectIdx = -1;       // index trong listKMHienTai sẽ tự chọn
        double maxPhanTram = -1;       // Fix 3: theo dõi KM % cao nhất
        
        // Lọc ra các KM đủ điều kiện
        for (KhuyenMai km : allActiveKMs) {
            if (km.getDieuKienToiThieu() <= tienHang) {
                listKMHienTai.add(km);
                cbKhuyenMai.addItem(km.getTenKM() + " (" + km.getMucGiamGia() + "%)");
                
                int curIdx = listKMHienTai.size() - 1;
                boolean isKMVip = km.getTenKM().toLowerCase().contains("vip");
                
                if (isVIP && isKMVip) {
                    // Fix 2+3: VIP → ưu tiên KM VIP trước
                    autoSelectIdx = curIdx;
                    maxPhanTram = 999; // Lock uu tien VIP
                } else if (maxPhanTram < 999 && km.getMucGiamGia() > maxPhanTram) {
                    // Fix 3: Khäng phải VIP KM → chọn % cao nhất
                    maxPhanTram = km.getMucGiamGia();
                    autoSelectIdx = curIdx;
                }
            }
            
            // Tìm KM VIP mà khách chưa đủ điều kiện (upsell)
            if (isVIP && km.getTenKM().toLowerCase().contains("vip") && km.getDieuKienToiThieu() > tienHang) {
                kmVIPTiemNang = km;
            }
        }
        
        // Cảnh báo Upsell cho VIP
        if (kmVIPTiemNang != null && autoSelectIdx == -1) {
            double tienThieu = kmVIPTiemNang.getDieuKienToiThieu() - tienHang;
            lblGoiYUpsell.setText("▶ KH VIP: Mua thêm " + df.format(tienThieu) + " được " + kmVIPTiemNang.getTenKM() + "!");
        }
        
        // Fix 3: Tự động chọn KM tốt nhất (VIP KM ưu tiên, sau đó % cao nhất)
        if (autoSelectIdx >= 0) {
            cbKhuyenMai.setSelectedIndex(autoSelectIdx + 1); // +1 vì index 0 là "Không có"
        }
    }
    
    private void tinhToanKhuyenMai() {
        int idx = cbKhuyenMai.getSelectedIndex();
        if(idx > 0 && listKMHienTai != null && idx - 1 < listKMHienTai.size()) {
            KhuyenMai km = listKMHienTai.get(idx - 1);
            tienGiamGia = tienHang * (km.getMucGiamGia() / 100.0);
            maKMDuocChon = Integer.parseInt(km.getMaKM()); // Fix 4: lưu maKM
        } else {
            tienGiamGia = 0;
            maKMDuocChon = null; // Fix 4: không có KM
        }
        lblGiamGia.setText("-" + df.format(tienGiamGia));
        
        // VAT mặc định 10% tính trên (Tiền hàng - Giảm giá)
        thueVAT = (tienHang - tienGiamGia) * 0.10;
        if(thueVAT < 0) thueVAT = 0;
        lblVAT.setText(df.format(thueVAT));
        
        khachCanTra = tienHang - tienGiamGia + thueVAT;
        if(khachCanTra < 0) khachCanTra = 0;
        lblTongTien.setText(df.format(khachCanTra));
        
        tongTien = khachCanTra; // Tương thích hàm lưu DB cũ
        
        calculateTienThua();
    }

    private void calculateTienThua() {
        try {
            String strKhach = txtKhachDua.getText().replaceAll("[^0-9]", "");
            if(strKhach.isEmpty()) {
                lblTienThua.setText("0 VND");
                lblTienThua.setForeground(Color.BLACK);
                return;
            }
            double khachDua = Double.parseDouble(strKhach);
            double tienThua = khachDua - khachCanTra;
            if(tienThua < 0) {
                lblTienThua.setText("Khách thiếu tiền!");
                lblTienThua.setForeground(Color.RED);
            } else {
                lblTienThua.setText(df.format(tienThua));
                lblTienThua.setForeground(new Color(34, 139, 34)); // Xanh lá
            }
        } catch (Exception e) {
            lblTienThua.setText("0 VND");
        }
    }

    private void styleTable(JTable tbl) {
        tbl.setRowHeight(35);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tbl.setSelectionBackground(new Color(111, 78, 55)); // Coffee Brown
        tbl.setSelectionForeground(Color.WHITE);
        tbl.setShowGrid(true);
        tbl.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = tbl.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer actionRenderer = new DefaultTableCellRenderer();
        actionRenderer.setHorizontalAlignment(JLabel.CENTER);
        actionRenderer.setFont(new Font("Segoe UI", Font.BOLD, 20));
        actionRenderer.setForeground(new Color(192, 57, 43)); // Đỏ sậm cho nổi bật

        for (int i = 0; i < tbl.getColumnCount(); i++) {
            String colName = tbl.getColumnName(i);
            if (colName.equals("Tên món")) continue; // Tên món canh trái
            
            if (colName.equals("+") || colName.equals("-") || colName.equals("x")) {
                tbl.getColumnModel().getColumn(i).setCellRenderer(actionRenderer);
            } else {
                tbl.getColumnModel().getColumn(i).setCellRenderer(center);
            }
        }
    }

    private JButton createButton(String text, Color bg, int w, int h) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(w, h));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.brighter());
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return btn;
    }
}
