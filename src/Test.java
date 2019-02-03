
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LineShape.setAreaBound(0, 0, 100, 100);
		int[] ret=LineShape.updatePoints(50, 50,-1,50);
		for(int i:ret)System.out.println(i);
	}

}
