package org.apache.log4j.pattern;

public abstract class PatternConverter {
    private final String name;
    private final String style;

    public abstract void format(Object obj, StringBuffer stringBuffer);

    protected PatternConverter(String name2, String style2) {
        this.name = name2;
        this.style = style2;
    }

    public final String getName() {
        return this.name;
    }

    public String getStyleClass(Object e) {
        return this.style;
    }
}
