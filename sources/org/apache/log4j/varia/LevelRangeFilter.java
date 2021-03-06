package org.apache.log4j.varia;

import org.apache.log4j.Level;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class LevelRangeFilter extends Filter {
    boolean acceptOnMatch = false;
    Level levelMax;
    Level levelMin;

    public int decide(LoggingEvent event) {
        if (this.levelMin != null && !event.getLevel().isGreaterOrEqual(this.levelMin)) {
            return -1;
        }
        if (this.levelMax != null && event.getLevel().toInt() > this.levelMax.toInt()) {
            return -1;
        }
        if (this.acceptOnMatch) {
            return 1;
        }
        return 0;
    }

    public Level getLevelMax() {
        return this.levelMax;
    }

    public Level getLevelMin() {
        return this.levelMin;
    }

    public boolean getAcceptOnMatch() {
        return this.acceptOnMatch;
    }

    public void setLevelMax(Level levelMax2) {
        this.levelMax = levelMax2;
    }

    public void setLevelMin(Level levelMin2) {
        this.levelMin = levelMin2;
    }

    public void setAcceptOnMatch(boolean acceptOnMatch2) {
        this.acceptOnMatch = acceptOnMatch2;
    }
}
