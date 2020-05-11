package com.unibroad.c2py;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class Parser {
    private static boolean sInitialized = false;
    private static final String sName = "unicode_to_hanyu_pinyin.txt";
    protected static Properties sProp;
    public static Map<String, String> specialHanzi = new HashMap();

    static {
        specialHanzi.put("薄", "搏");
        specialHanzi.put("秘", "闭");
        specialHanzi.put("禇", "楚");
        specialHanzi.put("重", "崇");
        specialHanzi.put("盖", "舸");
        specialHanzi.put("贾", "甲");
        specialHanzi.put("解", "谢");
        specialHanzi.put("缪", "妙");
        specialHanzi.put("那", "纳");
        specialHanzi.put("区", "欧");
        specialHanzi.put("覃", "勤");
        specialHanzi.put("瞿", "欋");
        specialHanzi.put("仇", "球");
        specialHanzi.put("盛", "剩");
        specialHanzi.put("石", "时");
        specialHanzi.put("沈", "审");
        specialHanzi.put("折", "蛇");
        specialHanzi.put("单", "善");
        specialHanzi.put("塔", "它");
        specialHanzi.put("夏", "下");
        specialHanzi.put("冼", "险");
        specialHanzi.put("俞", "于");
        specialHanzi.put("於", "淤");
        specialHanzi.put("曾", "增");
        specialHanzi.put("查", "吒");
        specialHanzi.put("藏", "葬");
        specialHanzi.put("翟", "宅");
        specialHanzi.put("万俟", "莫奇");
        specialHanzi.put("尉迟", "玉迟");
    }

    public static final void release() {
        if (sInitialized) {
            synchronized (Parser.class) {
                if (sInitialized) {
                    if (sProp != null) {
                        sProp.clear();
                        sProp = null;
                    }
                    sInitialized = false;
                }
            }
        }
    }

    public static void initialize() throws IOException {
        if (!sInitialized) {
            synchronized (Parser.class) {
                if (!sInitialized) {
                    sProp = new Properties();
                    InputStream is = Parser.class.getResourceAsStream(sName);
                    try {
                        sProp.load(is);
                        if (is != null) {
                            is.close();
                        }
                        sInitialized = true;
                    } catch (Exception e) {
                        throw new IOException(e);
                    } catch (Throwable th) {
                        if (is != null) {
                            is.close();
                        }
                        throw th;
                    }
                }
            }
        }
    }

    public static final String getPinyin(char ch) {
        if (ch >= 0 && ch < 256) {
            return String.valueOf(ch).toLowerCase(Locale.US);
        }
        if (!sInitialized) {
            try {
                initialize();
            } catch (IOException e) {
                System.err.println("PinyinParser initialize error: " + e.getMessage());
            }
        }
        String result = sProp.getProperty(Integer.toHexString(ch).toUpperCase(Locale.US));
        if (result == null) {
            return "#";
        }
        return result.split(",")[0];
    }

    public static final String getPinyinFirstLetter(char ch) {
        return String.valueOf(getPinyin(ch).charAt(0));
    }

    public static final String getPinyin(String chinese) {
        if (chinese == null || "".equals(chinese)) {
            return "#";
        }
        char[] chs = chinese.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char ch : chs) {
            builder.append(getPinyin(ch));
        }
        return builder.toString();
    }

    public static final String getAllPinyin(String chinese) {
        if (chinese == null || "".equals(chinese)) {
            return "#";
        }
        return getPinyin(isPolyphone(chinese));
    }

    public static final String getPinyinFirstWord(String chinese) {
        if (chinese == null || "".equals(chinese)) {
            return "#";
        }
        return getPinyin(chinese.charAt(0));
    }

    public static final String getPinyinFirstLetter(String chinese) {
        if (chinese == null || "".equals(chinese)) {
            return "#";
        }
        return getPinyinFirstLetter(chinese.charAt(0));
    }

    public static final String getPinyinSecondWords(String chinese) {
        if (chinese == null || "".equals(chinese)) {
            return "#";
        }
        String s1 = getPinyin(chinese.charAt(0));
        if (chinese.length() <= 1) {
            return s1;
        }
        return String.valueOf(s1) + getPinyin(chinese.charAt(1));
    }

    public static final String getPinyinAllFirstLetters(String chinese) {
        if (chinese == null || "".equals(chinese)) {
            return "#";
        }
        char[] chs = chinese.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char ch : chs) {
            builder.append(getPinyinFirstLetter(ch));
        }
        return builder.toString();
    }

    private static String isPolyphone(String chinese) {
        String firstName = chinese.substring(0, 1);
        if (chinese.length() > 2) {
            String firstName1 = chinese.substring(0, 2);
            if ("万俟".equals(firstName1) || "尉迟".equals(firstName1)) {
                return String.valueOf(specialHanzi.get(firstName1)) + chinese.substring(2, chinese.length());
            }
        }
        if (specialHanzi.get(firstName) == null) {
            return chinese;
        }
        return String.valueOf(specialHanzi.get(firstName)) + chinese.substring(1, chinese.length());
    }
}
