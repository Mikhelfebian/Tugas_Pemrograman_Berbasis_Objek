package arenabattle.model;

public class Pemanah extends Karakter implements DapatKritikal {

    private static final int BIAYA_MANA = 25;
    private static final double PELUANG_KRITIS = 0.35;
    private static final double PENGALI_KRITIS = 2.0;

    public Pemanah(String nama) {
        super(nama, 105, 70, 13, 5);
    }

    @Override
    public String getKelas() {
        return "Pemanah";
    }

    @Override
    public String getNamaSkill() {
        return "Hujan Panah";
    }

    @Override
    public String getDeskripsiSkill() {
        return "Tiga panah beruntun, masing-masing bisa kritis.";
    }

    @Override
    public int getBiayaMana() {
        return BIAYA_MANA;
    }

    @Override
    public double getPeluangKritis() {
        return PELUANG_KRITIS;
    }

    @Override
    public double getPengaliKritis() {
        return PENGALI_KRITIS;
    }

    @Override
    public HasilAksi serang(Karakter target) {
        int damage = hitungDamageDasar();
        boolean kritis = cobaKritis();
        if (kritis) {
            damage = (int) (damage * getPengaliKritis());
        }
        int diterima = target.terimaSerangan(damage, false);
        String teks = getNama() + " melepas panah ke " + target.getNama()
                + " sebesar " + diterima + " damage";
        return new HasilAksi(kritis ? teks + " - KRITIS!" : teks + ".", diterima, kritis);
    }

    @Override
    public HasilAksi gunakanSkill(Karakter target) {
        if (!pakaiMana(BIAYA_MANA)) {
            return new HasilAksi(getNama() + " kehabisan mana untuk " + getNamaSkill() + ".", 0);
        }
        int total = 0;
        int jumlahKritis = 0;
        for (int i = 0; i < 3; i++) {
            int damage = 9;
            boolean kritis = cobaKritis();
            if (kritis) {
                damage = (int) (damage * getPengaliKritis());
                jumlahKritis++;
            }
            total += target.terimaSerangan(damage, false);
        }
        String teks = getNama() + " melepaskan HUJAN PANAH ke " + target.getNama()
                + " dengan total " + total + " damage";
        if (jumlahKritis > 0) {
            teks += " (" + jumlahKritis + " panah kritis)";
        }
        return new HasilAksi(teks + ".", total, jumlahKritis > 0);
    }
}
