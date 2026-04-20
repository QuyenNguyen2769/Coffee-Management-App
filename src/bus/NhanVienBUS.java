package bus;

import dao.NhanVienDAO;
import entity.NhanVien;
import java.util.ArrayList;

public class NhanVienBUS {

    NhanVienDAO dao = new NhanVienDAO();

    public ArrayList<NhanVien> getAllNhanVien() {
        return dao.getAllNhanVien();
    }
}