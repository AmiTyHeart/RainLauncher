package rainlauncher.main;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Toast;


public class MainActivity extends Activity {

	private Context mCtx;
	
	//保存所有app
	private List<ResolveInfo> mList;
	private PackageManager mPackageManager;
	private ScrollPanel layout;
	Dialog menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCtx = this;
		//初始化
		mPackageManager = getPackageManager();
		layout = new ScrollPanel(mCtx);
		setContentView(layout);

		//查询所有app
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		mList = mPackageManager.queryIntentActivities(i, 0);
		//排序
		Collections.sort(mList, new ResolveInfo.DisplayNameComparator(mPackageManager));
		
		//初始化
		//init();
		AppPanel panel = new AppPanel(this, mCtx, mPackageManager, mList);
		layout.addView(panel);
		
		menu = new MenuDialog(this, R.style.Dialog_WrapContent);
	}
	
/*
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
        grid.setNumColumns(getNumColumns());
        grid.setOnItemClickListener(listener);
        grid.setAdapter(new AppsAdapter()); 
    }
    
    private void loadApps() {           
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);           
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);             
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);       
    }  

    private int getNumColumns() {
    	DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        	return (int)metric.widthPixels / 145;
    }
    
    private OnItemClickListener listener = new OnItemClickListener() {
    	@Override public void onItemClick(AdapterView<?> parent,
    			View view, int position,long id) {
    		ResolveInfo info = mApps.get(position);                          
    	    //该应用的包名            
    		String pkg = info.activityInfo.packageName;              
    	    //应用的主activity类            
    		String cls = info.activityInfo.name;  
//    		Toast.makeText(getApplicationContext(), info + "\n" + cls,
//    				Toast.LENGTH_SHORT).show();
    	    ComponentName componet = new ComponentName(pkg, cls);                          
    	    Intent i = new Intent();              
    	    i.setComponent(componet);
    	    startActivity(i); 
    	}
    	
    };
    
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
*/       
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, "哈哈，回不去了！", Toast.LENGTH_SHORT).show();
			return false;
		}else if(keyCode == KeyEvent.KEYCODE_MENU){
			menu.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
