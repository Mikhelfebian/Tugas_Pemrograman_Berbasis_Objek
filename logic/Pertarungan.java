package arenabattle.logic;

import arenabattle.model.DapatMenyembuhkan;
import arenabattle.model.HasilAksi;
import arenabattle.model.Karakter;
import arenabattle.util.Acak;


public class Pertarungan {
    public enum Aksi {
        SERANG, SKILL, BERTAHAN
    }

    private static final int GILIRAN_TEKANAN = 8;

    private final Karakter pemain;
    private final Karakter lawan;
    private final LogPertarungan log = new LogPertarungan();

    private int nomorGiliran = 1;
    private boolean selesai = false;
    private Karakter pemenang;

    public Pertarungan(Karakter pemain, Karakter lawan) {
        this.pemain = pemain;
        this.lawan = lawan;

        log.tambah("=== ARENA BATTLE DIMULAI ===");
        log.tambah(pemain + "  VS  " + lawan);
        log.tambah("");
        log.tambah(pemain.getNama() + " memakai skill " + pemain.getNamaSkill()
                + " - " + pemain.getDeskripsiSkill());
        log.tambah(lawan.getNama() + " memakai skill " + lawan.getNamaSkill()
                + " - " + lawan.getDeskripsiSkill());

        log.pemisah("GILIRAN " + nomorGiliran);
        awaliGiliran(pemain);
    }

    private void awaliGiliran(Karakter aktor) {
        log.tambahSemua(aktor.mulaiGiliran());

        if (nomorGiliran >= GILIRAN_TEKANAN) {
            int damage = (nomorGiliran - GILIRAN_TEKANAN + 1) * 3;
            aktor.terimaDamageLangsung(damage);
            log.tambah("Tekanan Arena melukai " + aktor.getNama()
                    + " sebesar " + damage + " damage (tidak tertahan armor).");
        }
    }

    public void giliranPemain(Aksi aksi) {
        if (selesai) {
            return;
        }

        jalankanAksi(pemain, lawan, aksi);
        if (periksaSelesai()) {
            return;
        }

        awaliGiliran(lawan);
        if (periksaSelesai()) {
            return;
        }

        Aksi aksiLawan = pilihAksiOtomatis(lawan, pemain);
        jalankanAksi(lawan, pemain, aksiLawan);
        if (periksaSelesai()) {
            return;
        }

        nomorGiliran++;
        log.pemisah("GILIRAN " + nomorGiliran);
        if (nomorGiliran == GILIRAN_TEKANAN) {
            log.tambah(">> TEKANAN ARENA AKTIF: kedua petarung mulai kehilangan HP setiap giliran.");
        }
        awaliGiliran(pemain);
        periksaSelesai();
    }

    private void jalankanAksi(Karakter aktor, Karakter target, Aksi aksi) {
        if (aksi == Aksi.SKILL && !aktor.manaCukup()) {
            log.tambah(aktor.getNama() + " tidak punya cukup mana untuk "
                    + aktor.getNamaSkill() + ", jadi menyerang biasa.");
            aksi = Aksi.SERANG;
        }

        HasilAksi hasil;
        switch (aksi) {
            case SKILL:
                hasil = aktor.gunakanSkill(target);
                break;
            case BERTAHAN:
                hasil = aktor.bertahan();
                break;
            case SERANG:
            default:
                hasil = aktor.serang(target);
                break;
        }
        log.tambah(hasil.getDeskripsi());
    }

    private Aksi pilihAksiOtomatis(Karakter aktor, Karakter musuh) {
        double rasioHp = (double) aktor.getHp() / aktor.getHpMaks();

        if (aktor instanceof DapatMenyembuhkan && rasioHp < 0.5 && aktor.manaCukup()) {
            return Aksi.SKILL;
        }
        
        if (rasioHp < 0.3 && !aktor.manaCukup()) {
            return Aksi.BERTAHAN;
        }

        if (aktor.manaCukup() && Acak.peluang(0.6)) {
            return Aksi.SKILL;
        }
        return Aksi.SERANG;
    }

    private boolean periksaSelesai() {
        if (!pemain.isHidup() || !lawan.isHidup()) {
            selesai = true;
            pemenang = pemain.isHidup() ? pemain : lawan;
            log.tambah("");
            log.tambah("=== PERTARUNGAN SELESAI ===");
            if (!pemain.isHidup() && !lawan.isHidup()) {
                log.tambah("Keduanya tumbang. Hasil: SERI.");
                pemenang = null;
            } else {
                log.tambah("Pemenang: " + pemenang + " dengan sisa "
                        + pemenang.getHp() + " HP.");
            }
            return true;
        }
        return false;
    }

    public Karakter getPemain()       { return pemain; }
    public Karakter getLawan()        { return lawan; }
    public LogPertarungan getLog()    { return log; }
    public int getNomorGiliran()      { return nomorGiliran; }
    public boolean isSelesai()        { return selesai; }
    public Karakter getPemenang()     { return pemenang; }
}
