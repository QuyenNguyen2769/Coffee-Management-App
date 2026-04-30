package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class PanelKhuyenMai extends JPanel {

    // ── Bảng màu ─────────────────────────────────────────
    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_CREAM  = new Color(254, 243, 224);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_RED    = new Color(220, 53,  69);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    private DefaultTableModel tableModel;
    private JTable            table;

    public PanelKhuyenMai() {
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

        JLabel lblTitle = new JLabel("CHƯƠNG TRÌNH KHUYẾN MÃI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Tạo và quản lý các mã giảm giá cho khách hàng.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        JButton btnTao = roundBtn("Tạo Voucher +", CLR_AMBER, CLR_WHITE);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnTao);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(22, 28, 22, 28));
        content.add(buildStatCards(), BorderLayout.NORTH);
        content.add(buildTable(),     BorderLayout.CENTER);
        return content;
    }

    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);
        row.add(statCard("Voucher", "Tổng Chương Trình", "3", CLR_AMBER, new Color(254, 243, 199)));
        row.add(statCard("Hoạt Động", "Đang Áp Dụng", "2", CLR_GREEN, new Color(220, 252, 231)));
        row.add(statCard("Hết Hạn", "Đã Kết Thúc", "1", CLR_RED, new Color(254, 226, 226)));
        return row;
    }

    private JPanel statCard(String title, String label, String val, Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));

        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblVal.setForeground(accent);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLbl.setForeground(CLR_GRAY);

        txt.add(lblVal);
        txt.add(lblLbl);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTable() {
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
        tblHdr.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER), new EmptyBorder(14, 18, 14, 18)));

        JLabel lblTblTitle = new JLabel("Danh sách Mã Giảm Giá");
        lblTblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTblTitle.setForeground(CLR_TEXT);
        tblHdr.add(lblTblTitle, BorderLayout.WEST);

        JTextField txSearch = new JTextField(14);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txSearch.setText("Tìm kiếm mã...");
        tblHdr.add(txSearch, BorderLayout.EAST);
        wrap.add(tblHdr, BorderLayout.NORTH);

        String[] cols = {"Mã Voucher", "Tên Chương Trình", "Mức Giảm", "Loại", "Hạn Sử Dụng", "Trạng Thái", "Thao Tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(48);
        table.setShowHorizontalLines(true);
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
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel buildActPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        p.setBackground(CLR_WHITE);
        p.add(miniBtn("Sửa", new Color(59, 130, 246), CLR_WHITE));
        p.add(miniBtn("Xóa", CLR_RED, CLR_WHITE));
        return p;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Object[] row : DataManager.getDsKhuyenMai())
            tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], ""});
    }

    private JButton roundBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(fg);
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
        btn.setPreferredSize(new Dimension(140, 36));
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
}