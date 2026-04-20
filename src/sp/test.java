package sp;

public class test {

	public static void main(String[] args) {
		DanhSachSanPham dao = new DanhSachSanPham();
		 try {
		        filedocghi fi = new filedocghi();
		        dao = (DanhSachSanPham)fi.readFromFile("data/abcd");
		    } catch (Exception e) {
		        System.out.println("Khong co file, tao moi...");
		    }
		new frmSanPham(dao);
	}

}
 