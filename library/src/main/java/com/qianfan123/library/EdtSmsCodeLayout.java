package com.qianfan123.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangcong on 2017/2/14.
 * <p>
 */

public class EdtSmsCodeLayout extends GridLayout implements TextWatcher, View.OnKeyListener {

    private EditText[] edts;

    private int edt_position = 0;

    private StringBuffer code;

    private InputFinishListener inputFinishListener;

    private TypedArray typedArray;

    private int maxLen;    // 布局总长度 默认六位
    private int txtSize;  // 文本大小
    private int txtColor;    // 文本颜色
    private int edtSize;    // 每个输入框长宽

    public EdtSmsCodeLayout(Context context) {
        super(context);
        init(null);
    }

    public EdtSmsCodeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EdtSmsCodeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public EditText[] getEdts() {
        return edts;
    }

    private void init(AttributeSet set) {
        initDefValue();
        initSet(set);
        code = new StringBuffer();
        edts = initEdts(maxLen);
        edts[edt_position].setFocusableInTouchMode(true);
        edts[edt_position].requestFocus();
        final InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                imm.showSoftInput(edts[edt_position], 0);
            }
        }, 500);
    }

    /**
     * 初始化默认值
     */
    private void initDefValue() {
        maxLen = DefValue.MAX_LEN.value;
        txtSize = DefValue.TXT_SIZE.value;
        txtColor = getContext().getResources().getColor(DefValue.TXT_COLOR.value);
        edtSize = dip2px(DefValue.SIZE.value);
    }

    /**
     * 初始化输入框
     *
     * @param maxLen 输入框个数 默认6
     * @return
     */
    private EditText[] initEdts(int maxLen) {
        EditText[] editTexts = new EditText[maxLen];
        for (int i = 1; i <= maxLen; i++) {
            final EditText editText = new EditText(getContext());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setOnClickListener(null);
            editText.setLayoutParams(new LayoutParams(new LinearLayout.LayoutParams((edtSize), (edtSize))));
            editText.setGravity(Gravity.CENTER);
            editText.setTextSize(txtSize);
            editText.setTextColor(txtColor);
            editText.addTextChangedListener(this);
            editText.setOnKeyListener(this);
            Field f = null;
            try {
                f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editText, R.drawable.edt_cursor);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            setEdtBg(editText, i, maxLen);
            editText.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(1)});
            editTexts[i - 1] = editText;
            addView(editText);
            editText.setFocusableInTouchMode(false);
        }
        return editTexts;
    }

    /**
     * 初始化xml set
     *
     * @param set
     */
    private void initSet(AttributeSet set) {
        if (set == null)
            return;
        typedArray = getContext().obtainStyledAttributes(set, R.styleable.EdtSmsCodeLayout);
        if (typedArray != null) {
            txtSize = typedArray.getDimensionPixelSize(R.styleable.EdtSmsCodeLayout_text_size, -1) == -1 ? txtSize : px2dip(typedArray.getDimensionPixelSize(R.styleable.EdtSmsCodeLayout_text_size, -1));
            txtColor = typedArray.getColor(R.styleable.EdtSmsCodeLayout_text_color, txtColor);
            edtSize = typedArray.getDimensionPixelSize(R.styleable.EdtSmsCodeLayout_item_size, edtSize);
            maxLen = typedArray.getInt(R.styleable.EdtSmsCodeLayout_max_len, maxLen);
        }
    }

    public void setInputFinishListener(InputFinishListener listener) {
        inputFinishListener = listener;
    }

    private void setEdtBg(EditText text, int position, int maxLen) {
        if (position == 1)
            text.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edt_sms_left));
        else if (position == 2)
            text.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edt_sms_second));
        else if (position == maxLen)
            text.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edt_sms_right));
        else
            text.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edt_sms));

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (edts[edt_position].getText().toString().length() >= 1) {
            if (edt_position == edts.length - 1) {
                code.append(edts[edt_position].getText().toString());
                if (inputFinishListener != null)
                    inputFinishListener.onInputFinish(code.toString());
                removeCode();
                return;
            }
            nextEdt();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private void nextEdt() {
        setEdtFocus(1);
    }

    private void backEdt() {
        if (edt_position == 0)
            return;
        edts[edt_position - 1].setText(null);
        setEdtFocus(-1);
    }

    /**
     * 清空验证码，默认输入完成后清空，也可手动调用
     */
    public void removeCode() {
        edt_position = 0;
        code.delete(0, code.length());
        for (EditText text : edts) {
            text.setFocusableInTouchMode(false);
            text.setText(null);
        }
        edts[edt_position].setFocusableInTouchMode(true);
        edts[edt_position].requestFocus();
    }

    private void setEdtFocus(int type) {
        if (type > 0) {
            code.append(edts[edt_position].getText().toString());
        } else {
            code.deleteCharAt(code.length() - 1);
        }
        edts[edt_position].setFocusableInTouchMode(false);
        edt_position += type;
        edts[edt_position].setFocusableInTouchMode(true);
        edts[edt_position].requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN)
            backEdt();
        return false;
    }

    public interface InputFinishListener {
        void onInputFinish(String code);
    }
}
