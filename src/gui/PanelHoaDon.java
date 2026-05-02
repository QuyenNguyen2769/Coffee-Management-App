package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class PanelHoaDon extends JPanel {

    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    private DefaultTableModel tableModel;
    private JTable            table;

    public PanelHoaDon() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_WHITE);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(18, 28, 18, 28)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("  Quản Lý Hoá Đơn");
        try {
            ImageIcon icon = new ImageIcon("images/icon_hoadon.png");
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            lblTitle.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Tra cứu lịch sử giao dịch và chi tiết bán hàng.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(roundBtn("📊 Xuất Báo Cáo", new Color(243, 244, 246), CLR_TEXT));
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(22, 28, 22, 28));

        // Thống kê nhanh
        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 80));
        stats.add(statCard("images/icon_thongke.png",  "Doanh Thu",   "250,000đ", CLR_GREEN, new Color(220, 252, 231)));
        stats.add(statCard("images/icon_hoadon.png",   "Số Hoá Đơn",  "12",       CLR_AMBER, new Color(254, 243, 199)));
        stats.add(statCard("images/icon_khachhang.png", "Khách Hàng",  "8",        new Color(59, 130, 246), new Color(219, 234, 254)));
        content.add(stats, BorderLayout.NORTH);

        // Bảng
        content.add(buildTablePanel(), BorderLayout.CENTER);

        return content;
    }

    private JPanel buildTablePanel() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        // Filter bar
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        filter.setOpaque(false);
        filter.setBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER));
        
        // Tạo khung tìm kiếm hiện đại (Icon nằm trong khung)
        JPanel searchBox = new JPanel(new BorderLayout(8, 0));
        searchBox.setBackground(CLR_WHITE);
        searchBox.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(4, 8, 4, 8)));
        
        // Icon kính lúp bên trong
        JLabel lblIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("images/icon_timkiem.png");
            Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        
        // Ô nhập liệu không viền
        JTextField txSearch = new JTextField(15);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txSearch.setBorder(null); // Xóa viền của riêng ô nhập
        
        final String PLACEHOLDER = "Tìm mã HD...";
        txSearch.setText(PLACEHOLDER);
        txSearch.setForeground(CLR_GRAY);
        
        txSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txSearch.getText().equals(PLACEHOLDER)) {
                    txSearch.setText("");
                    txSearch.setForeground(CLR_TEXT);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txSearch.getText().isEmpty()) {
                    txSearch.setText(PLACEHOLDER);
                    txSearch.setForeground(CLR_GRAY);
                }
            }
        });

        // Thêm sự kiện tìm kiếm khi nhấn Enter
        ActionListener searchAction = e -> {
            String ma = txSearch.getText().trim();
            if (ma.isEmpty() || ma.equals(PLACEHOLDER)) {
                loadData();
            } else {
                locHoaDonTheoMa(ma);
            }
        };
        txSearch.addActionListener(searchAction);

        // Cho phép click vào kính lúp để tìm kiếm
        lblIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchAction.actionPerformed(null);
            }
        });

        searchBox.add(txSearch, BorderLayout.CENTER);
        searchBox.add(lblIcon, BorderLayout.EAST);

        filter.add(searchBox);
        filter.add(Box.createHorizontalStrut(15));
        filter.add(new JLabel("Từ ngày:"));
        
        JTextField tfDate = new JTextField("30/04/2026", 8);
        tfDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tfDate.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(5, 8, 5, 8)));
        filter.add(tfDate);
        
        wrap.add(filter, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã HD", "Ngày Lập", "Nhân Viên", "Khách Hàng", "Tổng Tiền", "Trạng Thái", "Thao Tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(48);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(243, 234, 220));
        table.setSelectionBackground(new Color(254, 243, 224));
        table.setSelectionForeground(CLR_BROWN); // Đổi chữ sang màu Nâu khi chọn
        table.setShowVerticalLines(false);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setPreferredSize(new Dimension(0, 40));

        table.getColumnModel().getColumn(6).setCellRenderer((t,v,s,f,r,c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            p.setBackground(CLR_WHITE);
            p.add(miniBtn("Chi Tiết", CLR_BROWN, CLR_WHITE));
            return p;
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 6) {
                    moChiTietHoaDon(tableModel.getValueAt(row, 0).toString());
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);

        return wrap;
    }

    private void moChiTietHoaDon(String maHD) {
        // Tạo dữ liệu mẫu cho bảng chi tiết (trong thực tế sẽ lấy từ Database)
        String[] cols = {"STT", "Tên", "Đơn Giá", "SL", "Thành Tiền"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        model.addRow(new Object[]{"1", "Cà Phê Sữa", "30,000đ", "2", "60,000đ"});
        model.addRow(new Object[]{"2", "Trà Đào Cam Sả", "35,000đ", "1", "35,000đ"});
        
        // Gọi Dialog tập trung
        DialogHoaDon dlg = new DialogHoaDon(
            (Frame)SwingUtilities.getWindowAncestor(this), 
            maHD, "admin", "Khách Lẻ", "", model, 95000.0
        );
        dlg.setVisible(true);
    }

    private void locHoaDonTheoMa(String ma) {
        tableModel.setRowCount(0);
        java.util.List<Object[]> ds = DataManager.getDsHoaDon();
        String search = ma.toLowerCase();
        for (int i = ds.size() - 1; i >= 0; i--) {
            Object[] row = ds.get(i);
            String maHD = row[0].toString().toLowerCase();
            if (maHD.contains(search)) {
                tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], ""});
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        java.util.List<Object[]> ds = DataManager.getDsHoaDon();
        // Duyệt ngược danh sách để đưa hóa đơn mới nhất lên đầu
        for (int i = ds.size() - 1; i >= 0; i--) {
            Object[] row = ds.get(i);
            tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], ""});
        }
    }

    private JPanel statCard(String imgPath, String label, String val, Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(15, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 18, 12, 18));
        
        JLabel lblIco = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            lblIco.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        
        lblIco.setOpaque(true); lblIco.setBackground(bg); lblIco.setPreferredSize(new Dimension(50, 50));
        lblIco.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 2)); txt.setOpaque(false);
        JLabel l1 = new JLabel(val); l1.setFont(new Font("Segoe UI", Font.BOLD, 20)); l1.setForeground(accent);
        JLabel l2 = new JLabel(label); l2.setFont(new Font("Segoe UI", Font.PLAIN, 13)); l2.setForeground(CLR_GRAY);
        txt.add(l1); txt.add(l2);
        
        card.add(lblIco, BorderLayout.WEST);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private JButton roundBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 40));
        return btn;
    }

    private JButton miniBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(fg);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(85, 32));
        return btn;
    }
}
