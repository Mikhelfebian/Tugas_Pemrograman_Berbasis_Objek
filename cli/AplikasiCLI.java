package arenabattle.cli;

import arenabattle.logic.KarakterFactory;
import arenabattle.logic.Pertarungan;
import arenabattle.model.Karakter;
import java.util.List;
import java.util.Scanner;


public class AplikasiCLI {

    private final Scanner scanner = new Scanner(System.in);

    public void jalankan() {
        cetakBanner();

        boolean mainLagi = true;
        while (mainLagi) {
            Karakter pemain = buatKarakterPemain();
            Karakter lawan = buatKarakterLawan();

            System.out.println();
            System.out.println(TampilanUtil.garis());
            System.out.println("PERTARUNGAN DIMULAI: " + pemain + "  VS  " + lawan);
            System.out.println(TampilanUtil.garis());

            Pertarungan pertarungan = new Pertarungan(pemain, lawan);
            cetakLogBaru(pertarungan, 0);

            jalankanLoopPertarungan(pertarungan);

            mainLagi = tanyaYaTidak("Main lagi? (y/n): ");
        }

        System.out.println();
        System.out.println("Terima kasih sudah bermain Arena Battle!");
        scanner.close();
    }

    // =========================================================
    // PEMBUATAN KARAKTER
    // =========================================================

    private Karakter buatKarakterPemain() {
        System.out.println();
        System.out.println(TampilanUtil.garis());
        System.out.println("PEMILIHAN KARAKTER ANDA");
        System.out.println(TampilanUtil.garis());

        System.out.print("Masukkan nama karaktermu: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) {
            nama = "Pahlawan";
        }

        String kelas = pilihKelas("Pilih kelas untuk " + nama + ":");
        return KarakterFactory.buat(kelas, nama);
    }

    private Karakter buatKarakterLawan() {
        System.out.println();
        System.out.println("Lawan: (1) Pilih manual   (2) Acak");
        System.out.print("Pilihan: ");
        String pilihan = scanner.nextLine().trim();

        String kelas;
        if (pilihan.equals("2")) {
            kelas = KarakterFactory.kelasAcak();
            System.out.println("Lawan acak terpilih: " + kelas);
        } else {
            kelas = pilihKelas("Pilih kelas untuk lawan:");
        }
        return KarakterFactory.buat(kelas, "Musuh " + kelas);
    }

    private String pilihKelas(String judul) {
        String[] daftar = KarakterFactory.DAFTAR_KELAS;
        System.out.println(judul);
        for (int i = 0; i < daftar.length; i++) {
            Karakter contoh = KarakterFactory.contoh(daftar[i]);
            System.out.printf("  %d. %-10s (HP %d, Mana %d, Serang %d, Tahan %d) - Skill: %s%n",
                    i + 1, daftar[i], contoh.getHpMaks(), contoh.getManaMaks(),
                    contoh.getSeranganDasar(), contoh.getPertahananDasar(), contoh.getNamaSkill());
        }

        int indeks = bacaAngka("Nomor pilihan (1-" + daftar.length + "): ", 1, daftar.length);
        return daftar[indeks - 1];
    }

    // =========================================================
    // LOOP PERTARUNGAN
    // =========================================================

    private void jalankanLoopPertarungan(Pertarungan pertarungan) {
        Karakter pemain = pertarungan.getPemain();
        Karakter lawan = pertarungan.getLawan();

        while (!pertarungan.isSelesai()) {
            cetakStatus(pertarungan);

            Pertarungan.Aksi aksi = pilihAksi(pemain);

            int barisSebelum = pertarungan.getLog().jumlahBaris();
            pertarungan.giliranPemain(aksi);
            cetakLogBaru(pertarungan, barisSebelum);
        }

        cetakStatus(pertarungan);
        System.out.println();
        System.out.println(TampilanUtil.garis());
        Karakter pemenang = pertarungan.getPemenang();
        if (pemenang == null) {
            System.out.println("HASIL AKHIR: SERI - keduanya tumbang bersamaan!");
        } else if (pemenang == pemain) {
            System.out.println("HASIL AKHIR: ANDA MENANG sebagai " + pemenang + "!");
        } else {
            System.out.println("HASIL AKHIR: ANDA KALAH. Pemenang: " + pemenang);
        }
        System.out.println(TampilanUtil.garis());
    }

