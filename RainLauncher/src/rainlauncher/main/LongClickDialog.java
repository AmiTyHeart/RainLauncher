package rainlauncher.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LongClickDialog extends Dialog{

	Context context;
    ListView list;
    String pkg, cls;
    public LongClickDialog(Context context, String pkg, String cls) {
        super(context);
        this.context = context;
        this.pkg = pkg;
        this.cls = cls;
    }
    public LongClickDialog(Context context, int theme, String pkg, String cls){
        super(context, theme);
        this.context = context;
        this.pkg = pkg;
        this.cls = cls;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.longclick);

        list = (ListView)findViewById(R.id.long_list);
        
        String[] lists = {"Òþ²Ø£º" + cls ,"É¾³ý£º" + cls };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
        		android.R.layout.simple_list_item_1, lists);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch(arg2){
				case 0:
					dismiss();
					
					break;
				case 1:
					dismiss();
					Uri packageURI = Uri.parse("package:" + pkg);           
					Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);           
					context.startActivity(uninstallIntent); 
					
					break;
					default:
				}
			}
        });
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_MENU){
    		dismiss();
    		return false;
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
