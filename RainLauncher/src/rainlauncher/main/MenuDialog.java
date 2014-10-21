package rainlauncher.main;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuDialog extends Dialog{

	Context context;
    ImageButton wallpaper, set, selfset;
    Button button;
    public MenuDialog(Context context) {
        super(context);
        this.context = context;
    }
    public MenuDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.menu);

        wallpaper = (ImageButton) findViewById(R.id.wallpaper);
        set = (ImageButton) findViewById(R.id.set);
        selfset = (ImageButton) findViewById(R.id.selfset);
        button = (Button) findViewById(R.id.miss_button);
        
        button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	dismiss();
            	onSetWallpaper();
            }
        });
        
        set.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dismiss();
				ComponentName componet = new ComponentName("com.android.settings",
	        			"com.android.settings.Settings");                          
	        	Intent i = new Intent();              
	        	i.setComponent(componet);  
	        	context.startActivity(i); 
			}
        });
        
        selfset.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dismiss();
				Toast.makeText(context, "selfset", Toast.LENGTH_SHORT).show();
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
    
    public void onSetWallpaper() {                           
        //生成一个设置壁纸的请求                
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);                   
        Intent chooser = Intent.createChooser(pickWallpaper,"选择壁纸");                   
        //发送设置壁纸的请求                   
        context.startActivity(chooser);       
    }
}
