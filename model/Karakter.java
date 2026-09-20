package arenabattle.model;

import arenabattle.util.Acak;
import java.util.ArrayList;
import java.util.List;

public abstract class Karakter {
    private final String nama;
    private final int hpMaks;
    private final int manaMaks;
    private final int seranganDasar;
    private final int pertahananDasar;

    private int hp;
    private int mana;
    private final List<StatusEfek> statusAktif = new ArrayList<>();

    protected Karakter(String nama, int hpMaks, int manaMaks,
                       int seranganDasar, int pertahananDasar) {
        this.nama = nama;
        this.hpMaks = hpMaks;
        this.manaMaks = manaMaks;
        this.seranganDasar = seranganDasar;
        this.pertahananDasar = pertahananDasar;
        this.hp = hpMaks;
        this.mana = manaMaks;
    }

    public abstract String getKelas();

    public abstract String getNamaSkill();

    public abstract String getDeskripsiSkill();

    public abstract int getBiayaMana();

    public abstract HasilAksi gunakanSkill(Karakter target);

    public HasilAksi serang(Karakter target) {
        int damage = hitungDamageDasar();
        int diterima = target.terimaSerangan(damage, false);
        return new HasilAksi(nama + " menyerang " + target.getNama()
                + " sebesar " + diterima + " damage.", diterima);
    }

    public HasilAksi bertahan() {
        tambahStatus(new StatusEfek("Bertahan", 1, 8, 0));
        pulihkanMana(15);
        return new HasilAksi(nama + " memasang kuda-kuda. Pertahanan +8, mana +15.", 0);
    }

    protected int hitungDamageDasar() {
        return Math.max(1, seranganDasar + Acak.antara(-2, 3));
    }

    public int terimaSerangan(int damage, boolean tembusArmor) {
        int tahan = tembusArmor ? getPertahananTotal() / 2 : getPertahananTotal();
        int diterima = Math.max(1, damage - tahan);
        hp = Math.max(0, hp - diterima);
        return diterima;
    }

    public int terimaDamageLangsung(int damage) {
        int diterima = Math.max(0, damage);
        hp = Math.max(0, hp - diterima);
        return diterima;
    }

    public void pulihkanHp(int jumlah) {
        if (jumlah > 0) {
            hp = Math.min(hpMaks, hp + jumlah);
        }
    }

    public void pulihkanMana(int jumlah) {
        if (jumlah > 0) {
            mana = Math.min(manaMaks, mana + jumlah);
        }
    }

    protected boolean pakaiMana(int jumlah) {
        if (mana < jumlah) {
            return false;
        }
        mana -= jumlah;
        return true;
    }

    public boolean manaCukup() {
        return mana >= getBiayaMana();
    }

    public void tambahStatus(StatusEfek efek) {
        statusAktif.add(efek);
    }

    public List<String> mulaiGiliran() {
        List<String> catatan = new ArrayList<>();
        pulihkanMana(5);

        List<StatusEfek> habis = new ArrayList<>();
        for (StatusEfek efek : statusAktif) {
            int perubahan = efek.getHpPerGiliran();
            if (perubahan > 0) {
                pulihkanHp(perubahan);
                catatan.add(nama + " pulih " + perubahan + " HP dari " + efek.getNama() + ".");
            } else if (perubahan < 0) {
                hp = Math.max(0, hp + perubahan);
                catatan.add(nama + " menerima " + (-perubahan) + " damage dari " + efek.getNama() + ".");
            }
            efek.kurangiDurasi();
            if (!efek.isAktif()) {
                habis.add(efek);
                catatan.add("Efek " + efek.getNama() + " pada " + nama + " berakhir.");
            }
        }
        statusAktif.removeAll(habis);
        return catatan;
    }

    public int getPertahananTotal() {
        int total = pertahananDasar;
        for (StatusEfek efek : statusAktif) {
            total += efek.getBonusPertahanan();
        }
        return total;
    }

    public boolean isHidup() {
        return hp > 0;
    }

    public String getRingkasanStatus() {
        if (statusAktif.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statusAktif.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(statusAktif.get(i).toString());
        }
        return sb.toString();
    }

    public String getNama()          { return nama; }
    public int getHp()               { return hp; }
    public int getHpMaks()           { return hpMaks; }
    public int getMana()             { return mana; }
    public int getManaMaks()         { return manaMaks; }
    public int getSeranganDasar()    { return seranganDasar; }
    public int getPertahananDasar()  { return pertahananDasar; }

    public List<StatusEfek> getStatusAktif() {
        return new ArrayList<>(statusAktif);
    }

    @Override
    public String toString() {
        return nama + " (" + getKelas() + ")";
    }
}
