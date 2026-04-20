package sp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class frmSanPham extends JFrame implements ActionListener, MouseListener {
    private DanhSachSanPham dssp;
    private List<SanPham> list;
    private DefaultTableModel model;
    private JLabel lbn, lbmaSP, lbTenSP, lbsoluong, lbdongia, lbLoai;
    private JTextField txmaSP, txtenSP, txSoLuong, txDongia, txtim;
    private JButton btntim, btnthem, btnxoa, btnluu, btnxoatrang;
    private JRadioButton radMoi, radCu;
    private ButtonGroup groupLoai;
    private JTable table;
    filedocghi fi;
    private static final String tenfile = "data/abcd";

    public frmSanPham(DanhSachSanPham dao) {
        this.dssp = dao;
        list = dao.getList();

        JPanel pn = new JPanel();
        pn.add(lbn = new JLabel("DANH SÁCH SẢN PHẨM"));
        lbn.setFont(new Font("Arial", Font.BOLD, 26));
        lbn.setForeground(Color.blue);
        add(pn, BorderLayout.NORTH);

        Box b = Box.createVerticalBox();
        Box b1, b2, b3, b4, b5;

        b.add(b1 = Box.createHorizontalBox());
        b.add(Box.createVerticalStrut(10));
        b1.add(lbmaSP = new JLabel("Ma San Pham"));
        b1.add(Box.createHorizontalStrut(0));
        b1.add(txmaSP = new JTextField(15));

        b.add(b2 = Box.createHorizontalBox());
        b.add(Box.createVerticalStrut(10));
        b2.add(lbTenSP = new JLabel("Ten SP:    "));
        b2.add(txtenSP = new JTextField(15));
        b2.add(lbLoai = new JLabel("Loai SP:   "));
        radMoi = new JRadioButton("moi");
        radCu  = new JRadioButton("cu");

        groupLoai = new ButtonGroup();
        groupLoai.add(radMoi);
        groupLoai.add(radCu);

        b2.add(radMoi);
        b2.add(radCu);

        b.add(b3 = Box.createHorizontalBox());
        b.add(Box.createVerticalStrut(10));
        b3.add(lbsoluong = new JLabel("So Luong  "));
        b3.add(txSoLuong = new JTextField(15));  

        b.add(b4 = Box.createHorizontalBox());
        b.add(Box.createVerticalStrut(10)); 
        b4.add(lbdongia = new JLabel("Don Gia:   "));
        b4.add(txDongia = new JTextField(15));

      

        lbmaSP.setPreferredSize(lbmaSP.getPreferredSize());
        lbTenSP.setPreferredSize(lbmaSP.getPreferredSize());
        lbsoluong.setPreferredSize(lbmaSP.getPreferredSize());
        lbdongia.setPreferredSize(lbmaSP.getPreferredSize());
        lbLoai.setPreferredSize(lbmaSP.getPreferredSize());

        b.add(b5 = Box.createHorizontalBox());
        b.add(Box.createVerticalStrut(10));
        		
        String[] columns = "MaSP;TenSP;SoLuong;DonGia;Loai".split(";");
        model = new DefaultTableModel(columns, 0);  
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(table = new JTable(model));
        table.setRowHeight(25);

        JComboBox<String> cboLoaiBang = new JComboBox<>(new String[]{"moi", "cu"});
        table.getColumn("Loai").setCellEditor(new DefaultCellEditor(cboLoaiBang));

        b5.add(scroll);
        add(b, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(split, BorderLayout.SOUTH);
        JPanel pleft, pright;
        split.add(pleft = new JPanel());
        split.add(pright = new JPanel());

        pleft.add(new JLabel("Nhap ma san pham can tim"));
        pleft.add(txtim = new JTextField(5));
        pleft.add(btntim = new JButton("Tim"));

        pright.add(btnthem     = new JButton("Them"));
        pright.add(btnxoa      = new JButton("Xoa"));
        pright.add(btnxoatrang = new JButton("Xoa trang"));
        pright.add(btnluu      = new JButton("Luu"));

        btntim.addActionListener(this);
        btnthem.addActionListener(this);
        btnxoatrang.addActionListener(this);
        btnxoa.addActionListener(this);
        btnluu.addActionListener(this);
        table.addMouseListener(this);

        setTitle("Quan ly san pham");
        setSize(650, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        loadDuLieu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if      (o == btnthem)      them();
        else if (o == btntim)       timkiem();
        else if (o == btnxoa)       xoa();
        else if (o == btnxoatrang)  xoatrang();
        else if (o == btnluu) {
            try {
                new java.io.File("data").mkdir();
                filedocghi.writetoFile(dssp, tenfile);
                JOptionPane.showMessageDialog(this, "Da luu file thanh cong");
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Luu file that bai");
            }
        }
    }

    private void xoatrang() {
        txmaSP.setText("");
        txtenSP.setText("");
        txSoLuong.setText("");
        txDongia.setText("");
        txtim.setText("");
        radMoi.setSelected(true);
        txmaSP.requestFocus();
    }

    private void them() {
        try {
            int maSP      = Integer.parseInt(txmaSP.getText().trim());
            String tenSP  = txtenSP.getText().trim();
            int soLuong   = Integer.parseInt(txSoLuong.getText().trim());
            double donGia = Double.parseDouble(txDongia.getText().trim());

            if (tenSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ten san pham khong duoc trong");
                return;
            }

            if (!radMoi.isSelected() && !radCu.isSelected()) {
                JOptionPane.showMessageDialog(this, "Vui long chon loai san pham");
                return;
            }
            String loai = radMoi.isSelected() ? "moi" : "cu";

            SanPham sp = new SanPham(maSP, tenSP, soLuong, donGia,loai);

            if (dssp.themSP(sp)) {
                model.addRow(new Object[]{maSP + "", tenSP, soLuong + "", donGia + "", loai});
                xoatrang();
            } else {
                JOptionPane.showMessageDialog(this, "Trung maSP");
                txmaSP.selectAll();
                txmaSP.requestFocus();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loi nhap du lieu");
        }
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui long chon san pham can xoa");
            return;
        }
        int maSp = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
        int notice = JOptionPane.showConfirmDialog(
            this, "Co chac chan xoa khong?", "Chu y", JOptionPane.YES_NO_OPTION
        );
        if (notice == JOptionPane.YES_OPTION) {
            if (dssp.xoaSP(maSp)) {
                model.removeRow(row);
                xoatrang();
            }
        }
    }

    private void timkiem() {
        try {
            int maSP   = Integer.parseInt(txtim.getText().trim());
            SanPham sp = dssp.timSP(maSP);

            if (sp != null) {
                txmaSP.setText(sp.getMaSP() + "");
                txtenSP.setText(sp.getTenSp());
                txSoLuong.setText(sp.getSoLuong() + "");
                txDongia.setText(sp.getDonGia() + "");

                for (int i = 0; i < table.getRowCount(); i++) {
                    if (Integer.parseInt(table.getValueAt(i, 0).toString()) == maSP) {
                        table.setRowSelectionInterval(i, i);
                        table.scrollRectToVisible(table.getCellRect(i, 0, true));

                        String loai = table.getValueAt(i, 4).toString();
                        if (loai.equals("moi")) radMoi.setSelected(true);
                        else                             radCu.setSelected(true);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Khong tim thay san pham");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Nhap ma san pham khong hop le");
        }
    }

    private void loadDuLieu() {
        model.setRowCount(0);
        for (SanPham sp : list) {
            model.addRow(new Object[]{
                sp.getMaSP() + "",
                sp.getTenSp(),
                sp.getSoLuong() + "",
                sp.getDonGia() + "",
                sp.getLoai()
            });
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txmaSP.setText(table.getValueAt(row, 0).toString());
        txtenSP.setText(table.getValueAt(row, 1).toString());
        txSoLuong.setText(table.getValueAt(row, 2).toString());
        txDongia.setText(table.getValueAt(row, 3).toString());

        String loai = table.getValueAt(row, 4).toString();
        if (loai.equals("moi")) radMoi.setSelected(true);
        else                             radCu.setSelected(true);
    }

    @Override public void mousePressed(MouseEvent e)  {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e)  {}
    @Override public void mouseExited(MouseEvent e)   {}
}