package rainlauncher.main;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AppPanel extends ViewGroup {
	private Context mContext;
	//x,y轴[x控制列,y控制行]
	private final static int x = 4;
	private final static int y = 4;
	private int mPaddingTop = 5;
	private int mControlWidth;
	//屏数
	private int mScreen;
	
	
	public AppPanel(Context context, PackageManager pm, List<ResolveInfo> appList) {
		super(context);
		this.mContext = context;
		//总屏数
		this.mScreen = (int)Math.ceil(((double)appList.size()) / (x * y));
		
		//初始化childview
		for (ResolveInfo info : appList) {
			final CharSequence text = info.loadLabel(pm);
			final String pkg = info.activityInfo.packageName;                  
			final String cls = info.activityInfo.name;  
			Drawable d = info.loadIcon(pm);
			View view = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);
			ImageView iv = (ImageView)view.findViewById(R.id.imageView);
			iv.setImageDrawable(d);

			// 制出透明图标, 放到statelist中
	        StateListDrawable sld = createSLD(mContext, d);
	        iv.setImageDrawable(sld);
			
			iv.setOnClickListener(
					new OnClickListener(){
						@Override
						public void onClick(View arg0) {							
							ComponentName componet = new ComponentName(pkg, cls);                          
				    	    Intent i = new Intent();              
				    	    i.setComponent(componet);
				    	    mContext.startActivity(i); 
						}						
					});
			((TextView)view.findViewById(R.id.textView)).setText(text);
			view.setFocusable(true);
			addView(view);
			
		}
	}

	
	public Drawable createDrawable(Drawable d, Paint p) {
		
        BitmapDrawable bd = (BitmapDrawable)d;
        Bitmap b = bd.getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
                bd.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

        return new BitmapDrawable(bitmap);
    }


    /** 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果*/ 
    public StateListDrawable createSLD(Context context, Drawable drawable) {
        StateListDrawable bg = new StateListDrawable();
        Paint p = new Paint();
        p.setColor(0x40222222); //Paint ARGB色值，A = 0x40 不透明。RGB222222 暗色


        Drawable normal = drawable;
        Drawable pressed = createDrawable(drawable, p);
        // p = new Paint();
        // p.setColor(0x8000FF00);
        // Drawable focused = createDrawable(drawable, p);
        // p = new Paint();
        // p.setColor(0x800000FF);
        // Drawable unable = createDrawable(drawable, p);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_pressed,
                android.R.attr.state_enabled }, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        // bg.addState(new int[] { android.R.attr.state_enabled,
        // android.R.attr.state_focused }, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);
        // View.FOCUSED_STATE_SET
        // bg.addState(new int[] { android.R.attr.state_focused }, focused);
        // // View.WINDOW_FOCUSED_STATE_SET
        // bg.addState(new int[] { android.R.attr.state_window_focused },
        // unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[] {}, normal);
        return bg;
    }
	
	@Override
	protected void onLayout(boolean change, int l, int t, int r, int b) {
		if(change){
			final int count = getChildCount();
			int childLeft = l;
			int childTop = t + mPaddingTop;
			View view = null;
			int index = 0;
			//屏,行,列
			A : for (int i = 0; i < mScreen; i++) {
				for (int j = 0; j < y; j++) {
					for (int k = 0; k < x; k++) {
						if(index >= count){
							break A;
						}
						view = getChildAt(index);
						view.layout(childLeft, childTop, childLeft + view.getMeasuredWidth(), childTop + view.getMeasuredHeight());
						childLeft += view.getMeasuredWidth();
						index ++;
					}
					childLeft = mControlWidth * i;
					childTop = view.getMeasuredHeight() * (j + 1) + mPaddingTop;
				}
				childLeft = mControlWidth * (i + 1);
				childTop = t + mPaddingTop;
			}
		}
	}

	//测量
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);//800
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);//560
		
		mControlWidth = widthSize;
		
		final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize / x, MeasureSpec.EXACTLY);
		final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize / y, MeasureSpec.EXACTLY);
		
		final int count = getChildCount();
		for(int i = 0; i < count; i ++){
			getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}
		setMeasuredDimension(widthSize * mScreen, heightSize);
	}
}
