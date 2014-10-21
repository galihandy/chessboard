package test.chessboard.util;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

import test.chessboard.model.Pawn;

public class DataLoaderFunction {
	
	public static ArrayList<Pawn> getDataFromUrl(String url){

		ArrayList<Pawn> pawnList = new ArrayList<Pawn>();
		
		try {
			Document doc = Jsoup.connect(url).get();
			Element body = doc.body();
			String content = body.text();
			
			Log.d("RETURN DATA", content);

			for (String c : content.split(" ")) {
				String[] cData = c.split(",");
				
				Pawn p = new Pawn(cData[0], cData[2], cData[1]);
				pawnList.add(p);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pawnList;
	}
}
