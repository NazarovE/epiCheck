package com.example.appfond;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class EventDialogFragment extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_event_dialog, container, false);

        // Предположим, у вас есть кнопка закрытия в вашем фрагменте
        Button closeButton = view.findViewById(R.id.buttonEventOk);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Закрытие диалога
                dismiss();
                //onDestroy();
                //dismissAllowingStateLoss();

            }
        });

        return view;
    }

    /*@Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        // Попробуем вручную обновить UI активности
        Activity activity = getActivity();
        if (activity != null) {
            activity.recreate();
        }

    }*/

}
