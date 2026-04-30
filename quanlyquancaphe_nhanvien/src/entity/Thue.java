package entity;

public class Thue {
    private String maThue;
    private String tenThue;
    private Double thueSuat;

    public Thue(String maThue, String tenThue, Double thueSuat) {
        this.maThue   = maThue;
        this.tenThue  = tenThue;
        this.thueSuat = thueSuat;
    }

    public Thue(String maThue) {
        this(maThue, "", 0.0);
    }

	public String getMaThue() {
		return maThue;
	}

	public void setMaThue(String maThue) {
		this.maThue = maThue;
	}

	public String getTenThue() {
		return tenThue;
	}

	public void setTenThue(String tenThue) {
		this.tenThue = tenThue;
	}

	public Double getThueSuat() {
		return thueSuat;
	}

	public void setThueSuat(Double thueSuat) {
		this.thueSuat = thueSuat;
	}


}