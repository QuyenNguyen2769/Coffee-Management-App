package sp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class filedocghi {
	public static void writetoFile(DanhSachSanPham dssp, String file) throws Exception {
		ObjectOutputStream out = null;
		out = new ObjectOutputStream(new FileOutputStream(file));
	    out.writeObject(dssp);
	    out.close();
	}
	public Object readFromFile(String file) throws Exception{
		ObjectInputStream ois  = new ObjectInputStream(new FileInputStream(file));
		Object list = ois.readObject();
		ois.close();
		return list;
	}
}
  