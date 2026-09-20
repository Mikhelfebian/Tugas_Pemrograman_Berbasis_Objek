package arenabattle.util;

import java.util.Random;

public final class Acak {

    private static final Random RANDOM = new Random();

    private Acak() {
    }
    
    public static int antara(int min, int maks) {
        if (maks < min) {
            int tukar = min;
            min = maks;
            maks = tukar;
        }
        return RANDOM.nextInt(maks - min + 1) + min;
    }

    public static boolean peluang(double probabilitas) {
        return RANDOM.nextDouble() < probabilitas;
    }
}
