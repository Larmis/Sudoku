package com.example.isaak.sudoku;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isaak on 25/04/2018.
 */

public class Draw extends View implements View.OnTouchListener {

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int coordX = 100;
    private int coordY = 100;
    private int min = screenWidth < screenHeight ? screenWidth : screenHeight;
    private int step = min / 9;
    private List<coordinate> coordinateList = new ArrayList<coordinate>();
    private List<coordinate> beginList = new ArrayList<coordinate>();

    private int selectedValue = 0;
    private boolean isValidGrid = true;
    private boolean endOfTheGame = false;
    List<List<Integer>> grid = new ArrayList<>();

    public Draw(Context context, String grid) {
        super(context);
        this.initGrid(grid);
        this.setOnTouchListener(this);
    }

    public void initGrid(String line){
        int begin = 0;
        int end = 9;
        for(int i = 0; i<9 ; i++){
            List<Integer> tmpList = new ArrayList<>();
            String gridLine = line.substring(begin,end);
            begin += 9;
            end += 9;
            for(int j = 0; j<9 ; j++){
                char c = gridLine.charAt(j);
                tmpList.add(Integer.parseInt(gridLine.valueOf(c)));
            }

            grid.add(tmpList);
        }

        refreshGrid(true);
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint gridLine = new Paint();
        gridLine.setColor(Color.WHITE);

        Paint bigGridLine = new Paint();
        bigGridLine.setColor(Color.BLACK);

        Paint number = new Paint();
        number.setColor(Color.BLACK);
        number.setTextSize(step - 10);

        Paint newNumber = new Paint();
        newNumber.setColor(Color.BLUE);
        newNumber.setTextSize(step - 10);

        Paint invalidNumber = new Paint();
        invalidNumber.setColor(Color.RED);
        invalidNumber.setTextSize(step - 10);

        Paint circle = new Paint();
        circle.setStyle(Paint.Style.STROKE);
        circle.setColor(Color.BLUE);
        circle.setStrokeWidth(6);
        circle.setAlpha(255);
        circle.setAntiAlias(true);

        canvas.drawColor(Color.rgb(191, 215, 116));

        for (int i = 0; i <= 9; i++) {
            if(i%3!=0){
                canvas.drawLine(0, i * step, min, i * step, gridLine);
                canvas.drawLine(i * step, 0, i * step, min, gridLine);
            }
        }

        for (int i = 0; i <= 9; i++) {
            if(i%3==0){
                canvas.drawLine(0, i * step, min, i * step, bigGridLine);
                canvas.drawLine(0, i * step + 1, min, i * step + 1, bigGridLine);
                canvas.drawLine(0, i * step - 1, min, i * step - 1, bigGridLine);

                canvas.drawLine(i * step, 0, i * step, min, bigGridLine);
                canvas.drawLine(i * step + 1, 0, i * step + 1, min, bigGridLine);
                canvas.drawLine(i * step - 1, 0, i * step - 1, min, bigGridLine);
            }
        }

        if(endOfTheGame){
            canvas.drawText("Fin de partie", screenWidth/2 - screenWidth/3, 12 * step, newNumber);
        }else {
            for (int i = 0; i < 9; i++) {
                canvas.drawText(String.valueOf(i + 1), i * step + 20, 12 * step, newNumber);
                //canvas.drawCircle(i * step+60, 12 * step-60 , 50, circle);
            }
        }

        if (selectedValue != 0) {
            canvas.drawText(String.valueOf(selectedValue), coordX, coordY, newNumber);
        }

        for (coordinate c : coordinateList) {
            if (c.canChange) {
                if (c.isValid){
                    canvas.drawText(String.valueOf(c.value), (c.X - 1) * step + 20, c.Y * step - 15, newNumber);
                }else {
                    canvas.drawText(String.valueOf(c.value), (c.X - 1) * step + 20, c.Y * step - 15, invalidNumber);
                }
            }else {
                canvas.drawText(String.valueOf(c.value), (c.X - 1) * step + 20, c.Y * step - 15, number);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(!endOfTheGame) {
            coordX = (int) motionEvent.getX();
            coordY = (int) motionEvent.getY();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    selectedValue = getSelectedNumber(coordX, coordY);
                    break;
                case MotionEvent.ACTION_UP:
                    if (selectedValue != 0) {
                        coordinate gridCoord = getCoordinate(coordY, coordX);
                        if (gridCoord.X < 10) {
                            gridCoord.value = selectedValue;
                            grid.get(gridCoord.X - 1).set(gridCoord.Y - 1, gridCoord.value);
                            coordinateList = new ArrayList<>();
                            refreshGrid(false);
                        }
                    }
                    selectedValue = 0;
                    break;
            }

            this.invalidate();
        }
        return true;
    }

    private coordinate getCoordinate(int x , int y){
        return new coordinate((x / step) + 1, (y / step) + 1);
    }

    public void refreshGrid(boolean isInit){
        int x = 1;
        int y = 1;
        boolean tmpValid = true;
        for(List<Integer> list : grid){
            for(int number:list){
                if(number !=0) {
                    coordinate gridCoord = new coordinate(y, x);
                    gridCoord.value = number;
                    if(isInit) {
                        gridCoord.canChange = false;
                        beginList.add(gridCoord);
                    } else {
                        if (!absentLine(x-1, gridCoord.value) || !absentColumn(y-1, gridCoord.value) || !absentBlock(x-1,y-1, gridCoord.value)){
                            gridCoord.isValid = false;
                            tmpValid = false;
                        }
                        gridCoord.canChange = true;
                        coordinateList.add(gridCoord);
                    }
                }
                y ++;
            }
            x ++;
            y = 1;
        }

        this.isValidGrid = tmpValid;

        for (coordinate tmpCoord : beginList) {
                coordinateList.add(tmpCoord);
        }

        if(coordinateList.size()==81 && isValidGrid){
            this.endOfTheGame = true;
        }

    }

    private boolean absentLine (int line, int value){
        int cpt = 0;
        for (int i=0; i < 9; i++) {
            if (grid.get(line).get(i) == value) {
                cpt ++;
            }
        }
        return (cpt < 2);
    }

    private boolean absentColumn (int column, int value){
        int cpt = 0;
        for (int i=0; i < 9; i++) {
            if (grid.get(i).get(column) == value) {
                cpt ++;
            }
        }
        return (cpt < 2);
    }

    private boolean absentBlock (int line, int column, int value){
        int cpt = 0;
        int l = line-(line%3), c = column-(column%3);
        for (line=l; line < l+3; line++){
            for (column=c; column < c+3; column++) {
                if (grid.get(line).get(column) == value) {
                    cpt++;
                }
            }
        }
        return (cpt < 2);
    }

    public class coordinate {
        public int X;
        public int Y;
        public int value;
        public boolean canChange;
        public boolean isValid;

        public coordinate(int x, int y){
            this.X = x;
            this.Y = y;
            this.canChange = true;
            this.isValid = true;
        }

        //override la fonction equals de la classe pour savoir si beginList contient la nouvelle coordonnée ?


    }

    private int getSelectedNumber( int x, int y){
        if (y < 11 * step + 1 || y > 12 * step + 1 ) {
            return 0;
        } else {
            return (x / step) + 1;
        }
    }


}

