package arenabattle.cli;

public final class TampilanUtil {

    private static final int PANJANG_BAR = 20;

    private TampilanUtil() {
    }

    public static String bar(int nilai, int maksimum) {
        int isi = maksimum == 0 ? 0 : (int) Math.round((double) nilai / maksimum * PANJANG_BAR);
        isi = Math.max(0, Math.min(PANJANG_BAR, isi));
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < PANJANG_BAR; i++) {
            sb.append(i < isi ? '#' : '-');
        }
        sb.append("] ").append(nilai).append("/").append(maksimum);
        return sb.toString();
    }

    public static String garis() {
        return "=".repeat(60);
    }

    public static String garisTipis() {
        return "-".repeat(60);
    }
}
