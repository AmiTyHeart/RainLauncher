package rainlauncher.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

public class ScrollPanel extends ViewGroup {
	private final static String TAG = "WorkSpace";
	//touch状态
	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	private int mTouchState;
	//滑动时最小速度
	private final static int SNAP_VELOCITY = 600;
	//滑动
	private Scroller mScroller;
	//滑动速度
	private VelocityTracker mVelocityTracker;
	//初始屏
	private int mDefaultScreen = 0;
	//当前屏
	private int mCurrentScreen;
	//总屏
	private int mTotalScreen;
	//缓存最后按下的位置
	private float mLastMotionX;
	//private float mLastMotionY;
	private int mTouchSlop;
	private int mMaxnumVelocity;
	
	public ScrollPanel(Context context) {
		super(context);
		//初始化滑动
		mScroller = new Scroller(context);
		mCurrentScreen = mDefaultScreen;
		
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMaxnumVelocity = configuration.getScaledMaximumFlingVelocity();
	}
	
	public ScrollPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed){
			int childLeft = l;
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View child = getChildAt(i);
				if(child.getVisibility() != View.GONE){
					final int childWidth = child.getMeasuredWidth();
					child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
			//总屏数
			mTotalScreen = childLeft / getWidth();
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int childMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		//设置滚动的初始位置
		scrollTo(mCurrentScreen * childMeasureWidth, 0);
		setMeasuredDimension(childMeasureWidth, heightMeasureSpec);
	}
	
	/**
	 * Call this when you want to know the new location. If it returns true, 
	 * the animation is not yet finished. loc will be altered to provide the new location. 
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	//分发事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if (action == MotionEvent.ACTION_MOVE && mTouchState != TOUCH_STATE_REST) {
			return true;
		}
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG, "onInterceptTouchEvent.ACTION_DOWN");
				mLastMotionX = (int)ev.getX();
				//mLastMotionY = y;
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG, "onInterceptTouchEvent.ACTION_MOVE");
				final int x = (int)ev.getX();
				final int diffX = (int)Math.abs(x - mLastMotionX);
				if(diffX > mTouchSlop){
					mTouchState = TOUCH_STATE_SCROLLING;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				Log.i(TAG, "onInterceptTouchEvent.ACTION_UP");
				mTouchState = TOUCH_STATE_REST;
				break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(TAG, "onTouchEvent");
		if(mVelocityTracker == null){
			mVelocityTracker = VelocityTracker.obtain();
		}
		//加入event
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG, "onTouchEvent.ACTION_DOWN");
				//aborting the animating cause the scroller to move to the final x and y position
				if(!mScroller.isFinished()){
					mScroller.abortAnimation();
				}
				mLastMotionX = (int) event.getX();
				//mLastMotionY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG, "onTouchEvent.ACTION_MOVE");
				final int x = (int) event.getX();
				int tempX = (int)(mLastMotionX - x);
				mLastMotionX = x;
				Log.i(TAG, "tempX:" + tempX + ",x:" + x + ",mLastMotionX:" + mLastMotionX);
				scrollBy(tempX, 0);
				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG, "onTouchEvent.ACTION_UP");
				final VelocityTracker tempVelocityTracker = mVelocityTracker;
				tempVelocityTracker.computeCurrentVelocity(1000, mMaxnumVelocity);
				int velocityX = (int)tempVelocityTracker.getXVelocity();
				
				final int screenWidth = getWidth();
				//	|**0****1**|**1****2**|**2****3**|
				final int whichScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;
				//	0**********1**********2**********3
				final float scrolledPos = (float) getScrollX() / screenWidth;
				
				if(velocityX > SNAP_VELOCITY && mCurrentScreen > 0){
//					Toast.makeText(getContext(), "<==", Toast.LENGTH_SHORT).show();
//					final int bound = scrolledPos < whichScreen ? mCurrentScreen - 1 : mCurrentScreen;
					snapToScreen(mCurrentScreen - 1);
				}else if(velocityX < - SNAP_VELOCITY && mCurrentScreen < mTotalScreen - 1){
//					Toast.makeText(getContext(), "==>", Toast.LENGTH_SHORT).show();
//					final int bound = scrolledPos > whichScreen ? mCurrentScreen + 1 : mCurrentScreen;
					snapToScreen(mCurrentScreen + 1);
//					snapToScreen(Math.min(whichScreen, bound));
				}else{
					snapToScreen(whichScreen);
				}
				if(mVelocityTracker != null){
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				mTouchState = TOUCH_STATE_REST;
				break;
			case MotionEvent.ACTION_CANCEL:
				Log.i(TAG, "onTouchEvent.ACTION_CANCEL");
				mTouchState = TOUCH_STATE_REST;
				break;
		}
		return true;
	}
	
	private void snapToScreen(int whichScreen){
		whichScreen = Math.max(0, Math.min(whichScreen, mTotalScreen - 1));
    	if (getScrollX() != (whichScreen * getWidth())) {
    		//及目的页面的坐标减去目前已经移动的距离,就是剩下要移动的距离
    		final int delta = whichScreen * getWidth() - getScrollX();
    		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
    		mCurrentScreen = whichScreen;
    		invalidate();
    	}
	}
	
}
