package meow.dogs.work;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IPnPORT extends Activity implements OnClickListener
{
    private EditText e1;
    private EditText e2;
    private EditText e3;
    private EditText e4;
    private EditText eport;
    private Dialog dialog;
    private Activity activity;
    public IPnPORT(Activity activity)
    {
        this.activity=activity;
        dialog=new Dialog(activity);
        dialog.setContentView(R.layout.ipnport);
        e1=(EditText)dialog.findViewById(R.id.editText1);
        e2=(EditText)dialog.findViewById(R.id.editText2);
        e3=(EditText)dialog.findViewById(R.id.editText3);
        e4=(EditText)dialog.findViewById(R.id.editText4);
        eport=(EditText)dialog.findViewById(R.id.editText5);
        ((Button)dialog.findViewById(R.id.dialog_button)).setOnClickListener(this);
        e1.setText(MainActivity.IPadr[0]);
        e2.setText(MainActivity.IPadr[1]);
        e3.setText(MainActivity.IPadr[2]);
        e4.setText(MainActivity.IPadr[3]);
        eport.setText(MainActivity.Portgen);
        dialog.setTitle("Введите IP и PORT сервера");
        dialog.show();
    }
    @Override
    public void onClick(View arg0)
    {
        if(e1.getText().toString().length()==0||
                e2.getText().toString().length()==0||
                e3.getText().toString().length()==0||
                e4.getText().toString().length()==0||
                eport.getText().toString().length()<4)
        {
            Toast.makeText(activity,"Попробуйте ещё раз", Toast.LENGTH_SHORT).show();
        }
        else
        {
            MainActivity.
                    IPadr=(
                    e1.getText().toString()+"\t"+
                    e2.getText().toString()+"\t"+
                    e3.getText().toString()+"\t"+
                    e4.getText().toString()).split("\t");
            MainActivity.Portgen=eport.getText().toString();
            Toast.makeText(activity,"IP - "+MainActivity.getIPstr()+"\nPORT - "+
                    MainActivity.Portgen,
                    Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }
    }
}
