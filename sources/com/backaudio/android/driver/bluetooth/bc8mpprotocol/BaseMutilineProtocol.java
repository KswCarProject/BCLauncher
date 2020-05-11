package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

import java.util.ArrayList;
import java.util.List;

public class BaseMutilineProtocol {
    private List<Object> units = new ArrayList();

    public void addUnit(PairingListUnitProtocol unit) {
        if (!this.units.contains(unit)) {
            this.units.add(unit);
        }
    }

    public List getUnits() {
        return this.units;
    }
}
