package it.polito.tdp.crimes.model;

public class Vicino  implements Comparable<Vicino>{
	District d;
	double distanza;
	public Vicino(District d, double distanza) {
		super();
		this.d = d;
		this.distanza = distanza;
	}
	public District getD() {
		return d;
	}
	public double getDistanza() {
		return distanza;
	}
	@Override
	public int compareTo(Vicino o) {
		// TODO Auto-generated method stub
		return (int)(this.distanza-o.distanza);
	}
	@Override
	public String toString() {
		return d.getDistrictId() +" ( "+this.distanza+")";
	}
	
	
}
