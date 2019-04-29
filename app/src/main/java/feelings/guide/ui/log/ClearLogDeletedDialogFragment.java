package feelings.guide.ui.log;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import feelings.guide.R;


public class ClearLogDeletedDialogFragment extends DialogFragment {

    interface ClearLogDeletedDialogListener {
        void onClearLogDeletedConfirmed();
    }

    private ClearLogDeletedDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            throw new RuntimeException("Host Activity is null");
        }
        return new AlertDialog.Builder(activity)
                .setMessage(R.string.title_confirm_clear_log_deleted_dialog)
                .setPositiveButton(R.string.btn_delete, (dialog, id) -> listener.onClearLogDeletedConfirmed())
                .setNegativeButton(R.string.btn_cancel, (dialog, id) -> dismiss())
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof ClearLogDeletedDialogListener)) {
            throw new RuntimeException(context.getClass() + " must implement "
                    + ClearLogDeletedDialogListener.class);
        }
        listener = (ClearLogDeletedDialogListener) context;
    }
}
