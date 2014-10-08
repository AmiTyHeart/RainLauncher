package rainlauncher.main;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private List<ResolveInfo> mApps;
	GridView grid;
	final int MENU_FIRST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadApps();
        setContentView(R.layout.activity_main);
        grid = (GridView) findViewById(R.id.apps_list);   
        grid.setOnItemClickListener(listener);
        grid.setAdapter(new AppsAdapter()); 
    }
    
    private void loadApps() {           
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);           
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);             
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);       
    }  

    private OnItemClickListener listener = new OnItemClickListener() {
    	@Override public void onItemClick(AdapterView<?> parent,
    			View view, int position,long id) {
    		ResolveInfo info = mApps.get(position);                          
    	    //该应用的包名            
    		String pkg = info.activityInfo.packageName;              
    	    //应用的主activity类            
    		String cls = info.activityInfo.name;  
    		Toast.makeText(getApplicationContext(), info + "\n" + cls,
    				Toast.LENGTH_SHORT).show();
    	    ComponentName componet = new ComponentName(pkg, cls);                          
    	    Intent i = new Intent();              
    	    i.setComponent(componet);  
    	    i.setAction("android.intent.action.VIEW");
    	    startActivity(i); 
    	}
    	
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
    	menu.add(Menu.NONE, MENU_FIRST, 1, "设置壁纸")
    		.setIcon(android.R.drawable.btn_star);
    	menu.add(Menu.NONE, MENU_FIRST + 1, 2, "设置")
			.setIcon(android.R.drawable.ic_menu_set_as);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
        case MENU_FIRST:
        	onSetWallpaper(grid);
            return true;
        case MENU_FIRST + 1:
        	ComponentName componet = new ComponentName("com.android.settings",
        			"com.android.settings.Settings");                          
        	Intent i = new Intent();              
        	i.setComponent(componet);  
        	startActivity(i); 
        	return true;
        } 
        return super.onOptionsItemSelected(item);
    }
    
    public void onSetWallpaper(View view) {                           
        //生成一个设置壁纸的请求                
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);                   
        Intent chooser = Intent.createChooser(pickWallpaper,"选择壁纸");                   
        //发送设置壁纸的请求                   
        startActivity(chooser);       
    }  
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, "哈哈，回不去了！", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
    
    public class AppsAdapter extends BaseAdapter {           
        public AppsAdapter() {         }               
        public View getView(int position, View convertView, ViewGroup parent) {               
            ImageView i;                   
            if (convertView == null) {                   
                i = new ImageView(MainActivity.this);                   
                i.setScaleType(ImageView.ScaleType.FIT_CENTER);                   
                i.setLayoutParams(new GridView.LayoutParams(50, 50));               
            } else {                   
                i = (ImageView) convertView;               
            }                   
            ResolveInfo info = mApps.get(position);              
            i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));                   
            return i;           
        }                   
        public final int getCount() {              
            return mApps.size();           
        }               
        public final Object getItem(int position) {               
            return mApps.get(position);           
        }               
        public final long getItemId(int position) {               
            return position;           
        }       
    }   
}
