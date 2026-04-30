package gui;

import connectDB.ConnectDB;
import entity.TaiKhoan;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PanelTaiKhoan.java — Hệ Thống → Tài Khoản
 * Thông tin cá nhân (từ bảng NhanVien): Họ Tên, SĐT, Địa Chỉ, Email, Chức Vụ, Ngày Vào Làm
 * Đổi mật khẩu: kiểm tra mật khẩu cũ → cập nhật mới
 */
public class PanelTaiKhoan extends JPanel {

    private static final Color C_BG      = new Color(245, 246, 250);
    private static final Color C_WHITE    = Color.WHITE;
    private static final Color C_TITLE    = new Color(44, 28, 18);
    private static final Color C_BORDER   = new Color(210, 190, 170);
    private static final Color C_LBL      = new Color(60, 40, 20);
    private static final Color C_VAL      = new Color(30, 30, 30);
    private static final Color C_BTN_SAVE = new Color(46, 139, 87);
    private static final Color C_BTN_CLR  = new Color(120, 120, 120);
    private static final Font  F_TITLE    = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  F_SECTION  = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font  F_LABEL    = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_VALUE    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  F_BTN      = new Font("Segoe UI", Font.BOLD, 13);

    private TaiKhoan taiKhoan;

    // Giá trị hiển thị (từ NhanVien)
    private JLabel valHoTen, valSDT, valDiaChi, valEmail, valChucVu, valNgayVaoLam;

    // Đổi mật khẩu
    private JPasswordField txtCu, txtMoi, txtXacNhan;

    public PanelTaiKhoan(TaiKhoan tk) {
        this.taiKhoan = tk;
        setLayout(new BorderLayout(10, 10));
        setBackground(C_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        add(buildNorth(),  BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        loadThongTin();
    }

    public PanelTaiKhoan() { this(null); }

    // ── Tiêu đề ───────────────────────────────────────────────
    private JPanel buildNorth() {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        JLabel lbl = new JLabel("  Tài Khoản Của Tôi");
        lbl.setFont(F_TITLE); lbl.setForeground(C_TITLE);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    // ── 2 Card nằm ngang ──────────────────────────────────────
    private JPanel buildCenter() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.add(buildCardThongTin());
        row.add(buildCardDoiMatKhau());
        return row;
    }

    // ══════════════════════════════════════════════════════════
    //  CARD 1: Thông tin cá nhân (lấy từ NhanVien)
    // ══════════════════════════════════════════════════════════
    private JPanel buildCardThongTin() {
        JPanel card = makeCard();

        // Avatar
        JPanel avatarWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarWrap.setOpaque(false);
        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        avatarWrap.add(avatar);

        JLabel section = new JLabel("Thông Tin Cá Nhân");
        section.setFont(F_SECTION); section.setForeground(new Color(101, 67, 33));
        section.setBorder(new EmptyBorder(0, 0, 8, 0));

        JSeparator sep = new JSeparator(); sep.setForeground(C_BORDER);

        JPanel grid = new JPanel(new GridBagLayout()); grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5); gbc.anchor = GridBagConstraints.WEST;

        valHoTen      = makeVal("—");
        valSDT        = makeVal("—");
        valDiaChi     = makeVal("—");
        valEmail      = makeVal("—");
        valChucVu     = makeVal("—");
        valNgayVaoLam = makeVal("—");

        // Màu chức vụ sẽ được set sau khi load
        addInfo(grid, gbc, 0, "Họ Tên:",         valHoTen);
        addInfo(grid, gbc, 1, "Số Điện Thoại:",   valSDT);
        addInfo(grid, gbc, 2, "Địa Chỉ:",         valDiaChi);
        addInfo(grid, gbc, 3, "Email:",            valEmail);
        addInfo(grid, gbc, 4, "Chức Vụ:",         valChucVu);
        addInfo(grid, gbc, 5, "Ngày Vào Làm:",    valNgayVaoLam);

        card.add(avatarWrap);
        card.add(Box.createVerticalStrut(4));
        card.add(section); card.add(sep);
        card.add(Box.createVerticalStrut(8));
        card.add(grid);
        return card;
    }

    // ══════════════════════════════════════════════════════════
    //  CARD 2: Đổi mật khẩu
    // ══════════════════════════════════════════════════════════
    private JPanel buildCardDoiMatKhau() {
        JPanel card = makeCard();

        JLabel section = new JLabel("Đổi Mật Khẩu");
        section.setFont(F_SECTION); section.setForeground(new Color(101, 67, 33));
        section.setBorder(new EmptyBorder(0, 0, 8, 0));
        JSeparator sep = new JSeparator(); sep.setForeground(C_BORDER);

        JPanel form = new JPanel(new GridBagLayout()); form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtCu     = styledPF();
        txtMoi    = styledPF();
        txtXacNhan= styledPF();
        addPF(form, gbc, 0, "Mật khẩu hiện tại:", txtCu);
        addPF(form, gbc, 1, "Mật khẩu mới:", txtMoi);
        addPF(form, gbc, 2, "Xác nhận mật khẩu mới:", txtXacNhan);

        gbc.gridy=3; gbc.gridx=0; gbc.gridwidth=2;
        JLabel note = new JLabel("<html><i>Mật khẩu mới ít nhất 6 ký tự.</i></html>");
        note.setFont(new Font("Segoe UI",Font.ITALIC,11)); note.setForeground(Color.GRAY);
        form.add(note, gbc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER,12,0)); btnRow.setOpaque(false);
        JButton btnSave  = makeBtn("Lưu Mật Khẩu", C_BTN_SAVE);
        JButton btnClear = makeBtn("Xóa Trắng",    C_BTN_CLR);
        btnRow.add(btnSave); btnRow.add(btnClear);
        btnSave .addActionListener(e -> doDoiMatKhau());
        btnClear.addActionListener(e -> { txtCu.setText(""); txtMoi.setText(""); txtXacNhan.setText(""); });

        card.add(Box.createVerticalStrut(60));
        card.add(section); card.add(sep);
        card.add(Box.createVerticalStrut(10));
        card.add(form);
        card.add(Box.createVerticalStrut(15));
        card.add(btnRow);
        return card;
    }

