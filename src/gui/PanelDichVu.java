package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class PanelDichVu extends JPanel {

    // ── Bảng màu ─────────────────────────────────────────
    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_CREAM  = new Color(254, 243, 224);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_RED    = new Color(220, 53,  69);
    private static final Color CLR_BLUE   = new Color(37,  99,  235);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    private static final String[] DANH_MUC = {
        "Tất Cả", "Cà Phê", "Trà", "Bánh & Ăn Vặt", "Nước Ép", "Khác"
    };

    private DefaultTableModel tableModel;
    private JTable            table;
    private JPanel            gridPanel;
    private boolean           xemDanhSach = true;
    private JButton           btnToggle;

    public PanelDichVu() {
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

        JLabel lblTitle = new JLabel("QUẢN LÝ DỊCH VỤ"); // Bỏ Emoji cà phê
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Quản lý món ăn, đồ uống và giá tiền hệ thống.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        btnToggle = roundBtn("Dạng Lưới", new Color(229, 231, 235), CLR_TEXT);
        btnToggle.addActionListener(e -> toggleView());

        JButton btnThem = roundBtn("Thêm Món +", CLR_BROWN, CLR_WHITE);
        btnThem.addActionListener(e -> moForm(null, -1));

        right.add(btnToggle);
        right.add(btnThem);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(20, 28, 20, 28));

        content.add(buildStatCards(),   BorderLayout.NORTH);
        content.add(buildFilterBar(),   BorderLayout.CENTER);
        return content;
    }

    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        row.add(statCard("Sản Phẩm", "Tổng Số Món", "12", CLR_BROWN, new Color(253, 243, 221)));
        row.add(statCard("Trạng Thái", "Đang Phục Vụ", "9", CLR_GREEN, new Color(220, 252, 231)));
        row.add(statCard("Yêu Thích", "Bán Chạy", "Cà Phê", CLR_BLUE, new Color(219, 234, 254)));
        row.add(statCard("Doanh Số", "Giá TB", "45,000đ", CLR_AMBER, new Color(254, 243, 199)));

        return row;
    }

    private JPanel statCard(String title, String label, String val, Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));

        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVal.setForeground(accent);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLbl.setForeground(CLR_GRAY);

        txt.add(lblVal);
        txt.add(lblLbl);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private JPanel filterBar;
    private JPanel viewWrapper;

    private JPanel buildFilterBar() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setOpaque(false);

        filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterBar.setOpaque(false);

        for (String dm : DANH_MUC) {
            JButton btnDM = new JButton(dm) {
                boolean active = dm.equals("Tất Cả");
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color bg = active ? CLR_BROWN : CLR_WHITE;
                    g2.setColor(bg);
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                    if (!active) {
                        g2.setColor(CLR_BORDER);
                        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 20, 20));
                    }
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            btnDM.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDM.setForeground(dm.equals("Tất Cả") ? CLR_WHITE : CLR_GRAY);
            btnDM.setContentAreaFilled(false);
            btnDM.setBorderPainted(false);
            btnDM.setFocusPainted(false);
            btnDM.setBorder(new EmptyBorder(6, 16, 6, 16));
            btnDM.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDM.addActionListener(e -> {
                for (Component c : filterBar.getComponents()) {
                    if (c instanceof JButton b) b.setForeground(CLR_GRAY);
                }
                btnDM.setForeground(CLR_WHITE);
            });
            filterBar.add(btnDM);
        }

        outer.add(filterBar, BorderLayout.NORTH);
        viewWrapper = new JPanel(new BorderLayout());
        viewWrapper.setOpaque(false);
        viewWrapper.add(buildTableView(), BorderLayout.CENTER);
        outer.add(viewWrapper, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildTableView() {
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

        JPanel tblHdr = new JPanel(new BorderLayout());
        tblHdr.setOpaque(false);
        tblHdr.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER), new EmptyBorder(12, 18, 12, 18)));

        JLabel lblT = new JLabel("Danh Sách Món Ăn");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);

        JTextField txS = new JTextField(14);
        txS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txS.setBorder(new CompoundBorder(new LineBorder(CLR_BORDER, 1, true), new EmptyBorder(5, 10, 5, 10)));
        txS.setText("Tìm kiếm...");
        txS.setForeground(CLR_GRAY);

        tblHdr.add(lblT, BorderLayout.WEST);
        tblHdr.add(txS,  BorderLayout.EAST);
        wrap.add(tblHdr, BorderLayout.NORTH);

        String[] cols = {"STT", "Tên Món", "Danh Mục", "Giá Bán", "Đơn Vị", "Trạng Thái", "Thao Tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(48);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(243, 234, 220));
        table.setBackground(CLR_WHITE);
        table.setSelectionBackground(CLR_CREAM);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setForeground(CLR_GRAY);
        th.setPreferredSize(new Dimension(0, 42));

        table.getColumnModel().getColumn(6).setCellRenderer((t, val, sel, foc, r, c) -> buildActPanel());
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.getVerticalScrollBar().setUnitIncrement(100); // Tốc độ tối ưu
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    private void toggleView() {
        xemDanhSach = !xemDanhSach;
        viewWrapper.removeAll();
        if (xemDanhSach) {
            viewWrapper.add(buildTableView(), BorderLayout.CENTER);
            loadData();
            btnToggle.setText("Dạng Lưới");
        } else {
            viewWrapper.add(buildGridView(), BorderLayout.CENTER);
            btnToggle.setText("Dạng Bảng");
        }
        viewWrapper.revalidate();
        viewWrapper.repaint();
    }

    private JScrollPane buildGridView() {
        JPanel grid = new JPanel(new WrapLayout(FlowLayout.LEFT, 14, 14));
        grid.setBackground(BG_PAGE);
        // Load cards from DataManager...
        for (Object[] d : DataManager.getDsDichVu()) {
            grid.add(buildMiniCard(new String[]{d[1].toString(), d[2].toString(), d[3].toString(), d[5].toString()}));
        }
        JScrollPane sp = new JScrollPane(grid);
        sp.getVerticalScrollBar().setUnitIncrement(100); // Tốc độ tối ưu
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_PAGE);
        return sp;
    }

    private JPanel buildMiniCard(String[] d) {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(200, 260));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Phần ảnh sản phẩm
        JLabel lblImg = new JLabel("", SwingConstants.CENTER);
        lblImg.setPreferredSize(new Dimension(180, 160));
        try {
            // Thử tìm ảnh theo tên hoặc mã món trong thư mục images
            String imgName = d[0].toLowerCase().replace(" ", "_") + ".png";
            java.io.File f = new java.io.File("images/" + imgName);
            if (!f.exists()) f = new java.io.File("images/douong_default.png");
            
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        card.add(lblImg, BorderLayout.NORTH);

        JLabel lblTen = new JLabel(d[0]);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTen.setForeground(CLR_TEXT);

        JLabel lblGia = new JLabel(d[2]);
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGia.setForeground(CLR_AMBER);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(lblTen); 
        info.add(Box.createVerticalStrut(5));
        info.add(lblGia);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    private void moForm(Object[] data, int editRow) {
        // Form logic here (simplified for integration)
    }

    private void loadData() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (Object[] row : DataManager.getDsDichVu())
            tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], ""});
    }

    private JPanel buildActPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        p.setBackground(CLR_WHITE);
        p.add(miniBtn("Sửa", new Color(59, 130, 246), CLR_WHITE));
        p.add(miniBtn("Xóa", CLR_RED, CLR_WHITE));
        return p;
    }

    private JButton roundBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setPaint(fg);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 36));
        return btn;
    }

    private JButton miniBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(fg);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(75, 30));
        return btn;
    }

    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        @Override public Dimension preferredLayoutSize(Container t) { return layout(t, true); }
        private Dimension layout(Container t, boolean pref) {
            synchronized (t.getTreeLock()) {
                int w = t.getWidth(); if (w == 0) w = Integer.MAX_VALUE;
                Insets ins = t.getInsets();
                w -= ins.left + ins.right + getHgap() * 2;
                int x = getHgap(), y = getVgap(), rowH = 0, maxW = 0;
                for (Component c : t.getComponents()) {
                    Dimension d = pref ? c.getPreferredSize() : c.getMinimumSize();
                    if (x + d.width > w) { x = getHgap(); y += rowH + getVgap(); rowH = 0; }
                    x += d.width + getHgap(); rowH = Math.max(rowH, d.height);
                    maxW = Math.max(maxW, x);
                }
                return new Dimension(maxW + ins.left + ins.right, y + rowH + getVgap() + ins.top + ins.bottom);
            }
        }
    }
}