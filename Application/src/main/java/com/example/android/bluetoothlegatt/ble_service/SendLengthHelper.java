package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 02-Oct-17
 */

public class SendLengthHelper {
    private static final int connectSpace = 24;

    public static int getSendLengthDelay(int sendLength, int receverLength) {
        return (((max(sendLength, receverLength) / 20) * 24) + 100) + random(24, 100);
    }

    public int getSendLengthDelayTime(int sendLength) {
        return (sendLength * 24) + random(24, 100);
    }

    private static int random(int connectSpace, int i) {
        return (int) (((double) connectSpace) + (Math.random() * ((double) (i - connectSpace))));
    }

    private static int max(int sendLength, int receverLength) {
        return sendLength > receverLength ? sendLength : receverLength;
    }
}
