package com.example.android.bluetoothlegatt.ble_service.entity;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

import java.io.Serializable;

public class sitRemindEntity implements Serializable {
    private String beginTime;
    private int duration;
    private String endTime;
    private int number;
    private int openOrno;

    public sitRemindEntity(int number, int openOrno, String beginTime, String endTime, int duration) {
        this.number = number;
        this.openOrno = openOrno;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public int getNumber() {
        return this.number;
    }

    public int getOpenOrno() {
        return this.openOrno;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setOpenOrno(int openOrno) {
        this.openOrno = openOrno;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if ((o instanceof sitRemindEntity) && ((sitRemindEntity) o).getNumber() == this.number) {
            return true;
        }
        return false;
    }
}
