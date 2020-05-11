package cn.kuwo.autosdk;

enum e {
    NOTIFY_START,
    NOTIFY_FAILED,
    NOTIFY_FINISH;

    public static e[] a() {
        e[] eVarArr = d;
        int length = eVarArr.length;
        e[] eVarArr2 = new e[length];
        System.arraycopy(eVarArr, 0, eVarArr2, 0, length);
        return eVarArr2;
    }
}
