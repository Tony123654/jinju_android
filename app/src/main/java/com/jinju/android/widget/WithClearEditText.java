package com.jinju.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jinju.android.R;


/**
 * Created by miyun-8767 on 2016/3/15.
 */
public class WithClearEditText extends EditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {
    private Drawable mClearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;
    private int drawWidth;
    private final float scale = getResources().getDisplayMetrics().density;
    private boolean hasInit = false;

    public WithClearEditText(Context context) {
        super(context);
        init();
    }

    public WithClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WithClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        int padding = (int) (10 * scale + 0.5f);
        drawWidth = (int) (15 * scale + 0.5f);

        setBackgroundColor(getResources().getColor(R.color.transparent));
        mClearTextIcon = getResources().getDrawable(R.mipmap.btn_clear);
        mClearTextIcon.setBounds(0, 0, drawWidth, drawWidth);

        setPadding(0, 0, padding, 0);

        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
        hasInit = true;
    }

    private void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }
    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - drawWidth * 1.5) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                requestFocus();
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        if (hasFocus) {
//            setClearIconVisible(getText().length() > 0);
//        } else {
//            setClearIconVisible(false);
//        }
//        if (mOnFocusChangeListener != null) {
//            mOnFocusChangeListener.onFocusChange(v, hasFocus);
//        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (hasInit) {
            setClearIconVisible(text.length() > 0);
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setSelection(s.length());
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //保证光标始终在最后面
        if(selStart==selEnd){//防止不能多选
            setSelection(getText().length());
        }
    }
}