    private Pertarungan.Aksi pilihAksi(Karakter pemain) {
        System.out.println();
        System.out.println("Pilih aksi:");
        System.out.println("  1. Serang");
        System.out.printf("  2. Skill: %s (%d mana) - %s%n",
                pemain.getNamaSkill(), pemain.getBiayaMana(), pemain.getDeskripsiSkill());
        System.out.println("  3. Bertahan (pulihkan mana + pertahanan sementara)");

        int pilihan = bacaAngka("Nomor aksi (1-3): ", 1, 3);
        switch (pilihan) {
            case 2:
                return Pertarungan.Aksi.SKILL;
            case 3:
                return Pertarungan.Aksi.BERTAHAN;
            default:
                return Pertarungan.Aksi.SERANG;
        }
    }

    // =========================================================
    // TAMPILAN
    // =========================================================

    private void cetakStatus(Pertarungan pertarungan) {
        Karakter pemain = pertarungan.getPemain();
        Karakter lawan = pertarungan.getLawan();

        System.out.println();
        System.out.println(TampilanUtil.garisTipis());
        System.out.println("GILIRAN " + pertarungan.getNomorGiliran());
        System.out.println(TampilanUtil.garisTipis());

        cetakBarisKarakter(pemain);
        cetakBarisKarakter(lawan);
    }

    private void cetakBarisKarakter(Karakter k) {
        System.out.printf("%-22s %s%n", k.getNama() + " (" + k.getKelas() + ")",
                "HP " + TampilanUtil.bar(k.getHp(), k.getHpMaks()));
        System.out.printf("%-22s %s%n", "",
                "MP " + TampilanUtil.bar(k.getMana(), k.getManaMaks()));
        System.out.println("   Pertahanan: " + k.getPertahananTotal()
                + "   Status: " + k.getRingkasanStatus());
    }

    /** Mencetak hanya baris log yang baru ditambahkan sejak aksi terakhir. */
    private void cetakLogBaru(Pertarungan pertarungan, int mulaiDari) {
        List<String> semuaBaris = pertarungan.getLog().getBaris();
        System.out.println();
        for (int i = mulaiDari; i < semuaBaris.size(); i++) {
            System.out.println(semuaBaris.get(i));
        }
    }

    private void cetakBanner() {
        System.out.println(TampilanUtil.garis());
        System.out.println("             A R E N A   B A T T L E");
        System.out.println("       Simulator Pertarungan Turn-Based (CLI)");
        System.out.println(TampilanUtil.garis());
    }

    // =========================================================
    // INPUT HELPER
    // =========================================================

    private int bacaAngka(String prompt, int min, int maks) {
        while (true) {
            System.out.print(prompt);
            String masukan = scanner.nextLine().trim();
            try {
                int angka = Integer.parseInt(masukan);
                if (angka >= min && angka <= maks) {
                    return angka;
                }
                System.out.println("Masukkan angka antara " + min + " dan " + maks + ".");
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid, masukkan angka.");
            }
        }
    }

    private boolean tanyaYaTidak(String prompt) {
        while (true) {
            System.out.print(prompt);
            String jawaban = scanner.nextLine().trim().toLowerCase();
            if (jawaban.equals("y") || jawaban.equals("ya")) {
                return true;
            }
            if (jawaban.equals("n") || jawaban.equals("tidak")) {
                return false;
            }
            System.out.println("Jawab dengan y atau n.");
        }
    }
}
