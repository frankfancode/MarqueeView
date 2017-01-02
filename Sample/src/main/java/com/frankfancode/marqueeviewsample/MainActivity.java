package com.frankfancode.marqueeviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.frankfancode.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MarqueeView marqueeView;
    private MarqueeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        mAdapter = new MarqueeAdapter();

        List<MarqueeEntity> marqueeEntities = new ArrayList<>();
        marqueeEntities.add(new MarqueeEntity("1[首发]新系列 ThinkPad 不再预装软件", Integer.toString(1)));
        marqueeEntities.add(new MarqueeEntity("2[攻略]Kindle 对比", Integer.toString(2)));
//        marqueeEntities.add(new MarqueeEntity("3[大促]新年换新机，最高领券1000元", Integer.toString(3)));
//        marqueeEntities.add(new MarqueeEntity("4[热门]MacBook Pro 大降价", Integer.toString(3)));
//        marqueeEntities.add(new MarqueeEntity("5[新闻]牙膏厂这回猛了", Integer.toString(3)));
//        marqueeEntities.add(new MarqueeEntity("6[热议]比 POLO 大，只要 4 万", Integer.toString(3)));
        mAdapter.setData(marqueeEntities,2);
        marqueeView.setAdapter(mAdapter);
        marqueeView.startScroll();
    }
}
