package com.flipbox.muhammadiyahthisweek.fragment;

import com.flipbox.muhammadiyahthisweek.database.Database;
import com.flipbox.muhammadiyahthisweek.model.Event;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class ConfirmationDialog extends DialogFragment {

	public final static String DIALOG_TYPE = "dialog type";
	public final static String DELETE_FROM_LIST_TAG = "confirm dialog delete from list";
	public final static String DELETE_FROM_DETAIL_TAG = "confirm dialog delete from detail";
	public final static String EVENT_KEY = "event key";
	public final static String DATABASE_KEY = "database key";
	public final static int DELETE_FROM_LIST = 0;
	public final static int DELETE_FROM_DETAIL = 1;

	private final String successDeleteEventMsg = " successfully deleted";

	public ConfirmationDialog() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Bundle b = getArguments();
		int dialogType = b.getInt(DIALOG_TYPE);

		switch (dialogType) {
		case DELETE_FROM_LIST:
			builder = buildDeleteFromListDialog(builder, b);
			break;
		case DELETE_FROM_DETAIL:
			builder = buildDeleteFromDetailDialog(builder, b);
			break;
		}

		return builder.create();
	}

	private AlertDialog.Builder buildDeleteFromListDialog(
			AlertDialog.Builder builder, Bundle b) {

		final Event e = (Event) b.getSerializable(EVENT_KEY);
		final Database db = (Database) b.getSerializable(DATABASE_KEY);

		builder.setTitle("Delete Event")
				.setMessage("Are you sure want to delete " + e.getName() + " ?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								db.removeEvent(e.getId());
								DetailEventFragment.stopAlarm(getActivity(),
										e.getName());
								updateSavedEventList();
								Toast.makeText(getActivity(),
										e.getName() + successDeleteEventMsg,
										Toast.LENGTH_SHORT).show();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		return builder;
	}

	private AlertDialog.Builder buildDeleteFromDetailDialog(
			AlertDialog.Builder builder, Bundle b) {
		final Event e = (Event) b.getSerializable(EVENT_KEY);
		final Database db = (Database) b.getSerializable(DATABASE_KEY);

		builder.setTitle("Delete Event")
				.setMessage("Are you sure want to delete " + e.getName() + " ?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								db.removeEvent(e.getId());
								DetailEventFragment.stopAlarm(getActivity(),
										e.getName());
								getFragmentManager().beginTransaction()
										.remove(getTargetFragment()).commit();
								getActivity().finish();
								Toast.makeText(getActivity(),
										e.getName() + successDeleteEventMsg,
										Toast.LENGTH_SHORT).show();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return builder;
	}

	private void updateSavedEventList() {
		SavedEventFragment sea = (SavedEventFragment) getTargetFragment();
		
		sea.onResume();
	}
}
