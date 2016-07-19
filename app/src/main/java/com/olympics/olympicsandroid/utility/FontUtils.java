package com.olympics.olympicsandroid.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Hashtable;

public class FontUtils {

    public static final String Gotham_Bold = "Gotham-Bold.otf";

    public static final String Gotham_Book = "Gotham-Book.otf";

    private static Hashtable<String, Typeface> mFontMap = new Hashtable<String, Typeface>();

    /**
     * Loads the desired font.
     * 
     * @param context This should be application context.
     * @param fontName
     * @return
     */
    public static synchronized Typeface loadTypeFace(final Context context,
            final String fontName) {

        if (TextUtils.isEmpty(fontName)) {
            throw new IllegalArgumentException("Font name can not be NULL!");
        }

        if (mFontMap.containsKey(fontName)) {
            return mFontMap.get(fontName);
        }

        if (context == null) {
            throw new IllegalArgumentException("Context name can not be NULL!");
        }

        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);

        if (typeFace != null) {
            mFontMap.put(fontName, typeFace);
        }

        return typeFace;
}

    /**
     * Will recursively search for views in root view and set the fontfamily
     * suppiled keeping rest of the style same as in xml.
     *
     * @param context
     * @param v
     * @param fontFamily
     *            enum FontFamily.ARIAL,GEORGIA
     */

    public static void setFonts(final Context context, final View v,
            String fontFamily) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setFonts(context, child, fontFamily);
                }
            } else if (v instanceof TextView) {
                if (((TextView) v).getTypeface() != null) {
                    ((TextView) v).setTypeface(
                            loadTypeFace(context, fontFamily), ((TextView) v)
                            .getTypeface().getStyle());
                } else {
                    ((TextView) v)
                    .setTypeface(loadTypeFace(context, fontFamily));
                }
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(loadTypeFace(context, fontFamily),
                        ((Button) v).getTypeface().getStyle());
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(loadTypeFace(context, fontFamily),
                        ((EditText) v).getTypeface().getStyle());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }
}

