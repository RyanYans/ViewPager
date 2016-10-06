package com.rya.viewpager;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewPager extends AppCompatActivity {

    private android.support.v4.view.ViewPager pager_root;
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout ll_pager_point;
    private TextView tv_pager;
    private String[] dess;
    boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        initUI();       // View

        initData();     // Model

        initAdapter();  // Controller

        adapterLooper();

    }

    private void adapterLooper() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(3500);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pager_root.setCurrentItem(pager_root.getCurrentItem() + 1);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private void initAdapter() {
        pager_root.setAdapter(new MyViewAdapter());

        int currentItem = mImageViewList.size() * 1024;
        pager_root.setCurrentItem(currentItem);
    }

    private void initData() {
        int[] images = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

        dess = new String[]{"干露露为自己正名，巩俐躺着也中枪",
                "乐坛新星辈出，张学友也称欧巴", "接二连三的片场，明星累不累？",
                "绿军国安再次挑战恒大，能否逆袭？", "喊得撕心裂肺，期待能夺得奖项"};

        // 初始化第一条数据
        tv_pager.setText(dess[0]);
        mImageViewList = new ArrayList<>();
        // 遍历
        for (int index = 0; index < images.length; index++) {
            // 初始化图片
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(images[index]);
            mImageViewList.add(imageView);

            //初始化滚动小白点 + 首张文本
            View point = new View(getApplicationContext());
            point.setBackgroundResource(R.drawable.selector_point);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
            if (index != 0) {
                layoutParams.leftMargin = 15;
                point.setEnabled(false);
            } else {
                point.setEnabled(true);
            }

            ll_pager_point.addView(point, layoutParams);
        }
    }

    private void initUI() {
        pager_root = (android.support.v4.view.ViewPager) findViewById(R.id.pager_root);
        ll_pager_point = (LinearLayout) findViewById(R.id.ll_pager_point);
        tv_pager = (TextView) findViewById(R.id.tv_pager);

        pager_root.addOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % mImageViewList.size();

                for (int index = 0; index < ll_pager_point.getChildCount(); index++) {
                    View point = ll_pager_point.getChildAt(index);
                    point.setEnabled(position == index);
                    tv_pager.setText(dess[position]);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        isRunning = false;

        super.onDestroy();
    }

    class MyViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        // 指复用的判断逻辑
        @Override
        public boolean isViewFromObject(View view, Object object) {
            System.out.println(view == object);
            return (view == object);
        }

        // 返回要显示的条目信息
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mImageViewList.size();

            System.out.println("instant >> " + position);

            ImageView imageView = mImageViewList.get(position);

            container.addView(imageView);

            return imageView;
        }

        // 销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 手动重写 - object 需要销毁的对象
            System.out.println("destroy" + position);

            container.removeView((View) object);
        }
    }
}
