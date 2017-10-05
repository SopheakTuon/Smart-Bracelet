package com.example.android.bluetoothlegatt.ble_service.util;

/**
 * @author Sopheak Tuon
 * @created on 05-Oct-17
 */

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormatUtils {
    private static final String BLUETOOTH_BASE_UUID_POSTFIX = "-0000-1000-8000-00805F9B34FB";
    private static final String BLUETOOTH_BASE_UUID_PREFIX = "0000";

    public static String dateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String dateFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
    }

    public static String dateFormat24(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.getDefault()).format(date);
    }

    public static String dateFormat24Ms(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS", Locale.getDefault()).format(date);
    }

    public static String decimalFormat(float f) {
        return new DecimalFormat("##0.00").format((double) f);
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) {
            bs[i - begin] = src[i];
        }
        return bs;
    }

    public static byte[] short2Byte(short a) {
        return new byte[]{(byte) (a >> 8), (byte) a};
    }

    public static byte[] reverseShort2Byte(short a) {
        return new byte[]{(byte) (a >> 8), (byte) a};
    }

    public static short reverseByte2Short(byte[] b) {
        return (short) (((b[1] & 255) << 8) | (b[0] & 255));
    }

    public static void short2Byte(short a, byte[] b, int offset) {
        b[offset] = (byte) (a >> 8);
        b[offset + 1] = (byte) a;
    }

    public static short byte2Short(byte[] b) {
        return (short) ((b[0] & 255) | ((b[1] & 255) << 8));
    }

    public static short byte2Short(byte[] b, int offset) {
        return (short) ((b[offset] & 255) | ((b[offset + 1] & 255) << 8));
    }

    public static void long2Byte(long a, byte[] b, int offset) {
        b[offset + 0] = (byte) ((int) (a >> 56));
        b[offset + 1] = (byte) ((int) (a >> 48));
        b[offset + 2] = (byte) ((int) (a >> 40));
        b[offset + 3] = (byte) ((int) (a >> 32));
        b[offset + 4] = (byte) ((int) (a >> 24));
        b[offset + 5] = (byte) ((int) (a >> 16));
        b[offset + 6] = (byte) ((int) (a >> 8));
        b[offset + 7] = (byte) ((int) a);
    }

    public static long byte2Long(byte[] b, int offset) {
        return ((((((((((long) b[offset + 0]) & 255) << 56) | ((((long) b[offset + 1]) & 255) << 48)) | ((((long) b[offset + 2]) & 255) << 40)) | ((((long) b[offset + 3]) & 255) << 32)) | ((((long) b[offset + 4]) & 255) << 24)) | ((((long) b[offset + 5]) & 255) << 16)) | ((((long) b[offset + 6]) & 255) << 8)) | (((long) b[offset + 7]) & 255);
    }

    public static long byte2Long(byte[] b) {
        return (long) (((((((((b[0] & 255) << 56) | ((b[1] & 255) << 48)) | ((b[2] & 255) << 40)) | ((b[3] & 255) << 32)) | ((b[4] & 255) << 24)) | ((b[5] & 255) << 16)) | ((b[6] & 255) << 8)) | (b[7] & 255));
    }

    public static byte[] long2Byte(long a) {
        return new byte[]{(byte) ((int) (a >> 56)), (byte) ((int) (a >> 48)), (byte) ((int) (a >> 40)), (byte) ((int) (a >> 32)), (byte) ((int) (a >> 24)), (byte) ((int) (a >> 16)), (byte) ((int) (a >> 8)), (byte) ((int) a)};
    }

    public static int byte2Int(byte[] b, int offset) {
        int offset2 = offset + 1;
        offset = offset2 + 1;
        offset2 = offset + 1;
        offset = offset2 + 1;
        return (((b[offset] & 255) | ((b[offset2] & 255) << 8)) | ((b[offset] & 255) << 16)) | ((b[offset2] & 255) << 24);
    }

    public static byte[] int2Byte_HL(int a) {
        return new byte[]{(byte) (a >> 24), (byte) (a >> 16), (byte) (a >> 8), (byte) a};
    }

    public static byte[] int2Byte_HL_(int a) {
        return new byte[]{(byte) a, (byte) (a >> 8), (byte) (a >> 16), (byte) (a >> 24)};
    }

    public static byte[] int2Byte_LH(int a) {
        return new byte[]{(byte) (a >> 24), (byte) (a >> 16), (byte) (a >> 8), (byte) a};
    }

    public static void int2Byte(int a, byte[] b, int offset) {
        int i = offset + 1;
        b[offset] = (byte) (a >> 24);
        offset = i + 1;
        b[i] = (byte) (a >> 16);
        i = offset + 1;
        b[offset] = (byte) (a >> 8);
        offset = i + 1;
        b[i] = (byte) a;
    }

    public static String convert16to128UUID(String uuid) {
        return BLUETOOTH_BASE_UUID_PREFIX + uuid + BLUETOOTH_BASE_UUID_POSTFIX;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(byte[] src, int offset, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= len) {
            return null;
        }
        for (int i = offset; i < len; i++) {
            String hv = Integer.toHexString(src[i] & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String byteToHexString(byte src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        String hv = Integer.toHexString(src & 255);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }

    public static String byteToHexStringNo0X(byte src) {
        StringBuilder stringBuilder = new StringBuilder();
        String hv = Integer.toHexString(src & 255);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static int unsignedByteToInt(byte b) {
        return b & 255;
    }

    public static int unsignedBytesToInt(byte b0, byte b1) {
        return unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8);
    }

    public static boolean isHex(String aNumber) {
        return Pattern.matches("[a-f0-9A-F]{" + aNumber.length() + "}", aNumber);
    }

    public static byte[] hexString2ByteArray(String hexString) {
        int length = hexString.length();
        byte[] b = new byte[(length / 2)];
        int i = 0;
        int j = 0;
        while (i < length) {
            b[j] = (byte) Short.parseShort(hexString.substring(i, i + 2), 16);
            i += 2;
            j++;
        }
        return b;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] hexStringToByteArrays(String s) {
        return new BigInteger(s, 16).toByteArray();
    }

    public static String addPre0ForHexString(String hexString, int targetLength) {
        if (hexString.length() >= targetLength) {
            return hexString;
        }
        int s = targetLength - hexString.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s; i++) {
            sb.append('0');
        }
        sb.append(hexString);
        return sb.toString();
    }

    public static byte[] optimizeByteArray(byte[] value) {
        int start = 0;
        for (int i = 0; i < value.length; i++) {
            if (value[i] != (byte) 0) {
                start = i;
                break;
            }
        }
        return Arrays.copyOfRange(value, start, value.length);
    }

    public static boolean isZero(byte[] value) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] != (byte) 0) {
                int start = i;
                return false;
            }
        }
        return true;
    }

    public static byte mergeByte(byte high, byte low) {
        return (byte) ((high << 4) + (low & 15));
    }

    public static byte[] mergeByteArray(byte[] first, byte[] end) {
        byte[] mm = new byte[(first.length + end.length)];
        for (int i = 0; i < mm.length; i++) {
            if (i < first.length) {
                mm[i] = first[i];
            } else {
                mm[i] = end[i - first.length];
            }
        }
        return mm;
    }

    public static boolean byteArrayStartWithByteArray(byte[] src, byte[] desc) {
        if (src == null || desc == null || src.length < desc.length) {
            return false;
        }
        for (int i = 0; i < desc.length; i++) {
            if (desc[i] != src[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte getLowbit(byte b) {
        return (byte) (b & 15);
    }

    public static byte getHightbit(byte b) {
        return (byte) ((b & 240) >> 4);
    }
}
