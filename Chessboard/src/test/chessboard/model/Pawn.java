package test.chessboard.model;

import test.chessboard.R;

public class Pawn {
	private String name;
	private int row;
	private String col;
	
	public Pawn (String n, String r, String c) {
		this.name = n;
		this.col = c;
		this.row = Integer.valueOf(r).intValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		
		int res = 0;
		switch (this.col) {
		case "a":
			res = 1;
			break;
		
		case "b":
			res = 2;
			break;
			
		case "c":
			res = 3;
			break;
		
		case "d":
			res = 4;
			break;
		
		case "e":
			res = 5;
			break;
			
		case "f":
			res = 6;
			break;
		
		case "g":
			res = 7;
			break;
		
		case "h":
			res = 8;
			break;
		}
		
		return res;
	}

	public void setCol(String col) {
		this.col = col;
	}
	
	public int getImageResouce(){
		int res = 0;
		
		switch (this.name) {
		case "K":
			res = R.drawable.k;
			break;
		
		case "Q":
			res = R.drawable.q;
			break;
			
		case "N":
			res = R.drawable.n;
			break;
		
		case "B":
			res = R.drawable.b;
			break;
		
		case "k":
			res = R.drawable.bk;
			break;
			
		case "q":
			res = R.drawable.bq;
			break;
		
		case "n":
			res = R.drawable.bn;
			break;
		
		case "b":
			res = R.drawable.bb;
			break;
		}
		
		return res;
	}

}
