package gui;

import connectDB.ConnectDB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PanelTimKhachHang.java — Tìm kiếm + Cập nhật điểm tích lũy
 * - Tìm theo: Mã KH / Họ Tên / SĐT / Loại Khách
 * - Nút "Cập nhật điểm": cộng số điểm nhập vào điểm cũ, tự tính loại mới
 * - Sau khi cập nhật tự reload cả PanelKhachHang (nếu đang mở)
 */
public class PanelTimKhachHang extends JPanel {

    private static final Color C_BG        = new Color(245, 246, 250);
    private static final Color C_WHITE      = Color.WHITE;
    private static final Color C_TITLE      = new Color(44, 28, 18);
    private static final Color C_HDR_BG     = new Color(101, 67, 33);
    private static final Color C_HDR_FG     = new Color(62, 39, 35);
    private static final Color C_SELECT     = new Color(210, 180, 140);
    private static final Color C_BTN_FIND   = new Color(70, 130, 180);
    private static final Color C_BTN_RESET  = new Color(120, 120, 120);
    private static final Color C_BTN_UPDATE = new Color(46, 139, 87);
    private static final Font  F_TITLE      = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  F_LABEL      = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_INPUT      = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  F_BTN        = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_TABLE      = new Font("Segoe UI", Font.PLAIN, 13);

    private JTextField       txtKeyword, txtSoDiem;
    private JComboBox<String> cbCriteria;
    private JTable           table;
    private DefaultTableModel model;
    private JLabel           lblCount, lblDiemHienTai, lblLoaiHienTai;
    private JButton          btnCapNhat;

    /** Tham chiếu đến PanelKhachHang để đồng bộ sau khi cập nhật */
    private PanelKhachHang panelKhachHang;

    private String selectedMaKH = null;
    private int selectedDiem  = 0;

    public PanelTimKhachHang(PanelKhachHang pkh) {
        this.panelKhachHang = pkh;
        init();
    }

    public PanelTimKhachHang() {
        this(null);
    }

