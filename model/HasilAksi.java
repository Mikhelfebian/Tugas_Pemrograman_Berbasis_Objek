package arenabattle.model;

public class HasilAksi {

    private final String deskripsi;
    private final int nilai;
    private final boolean kritis;

    public HasilAksi(String deskripsi, int nilai) {
        this(deskripsi, nilai, false);
    }

    public HasilAksi(String deskripsi, int nilai, boolean kritis) {
        this.deskripsi = deskripsi;
        this.nilai = nilai;
        this.kritis = kritis;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getNilai() {
        return nilai;
    }

    public boolean isKritis() {
        return kritis;
    }

    @Override
    public String toString() {
        return deskripsi;
    }
}
