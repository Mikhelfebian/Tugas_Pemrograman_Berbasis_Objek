package arenabattle.model;

public class StatusEfek {

    private final String nama;
    private final int bonusPertahanan;
    private final int hpPerGiliran;   // positif = menyembuhkan, negatif = melukai
    private int sisaGiliran;

    public StatusEfek(String nama, int sisaGiliran, int bonusPertahanan, int hpPerGiliran) {
        this.nama = nama;
        this.sisaGiliran = Math.max(0, sisaGiliran);
        this.bonusPertahanan = bonusPertahanan;
        this.hpPerGiliran = hpPerGiliran;
    }

    public String getNama() {
        return nama;
    }

    public int getBonusPertahanan() {
        return bonusPertahanan;
    }

    public int getHpPerGiliran() {
        return hpPerGiliran;
    }

    public int getSisaGiliran() {
        return sisaGiliran;
    }

    public void kurangiDurasi() {
        if (sisaGiliran > 0) {
            sisaGiliran--;
        }
    }

    public boolean isAktif() {
        return sisaGiliran > 0;
    }

    @Override
    public String toString() {
        return nama + " (" + sisaGiliran + ")";
    }
}
