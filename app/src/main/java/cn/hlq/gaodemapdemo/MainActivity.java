package cn.hlq.gaodemapdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.hlq.gaodemapdemo.map.GaoDeLocationActivity;

/**
 * create by heliquan at 2017年5月3日
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        findViewById(R.id.id_main_gaode_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GaoDeLocationActivity.class));
            }
        });
    }

}
