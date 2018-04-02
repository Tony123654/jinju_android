package com.jinju.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.interfaces.TagWithListener;


/**
 * Created by Libra on 2018/3/9.
 */

public class TagView<T> extends TextView implements View.OnClickListener{
    private int itemDefaultDrawable;
    private int itemSelectDrawable;

    private int itemDefaultTextColor;
    private int itemSelectTextColor;

    private TagWithListener listener;
    private int item;

    private boolean isItemSelected;
    public void setListener(TagWithListener listener) {
        this.listener = listener;
    }
    public TagView(Context context) {
        super(context);
        init(context,null);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }
        TypedArray typedArray = null;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        itemDefaultTextColor = typedArray.getColor(R.styleable.TagView_defaultTextColor, Color.BLACK);
        itemSelectTextColor = typedArray.getColor(R.styleable.TagView_selectTextColor, Color.BLACK);
        itemDefaultDrawable = typedArray.getColor(R.styleable.TagView_selectDrawable, Color.GRAY);
        itemSelectDrawable = typedArray.getColor(R.styleable.TagView_selectDrawable, Color.RED);
        typedArray.recycle();

        setOnClickListener(this);
    }
    /**
     * 设置标签
     *
     * @param item
     */
    public void setItem(int item) {
        this.item = item;
    }
    @Override
    public void onClick(View v) {
        if (listener == null) return;
        listener.onItemSelect(item);
    }
    public void selectItemChangeColorState() {
        if (isItemSelected) {
            setBackgroundResource(itemDefaultDrawable);
            setTextColor(itemDefaultTextColor);
            isItemSelected = false;
        } else {
            setBackgroundResource(itemSelectDrawable);
            setTextColor(itemSelectTextColor);
            isItemSelected = true;
        }
    }
    public void setItemSelected(boolean itemSelected) {
        isItemSelected = itemSelected;
        if (itemSelected) {
            setBackgroundResource(itemSelectDrawable);
            setTextColor(itemSelectTextColor);
        } else {
            setBackgroundResource(itemDefaultDrawable);
            setTextColor(itemDefaultTextColor);
        }
    }

    public void setItemDefaultDrawable(int itemDefaultDrawable) {
        this.itemDefaultDrawable = itemDefaultDrawable;
        setBackgroundResource(itemDefaultDrawable);
    }

    public void setItemSelectDrawable(int itemSelectDrawable) {
        this.itemSelectDrawable = itemSelectDrawable;
    }

    public void setItemDefaultTextColor(int itemDefaultTextColor) {
        this.itemDefaultTextColor = itemDefaultTextColor;
        setTextColor(itemDefaultTextColor);
    }

    public void setItemSelectTextColor(int itemSelectTextColor) {
        this.itemSelectTextColor = itemSelectTextColor;
    }
}
