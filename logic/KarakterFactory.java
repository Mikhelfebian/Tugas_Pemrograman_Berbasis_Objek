package arenabattle.logic;

import arenabattle.model.Karakter;
import arenabattle.model.Ksatria;
import arenabattle.model.Pemanah;
import arenabattle.model.Penyembuh;
import arenabattle.model.Penyihir;
import arenabattle.util.Acak;


public class KarakterFactory {

    public static final String[] DAFTAR_KELAS = {"Ksatria", "Penyihir", "Pemanah", "Penyembuh"};

    private KarakterFactory() {
    }

    public static Karakter buat(String kelas, String nama) {
        switch (kelas) {
            case "Ksatria":
                return new Ksatria(nama);
            case "Penyihir":
                return new Penyihir(nama);
            case "Pemanah":
                return new Pemanah(nama);
            case "Penyembuh":
                return new Penyembuh(nama);
            default:
                throw new IllegalArgumentException("Kelas tidak dikenal: " + kelas);
        }
    }

    public static Karakter contoh(String kelas) {
        return buat(kelas, kelas);
    }

    public static String kelasAcak() {
        return DAFTAR_KELAS[Acak.antara(0, DAFTAR_KELAS.length - 1)];
    }
}
