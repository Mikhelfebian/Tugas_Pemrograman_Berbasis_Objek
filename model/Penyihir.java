package arenabattle.model;

import arenabattle.util.Acak;

public class Penyihir extends Karakter {

    private static final int BIAYA_MANA = 25;

    public Penyihir(String nama) {
        super(nama, 95, 110, 10, 3);
    }

    @Override
    public String getKelas() {
        return "Penyihir";
    }

    @Override
    public String getNamaSkill() {
        return "Bola Api";
    }

    @Override
    public String getDeskripsiSkill() {
        return "Damage besar (sekitar 26) yang menembus separuh armor lawan.";
    }

    @Override
    public int getBiayaMana() {
        return BIAYA_MANA;
    }

    @Override
    public HasilAksi gunakanSkill(Karakter target) {
        if (!pakaiMana(BIAYA_MANA)) {
            return new HasilAksi(getNama() + " kehabisan mana untuk " + getNamaSkill() + ".", 0);
        }
        int damage = 26 + Acak.antara(-3, 5);
        int diterima = target.terimaSerangan(damage, true);   // true = menembus armor
        return new HasilAksi(getNama() + " melemparkan BOLA API ke " + target.getNama()
                + " sebesar " + diterima + " damage (menembus armor).", diterima);
    }
}
