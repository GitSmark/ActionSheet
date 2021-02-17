package com.huangxy.actionsheet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActionSheet extends Dialog {

    public final static int ActionSheetIosStyle = 0;
    public final static int ActionSheetMetiralStyle = 1;

    private Context context;
    private LinearLayout parentLayout;
    private TextView titleTextView;
    private List<Button> sheetList;
    private Button cancelButton;

    // 样式
    private int sheetStyle;

    // 标题
    private String title;

    //就取消按钮文字
    private String cancel;

    // 选择项文字列表
    private List<String> sheetTextList;

    // 选择项颜色列表
    private List<Integer> sheetTextColorList;

    // 标题颜色
    private int titleTextColor;

    // 取消按钮文字颜色
    private int cancelTextColor;

    // 选择项文字颜色
    private int sheetTextColor;

    // 标题大小
    private int titleTextSize;

    // 取消按钮文字大小
    private int cancelTextSize;

    // 选择项文字大小
    private int sheetTextSize;

    // 标题栏高度
    private int titleHeight;

    // 取消按钮高度
    private int cancelHeight;

    // 选择项高度
    private int sheetHeight;

    // 弹出框距离两边的宽度
    private int marginFrame;

    // 弹出框距离底部的高度
    private int marginBottom;

    // 取消按钮点击回调
    private View.OnClickListener cancelListener;

    // 选择项点击回调列表
    private List<View.OnClickListener> sheetListenerList;

    public ActionSheet(Context context) {
        super(context, R.style.ActionSheetStyle);
        init(context);
    }

    public ActionSheet(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        cancel = "取消";
        titleTextSize = 15;
        sheetTextSize = 16;
        setTheme(ActionSheetIosStyle);
        titleHeight = dp2px(42);
        sheetHeight = dp2px(42);
        cancelHeight = dp2px(42);

        sheetList = new ArrayList<>();
        sheetTextList = new ArrayList<>();
        sheetListenerList = new ArrayList<>();
        sheetTextColorList = new ArrayList<>();
    }

    private ActionSheet createDialog() {

        parentLayout = new LinearLayout(context);
        parentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parentLayout.setBackgroundColor(Color.parseColor((sheetStyle == ActionSheetMetiralStyle)? "#F0EFF4": "#00ffffff"));
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        //fix只有一个选择项时宽度不会占满整个屏幕
        if (sheetTextList != null && sheetTextList.size() < 2) {
            View spaceDividerLine = new View(context);
            spaceDividerLine.setBackgroundColor(Color.parseColor("#00000000"));
            parentLayout.addView(spaceDividerLine, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }

        if (title != null) {
            titleTextView = new TextView(context);
            titleTextView.setGravity(Gravity.CENTER);
            titleTextView.setText(title);
            titleTextView.setTextColor(titleTextColor);
            titleTextView.setTextSize(titleTextSize);
            if ((sheetStyle == ActionSheetMetiralStyle)) {
                titleTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_white_selector));
            } else {
                titleTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_top_up));
            }
            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, titleHeight);
            parentLayout.addView(titleTextView, titleLayoutParams);
        }
        for (int i = 0; i < sheetTextList.size(); i++) {
            if (i == 0 && title != null) {
                View topDividerLine = new View(context);
                topDividerLine.setBackgroundColor(Color.parseColor("#bfbfbf"));
                parentLayout.addView(topDividerLine, new LinearLayout
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            }

            Button button = new Button(context);
            button.setGravity(Gravity.CENTER);
            button.setTag(sheetTextList.get(i));
            if (sheetTextColorList.get(i) == 0) {
                button.setTextColor(sheetTextColor); //默认
            } else {
                button.setTextColor(sheetTextColorList.get(i));
            }
            button.setText(sheetTextList.get(i));
            button.setTextSize(sheetTextSize);
            // 去除选择项点击阴影
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setStateListAnimator(null);
            }
            if ((sheetStyle == ActionSheetMetiralStyle)) {
                button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_white_selector));
            } else
            if (title != null) {
                if (i == sheetTextList.size() - 1) {
                    button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bottom_selector));
                } else {
                    button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_white_selector));
                }
            } else {
                if (sheetTextList.size() == 1) {
                    button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_white_selector));
                } else {
                    if (i == 0) {
                        button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_top_selector));
                    } else if (i == sheetTextList.size() - 1) {
                        button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_bottom_selector));
                    } else {
                        button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_white_selector));
                    }
                }
            }
            button.setOnClickListener(sheetListenerList.get(i));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, sheetHeight);
            parentLayout.addView(button, layoutParams);
            sheetList.add(button);

            if (i != sheetTextList.size() - 1) {
                View bottomDividerLine = new View(context);
                bottomDividerLine.setBackgroundColor(Color.parseColor("#bfbfbf"));
                parentLayout.addView(bottomDividerLine, new LinearLayout
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            }
        }

        cancelButton = new Button(context);
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setText(cancel);
        cancelButton.setTag(cancel);
        cancelButton.setTextColor(cancelTextColor);
        cancelButton.setTextSize(cancelTextSize);
        cancelButton.setTypeface(Typeface.defaultFromStyle((sheetStyle == ActionSheetMetiralStyle)? Typeface.NORMAL: Typeface.BOLD));
        if ((sheetStyle == ActionSheetMetiralStyle)) {
            cancelButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.layout_white_selector));
        } else {
            cancelButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_white_selector));
        }
        cancelButton.setOnClickListener(cancelListener);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, cancelHeight);
        cancelParams.setMargins(0, dp2px(10), 0, 0);
        parentLayout.addView(cancelButton, cancelParams);

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().y = marginBottom;
        getWindow().getDecorView().setPadding(marginFrame, 0, marginFrame, marginBottom);
        getWindow().getDecorView().setMinimumWidth(context.getResources().getDisplayMetrics().widthPixels);

        setContentView(parentLayout);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        return this;
    }

    private void setTheme(int style) {

        this.sheetStyle = style;

        if (sheetStyle == ActionSheetMetiralStyle) {
            cancelTextSize = 16;
            marginFrame = dp2px(2);
            marginBottom = dp2px(2);
            titleTextColor = Color.parseColor("#9e9e9e");
            sheetTextColor = Color.parseColor("#040404");
            cancelTextColor = Color.parseColor("#040404");
        } else {
            cancelTextSize = 15;
            marginFrame = dp2px(18);
            marginBottom = dp2px(8);
            titleTextColor = Color.parseColor("#9e9e9e");
            sheetTextColor = Color.parseColor("#007aff");
            cancelTextColor = Color.parseColor("#007aff");
        }
    }

    private void addSheet(String text, int sheetTextColor, View.OnClickListener listener) {
        sheetTextList.add(text);
        sheetListenerList.add(listener);
        sheetTextColorList.add(sheetTextColor);
    }

    public void setCancel(String text) {
        this.cancel = text;
    }

    public void setCancelHeight(int height) {
        this.cancelHeight = dp2px(height);
    }

    public void setCancelTextColor(int color) {
        this.cancelTextColor = color;
    }

    public void setCancelTextSize(int textSize) {
        this.cancelTextSize = textSize;
    }

    public void setSheetHeight(int height) {
        this.sheetHeight = dp2px(height);
    }

    public void setSheetTextColor(int color) {
        this.sheetTextColor = color;
    }

    public void setSheetTextSize(int textSize) {
        this.sheetTextSize = textSize;
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public void setTitleHeight(int height) {
        this.titleHeight = height;
    }

    public void setTitleTextColor(int color) {
        this.titleTextColor = color;
    }

    public void setTitleTextSize(int textSize) {
        this.titleTextSize = textSize;
    }

    public void setMargin(int bottom) {
        this.marginBottom = dp2px(bottom);
    }

    public void addCancelListener(View.OnClickListener listener) {
        this.cancelListener = listener;
    }

    private int dp2px(float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static class DialogBuilder {
        ActionSheet dialog;

        public DialogBuilder(Context context) {
            dialog = new ActionSheet(context);
        }

        public DialogBuilder setTheme(int style) {
            dialog.setTheme(style);
            return this;
        }

        /**
         * 添加一个选择项
         * @param text 选择项文字
         * @param listener 选择项点击回调监听
         * @return 当前DialogBuilder
         */
        public DialogBuilder addSheet(String text, View.OnClickListener listener) {
            dialog.addSheet(text, 0, listener);
            return this;
        }

        public DialogBuilder addSheet(String text, int sheetTextColor, View.OnClickListener listener) {
            dialog.addSheet(text, sheetTextColor, listener);
            return this;
        }

        /**
         * 设置取消按钮文字
         * @param text 文字内容
         * @return 当前DialogBuilder
         */
        public DialogBuilder setCancel(String text) {
            dialog.setCancel(text);
            return this;
        }

        /**
         * 设置取消按钮高度
         * @param height 高度值，单位dp
         * @return 当前DialogBuilder
         */
        public DialogBuilder setCancelHeight(int height) {
            dialog.setCancelHeight(height);
            return this;
        }

        /**
         * 设置取消按钮文字颜色
         * @param color 颜色值
         * @return 当前DialogBuilder
         */
        public DialogBuilder setCancelTextColor(int color) {
            dialog.setCancelTextColor(color);
            return this;
        }

        /**
         * 设置取消按钮文字大小
         * @param textSize 大小值，单位sp
         * @return 当前DialogBuilder
         */
        public DialogBuilder setCancelTextSize(int textSize) {
            dialog.setCancelTextSize(textSize);
            return this;
        }

        /**
         * 设置选择项高度
         * @param height 高度值，单位dp
         * @return 当前DialogBuilder
         */
        public DialogBuilder setSheetHeight(int height) {
            dialog.setSheetHeight(height);
            return this;
        }

        /**
         * 设置选择项文字颜色
         * @param color 颜色值
         * @return 当前DialogBuilder
         */
        public DialogBuilder setSheetTextColor(int color) {
            dialog.setSheetTextColor(color);
            return this;
        }

        /**
         * 设置选择项文字大小
         * @param textSize 大小值，单位sp
         * @return 当前DialogBuilder
         */
        public DialogBuilder setSheetTextSize(int textSize) {
            dialog.setSheetTextSize(textSize);
            return this;
        }

        /**
         * 设置标题
         * @param text 文字内容
         * @return 当前DialogBuilder
         */
        public DialogBuilder setTitle(String text) {
            dialog.setTitle(text);
            return this;
        }

        /**
         * 设置标题栏高度
         * @param height 高度值，单位dp
         * @return 当前DialogBuilder
         */
        public DialogBuilder setTitleHeight(int height) {
            dialog.setTitleHeight(height);
            return this;
        }

        /**
         * 设置标题颜色
         * @param color 颜色值
         * @return 当前DialogBuilder
         */
        public DialogBuilder setTitleTextColor(int color) {
            dialog.setTitleTextColor(color);
            return this;
        }

        /**
         * 设置标题大小
         * @param textSize 大小值，单位sp
         * @return 当前DialogBuilder
         */
        public DialogBuilder setTitleTextSize(int textSize) {
            dialog.setTitleTextSize(textSize);
            return this;
        }

        /**
         * 设置弹出框距离底部的高度
         * @param bottom 距离值，单位dp
         * @return 当前DialogBuilder
         */
        public DialogBuilder setMargin(int bottom) {
            dialog.setMargin(bottom);
            return this;
        }

        /**
         * 设置取消按钮的点击回调
         * @param listener 回调监听
         * @return
         */
        public DialogBuilder addCancelListener(View.OnClickListener listener) {
            dialog.addCancelListener(listener);
            return this;
        }

        /**
         * 创建弹出框，放在最后执行
         * @return 创建的 ActionSheet 实体
         */
        public ActionSheet create() {
            return dialog.createDialog();
        }
    }
}
