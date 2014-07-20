package meow.dogs.work;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ice.BaseMessage;
import ice.forget;
import ice.login;

public class Dlgshop extends Activity implements OnClickListener
{
    private Activity activity;
    private Dialog dialoggen;
    private Button dbutton;
    private Button dbutcncl;
    private EditText det;
    private double dx;
    private double dy;
    public Dlgshop(Activity activity,double x,double y)
    {
        this.activity=activity;
        dx=x;
        dy=y;
        init();
    }
    private void init()
    {
        dialoggen=new Dialog(activity);
        dialoggen.setContentView(R.layout.dialog);
        dialoggen.setCancelable(false);
        ((ListView)dialoggen.findViewById(R.id.listdlg)).setVisibility(View.GONE);
        det=(EditText)dialoggen.findViewById(R.id.dlget);
        det.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
        det.setHint("Название");
        dbutton=(Button)dialoggen.findViewById(R.id.dlgok);
        dbutcncl=(Button)dialoggen.findViewById(R.id.dlgcncl);
        dbutton.setOnClickListener(this);
        dbutcncl.setOnClickListener(this);
        dbutton.setText("Отправить");
        dbutcncl.setText("Отмена");
    }
    public void show()
    {
        dialoggen.setTitle("Введите название магазина" +
                " в котором вы работаете");
        dialoggen.show();
    }
    private void buton_Cancel()
    {
        dialoggen.cancel();
    }
    private void buton_Ok()
    {
        String str = Scktmy.sendobject((BaseMessage) MainActivity.DFR,activity);
        if(str==null)
            return;
        if(str.equals("recordok"))
        {
            Toast.makeText(activity, "Отправка прошла успешно", Toast.LENGTH_SHORT).show();
            if(Arbeiten.ssarb==Arbeiten.SessionStatus.open)
            {
                Arbeiten.closesession();
                Arbeiten.ssarb= Arbeiten.SessionStatus.close;
            }
            if(Arbeiten.ssarb==Arbeiten.SessionStatus.start)
            {
                Arbeiten.opensession();
                Arbeiten.ssarb= Arbeiten.SessionStatus.open;
            }
            finish();
            return;
        }
        else
        {
            Toast.makeText(activity, "Что-то пошло не так\nПопробуйте ещё раз", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.dlgok:
                buton_Ok();
                break;
            case R.id.dlgcncl:
                buton_Cancel();
        }
    }
}
