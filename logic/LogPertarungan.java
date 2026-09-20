package arenabattle.logic;

import java.util.ArrayList;
import java.util.List;


public class LogPertarungan {

    private final List<String> baris = new ArrayList<>();

    public void tambah(String teks) {
        baris.add(teks);
    }

    public void tambahSemua(List<String> daftar) {
        baris.addAll(daftar);
    }

    public void pemisah(String judul) {
        baris.add("");
        baris.add("----- " + judul + " -----");
    }

    public String getTeks() {
        StringBuilder sb = new StringBuilder();
        for (String b : baris) {
            sb.append(b).append("\n");
        }
        return sb.toString();
    }

    public int jumlahBaris() {
        return baris.size();
    }

    public List<String> getBaris() {
        return new ArrayList<>(baris);
    }
}