    // ══════════════════════════════════════════════════════════
    //  LOGIC
    // ══════════════════════════════════════════════════════════
    private void loadThongTin() {
        if (taiKhoan == null) return;
        String maNV = taiKhoan.getMaNV();
        if (maNV == null) return;
        try (Connection conn = ConnectDB.getConnection()) {
            if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT hoTen, sdt, diaChi, email, chucVu, " +
                "CONVERT(VARCHAR,ngayVaoLam,103) AS ngayVaoLam " +
                "FROM NhanVien WHERE maNV=?");
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valHoTen     .setText(nullSafe(rs.getString("hoTen")));
                valSDT       .setText(nullSafe(rs.getString("sdt")));
                valDiaChi    .setText(nullSafe(rs.getString("diaChi")));
                valEmail     .setText(nullSafe(rs.getString("email")));

                String cv = nullSafe(rs.getString("chucVu"));
                valChucVu.setText(cv.isEmpty() ? "—" : cv);
                valChucVu.setForeground(NhanVienHelper.mauChucVu(cv));
                valChucVu.setFont(new Font("Segoe UI", Font.BOLD, 13));

                String ngay = rs.getString("ngayVaoLam");
                valNgayVaoLam.setText(ngay == null ? "—" : ngay);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void doDoiMatKhau() {
        String cu   = new String(txtCu.getPassword()).trim();
        String moi  = new String(txtMoi.getPassword()).trim();
        String nhap = new String(txtXacNhan.getPassword()).trim();

        if (cu.isEmpty() || moi.isEmpty() || nhap.isEmpty()) {
            msg("Vui lòng điền đầy đủ các trường!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return;
        }
        if (moi.length() < 6) {
            msg("Mật khẩu mới ít nhất 6 ký tự!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return;
        }
        if (!moi.equals(nhap)) {
            msg("Mật khẩu mới và xác nhận không khớp!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return;
        }
        if (taiKhoan == null) {
            msg("Không xác định được tài khoản!","Lỗi",JOptionPane.ERROR_MESSAGE); return;
        }
        try (Connection conn = ConnectDB.getConnection()) {
            // Kiểm tra mật khẩu cũ
            PreparedStatement chk = conn.prepareStatement(
                "SELECT COUNT(*) FROM TaiKhoan WHERE userName=? AND password=?");
            chk.setString(1, taiKhoan.getUserName()); chk.setString(2, cu);
            ResultSet rs = chk.executeQuery(); rs.next();
            if (rs.getInt(1) == 0) {
                msg("Mật khẩu hiện tại không đúng!","Lỗi",JOptionPane.ERROR_MESSAGE); return;
            }
            // Cập nhật
            PreparedStatement upd = conn.prepareStatement(
                "UPDATE TaiKhoan SET password=? WHERE userName=?");
            upd.setString(1, moi); upd.setString(2, taiKhoan.getUserName());
            upd.executeUpdate();
            
            // Cập nhật pass vào object hiện tại
            taiKhoan.setPassword(moi);
            
            msg("Đổi mật khẩu thành công!","Thành công",JOptionPane.INFORMATION_MESSAGE);
            txtCu.setText(""); txtMoi.setText(""); txtXacNhan.setText("");
        } catch (Exception ex) { msg("Lỗi: "+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE); }
    }

    // ── Builders ──────────────────────────────────────────────
    private JPanel makeCard() {
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBackground(C_WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER,1),
            new EmptyBorder(20,25,20,25)));
        return c;
    }

    private void addInfo(JPanel p, GridBagConstraints gbc, int row, String label, JLabel val) {
        gbc.gridy=row; gbc.gridx=0; gbc.weightx=0;
        JLabel l=new JLabel(label); l.setFont(F_LABEL); l.setForeground(C_LBL);
        p.add(l,gbc);
        gbc.gridx=1; gbc.weightx=1; p.add(val,gbc);
    }

    private void addPF(JPanel p, GridBagConstraints gbc, int row, String label, JPasswordField pf) {
        gbc.gridy=row; gbc.gridx=0; gbc.weightx=0; gbc.gridwidth=1;
        JLabel l=new JLabel(label); l.setFont(F_LABEL); l.setForeground(C_LBL);
        p.add(l,gbc);
        gbc.gridx=1; gbc.weightx=1; p.add(pf,gbc);
    }

    private JLabel makeVal(String t) {
        JLabel l=new JLabel(t); l.setFont(F_VALUE); l.setForeground(C_VAL); return l;
    }

    private JPasswordField styledPF() {
        JPasswordField pf=new JPasswordField(20); pf.setFont(F_VALUE);
        pf.setPreferredSize(new Dimension(200,30)); return pf;
    }

    private JButton makeBtn(String t, Color bg) {
        JButton btn=new JButton(t);
        btn.setFont(F_BTN); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(160,36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter(){
            final Color o=bg;
            public void mouseEntered(MouseEvent e){btn.setBackground(o.darker());}
            public void mouseExited (MouseEvent e){btn.setBackground(o);}
        });
        return btn;
    }

    private String nullSafe(String s){ return s==null?"":s; }
    private void   msg(String m,String t,int type){ JOptionPane.showMessageDialog(this,m,t,type); }
}
