package gui;

import dao.BanDAO;
import entity.Ban;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class DialogChuyenBan extends JDialog {
    private String banHienTai;
    private int sucChuaHienTai;
    private JLabel lblBanMoi;
    private JTable tableBan;
    private DefaultTableModel model;
    private JTextField txtBanSo;

    public DialogChuyenBan(Window owner, String banHienTai, int sucChuaHienTai) {
        super(owner, "Chuyển Bàn", ModalityType.APPLICATION_MODAL);
        this.banHienTai = banHienTai;
        this.sucChuaHienTai = sucChuaHienTai;
        setSize(750, 480);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header - Màu Nâu Cà Phê
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlHeader.setBackground(new Color(111, 78, 55)); // Coffee Brown
        JLabel lblTitle = new JLabel("Chuyển bàn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Background
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(new Color(225, 230, 235));
        pnlCenter.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Top Info Panel
        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlInfo.setOpaque(false);
        
        JLabel lblHienTai = new JLabel("Bàn hiện tại: " + banHienTai);
        lblHienTai.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pnlInfo.add(lblHienTai);

        JLabel lblArrow = new JLabel(" >> ");
        lblArrow.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pnlInfo.add(lblArrow);

        lblBanMoi = new JLabel("...");
        lblBanMoi.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBanMoi.setForeground(new Color(111, 78, 55)); // Coffee Brown
        pnlInfo.add(lblBanMoi);

        pnlInfo.add(Box.createHorizontalStrut(50));

        JLabel lblSearch = new JLabel("Bàn số:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pnlInfo.add(lblSearch);

        txtBanSo = new JTextField(12);
        txtBanSo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pnlInfo.add(txtBanSo);

        JButton btnTim = createButton("Tìm", new Color(0, 51, 153));
        btnTim.setPreferredSize(new Dimension(80, 30));
        pnlInfo.add(btnTim);

        pnlCenter.add(pnlInfo, BorderLayout.NORTH);

        // Table Data
        String[] cols = {"Mã bàn", "Trạng thái", "Khu vực", "Sức chứa"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableBan = new JTable(model);
        tableBan.setRowHeight(30);
        tableBan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableBan.setSelectionBackground(new Color(111, 78, 55)); // Coffee Brown
        tableBan.setSelectionForeground(Color.WHITE);
        tableBan.setShowGrid(true);
        tableBan.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = tableBan.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        JScrollPane scroll = new JScrollPane(tableBan);
        pnlCenter.add(scroll, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // Footer Buttons
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(225, 230, 235));
        pnlFooter.setBorder(new EmptyBorder(0, 20, 15, 20));

        JPanel pnlLeftBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlLeftBtns.setOpaque(false);
        JButton btnQuayLai = createButton("Quay lại", new Color(30, 60, 200));
        JButton btnLamMoi = createButton("Làm mới", new Color(20, 140, 140));
        pnlLeftBtns.add(btnQuayLai);
        pnlLeftBtns.add(btnLamMoi);
        pnlFooter.add(pnlLeftBtns, BorderLayout.WEST);

        JPanel pnlRightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlRightBtns.setOpaque(false);
        JButton btnChuyen = createButton("Chuyển", new Color(50, 205, 50));
        pnlRightBtns.add(btnChuyen);
        pnlFooter.add(pnlRightBtns, BorderLayout.EAST);

        add(pnlFooter, BorderLayout.SOUTH);

        // Load Initial Data
        loadBanTrong("");

        // Event Listeners
        tableBan.getSelectionModel().addListSelectionListener(e -> {
            int row = tableBan.getSelectedRow();
            if (row >= 0) {
                lblBanMoi.setText(model.getValueAt(row, 0).toString());
            }
        });

        btnQuayLai.addActionListener(e -> dispose());
        
        btnLamMoi.addActionListener(e -> {
            txtBanSo.setText("");
            loadBanTrong("");
            lblBanMoi.setText("...");
            tableBan.clearSelection();
        });
        
        btnTim.addActionListener(e -> loadBanTrong(txtBanSo.getText().trim()));
        txtBanSo.addActionListener(e -> btnTim.doClick());

        btnChuyen.addActionListener(e -> {
            String banMoi = lblBanMoi.getText();
            if (banMoi.equals("...")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn muốn chuyển đến!");
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, 
                "Xác nhận chuyển từ Bàn " + banHienTai + " sang Bàn " + banMoi + "?", 
                "Chuyển bàn", JOptionPane.YES_NO_OPTION);
                
            if (opt == JOptionPane.YES_OPTION) {
                dao.HoaDonDAO hdDAO = new dao.HoaDonDAO();
                int mbCu = Integer.parseInt(banHienTai);
                int mbMoi = Integer.parseInt(banMoi);
                
                if (hdDAO.chuyenBan(mbCu, mbMoi)) {
                    JOptionPane.showMessageDialog(this, "Chuyển bàn thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Bàn này hiện tại không có Order để chuyển!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });
    }

    private void loadBanTrong(String timKiem) {
        model.setRowCount(0);
        BanDAO banDAO = new BanDAO();
        ArrayList<Ban> dsBan = banDAO.getAllBan();
        
        for (Ban b : dsBan) {
            String maBanStr = String.format("%03d", b.getSoBan());
            String status = b.getTrangThai();
            
            // Chỉ hiển thị bàn trống và có sức chứa >= sức chứa bàn hiện tại
            if (status.equalsIgnoreCase("Trống") && b.getSucChua() >= this.sucChuaHienTai) {
                if (timKiem.isEmpty() || maBanStr.contains(timKiem)) {
                    model.addRow(new Object[]{
                        maBanStr, status, b.getKhuVuc(), b.getSucChua() + " người"
                    });
                }
            }
        }
        
        // Căn giữa nội dung bảng
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableBan.getColumnCount(); i++) {
            tableBan.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private JButton createButton(String text, Color bg) {
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
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.brighter());
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return btn;
    }
}
