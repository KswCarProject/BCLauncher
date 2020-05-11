package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

import java.util.List;

public class PairingListProtocol {
    private List<PairingListUnitProtocol> units;

    public PairingListProtocol(BaseMultilineProtocol mutilineProtocol) {
        try {
            this.units = mutilineProtocol.getUnits();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<PairingListUnitProtocol> getUnits() {
        return this.units;
    }
}
