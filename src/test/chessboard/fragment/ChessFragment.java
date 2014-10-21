package test.chessboard.fragment;

import java.util.ArrayList;

import test.chessboard.R;
import test.chessboard.model.Pawn;
import test.chessboard.util.DataLoaderFunction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ChessFragment extends Fragment {

	private final String URL = "http://202.67.10.2:8891/bl/chess.php";
	private DataLoaderTask loaderTask;
	private RelativeLayout parentLayout;
	private ImageView ivWKing;
	private ImageView ivWQueen;
	private ImageView ivWBishop;
	private ImageView ivWKnight;
	private ImageView ivWRook;
	private ImageView ivBKing;
	private ImageView ivBQueen;
	private ImageView ivBBishop;
	private ImageView ivBKnight;
	private ImageView ivBRook;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		parentLayout = (RelativeLayout) rootView
				.findViewById(R.id.parentLayout);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loaderTask = new DataLoaderTask();
		loaderTask.execute(URL);
		
		//start runnable
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(loaderTask != null)
					loaderTask.cancel(true);
				
				loaderTask = new DataLoaderTask();
				loaderTask.execute(URL);
				
				handler.postDelayed(this, 3000);
			}
		};
		handler.postDelayed(runnable, 3000);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if (loaderTask != null)
			loaderTask.cancel(true);
	}

	private void locatePawn(Pawn p) {
		switch (p.getName()) {
		case "K":
			ivWKing = putPawn(ivWKing, p.getImageResouce(), p.getRow(), p.getCol());
			break;

		case "Q":
			ivWQueen = putPawn(ivWQueen, p.getImageResouce(), p.getRow(), p.getCol());
			break;

		case "N":
			ivWKnight = putPawn(ivWKnight, p.getImageResouce(), p.getRow(), p.getCol());
			break;

		case "B":
			ivWBishop = putPawn(ivWBishop, p.getImageResouce(), p.getRow(), p.getCol());
			break;
		case "R":
			ivWRook = putPawn(ivWRook, p.getImageResouce(), p.getRow(), p.getCol());
			break;

		case "k":
			ivBKing = putPawn(ivBKing, p.getImageResouce(), p.getRow(), p.getCol());
			break;

		case "q":
			ivBQueen = putPawn(ivBQueen, p.getImageResouce(), p.getRow(), p.getCol());
			break;

		case "n":
			ivBKnight = putPawn(ivBKnight, p.getImageResouce(), p.getRow(), p.getCol());
			break;

		case "b":
			ivBBishop = putPawn(ivBBishop, p.getImageResouce(), p.getRow(), p.getCol());
			break;
		
		case "r":
			ivBRook = putPawn(ivBRook, p.getImageResouce(), p.getRow(), p.getCol());
			break;
		}

	};

	private ImageView putPawn(ImageView iv, int imgSrc, int row, int col){
		ImageView tempIv = null;
		RelativeLayout.LayoutParams params = null;
		int mgTop = dpToPx(43*(8 - row));
		int mgLeft = dpToPx(41*(col-1));
		int width = dpToPx(35);
		int height = dpToPx(35);
		
		if(iv == null) {
			tempIv = new ImageView(getActivity());
			tempIv.setImageResource(imgSrc);
			params = new LayoutParams(width, height);
			
		} else {
			parentLayout.removeView(iv);
			params = (LayoutParams) iv.getLayoutParams();
		}
		
		params.setMargins(mgLeft, mgTop, 0, 0);
		
		if(iv == null) {
			tempIv.setLayoutParams(params);
			iv = tempIv;
		} else {
			iv.setLayoutParams(params);
		}
		
		parentLayout.addView(iv);
		
		return iv;
	}
	
	private int dpToPx(int dp){
		float density = getResources().getDisplayMetrics().density;
		
		return (int) Math.ceil(dp*density);
	}

	private class DataLoaderTask extends
			AsyncTask<String, Void, ArrayList<Pawn>> {

		@Override
		protected ArrayList<Pawn> doInBackground(String... params) {
			if (isCancelled())
				return null;

			return DataLoaderFunction.getDataFromUrl(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<Pawn> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Log.d("DATA", "RESULT " + result);

			for (int i = 0; i < result.size(); i++) {
				Pawn p = result.get(i);
				locatePawn(p);
			}
		}
	}
}
