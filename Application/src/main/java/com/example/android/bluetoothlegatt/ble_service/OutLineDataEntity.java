package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 05-Oct-17
 */

public class OutLineDataEntity {
    public static final int TYPE_AEROBIC = 5;
    public static final int TYPE_BALL_MOVEMENT = 3;
    public static final int TYPE_CLIMING = 2;
    public static final int TYPE_CUSTOM = 6;
    public static final int TYPE_MUSCLE = 4;
    public static final int TYPE_RUNNINT = 1;
    public static final int TYPE_UNKNOW = -1;
    public static final int TYPE_WALK = 0;
    private int calorie;
    private String day;
    private String heartReat = "";
    private String sportName;
    private int stepCount;
    private String time;
    private int type;

    public OutLineDataEntity(int type, String time, int stepCount, int calorie, String heartReat, String day, String sportName) {
        this.calorie = calorie;
        this.time = time;
        this.type = type;
        this.stepCount = stepCount;
        this.heartReat = heartReat;
        this.day = day;
        this.sportName = sportName;
    }

    public OutLineDataEntity() {

    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportName() {
        return this.sportName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String getTime() {
        return this.time;
    }

    public int getStepCount() {
        return this.stepCount;
    }

    public int getCalorie() {
        return this.calorie;
    }

    public String getHeartReat() {
        return this.heartReat;
    }

    public String getDay() {
        return this.day;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public void setHeartReat(String heartReat) {
        this.heartReat = heartReat;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if ((o instanceof OutLineDataEntity) && ((OutLineDataEntity) o).getTime().equals(getTime())) {
            return true;
        }
        return false;
    }
}
