package arenabattle.model;

public class Ksatria extends Karakter {

    private static final int BIAYA_MANA = 20;

    public Ksatria(String nama) {
        super(nama, 140, 50, 14, 8);
    }

    @Override
    public String getKelas() {
        return "Ksatria";
    }

    @Override
    public String getNamaSkill() {
        return "Tameng Baja";
    }

    @Override
    public String getDeskripsiSkill() {
        return "Pertahanan +12 selama 2 giliran dan memulihkan 12 HP.";
    }

    @Override
    public int getBiayaMana() {
        return BIAYA_MANA;
    }

    @Override
    public HasilAksi gunakanSkill(Karakter target) {
        if (!pakaiMana(BIAYA_MANA)) {
            return new HasilAksi(getNama() + " kehabisan stamina untuk " + getNamaSkill() + ".", 0);
        }
        tambahStatus(new StatusEfek("Tameng Baja", 2, 12, 0));
        pulihkanHp(12);
        return new HasilAksi(getNama() + " mengangkat TAMENG BAJA. Pertahanan +12 (2 giliran), HP +12.", 12);
    }
}
