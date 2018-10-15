package com.example.a20134833.studentattend;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddclassDialog extends Dialog implements View.OnClickListener {

    profAddclass profAddclass = new profAddclass();
    BeaconTransmitter beaconTransmitter = new BeaconTransmitter();

    TextView addclassdialogid;
    EditText addclassnum, addclassnum2, addclassname;
    TextView addclassDialogCanel, addclassDialogOK;

    public AddclassDialog(@NonNull Context context) {
        super(context);
    }

    public AddclassDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AddclassDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass_dialog);

        addclassnum = findViewById(R.id.addclassnum);
        addclassnum2 = findViewById(R.id.addclassnum2);
        addclassname = findViewById(R.id.addclassname);
        addclassdialogid = findViewById(R.id.addclassdialogid);

        addclassdialogid.setText(BeaconTransmitter.profidS);

        addclassDialogOK = findViewById(R.id.addclassDialogOK);
        addclassDialogCanel = findViewById(R.id.addclassDialogCancel);

        addclassDialogOK.setOnClickListener(AddclassDialog.this);
        addclassDialogCanel.setOnClickListener(AddclassDialog.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.addclassDialogOK:
                profAddclass.profAddclass_Asycn(addclassdialogid.getText().toString(), addclassnum.getText().toString(), addclassnum2.getText().toString(), addclassname.getText().toString());
                cancel();
                //Log.e("profaddclass --", addclassdialogid.getText().toString()+"/"+ addclassnum.getText().toString()+"/"+ addclassnum2.getText().toString()+"/"+ addclassname.getText().toString());
                break;
            case R.id.addclassDialogCancel:
                cancel();
                break;
        }
    }
}
