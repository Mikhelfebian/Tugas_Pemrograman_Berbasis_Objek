package arenabattle.model;

import arenabattle.util.Acak;

public interface DapatKritikal {
    double getPeluangKritis();
    double getPengaliKritis();

    default boolean cobaKritis() {
        return Acak.peluang(getPeluangKritis());
    }
}
