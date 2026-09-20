package arenabattle.model;

public class Penyembuh extends Karakter implements DapatMenyembuhkan {

    private static final int BIAYA_MANA = 30;
    private static final int KEKUATAN_SEMBUH = 24;

    public Penyembuh(String nama) {
        super(nama, 115, 100, 9, 6);
    }

    @Override
    public String getKelas() {
        return "Penyembuh";
    }

    @Override
    public String getNamaSkill() {
        return "Cahaya Pemulih";
    }

    @Override
    public String getDeskripsiSkill() {
        return "Memulihkan 24 HP dan memberi Regenerasi +6 HP selama 3 giliran.";
    }

    @Override
    public int getBiayaMana() {
        return BIAYA_MANA;
    }

    @Override
    public int getKekuatanPenyembuhan() {
        return KEKUATAN_SEMBUH;
    }

    @Override
    public HasilAksi sembuhkan(Karakter target) {
        target.pulihkanHp(KEKUATAN_SEMBUH);
        return new HasilAksi(getNama() + " memulihkan " + KEKUATAN_SEMBUH
                + " HP milik " + target.getNama() + ".", KEKUATAN_SEMBUH);
    }

    @Override
    public HasilAksi gunakanSkill(Karakter target) {
        if (!pakaiMana(BIAYA_MANA)) {
            return new HasilAksi(getNama() + " kehabisan mana untuk " + getNamaSkill() + ".", 0);
        }
        sembuhkan(this);
        tambahStatus(new StatusEfek("Regenerasi", 3, 0, 6));
        return new HasilAksi(getNama() + " memanggil CAHAYA PEMULIH. HP +"
                + KEKUATAN_SEMBUH + " dan Regenerasi aktif 3 giliran.", KEKUATAN_SEMBUH);
    }
}
