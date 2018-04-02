package com.jinju.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jinju.android.R;

public class AdjustImageView extends ImageView {

	private float mDesiredAspect;

	public AdjustImageView(Context context) {
		super(context);
	}

	public AdjustImageView(Context context, AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public AdjustImageView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);

		TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AdjustImageView, defStyle, 0);
		setDesiredAspect(typedArray.getFloat(R.styleable.AdjustImageView_desiredAspect, 0.0f));

		typedArray.recycle();
	}

	public void setDesiredAspect(float desiredAspect) {
		mDesiredAspect = desiredAspect;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		float desiredAspect = mDesiredAspect;
		Drawable drawable = getDrawable();

		if (drawable == null && desiredAspect == 0.0f)
			return;

		if (drawable != null && desiredAspect == 0.0f) {
			int width = drawable.getIntrinsicWidth();
			int height = drawable.getIntrinsicHeight();
			desiredAspect = (float) width / (float) height;
		}

		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

		boolean resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
		boolean resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;

		int widthSize = 0;
		int heightSize = 0;

		if (resizeWidth) {
			widthSize = (int) (heightSpecSize * desiredAspect);
			heightSize = heightSpecSize;
		} else if (resizeHeight) {
			widthSize = widthSpecSize;
			heightSize = (int) (widthSpecSize / desiredAspect);
		} else {
			widthSize = widthSpecSize;
			heightSize = heightSpecSize;
		}

		setMeasuredDimension(widthSize, heightSize);
	}

}