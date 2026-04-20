package sp;

import java.io.Serializable;
import java.util.ArrayList;

public class DanhSachSanPham implements Serializable{
	private ArrayList<SanPham> list;
	
	public DanhSachSanPham() {
		list = new ArrayList<>();
	}
	public Boolean themSP(SanPham sp) {
		if(list.contains(sp)) return false;
		list.add(sp);
		return true;
	}
	public Boolean xoaSP(int maSP) {
		SanPham sp = new SanPham(maSP);
		if(list.contains(sp)) {
			list.remove(sp);
			return true;
		}
		return false;
	}
	public Boolean suaSP(SanPham spmoi) {
		for(SanPham sp : list) {
			if(sp.getMaSP() == spmoi.getMaSP()) {
				 sp.setTenSp(spmoi.getTenSp());
		         sp.setSoLuong(spmoi.getSoLuong());
		         sp.setDonGia(spmoi.getDonGia());
		         return true;
			}
		}
		return false;
	}
	public SanPham timSP(int maSP) {
		for(SanPham sp: list) {
			if(sp.getMaSP() == maSP) {
				return sp;
			}
		}
		return null;
	}
	public ArrayList<SanPham> getList(){
		return list;
	}
}