    private void init() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        add(buildNorth(),  BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildSouth(),  BorderLayout.SOUTH);
        search("", "Tất cả");
    }

    private JPanel buildNorth() {
        JPanel w = new JPanel(new BorderLayout(0, 10)); w.setOpaque(false);
        JLabel lbl = new JLabel("🔍  Tìm Khách Hàng");
        lbl.setFont(F_TITLE); lbl.setForeground(C_TITLE);
        w.add(lbl, BorderLayout.NORTH);
        w.add(buildSearchBar(), BorderLayout.CENTER);
        return w;
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBackground(C_WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210,190,170),1),
            new EmptyBorder(8,12,8,12)));

        cbCriteria = new JComboBox<>(new String[]{"Tất cả","Mã KH","Họ Tên","Số ĐT","Loại Khách"});
        cbCriteria.setFont(F_INPUT); cbCriteria.setPreferredSize(new Dimension(130,32));

        txtKeyword = new JTextField();
        txtKeyword.setFont(F_INPUT); txtKeyword.setPreferredSize(new Dimension(260,32));
        txtKeyword.addActionListener(e -> doSearch());

        JButton btnFind  = makeBtn("Tìm",        C_BTN_FIND,  130);
        JButton btnReset = makeBtn("Tất Cả", C_BTN_RESET, 130);
        btnFind .addActionListener(e -> doSearch());
        btnReset.addActionListener(e -> { txtKeyword.setText(""); cbCriteria.setSelectedIndex(0); search("","Tất cả"); });

        lblCount = new JLabel("Tổng: 0 khách hàng");
        lblCount.setFont(new Font("Segoe UI",Font.ITALIC,12));
        lblCount.setForeground(new Color(100,80,60));

        bar.add(new JLabel("Tìm theo:") {{ setFont(F_LABEL); setForeground(new Color(60,40,20)); }});
        bar.add(cbCriteria);
        bar.add(new JLabel("Từ khóa:") {{ setFont(F_LABEL); setForeground(new Color(60,40,20)); }});
        bar.add(txtKeyword);
        bar.add(btnFind); bar.add(btnReset);
        bar.add(Box.createHorizontalStrut(20)); bar.add(lblCount);
        return bar;
    }

    private JScrollPane buildCenter() {
        String[] cols = {"Mã KH","Họ Tên","Số ĐT","Địa Chỉ","Email","Điểm TL","Loại Khách"};
        model = new DefaultTableModel(cols,0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        table = new JTable(model);
        table.setFont(F_TABLE); table.setRowHeight(30);
        table.setShowGrid(false); table.setIntercellSpacing(new Dimension(0,0));
        table.setSelectionBackground(C_SELECT); table.setSelectionForeground(Color.BLACK);

        JTableHeader hdr = table.getTableHeader();
        hdr.setFont(new Font("Segoe UI",Font.BOLD,13));
        hdr.setBackground(C_HDR_BG); hdr.setForeground(C_HDR_FG);
        hdr.setPreferredSize(new Dimension(0,35));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                setBackground(sel ? C_SELECT : (row%2==0 ? C_WHITE : new Color(245,240,235)));
                setForeground(Color.BLACK); setFont(F_TABLE);
                if (!sel && col==6 && v!=null) {
                    setForeground(KhachHangHelper.mauLoai(v.toString()));
                    setFont(new Font("Segoe UI",Font.BOLD,13));
                }
                setHorizontalAlignment(col==0||col==5 ? SwingConstants.CENTER : SwingConstants.LEFT);
                setBorder(new EmptyBorder(0,6,0,6));
                return this;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { onRowSelect(); }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210,190,170)));
        return sp;
    }

    private JPanel buildSouth() {
        JPanel outer = new JPanel(new BorderLayout()); outer.setOpaque(false);
        JPanel card = new JPanel(new FlowLayout(FlowLayout.CENTER,14,10));
        card.setBackground(C_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210,190,170),1),
            new EmptyBorder(6,16,6,16)));

        lblDiemHienTai = new JLabel("—");
        lblDiemHienTai.setFont(new Font("Segoe UI",Font.BOLD,13));
        lblLoaiHienTai = new JLabel("—");
        lblLoaiHienTai.setFont(new Font("Segoe UI",Font.BOLD,13));

        txtSoDiem = new JTextField("0");
        txtSoDiem.setFont(F_INPUT);
        txtSoDiem.setPreferredSize(new Dimension(90,30));
        txtSoDiem.setHorizontalAlignment(SwingConstants.CENTER);

        btnCapNhat = makeBtn("Cập Nhật Điểm", C_BTN_UPDATE, 180);
        btnCapNhat.setEnabled(false);
        btnCapNhat.addActionListener(e -> capNhatDiem());

        card.add(new JLabel("Điểm hiện tại:") {{ setFont(F_LABEL); }});
        card.add(lblDiemHienTai);
        card.add(new JLabel("Loại:") {{ setFont(F_LABEL); }});
        card.add(lblLoaiHienTai);
        card.add(new JLabel("Cộng thêm:") {{ setFont(F_LABEL); }});
        card.add(txtSoDiem);
        card.add(btnCapNhat);

        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    private void doSearch() { search(txtKeyword.getText().trim(), cbCriteria.getSelectedItem().toString()); }

    private void search(String kw, String cr) {
        model.setRowCount(0); selectedMaKH = null; resetDiemPanel();
        try (Connection conn = ConnectDB.getConnection()) {
            if (conn==null) return;
            PreparedStatement ps;
            String base = "SELECT maKH,hoTen,sdt,diaChi,email,diemTichLuy,loaiKhach FROM KhachHang ";
            if (cr.equals("Tất cả") || kw.isEmpty()) {
                ps = conn.prepareStatement(base + "ORDER BY maKH");
            } else {
                String col = cr.equals("Mã KH") ? "maKH" : (cr.equals("Họ Tên") ? "hoTen" : (cr.equals("Số ĐT") ? "sdt" : "loaiKhach"));
                ps = conn.prepareStatement(base + "WHERE "+col+" LIKE ? ORDER BY maKH");
                ps.setString(1, "%"+kw+"%");
            }
            ResultSet rs = ps.executeQuery();
            int count=0;
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("maKH"), rs.getString("hoTen"), rs.getString("sdt"),
                    rs.getString("diaChi"), rs.getString("email"),
                    rs.getInt("diemTichLuy"), rs.getString("loaiKhach")
                });
                count++;
            }
            lblCount.setText("Tổng: "+count+" khách hàng");
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    private void onRowSelect() {
        int row = table.getSelectedRow();
        if (row<0) { resetDiemPanel(); return; }
        selectedMaKH  = str(model.getValueAt(row,0));
        selectedDiem  = toInt(model.getValueAt(row,5));
        String loai   = str(model.getValueAt(row,6));
        lblDiemHienTai.setText(selectedDiem+" điểm");
        lblLoaiHienTai.setText(loai);
        lblLoaiHienTai.setForeground(KhachHangHelper.mauLoai(loai));
        btnCapNhat.setEnabled(true);
    }

    private void resetDiemPanel() {
        lblDiemHienTai.setText("—"); lblLoaiHienTai.setText("—");
        txtSoDiem.setText("0"); btnCapNhat.setEnabled(false);
    }

    private void capNhatDiem() {
        if (selectedMaKH == null) return;
        int them;
        try { them = Integer.parseInt(txtSoDiem.getText().trim()); }
        catch(Exception e) { JOptionPane.showMessageDialog(this, "Điểm phải là số!"); return; }
        
        int diemMoi = selectedDiem + them;
        String loaiMoi = KhachHangHelper.xepLoai(diemMoi);
        
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE KhachHang SET diemTichLuy=?, loaiKhach=? WHERE maKH=?");
            ps.setInt(1, diemMoi); ps.setString(2, loaiMoi); ps.setString(3, selectedMaKH);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                doSearch();
                if (panelKhachHang != null) panelKhachHang.loadData();
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    private JButton makeBtn(String txt, Color bg, int w) {
        JButton btn = new JButton(txt);
        btn.setFont(F_BTN); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(w,32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    private String str(Object o) { return o==null?"":o.toString(); }
    private int toInt(Object o) { try{return Integer.parseInt(o.toString());}catch(Exception e){return 0;} }
}
