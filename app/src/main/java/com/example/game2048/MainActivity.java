package com.example.game2048;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    //定义格子间隔为10px
    private final int GapLength = 10;

    //定义格子数量初始化为4×4
    private int cellNumber = 4;

    //用于存放二维数组textview
    TextView[][] GameMapArray = new TextView[cellNumber][cellNumber];

    //用于存放二维数组数字
    int[][] GameMapNumber = new int[cellNumber][cellNumber];

    //分数
    private int score = 0;

    private float startX = 0f;
    private float startY = 0f;
    private float offsetX = 0f;
    private float offsetY = 0f;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new StatusBar(this).initBar();
        new StatusBar(this).DarkAndLight();
        setContentView(R.layout.activity_main);


        initGame();
        initSpinner();

        findViewById(R.id.NewGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGame();
            }
        });


        findViewById(R.id.GameTouchLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = motionEvent.getX() - startX;
                        offsetY = motionEvent.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                Log.d("方向:", "左");
                                ToLeft();
                            } else if (offsetX > 5) {
                                Log.d("方向:", "右");
                                ToRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                Log.d("方向:", "上");
                                ToTop();
                            } else if (offsetY > 5) {
                                Log.d("方向:", "下");
                                ToBottom();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }


    //向上
    private void ToTop() {

        //每一行的去除零的集合
        ArrayList<Integer> list = new ArrayList<>();

        //每一行的合并后的集合
        ArrayList<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < cellNumber; i++) {

            list.clear();
            list2.clear();


            //将一行的数据添加到集合中(去除零)
            for (int j = 0; j < cellNumber; j++) {
                if (GameMapNumber[j][i] != 0) {
                    list.add(GameMapNumber[j][i]);
                }
            }

            //只有一个数
            if (list.size() == 1) {
                //移动到向前移动
                GameMapNumber[0][i] = list.get(0);
                for (int j = 1; j < cellNumber; j++) {
                    GameMapNumber[j][i] = 0;
                }
            }

            //2个及以上
            if (list.size() >= 2) {
                index = 0;
                while (true) {
                    if (index == list.size() - 1) {
                        list2.add(list.get(index));
                        break;
                    } else {
                        if (Objects.equals(list.get(index), list.get(index + 1))) {
                            list2.add(list.get(index) * 2);
                            score = score + list.get(index) * 2;
                            TextView textView = findViewById(R.id.NowScore);
                            textView.setText(String.valueOf(score));
                            index = index + 2;
                        } else {
                            list2.add(list.get(index));
                            index = index + 1;
                        }
                    }

                    if (index == list.size()) {
                        break;
                    }
                }
                //填充数据
                for (int k = 0; k < cellNumber; k++) {
                    if (k < list2.size()) {
                        GameMapNumber[k][i] = list2.get(k);
                    } else {
                        GameMapNumber[k][i] = 0;
                    }
                }
            }
        }

        refreshData();
        new MyThread11().start();
        addCell();
        refreshData();
    }

    //向下
    private void ToBottom() {
        //每一行的去除零的集合
        ArrayList<Integer> list = new ArrayList<>();

        //每一行的合并后的集合
        ArrayList<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < cellNumber; i++) {

            list.clear();
            list2.clear();


            //将一行的数据添加到集合中(去除零)
            for (int j = 0; j < cellNumber; j++) {
                if (GameMapNumber[j][i] != 0) {
                    list.add(GameMapNumber[j][i]);
                }
            }

            //只有一个数
            if (list.size() == 1) {
                //移动到向前移动
                GameMapNumber[cellNumber - 1][i] = list.get(0);
                for (int j = cellNumber - 2; j >= 0; j--) {
                    GameMapNumber[j][i] = 0;
                }
            }

            //2个及以上
            if (list.size() >= 2) {
                index = list.size() - 1;

                while (true) {
                    if (index == 0) {
                        list2.add(list.get(index));
                        break;
                    } else {
                        if (Objects.equals(list.get(index), list.get(index - 1))) {
                            list2.add(list.get(index) * 2);
                            score = score + list.get(index) * 2;
                            TextView textView = findViewById(R.id.NowScore);
                            textView.setText(String.valueOf(score));
                            index = index - 2;
                        } else {
                            list2.add(list.get(index));
                            index = index - 1;
                        }
                    }

                    if (index < 0) {
                        break;
                    }
                }
                int time = 0;
                //填充数据
                for (int k = cellNumber - 1; k >= 0; k--) {
                    if (time < list2.size()) {
                        GameMapNumber[k][i] = list2.get(time);
                        time++;
                    } else {
                        GameMapNumber[k][i] = 0;
                    }
                }
            }
        }

        refreshData();
        new MyThread11().start();
        addCell();
        refreshData();
    }


    //向左
    private void ToLeft() {

        //每一行的去除零的集合
        ArrayList<Integer> list = new ArrayList<>();

        //每一行的合并后的集合
        ArrayList<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < cellNumber; i++) {

            list.clear();
            list2.clear();


            //将一行的数据添加到集合中(去除零)
            for (int j = 0; j < cellNumber; j++) {
                if (GameMapNumber[i][j] != 0) {
                    list.add(GameMapNumber[i][j]);
                }
            }

            //只有一个数
            if (list.size() == 1) {
                //移动到向前移动
                GameMapNumber[i][0] = list.get(0);
                for (int j = 1; j < cellNumber; j++) {
                    GameMapNumber[i][j] = 0;
                }
            }

            //2个及以上
            if (list.size() >= 2) {
                index = 0;
                while (true) {
                    if (index == list.size() - 1) {
                        list2.add(list.get(index));
                        break;
                    } else {
                        if (Objects.equals(list.get(index), list.get(index + 1))) {
                            list2.add(list.get(index) * 2);
                            score = score + list.get(index) * 2;
                            TextView textView = findViewById(R.id.NowScore);
                            textView.setText(String.valueOf(score));

                            index = index + 2;
                        } else {
                            list2.add(list.get(index));
                            index = index + 1;
                        }
                    }

                    if (index == list.size()) {
                        break;
                    }
                }
                //填充数据
                for (int k = 0; k < cellNumber; k++) {
                    if (k < list2.size()) {
                        GameMapNumber[i][k] = list2.get(k);
                    } else {
                        GameMapNumber[i][k] = 0;
                    }
                }
            }
        }

        refreshData();
        new MyThread11().start();
        addCell();
        refreshData();
    }

    //向右
    private void ToRight() {
        //每一行的去除零的集合
        ArrayList<Integer> list = new ArrayList<>();

        //每一行的合并后的集合
        ArrayList<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < cellNumber; i++) {

            list.clear();
            list2.clear();


            //将一行的数据添加到集合中(去除零)
            for (int j = 0; j < cellNumber; j++) {
                if (GameMapNumber[i][j] != 0) {
                    list.add(GameMapNumber[i][j]);
                }
            }


            //只有一个数
            if (list.size() == 1) {
                //移动到向前移动
                GameMapNumber[i][cellNumber - 1] = list.get(0);
                for (int j = cellNumber - 2; j >= 0; j--) {
                    GameMapNumber[i][j] = 0;
                }
            }

            //2个及以上
            if (list.size() >= 2) {
                index = list.size() - 1;
                while (true) {
                    if (index == 0) {
                        list2.add(list.get(index));
                        break;
                    } else {
                        if (Objects.equals(list.get(index), list.get(index - 1))) {
                            list2.add(list.get(index) * 2);
                            score = score + list.get(index) * 2;
                            TextView textView = findViewById(R.id.NowScore);
                            textView.setText(String.valueOf(score));
                            index = index - 2;
                        } else {
                            list2.add(list.get(index));
                            index = index - 1;
                        }
                    }
                    if (index < 0) {
                        break;
                    }
                }
                int time = 0;
                //填充数据
                for (int k = cellNumber - 1; k >= 0; k--) {
                    if (time < list2.size()) {
                        GameMapNumber[i][k] = list2.get(time);
                        time++;
                    } else {
                        GameMapNumber[i][k] = 0;
                    }
                }
            }
        }

        refreshData();
        new MyThread11().start();
        addCell();
        refreshData();
    }


    private void initSpinner() {
        //数据源
        ArrayList<String> spinners = new ArrayList<>();
        spinners.add("4");
        spinners.add("5");
        spinners.add("6");
        spinners.add("7");
        spinners.add("8");
        spinners.add("9");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinners);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        Spinner spinner = findViewById(R.id.spinner);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //选取时候的操作
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cellNumber = Integer.parseInt(spinners.get(position));
                GameMapArray = new TextView[cellNumber][cellNumber];
                GameMapNumber = new int[cellNumber][cellNumber];
                initGame();
            }

            //没被选取时的操作
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //缩放效果
    private void scaleAnimator(int x, int y) {

        final float scale = 0.5f;
        AnimatorSet scaleSet = new AnimatorSet();
        ValueAnimator valueAnimatorSmall = ValueAnimator.ofFloat(1.0f, scale);
        valueAnimatorSmall.setDuration(200);

        ValueAnimator valueAnimatorLarge = ValueAnimator.ofFloat(scale, 1.0f);
        valueAnimatorLarge.setDuration(200);

        valueAnimatorSmall.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue();
                GameMapArray[x][y].setScaleX(scale);
                GameMapArray[x][y].setScaleY(scale);
            }
        });
        valueAnimatorLarge.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue();
                GameMapArray[x][y].setScaleX(scale);
                GameMapArray[x][y].setScaleY(scale);
            }
        });

        scaleSet.play(valueAnimatorLarge).after(valueAnimatorSmall);
        scaleSet.start();
    }


    //初始化游戏
    private void initGame() {
        //设置网格布局的每行每列的控件数

        score = 0;
        TextView NowScore = findViewById(R.id.NowScore);
        NowScore.setText(String.valueOf(score));
        GridLayout gameLayout = findViewById(R.id.GameLayout);
        gameLayout.removeAllViews();
        gameLayout.setColumnCount(cellNumber);
        gameLayout.setRowCount(cellNumber);
        GridLayout gameTouchLayout = findViewById(R.id.GameTouchLayout);
        gameTouchLayout.setMinimumHeight(getSideLength() * cellNumber);

        int num = 0;

        //添加控件
        for (int i = 0; i < cellNumber; i++) {
            for (int j = 0; j < cellNumber; j++) {
                TextView textView = new TextView(this);

                num++;

                //设置ID
                textView.setId(num);

                textView.setTextColor(Color.BLACK);

                //设置数字
                GameMapNumber[i][j] = 0;

                GameMapArray[i][j] = textView;

                //设置宽高
                textView.setWidth(getSideLength());
                textView.setHeight(getSideLength());

                //设置文字对其方式
                textView.setGravity(Gravity.CENTER);

                //设置背景颜色
                textView.setBackgroundResource(R.drawable.game_cell);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                params.setGravity(Gravity.CENTER);

                //最右边添加间隔
                if ((num) % cellNumber == 0) {
                    params.rightMargin = GapLength;
                }

                //最下边添加间隔
                if ((cellNumber * cellNumber - cellNumber) < num && num <= cellNumber * cellNumber) {
                    params.bottomMargin = GapLength;
                }

                params.topMargin = GapLength;
                params.leftMargin = GapLength;
                gameLayout.addView(textView, params);

            }
        }
        int[] ran = {2, 2, 2, 4, 4};

        //添加两个格子
        int x1 = (int) (0 + Math.random() * (cellNumber));
        int y1 = (int) (0 + Math.random() * (cellNumber));
        int num1 = (int) (Math.random() * 100 % 5);
        GameMapNumber[x1][y1] = ran[num1];

        int x2;
        int y2;
        int num2;

        while (true) {
            x2 = (int) (0 + Math.random() * (cellNumber));
            y2 = (int) (0 + Math.random() * (cellNumber));
            num2 = (int) (Math.random() * 100 % 5);
            if (x1 != x2 || y1 != y2) {
                GameMapNumber[x2][y2] = ran[num2];
                break;
            }
        }
        refreshData();
    }

    //添加格子
    private void addCell() {

        int x;
        int y;
        int num;
        int[] ran = {2, 2, 2, 4, 4};

        boolean GameOver = true;

        for (int i = 0; i < cellNumber; i++) {
            for (int j = 0; j < cellNumber; j++) {
                if (GameMapNumber[i][j] == 0) {
                    GameOver = false;
                }
            }
        }
        if (GameOver) {
            Toast.makeText(this, "游戏结束", Toast.LENGTH_SHORT).show();
        } else {
            while (true) {
                x = (int) (0 + Math.random() * (cellNumber));
                y = (int) (0 + Math.random() * (cellNumber));
                num = (int) (Math.random() * 100 % 5);
                if (GameMapNumber[x][y] == 0) {
                    GameMapNumber[x][y] = ran[num];
                    scaleAnimator(x, y);
                    break;
                }
            }
        }
    }

    //刷新界面
    private void refreshData() {
        for (int i = 0; i < cellNumber; i++) {
            for (int j = 0; j < cellNumber; j++) {
                changeColor(i, j);
            }
        }
    }

    //上色
    private void changeColor(int x, int y) {
        GradientDrawable gradientDrawable = (GradientDrawable) GameMapArray[x][y].getBackground();
        switch (GameMapNumber[x][y]) {
            case 0:
                gradientDrawable.setColor(Color.parseColor("#cdc1b4"));
                GameMapArray[x][y].setText("");
                break;
            case 2:
                gradientDrawable.setColor(Color.parseColor("#eee4da"));
                GameMapArray[x][y].setText("2");
                GameMapArray[x][y].setTextSize(20);
                break;
            case 4:
                gradientDrawable.setColor(Color.parseColor("#ede0c8"));
                GameMapArray[x][y].setText("4");
                GameMapArray[x][y].setTextSize(20);
                break;
            case 8:
                gradientDrawable.setColor(Color.parseColor("#f2b179"));
                GameMapArray[x][y].setText("8");
                GameMapArray[x][y].setTextSize(20);
                break;
            case 16:
                gradientDrawable.setColor(Color.parseColor("#f59563"));
                GameMapArray[x][y].setText("16");
                GameMapArray[x][y].setTextSize(18);
                break;
            case 32:
                gradientDrawable.setColor(Color.parseColor("#f67c5f"));
                GameMapArray[x][y].setText("32");
                GameMapArray[x][y].setTextSize(18);
                break;
            case 64:
                gradientDrawable.setColor(Color.parseColor("#f65e3b"));
                GameMapArray[x][y].setText("64");
                GameMapArray[x][y].setTextSize(18);
                break;
            case 128:
                gradientDrawable.setColor(Color.parseColor("#edcf72"));
                GameMapArray[x][y].setText("128");
                GameMapArray[x][y].setTextSize(16);
                break;
            case 256:
                gradientDrawable.setColor(Color.parseColor("#edcc61"));
                GameMapArray[x][y].setText("256");
                GameMapArray[x][y].setTextSize(16);
                break;
            case 512:
                gradientDrawable.setColor(Color.parseColor("#edc850"));
                GameMapArray[x][y].setText("512");
                GameMapArray[x][y].setTextSize(16);
                break;
        }

    }

    //此方法用于计算每一个格子的边长
    private int getSideLength() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        //比较去小的哪一个值
        int minLength = (Math.min(screenHeight, screenWidth) - 20) - (cellNumber + 1) * GapLength;
        //拿最小值除以格子数,向下取整
        return minLength / cellNumber;
    }
}

class MyThread11 extends Thread {
    @Override
    public void start() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

