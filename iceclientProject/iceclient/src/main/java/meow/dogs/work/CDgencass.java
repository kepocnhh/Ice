package meow.dogs.work;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import ice.DataCass;

public class CDgencass extends Activity implements OnClickListener
{
    private Activity activity;
    private Dialog dialoggen;
    private Button[] btns;
    static DataCass.TypeEvent DC;//для кассы
    public CDgencass(Activity activity)
    {
        this.activity=activity;
        dialoggen=new Dialog(activity);
        dialoggen.setContentView(R.layout.dlggencass);
        dialoggen.setTitle("Взять деньги из кассы");
        btns=new Button[4];
        btns[0] = (Button) dialoggen.findViewById(R.id.bink);
        btns[1] = (Button) dialoggen.findViewById(R.id.bpro);
        btns[2] = (Button) dialoggen.findViewById(R.id.bcas);
        btns[3] = (Button) dialoggen.findViewById(R.id.bgencncl);
        for(int i=0;i<btns.length-1;i++)
        {
            btns[i].setText(MainActivity.mainstrings.DataCass.get(i));
            btns[i].setOnClickListener(this);
        }
        btns[3].setOnClickListener(this);
        dialoggen.show();
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.bink:
                DC=DataCass.TypeEvent.inkasator;
                break;
            case R.id.bpro:
                DC=DataCass.TypeEvent.promoter;
                break;
            case R.id.bcas:
                DC=DataCass.TypeEvent.cass;
                break;
            case R.id.bgencncl:
                dialoggen.cancel();
                return;
        }
        dialoggen.cancel();
        new CDcass(this.activity);
    }
}
