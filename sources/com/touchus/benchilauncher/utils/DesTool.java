package com.touchus.benchilauncher.utils;

import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import com.touchus.benchilauncher.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class DesTool {
    public static SecretKey getDESKeyFromFile(Context context) {
        try {
            return (SecretKey) new ObjectInputStream(context.getResources().openRawResource(R.raw.deskey)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DESDecrypt(String input, Context context) {
        SecretKey key = getDESKeyFromFile(context);
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, key);
            return new String(cipher.doFinal(hexStringToByteArray(input)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DESEncrypt(String input, Context context) {
        SecretKey key = getDESKeyFromFile(context);
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, key);
            return byteArrayToHexString(cipher.doFinal(input.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMD5String(String input, Context cont) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes(VCardParser_V21.DEFAULT_CHARSET));
        } catch (NoSuchAlgorithmException e) {
            Log.e("", e.getMessage());
        } catch (UnsupportedEncodingException e2) {
            Log.e("", e2.getMessage());
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(byteArray[i] & 255).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(byteArray[i] & 255));
            } else {
                md5StrBuff.append(Integer.toHexString(byteArray[i] & 255));
            }
        }
        return md5StrBuff.toString().toUpperCase();
    }

    public static String getKey(Context context) {
        return byteArrayToHexString(getDESKeyFromFile(context).getEncoded());
    }

    public static byte[] objectToByteArray(Object object) {
        try {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(bStream);
            outStream.writeObject(object);
            outStream.close();
            return bStream.toByteArray();
        } catch (IOException e) {
            Log.e("", e.getMessage());
            return null;
        }
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0) {
            return null;
        }
        for (byte b : byteArray) {
            String hv = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String objectToHexString(Object object) {
        return byteArrayToHexString(objectToByteArray(object));
    }

    @SuppressLint({"DefaultLocale"})
    public static byte[] hexStringToByteArray(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        String hexString2 = hexString.toUpperCase();
        int length = hexString2.length() / 2;
        char[] hexChars = hexString2.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
