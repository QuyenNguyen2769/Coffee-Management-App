package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class PanelTroGiup extends JPanel {

    // ── Màu sắc ──────────────────────────────────────
    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_CREAM  = new Color(254, 243, 224);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_BLUE   = new Color(37,  99,  235);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    // ── Nội dung hướng dẫn ───────────────────────────
    private static final Object[][] CHUONG = {
        {"", "Trang Chủ", new String[][]{
            {"Trang Chủ là gì?",
             "Trang Chủ là màn hình chào mừng khi bạn đăng nhập vào hệ thống. "
             + "Tại đây bạn có thể thấy tổng quan về hoạt động của quán."},
            {"Cách quay về Trang Chủ?",
             "Nhấn vào menu 'Hệ Thống' ở thanh trên cùng → chọn 'Trang Chủ'. "
             + "Hoặc nhấn vào logo quán ở góc trên bên trái."},
        }},
        {"", "Dịch Vụ", new String[][]{
            {"Thêm món mới?",
             "Vào menu 'Danh Mục' → 'Dịch Vụ' → nhấn nút '+ Thêm Món' ở góc phải trên. "
             + "Điền tên món, danh mục, giá bán rồi nhấn 'Thêm Món' để lưu."},
            {"Sửa thông tin món?",
             "Trong bảng danh sách món, tìm món cần sửa → nhấn nút '✏ Sửa' ở cột Thao Tác. "
             + "Cập nhật thông tin rồi nhấn 'Lưu'."},
            {"Xóa món?",
             "Nhấn nút '🗑 Xóa' ở cột Thao Tác. Hệ thống sẽ hỏi xác nhận trước khi xóa. "
             + "Lưu ý: không thể hoàn tác sau khi xóa."},
            {"Chuyển dạng hiển thị?",
             "Nhấn nút '⊞ Dạng Lưới' để xem ảnh sản phẩm, "
             + "hoặc '☰ Dạng Bảng' để xem dạng danh sách chi tiết."},
        }},
        {"", "Khuyến Mãi", new String[][]{
            {"Tạo voucher mới?",
             "Vào menu 'Danh Mục' → 'Khuyến Mãi' → nhấn '+ Tạo Voucher'. "
             + "Điền mã voucher, tên chương trình, mức giảm, loại giảm (% hoặc số tiền) "
             + "và hạn sử dụng."},
            {"Các loại giảm giá?",
             "Hệ thống hỗ trợ 2 loại:\n"
             + "• Phần Trăm (%): giảm theo tỉ lệ phần trăm hóa đơn.\n"
             + "• Số Tiền (đ): giảm trực tiếp một số tiền cố định."},
            {"Trạng thái voucher?",
             "• Đang Hoạt Động: voucher đang có hiệu lực.\n"
             + "• Hết Hạn: voucher đã quá hạn sử dụng.\n"
             + "• Chờ Kích Hoạt: voucher chưa bắt đầu hiệu lực."},
        }},
        {"", "Thống Kê", new String[][]{
            {"Xem doanh thu theo ngày?",
             "Vào menu 'Thống Kê' → 'Doanh Thu' → 'Theo Ngày'. "
             + "Chọn ngày cần xem, hệ thống sẽ hiển thị biểu đồ và tổng doanh thu."},
            {"Xem thống kê khách hàng?",
             "Vào menu 'Thống Kê' → 'Khách Hàng' để xem danh sách khách hàng "
             + "mua nhiều nhất và có thể dùng làm cơ sở tặng quà, ưu đãi."},
        }},
        {"", "Tài Khoản", new String[][]{
            {"Xem thông tin cá nhân?",
             "Vào menu 'Hệ Thống' → 'Tài Khoản' để xem và cập nhật "
             + "thông tin cá nhân của bạn."},
            {"Đăng xuất?",
             "Vào menu 'Hệ Thống' → 'Đăng Xuất' hoặc nhấn Ctrl+X. "
             + "Nên đăng xuất khi rời khỏi máy tính để bảo mật."},
            {"Thoát chương trình?",
             "Vào menu 'Hệ Thống' → 'Thoát' hoặc nhấn Ctrl+O. "
             + "Hệ thống sẽ hỏi xác nhận trước khi đóng."},
        }},
    };

    private JPanel detailPanel;
    private JPanel menuPanel;

    public PanelTroGiup() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildBody(),    BorderLayout.CENTER);
    }

    // ═══════════════════════════════════════════════════
    //  HEADER
    // ═══════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_WHITE);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(18, 28, 18, 28)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblT = new JLabel("❓  Trợ Giúp");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblT.setForeground(CLR_BROWN);

        JLabel lblS = new JLabel("Hướng dẫn sử dụng phần mềm quản lý quán cà phê.");
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblS.setForeground(CLR_GRAY);

        left.add(lblT);
        left.add(Box.createVerticalStrut(3));
        left.add(lblS);
        hdr.add(left, BorderLayout.WEST);

        // Phiên bản
        JLabel lblVer = new JLabel("v1.0.0  •  QMT Cafe");
        lblVer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVer.setForeground(CLR_GRAY);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(lblVer);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    // ═══════════════════════════════════════════════════
    //  BODY: sidebar trái + nội dung phải
    // ═══════════════════════════════════════════════════
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(BG_PAGE);
        body.setBorder(new EmptyBorder(20, 28, 20, 28));

        // ── Sidebar trái ──
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(CLR_WHITE);
        menuPanel.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(8, 0, 8, 0)));
        menuPanel.setPreferredSize(new Dimension(210, 0));

        JLabel lblMenu = new JLabel("  Chủ Đề");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenu.setForeground(CLR_GRAY);
        lblMenu.setBorder(new EmptyBorder(8, 16, 8, 16));
        menuPanel.add(lblMenu);

        // Panel nội dung phải
        detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(BG_PAGE);

        // Mặc định chọn chủ đề đầu tiên
        JButton[] btns = new JButton[CHUONG.length];
        for (int i = 0; i < CHUONG.length; i++) {
            final int idx = i;
            String ico  = (String)   CHUONG[i][0];
            String name = (String)   CHUONG[i][1];
            JButton btn = sideBtn(ico + "  " + name);
            btns[i] = btn;
            btn.addActionListener(e -> {
                // Reset tất cả
                for (JButton b : btns) {
                    b.setBackground(CLR_WHITE);
                    b.setForeground(CLR_TEXT);
                    b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
                // Active
                btn.setBackground(CLR_CREAM);
                btn.setForeground(CLR_BROWN);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                // Hiện nội dung
                detailPanel.removeAll();
                detailPanel.add(buildDetail(idx), BorderLayout.CENTER);
                detailPanel.revalidate();
                detailPanel.repaint();
            });
            menuPanel.add(btn);
        }

        // Click chủ đề đầu tiên mặc định
        btns[0].doClick();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            menuPanel, detailPanel);
        split.setDividerLocation(210);
        split.setDividerSize(1);
        split.setBorder(null);
        split.setBackground(BG_PAGE);

        body.add(split, BorderLayout.CENTER);
        return body;
    }

    // ═══════════════════════════════════════════════════
    //  PANEL NỘI DUNG TỪNG CHỦ ĐỀ
    // ═══════════════════════════════════════════════════
    private JScrollPane buildDetail(int idx) {
        String ico    = (String)     CHUONG[idx][0];
        String name   = (String)     CHUONG[idx][1];
        String[][] qa = (String[][]) CHUONG[idx][2];

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PAGE);
        panel.setBorder(new EmptyBorder(0, 20, 20, 8));

        // Tiêu đề chủ đề
        JPanel titRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titRow.setOpaque(false);
        titRow.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel lblIco = new JLabel(ico);
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel titTxt = new JPanel();
        titTxt.setOpaque(false);
        titTxt.setLayout(new BoxLayout(titTxt, BoxLayout.Y_AXIS));
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblName.setForeground(CLR_BROWN);
        JLabel lblCount = new JLabel(qa.length + " câu hỏi thường gặp");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCount.setForeground(CLR_GRAY);
        titTxt.add(lblName);
        titTxt.add(lblCount);

        titRow.add(lblIco);
        titRow.add(titTxt);
        panel.add(titRow);

        // Từng câu hỏi dạng Accordion
        for (String[] item : qa) {
            panel.add(buildAccordion(item[0], item[1]));
            panel.add(Box.createVerticalStrut(10));
        }

        // Card liên hệ hỗ trợ ở dưới cùng
        panel.add(Box.createVerticalStrut(10));
        panel.add(buildContactCard());

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.setBackground(BG_PAGE);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ── Accordion (câu hỏi bấm mở/đóng) ─────────────
    private JPanel buildAccordion(String question, String answer) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1000));

        // Nút câu hỏi
        JButton btnQ = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ?
                             new Color(254, 249, 241) : CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 12, 12));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnQ.setLayout(new BorderLayout(12, 0));
        btnQ.setContentAreaFilled(false);
        btnQ.setBorderPainted(false);
        btnQ.setFocusPainted(false);
        btnQ.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQ.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel lblQ = new JLabel(question);
        lblQ.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblQ.setForeground(CLR_TEXT);

        JLabel lblArrow = new JLabel("▶");
        lblArrow.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblArrow.setForeground(CLR_AMBER);

        btnQ.add(lblArrow, BorderLayout.WEST);
        btnQ.add(lblQ,     BorderLayout.CENTER);
        card.add(btnQ, BorderLayout.NORTH);

        // Panel câu trả lời (ẩn ban đầu)
        JPanel ansPanel = new JPanel(new BorderLayout());
        ansPanel.setOpaque(false);
        ansPanel.setVisible(false);
        ansPanel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, CLR_BORDER),
            new EmptyBorder(12, 18, 16, 18)));

        // Xử lý xuống dòng \n trong câu trả lời
        String htmlAns = "<html>" + answer
            .replace("\n", "<br>")
            .replace("•", "&bull;") + "</html>";
        JLabel lblA = new JLabel(htmlAns);
        lblA.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblA.setForeground(new Color(55, 45, 30));

        ansPanel.add(lblA, BorderLayout.CENTER);
        card.add(ansPanel, BorderLayout.CENTER);

        // Toggle
        btnQ.addActionListener(e -> {
            boolean show = !ansPanel.isVisible();
            ansPanel.setVisible(show);
            lblArrow.setText(show ? "▼" : "▶");
            lblArrow.setForeground(show ? CLR_BROWN : CLR_AMBER);
            card.revalidate();
            card.repaint();
            // Scroll để thấy câu trả lời
            SwingUtilities.invokeLater(() -> {
                JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(
                    JScrollPane.class, card);
                if (sp != null) {
                    Rectangle r = card.getBounds();
                    sp.getViewport().scrollRectToVisible(r);
                }
            });
        });

        return card;
    }

    // ── Card liên hệ hỗ trợ ──────────────────────────
    private JPanel buildContactCard() {
        JPanel card = new JPanel(new BorderLayout(16, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Nền gradient nhẹ
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(254, 243, 199),
                    getWidth(), 0, new Color(254, 215, 170));
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblIco = new JLabel("💬");
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));

        JLabel lblT = new JLabel("Vẫn còn thắc mắc?");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblT.setForeground(CLR_BROWN);

        JLabel lblS = new JLabel(
            "Liên hệ quản lý hệ thống để được hỗ trợ trực tiếp.");
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblS.setForeground(new Color(120, 80, 40));

        txt.add(lblT);
        txt.add(Box.createVerticalStrut(3));
        txt.add(lblS);

        card.add(lblIco, BorderLayout.WEST);
        card.add(txt,    BorderLayout.CENTER);
        return card;
    }

    // ═══════════════════════════════════════════════════
    //  HELPERS UI
    // ═══════════════════════════════════════════════════
    private JButton sideBtn(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(CLR_TEXT);
        btn.setBackground(CLR_WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 16, 12, 16));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(CLR_CREAM)) {
                    btn.setBackground(new Color(250, 245, 235));
                    btn.repaint();
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(CLR_CREAM)) {
                    btn.setBackground(CLR_WHITE);
                    btn.repaint();
                }
            }
        });
        return btn;
    }
}