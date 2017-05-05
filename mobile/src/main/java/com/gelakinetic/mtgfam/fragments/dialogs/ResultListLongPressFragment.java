package com.gelakinetic.mtgfam.fragments.dialogs;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gelakinetic.mtgfam.R;
import com.gelakinetic.mtgfam.helpers.WishlistHelpers;
import com.gelakinetic.mtgfam.helpers.database.CardDbAdapter;
import com.gelakinetic.mtgfam.helpers.database.DatabaseManager;
import com.gelakinetic.mtgfam.helpers.database.FamiliarDbException;

import org.jetbrains.annotations.NotNull;

/**
 * Class that creates dialogs for ResultListLongPressFragment
 */
public class ResultListLongPressFragment extends FamiliarDialogFragment {

    private static final int DIALOG_SET_WARNINGS = 1;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

                /* This will be set to false if we are returning a null dialog. It prevents a crash */
        setShowsDialog(true);

        switch (DIALOG_SET_WARNINGS) {
            case DIALOG_SET_WARNINGS: {
                Bundle args = getArguments();
                final long id = args.getLong("starting_card_id");

                final View v = View.inflate(this.getActivity(), R.layout.result_list_longpress_dialog, null);
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(v);

                Button addWishlist = (Button) v.findViewById(R.id.add_wishlist);
                addWishlist.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View view) {
                        // Take into account consolidate variable!
                        // If no consolidation, directly add card. Else, show menu.
                        // boolean consolidate = getFamiliarActivity().mPreferenceAdapter.getConsolidateSearch();
                        SQLiteDatabase database = DatabaseManager.getInstance(getActivity(), false).openDatabase(false);
                        Cursor cCardById;
                        try {
                            cCardById = CardDbAdapter.fetchCards(new long[]{id}, null, database);
                        } catch (FamiliarDbException e) {
                            //handleFamiliarDbException(true);
                            DatabaseManager.getInstance(getActivity(), false).closeDatabase(false);
                            return;
                        }

                        String cardName = cCardById.getString(cCardById.getColumnIndex(CardDbAdapter.KEY_NAME));
                        Dialog dialog = WishlistHelpers.getDialog(cardName, getFamiliarFragment(), false);

                        if (dialog == null) {
                            //getCardViewFragment().handleFamiliarDbException(false);
                            //return DontShowDialog();
                        }
                        else {
                            getFamiliarFragment().removeDialog(getFragmentManager());
                            dialog.show();
                        }
                        //return dialog;
                    }
                });

                return dialog;
            }
            default: {
                return DontShowDialog();
            }
        }
    }
}