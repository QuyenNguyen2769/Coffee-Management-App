package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DialogGoiMon extends JDialog {

    private DefaultTableModel modelLeft;
    private JTable tblLeft;
    
    private DefaultTableModel modelRight;
    private JTable tblRight;
    private JLabel lblTongTien;

    public DialogGoiMon(Window owner, String banCap) {
        super(owner, "Gọi Món", ModalityType.APPLICATION_MODAL);
        setSize(1200, 650); // Rộng hơn 200px để không mất chữ
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(111, 78, 55), 2));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(111, 78, 55)); 
        pnlHeader.setPreferredSize(new Dimension(0, 55)); // Tăng chiều cao header
        
        JLabel lblTitle = new JLabel("Cập nhật dịch vụ (Gọi món)", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28)); // 20 -> 28
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        contentPane.add(pnlHeader, BorderLayout.NORTH);

        // --- BODY ---
        JPanel pnlBody = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlBody.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlBody.setBackground(Color.WHITE);
        contentPane.add(pnlBody, BorderLayout.CENTER);

        // ==== LEFT PANEL (Tất cả sản phẩm) ====
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Tất cả sản phẩm", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 24))); // 16 -> 24
        pnlLeft.setBackground(Color.WHITE);
        
        // Search Bar
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(Color.WHITE);
        
        JLabel lblTenMon = new JLabel("Tên món:");
        lblTenMon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pnlSearch.add(lblTenMon);
        
        JTextField txtTen = new JTextField(8);
        txtTen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pnlSearch.add(txtTen);
        
        JLabel lblMaMon = new JLabel("Mã món:");
        lblMaMon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pnlSearch.add(lblMaMon);
        
        JTextField txtMa = new JTextField(6);
        txtMa.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pnlSearch.add(txtMa);
        
        JButton btnTim = new JButton("Tìm") {
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
        btnTim.setBackground(new Color(0, 102, 204));
        btnTim.setForeground(Color.WHITE);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnTim.setContentAreaFilled(false);
        btnTim.setBorderPainted(false);
        pnlSearch.add(btnTim);
        
        JButton btnLamMoi = new JButton("Làm mới") {
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
        btnLamMoi.setBackground(new Color(46, 204, 113));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLamMoi.setContentAreaFilled(false);
        btnLamMoi.setBorderPainted(false);
        pnlSearch.add(btnLamMoi);
        
        pnlLeft.add(pnlSearch, BorderLayout.NORTH);

        // Table Left
        String[] colsLeft = {"Mã", "Tên", "Đơn giá", "<html>Tồn<br>kho</html>", "Đơn vị", "Thêm"};
        modelLeft = new DefaultTableModel(colsLeft, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLeft = new JTable(modelLeft) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    // Dòng chẵn màu trắng, dòng lẻ màu xám nhạt (zebra striping)
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        };
        tblLeft.setRowHeight(40); // 30 -> 40
        tblLeft.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // Header nổi bật cho bảng trái
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(200, 200, 200)); // Xám đậm hơn cho nổi bật
                label.setForeground(Color.BLACK);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Thu nhỏ lại để không bị cắt chữ (Đơn..., Tồn...)
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY),
                    new EmptyBorder(4, 4, 4, 4) // Giảm padding để có thêm chỗ cho chữ
                ));
                return label;
            }
        };
        tblLeft.getTableHeader().setDefaultRenderer(headerRenderer);
        tblLeft.getTableHeader().setOpaque(false);
        tblLeft.getTableHeader().setPreferredSize(new Dimension(0, 50)); // Tăng chiều cao để đủ chỗ cho 2 dòng text
        
        // Custom Renderer cho cột "Thêm" hiển thị icon_plus.png hoặc tự vẽ
        tblLeft.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            javax.swing.Icon iconPlus;
            {
                try {
                    // Cố gắng load hình icon_plus.png của bạn
                    java.io.File fileImages = new java.io.File("images/icon_plus.png");
                    java.io.File file = new java.io.File("icon_plus.png");
                    java.io.File fileImg = new java.io.File("img/icon_plus.png");
                    java.io.File fileSrc = new java.io.File("src/img/icon_plus.png");
                    String path = "";
                    if (fileImages.exists()) path = fileImages.getPath();
                    else if (file.exists()) path = file.getPath();
                    else if (fileImg.exists()) path = fileImg.getPath();
                    else if (fileSrc.exists()) path = fileSrc.getPath();
                    
                    if (!path.isEmpty()) {
                        iconPlus = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH));
                    } else {
                        // NẾU KHÔNG TÌM THẤY ẢNH -> Tự vẽ một icon mũi tên màu xanh ngọc bích giống y hệt hình mẫu
                        iconPlus = new javax.swing.Icon() {
                            public void paintIcon(Component c, Graphics g, int x, int y) {
                                Graphics2D g2 = (Graphics2D) g.create();
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2.setColor(new Color(0, 128, 100)); // Màu xanh ngọc
                                g2.fillOval(x + 4, y + 2, 26, 26);
                                g2.setColor(Color.WHITE);
                                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                                // Vẽ mũi tên
                                g2.drawLine(x + 12, y + 15, x + 22, y + 15);
                                g2.drawLine(x + 17, y + 10, x + 22, y + 15);
                                g2.drawLine(x + 17, y + 20, x + 22, y + 15);
                                g2.dispose();
                            }
                            public int getIconWidth() { return 34; }
                            public int getIconHeight() { return 30; }
                        };
                    }
                } catch (Exception ex) {}
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                label.setIcon(iconPlus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });
        
        JScrollPane scrollLeft = new JScrollPane(tblLeft);
        scrollLeft.getViewport().setBackground(Color.WHITE);
        pnlLeft.add(scrollLeft, BorderLayout.CENTER);

        // Sự kiện click Thêm món
        tblLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblLeft.rowAtPoint(e.getPoint());
                int col = tblLeft.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 5) {
                    String ten = (String) modelLeft.getValueAt(row, 1);
                    String giaStr = (String) modelLeft.getValueAt(row, 2);
                    
                    // Kiểm tra xem món này đã có bên giỏ hàng chưa, nếu có thì tăng số lượng
                    for (int i = 0; i < modelRight.getRowCount(); i++) {
                        if (modelRight.getValueAt(i, 1).equals(ten)) {
                            int sl = (int) modelRight.getValueAt(i, 3);
                            modelRight.setValueAt(sl + 1, i, 3);
                            capNhatThanhTien(i);
                            tinhTongTien();
                            return;
                        }
                    }
                    
                    // Nếu món mới hoàn toàn
                    int stt = modelRight.getRowCount() + 1;
                    modelRight.addRow(new Object[]{stt, ten, giaStr, 1, giaStr, "+", "-", "Xóa"});
                    tinhTongTien();
                }
            }
        });

        loadSanPhamData("", "");

        btnTim.addActionListener(e -> {
            loadSanPhamData(txtTen.getText().trim(), txtMa.getText().trim());
        });

        btnLamMoi.addActionListener(e -> {
            txtTen.setText("");
            txtMa.setText("");
            loadSanPhamData("", "");
        });

        pnlBody.add(pnlLeft);

        // ==== RIGHT PANEL (Món đã gọi) ====
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Món đã gọi", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 24))); // 16 -> 24
        pnlRight.setBackground(Color.WHITE);
        
        // Info
        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInfo.setBackground(Color.WHITE);
        JLabel lblBan = new JLabel("Bàn cấp: " + banCap);
        lblBan.setFont(new Font("Segoe UI", Font.BOLD, 20)); // 14 -> 20
        pnlInfo.add(lblBan);
        pnlRight.add(pnlInfo, BorderLayout.NORTH);

        // Table Right
        String[] colsRight = {"#", "Tên", "Đơn giá", "<html>Đã<br>thêm</html>", "<html>Thành<br>tiền</html>", "Thêm", "Bớt", "Xóa"};
        modelRight = new DefaultTableModel(colsRight, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Cho phép sửa trực tiếp cột "Đã thêm"
            }
        };
        
        // Sự kiện khi người dùng gõ số lượng trực tiếp vào ô
        modelRight.addTableModelListener(new javax.swing.event.TableModelListener() {
            boolean isUpdating = false;
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                if (isUpdating) return;
                if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 3) {
                    isUpdating = true;
                    int row = e.getFirstRow();
                    try {
                        Object val = modelRight.getValueAt(row, 3);
                        int sl = 0;
                        if (val instanceof String) {
                            sl = Integer.parseInt(val.toString().trim());
                            modelRight.setValueAt(sl, row, 3);
                        } else {
                            sl = (int) val;
                        }
                        if (sl <= 0) {
                            modelRight.setValueAt(1, row, 3);
                        }
                    } catch (Exception ex) {
                        modelRight.setValueAt(1, row, 3);
                    }
                    capNhatThanhTien(row);
                    tinhTongTien();
                    isUpdating = false;
                }
            }
        });
        tblRight = new JTable(modelRight) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    // Dòng chẵn màu trắng, dòng lẻ màu xám nhạt (zebra striping)
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        };
        tblRight.setRowHeight(40); // 30 -> 40
        tblRight.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // Canh giữa cho cột + và -
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblRight.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tblRight.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        tblRight.getTableHeader().setDefaultRenderer(headerRenderer);
        tblRight.getTableHeader().setOpaque(false);
        tblRight.getTableHeader().setPreferredSize(new Dimension(0, 50)); // Tăng chiều cao để đủ chỗ cho 2 dòng text
        JScrollPane scrollRight = new JScrollPane(tblRight);
        scrollRight.getViewport().setBackground(Color.WHITE);
        pnlRight.add(scrollRight, BorderLayout.CENTER);

        // Sự kiện click điều chỉnh số lượng hoặc xóa bên bảng món đã gọi
        tblRight.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblRight.rowAtPoint(e.getPoint());
                int col = tblRight.columnAtPoint(e.getPoint());
                if (row >= 0) {
                    if (col == 5) { // Cột Dấu cộng
                        int sl = (int) modelRight.getValueAt(row, 3);
                        modelRight.setValueAt(sl + 1, row, 3);
                        capNhatThanhTien(row);
                        tinhTongTien();
                    } else if (col == 6) { // Cột Dấu trừ
                        int sl = (int) modelRight.getValueAt(row, 3);
                        if (sl > 1) {
                            modelRight.setValueAt(sl - 1, row, 3);
                            capNhatThanhTien(row);
                            tinhTongTien();
                        }
                    } else if (col == 7) { // Cột Xóa
                        modelRight.removeRow(row);
                        // Cập nhật lại số thứ tự
                        for (int i = 0; i < modelRight.getRowCount(); i++) {
                            modelRight.setValueAt(i + 1, i, 0);
                        }
                        tinhTongTien();
                    }
                }
            }
        });

        // Footer Right (Tổng tiền & Quay lại)
        JPanel pnlFooterRight = new JPanel(new BorderLayout());
        pnlFooterRight.setBackground(Color.WHITE);
        lblTongTien = new JLabel("Tổng tiền: 0 VND   ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24)); // 18 -> 24
        lblTongTien.setForeground(Color.RED);
        pnlFooterRight.add(lblTongTien, BorderLayout.CENTER);
        
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
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 18)); // 14 -> 18
        btnQuayLai.setPreferredSize(new Dimension(120, 45)); // 100x35 -> 120x45
        btnQuayLai.setContentAreaFilled(false);
        btnQuayLai.setBorderPainted(false);
        btnQuayLai.addActionListener(e -> dispose());
        
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.add(btnQuayLai);
        pnlFooterRight.add(pnlBtn, BorderLayout.SOUTH);
        
        pnlRight.add(pnlFooterRight, BorderLayout.SOUTH);

        pnlBody.add(pnlRight);
    } // ĐÓNG CONSTRUCTOR TẠI ĐÂY

    private void loadSanPhamData(String ten, String ma) {
        modelLeft.setRowCount(0);
        dao.SanPhamDAO spDAO = new dao.SanPhamDAO();
        java.util.ArrayList<entity.SanPham> listSP = spDAO.getAllSanPham();

        java.text.DecimalFormat df = new java.text.DecimalFormat("#,### VND");
        for (entity.SanPham sp : listSP) {
            String maStr = String.format("SP%03d", sp.getMaSP());
            
            // Lọc bằng Java để áp dụng được cả Tên và Mã cùng lúc
            if (!ten.isEmpty() && !sp.getTenSP().toLowerCase().contains(ten.toLowerCase())) {
                continue;
            }
            if (!ma.isEmpty() && !maStr.toLowerCase().contains(ma.toLowerCase())) {
                continue;
            }
            
            modelLeft.addRow(new Object[]{
                maStr,
                sp.getTenSP(),
                df.format(sp.getGiaBan()),
                sp.getSoLuongTon(),
                sp.getDonViTinh(),
                "" // Ô này sẽ được CellRenderer đè hình icon lên
            });
        }
    }

    private void capNhatThanhTien(int row) {
        String giaStr = (String) modelRight.getValueAt(row, 2);
        double gia = parseGia(giaStr);
        int sl = (int) modelRight.getValueAt(row, 3);
        double thanhTien = gia * sl;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,### VND");
        modelRight.setValueAt(df.format(thanhTien), row, 4);
    }

    private double parseGia(String giaStr) {
        try {
            String cleanStr = giaStr.replaceAll("[^0-9]", "");
            return Double.parseDouble(cleanStr);
        } catch (Exception e) {
            return 0;
        }
    }

    private void tinhTongTien() {
        double tong = 0;
        for (int i = 0; i < modelRight.getRowCount(); i++) {
            String ttStr = (String) modelRight.getValueAt(i, 4);
            tong += parseGia(ttStr);
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,### VND");
        lblTongTien.setText("Tổng tiền: " + df.format(tong) + "   ");
    }
}
