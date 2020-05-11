package org.apache.log4j;

/* compiled from: PropertyConfigurator */
class NameValue {
    String key;
    String value;

    public NameValue(String key2, String value2) {
        this.key = key2;
        this.value = value2;
    }

    public String toString() {
        return new StringBuffer().append(this.key).append("=").append(this.value).toString();
    }
}
