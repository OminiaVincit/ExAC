package vcfstream;
import java.io.Serializable;

public class ChrObj implements Serializable {
	String chr;
	String pos;
	public ChrObj( String chr, String pos ) {
		this.chr = chr;
		this.pos = pos;
	}
	
	public String getChr() {
		return chr;
	}
	
	public String getPos() {
		return pos;
	}
	
	public void setChr( String chr ) {
		this.chr = chr;
	}
	
	public void setPos( String pos ) {
		this.pos = pos;
	}
	
	public String toString() {
		return this.chr + "\t" + this.pos;
	}
}
