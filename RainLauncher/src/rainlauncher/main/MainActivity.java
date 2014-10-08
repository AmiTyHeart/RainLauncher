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

    private OnItemClickListener listener = new OnItemClickListener() 
    {
    	@Override public void onItemClick(AdapterView<?> parent,
    			View view, int position,long id) {
    		ResolveInfo info = mApps.get(position);                          
    	    //该应用的包名            
    		String pkg = info.activityInfo.packageName;              
    	    //应用的主activity类            
    		String cls = info.activityInfo.name;                          
    	    ComponentName componet = new ComponentName(pkg, cls);                          
    	    Intent i = new Intent();              
    	    i.setComponent(componet);              
    	    startActivity(i); 
    	}
    	
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, "哈哈，回不去了！", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
    
    public class AppsAdapter extends BaseAdapter   
    {           
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
