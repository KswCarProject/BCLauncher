package a_vcard.android.text;

public interface InputFilter {
    CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4);

    public static class LengthFilter implements InputFilter {
        private int mMax;

        public LengthFilter(int max) {
            this.mMax = max;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int keep = this.mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                return "";
            }
            if (keep >= end - start) {
                return null;
            }
            return source.subSequence(start, start + keep);
        }
    }
}
