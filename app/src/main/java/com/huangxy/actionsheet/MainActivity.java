package com.huangxy.actionsheet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionSheet actionSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActionSheet1();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActionSheet2();
            }
        });
    }

    private void showActionSheet1() {
        actionSheet = new ActionSheet.DialogBuilder(this)
            .setTitle("这是标题")
            .addSheet("确定", Color.parseColor("#FF3B30"), this)
            .addSheet("体育", this)
            .addSheet("娱乐", this)
            .addCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    actionSheet.hide();
                }
            })
            .create();

        //actionSheet.setCanceledOnTouchOutside(true);
        //actionSheet.setCancelable(true);
        actionSheet.show();
    }

    private void showActionSheet2() {
        actionSheet = new ActionSheet.DialogBuilder(this)
                .setTheme(ActionSheet.ActionSheetMetiralStyle)
                .setCancel("我再想想")
                //.setTitleTextColor(Color.parseColor("#040404"))
                //.setSheetTextColor(Color.parseColor("#007AFF"))
                .setCancelTextColor(Color.parseColor("#FF3B30"))
                .addSheet("选项一", this)
                .addSheet("选项二", this)
                .addSheet("选项三", this)
                .addCancelListener(this)
                .create();

        actionSheet.setCanceledOnTouchOutside(false);
        actionSheet.setCancelable(false);
        actionSheet.show();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(MainActivity.this, ""+view.getTag(), Toast.LENGTH_SHORT).show();
        if (actionSheet != null) actionSheet.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (actionSheet != null) {
            actionSheet.dismiss();
        }
    }
}
