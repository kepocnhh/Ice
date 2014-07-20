package meow.dogs.work;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomDialog extends Activity implements OnClickListener
{
    private ArrayList<HashMap<String, Object>> myBooks=new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter adapter2;
    private static final String BOOKKEY = "bookname";
    private static final String PRICEKEY = "bookprice";
    private double matrix_cd[];
    private double matrix_cdsave[];
    private int len;
    private int number=-1;
    private int n_arb;
    private int n_arb_il;
    private String[] COLOR;
    private double[] dMass;
    private ListView lv;
    private EditText det;
    private Button dbutton;
    private Button dbutcncl;
    private Dialog dialoggen;
    private Activity activity;
    private String title=null;
    private double gnrl[]=null;
    private boolean flag=true;
    private String[] smgen=MainActivity.mainstrings.DataSubSale.toArray(new String[MainActivity.mainstrings.DataSubSale.size()]);
    private String[] sm;
    public CustomDialog(Activity activity,int n)
    {
        this.activity=activity;
        this.n_arb_il=n;
        this.n_arb=Arbeiten.matrix_link[n];
        this.sm=(smgen[this.n_arb]).split("\t");
        init();
    }
    private void init()
    {
        dialoggen=new Dialog(activity);
        dialoggen.setContentView(R.layout.dialog);
        dialoggen.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        dialoggen.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
        dialoggen.setCancelable(false);
        det=(EditText)dialoggen.findViewById(R.id.dlget);
        det.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (v == det)
                {
                    if (hasFocus)
                    {
                        ((InputMethodManager) activity.getApplicationContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE)).showSoftInput(det,
                                InputMethodManager.SHOW_FORCED);
                    }
                    else
                    {
                        ((InputMethodManager) activity.getApplicationContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                det.getWindowToken(), 0);
                    }
                }
            }
        });
        dbutton=(Button)dialoggen.findViewById(R.id.dlgok);
        dbutcncl=(Button)dialoggen.findViewById(R.id.dlgcncl);
        dbutton.setOnClickListener(this);
        dbutcncl.setOnClickListener(this);
        lv = (ListView)dialoggen.findViewById(R.id.listdlg);
        len=sm.length;
        dMass = new double[len];
        COLOR = new String[len];
        for(int i=0;i<len;i++)
            COLOR[i] ="#FFFFFF";
        adapter2 = new SimpleAdapter(this.activity,
                myBooks,
                R.layout.dlglistl, new String[]{
                BOOKKEY
                ,PRICEKEY
        }, new int[]{
                R.id.text1
                ,R.id.text2
        }){
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                LinearLayout ll = (LinearLayout) view.findViewById(R.id.lllist);
                TextView tv1 = (TextView) ll.getChildAt(0);
                TextView tv2 = (TextView) ll.getChildAt(1);
                tv1.setTextColor(Color.parseColor(COLOR[position]));
                return view;
            };
        };
        lv.setAdapter(adapter2);
    }
    public void show(double matrix[])
    {
        this.matrix_cd=new double[len];
        this.matrix_cdsave=matrix;
        dialoggen.setTitle("Заполните данные");
        dialoggen.show();
        String dstr;
        for(int i=0;i<len;i++)
        {
            if(matrix[i]<0)
            {
                dstr="ПУСТО";
            }
            else
            {
                dstr =""+matrix[i];
            }
            myBooks.add(addar(i, dstr));
            double numb=-1;
            try {
                numb=MainActivity.check_double(dstr);
            } catch (InterruptedException e) {
            }
            matrix_cd[i]=numb;
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View itemClicked, int position, long id) {
                LinearLayout ll = (LinearLayout) itemClicked.findViewById(R.id.lllist);
                TextView tv1 = (TextView) ll.getChildAt(0);
                TextView tv2 = (TextView) ll.getChildAt(1);
                lv_press(position);
            }
        });
        adapter2.notifyDataSetChanged();
    }
    private void buton_Cancel()
    {
        boolean flag=true;
        Arbeiten.flags_toopen[n_arb]=0;
        for(int i=0;i<len;i++)
        {
            if(matrix_cd[i]<0)
            {
                flag=false;
            }
        }
        String title="Сохранить изменения перед выходом?";
        if(!flag)
        {
            Arbeiten.flags_toopen[n_arb]=1;
            title+="\n[У вас остались незаполненные поля!]";
        }
        openNewGameDialog(title);
    }
    private void buton_Ok()
    {
        if(number<0)
        {
            Toast.makeText(this.activity,"Ничего не выбрано!", Toast.LENGTH_SHORT).show();
            return;
        }
        double numb=-1;
        try {
            numb=MainActivity.check_double(det.getText().toString());
        } catch (InterruptedException e) {
        }
        det.setText("");
        if(numb<0)
        {
            Toast.makeText(this.activity,"Данные введены неверно!", Toast.LENGTH_SHORT).show();
            return;
        }
            matrix_cd[number]=numb;
            myBooks.set(number,addar(number,""+numb));
            adapter2.notifyDataSetChanged();
        number++;
        if(number==len)
            number=0;
        lv_press(number);
    }
    public void lv_press(int position)
    {
        number=position;
        for(int i=0;i<len;i++)
        {
            if(i==position)
            {
                COLOR[i] ="#3399CC";
            }
            else
            {
                COLOR[i] ="#FFFFFF";
            }
        }
        dbutton.setText("Записать в [" + sm[position] + "]");
        det.clearFocus();
        det.requestFocus();
            if(matrix_cd[position]>=0)
                det.setText(""+matrix_cd[position]);
        det.setSelection(det.getText().toString().length());
        adapter2.notifyDataSetChanged();
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

    private HashMap<String, Object> addar(int i,String s2)
    {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put(BOOKKEY,sm[i]);
        hm.put(PRICEKEY,s2);
        return hm;
    }
    private void openNewGameDialog(String s1)
    {
        new AlertDialog.Builder(this.activity)
                .setTitle(s1)
                .setNegativeButton("ДА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<len;i++)
                        {
                            matrix_cdsave[i]=matrix_cd[i];
                        }
                        det.clearFocus();
                        dialoggen.cancel();
                        //OpenArb.set_flags(n_arb);
                        OpenArb.set_flags(n_arb_il);
                    }
                })
                .setNeutralButton("НЕТ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        det.clearFocus();
                        dialoggen.cancel();
                    }
                })
                .setPositiveButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
