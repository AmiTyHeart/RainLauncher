package rainlauncher.main;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

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
			CharSequence text = info.loadLabel(pm);
			Drawable d = info.loadIcon(pm);
			View view = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);
			((ImageView)view.findViewById(R.id.imageView)).setImageDrawable(d);
			((TextView)view.findViewById(R.id.textView)).setText(text);
			view.setFocusable(true);
			addView(view);
			
		}
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
