package com.olympics.olympicsandroid.view.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.olympics.olympicsandroid.utility.FontUtils;

public class GothamBookTextView extends TextView {
    Typeface mGothamBookTypeface;

    public GothamBookTextView(Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GothamBookTextView(Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GothamBookTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            mGothamBookTypeface = FontUtils.loadTypeFace(getContext(), FontUtils.Gotham_Book);
            setTypeface(mGothamBookTypeface);
        }
    }
}