# ⚔️ ArenaBattle

**Simulator pertarungan turn-based berbasis konsol (CLI)**
Tugas Individu — Mata Kuliah Pemrograman Berorientasi Objek (PBO)

---

## 👤 Identitas Mahasiswa

| | |
|---|---|
| **Nama** | Mikhel Febian |
| **NIM** | 2509116056 |
| **Kelas** | B 2025 |
| **Mata Kuliah** | Pemrograman Berorientasi Objek |

---

## 📖 Penjelasan Studi Kasus

**ArenaBattle** adalah simulator pertarungan turn-based (bergiliran) berbasis
konsol. Pengguna memilih satu karakter dari empat kelas yang tersedia
(Ksatria, Penyihir, Pemanah, atau Penyembuh), lalu bertarung melawan
karakter yang dikendalikan komputer (AI sederhana).

Setiap giliran, pemain memilih salah satu dari tiga aksi:

1. **Serang** — serangan dasar berdasarkan statistik karakter
2. **Skill** — kemampuan khusus yang berbeda cara kerjanya untuk setiap kelas
   (menggunakan mana)
3. **Bertahan** — memulihkan mana dan menambah pertahanan sementara

Pertarungan berlangsung sampai salah satu karakter kehabisan HP, atau
sampai mekanik *Tekanan Arena* mengakhiri duel yang berlarut-larut (mulai
giliran ke-8, kedua petarung menerima damage tambahan yang naik tiap
giliran, agar pertarungan selalu berakhir).

**Alasan pemilihan studi kasus ini:**
Studi kasus pertarungan game dipilih karena memberi alasan yang *logis*
untuk menerapkan inheritance dan polymorphism — setiap kelas karakter
benar-benar berperilaku berbeda saat menggunakan skill, bukan sekadar
label yang membedakan data. Ini membuat penerapan empat pilar OOP terlihat
jelas dalam perilaku program, bukan hanya dalam struktur datanya.

---

## 🧩 Diagram Kelas & Hierarki Inheritance

```
Karakter (abstract class)
│  - nama, hp, hpMaks, mana, manaMaks, seranganDasar, pertahananDasar (private)
│  - statusAktif : List<StatusEfek> (private)
│  + serang(target) : HasilAksi
│  + bertahan() : HasilAksi
│  + terimaSerangan(damage, tembusArmor) : int
│  + abstract getKelas() : String
│  + abstract getNamaSkill() : String
│  + abstract getBiayaMana() : int
│  + abstract gunakanSkill(target) : HasilAksi
│
├── Ksatria           → HP tinggi, pertahanan tinggi. Skill: buff pertahanan diri sendiri
├── Penyihir          → HP rendah, mana besar. Skill: damage besar menembus armor
├── Pemanah           → serangan kritis. Skill: 3x serangan beruntun
│                        implements DapatKritikal
└── Penyembuh         → mendukung. Skill: memulihkan HP diri sendiri + regenerasi
                         implements DapatMenyembuhkan
```

**Class pendukung (bukan bagian hierarki inheritance):**

| Class | Package | Fungsi |
|---|---|---|
| `HasilAksi` | `model` | Membungkus hasil dari satu aksi (deskripsi, nilai damage/heal) |
| `StatusEfek` | `model` | Buff/debuff sementara yang menempel pada karakter |
| `Pertarungan` | `logic` | Mesin giliran, AI lawan, penentu pemenang |
| `LogPertarungan` | `logic` | Menyimpan seluruh catatan jalannya duel |
| `KarakterFactory` | `logic` | Satu-satunya tempat objek karakter konkret dibuat |
| `AplikasiCLI` | `cli` | Alur menu dan interaksi dengan pengguna lewat `Scanner` |
| `TampilanUtil` | `cli` | Menggambar HP/mana bar berbasis teks |
| `Acak` | `util` | Utilitas angka acak |

Diagram detail beserta atribut/method lengkap tiap class ada di
[`docs/KONSEP-OOP.md`](docs/KONSEP-OOP.md).

---

## 🔗 Penerapan Inheritance

Relasi inheritance utama: **`Karakter` (superclass) → `Ksatria`, `Penyihir`,
`Pemanah`, `Penyembuh` (subclass)**.

Contoh konkret dari kode — konstruktor `Ksatria` memanggil `super(...)`
untuk mewarisi seluruh sistem HP/mana dari `Karakter`, lalu hanya
menambahkan perilaku skill yang spesifik untuknya:

```java
// model/Karakter.java — SUPERCLASS
public abstract class Karakter {
    private int hp;
    private int mana;
    // ... atribut dan method umum lainnya

    protected Karakter(String nama, int hpMaks, int manaMaks,
                       int seranganDasar, int pertahananDasar) {
        this.nama = nama;
        this.hp = hpMaks;
        this.mana = manaMaks;
        // ...
    }

    public HasilAksi serang(Karakter target) { /* diwarisi apa adanya */ }
    public abstract HasilAksi gunakanSkill(Karakter target); // wajib diisi subclass
}
```

```java
// model/Ksatria.java — SUBCLASS
public class Ksatria extends Karakter {

    public Ksatria(String nama) {
        super(nama, 140, 50, 14, 8);   // <- memanggil konstruktor superclass
    }

    @Override
    public HasilAksi gunakanSkill(Karakter target) {
        // perilaku unik Ksatria: menambah pertahanan diri sendiri
        tambahStatus(new StatusEfek("Tameng Baja", 2, 12, 0));
        pulihkanHp(12);
        return new HasilAksi(getNama() + " mengangkat TAMENG BAJA...", 12);
    }
    // getKelas(), getNamaSkill(), dst juga wajib diisi (abstract method)
}
```

**Yang diwarisi tanpa ditulis ulang:** atribut `hp`/`mana`/`pertahananDasar`,
method `serang()`, `bertahan()`, `terimaSerangan()`, `mulaiGiliran()`, dan
seluruh sistem status efek. Keempat subclass hanya berisi kode untuk hal
yang benar-benar berbeda dari kelas lain — ini bukti bahwa inheritance-nya
dipakai untuk menghindari duplikasi kode, bukan sekadar formalitas tugas.

Penjelasan lengkap empat pilar OOP (encapsulation, inheritance, polymorphism,
abstraction) dengan lebih banyak contoh ada di
[`docs/KONSEP-OOP.md`](docs/KONSEP-OOP.md#3-peta-konsep-oop-untuk-presentasi).

---

## 📸 Tangkapan Layar Program Berjalan

> ⚠️ **Ganti gambar di bawah ini dengan screenshot asli dari program Anda.**
> Simpan screenshot di folder `docs/screenshots/`, lalu ubah nama file
> di bawah agar sesuai. Lihat cara mengambilnya di bagian
> [Cara Mengambil Screenshot](#-cara-mengambil-screenshot).

![Contoh tampilan program berjalan](docs/screenshots/running-program.png)

---

## 🚀 Cara Menjalankan

### Lewat Apache NetBeans

1. Buka NetBeans → **File → New Project → Java Application**
2. Beri nama project, **hapus centang** "Create Main Class"
3. Salin folder [`src/arenabattle`](src/arenabattle) ke dalam folder `src` project Anda
4. Klik kanan project → **Clean and Build**
5. **Properties → Run** → set Main Class ke `arenabattle.ArenaBattle`
6. Jalankan dengan **F6**, lalu ketik jawaban di panel **Output**

### Lewat Terminal

```bash
git clone https://github.com/<username-anda>/ArenaBattle.git
cd ArenaBattle
javac -d build/classes $(find src -name "*.java")
java -cp build/classes arenabattle.ArenaBattle
```

---

## 📷 Cara Mengambil Screenshot

1. Jalankan program (lewat NetBeans atau terminal)
2. Mainkan sampai terlihat: pemilihan karakter, minimal 1 giliran pertarungan,
   dan log aksi di layar
3. Ambil screenshot:
   - **Windows:** tekan `Win + Shift + S`, pilih area, lalu tempel (paste) ke Paint dan simpan
   - **NetBeans (panel Output):** klik kanan di panel Output → pastikan seluruh log kebaca, lalu screenshot seperti biasa
4. Simpan sebagai `docs/screenshots/running-program.png`
5. Commit dan push ke GitHub

---

## ✨ Fitur

- 🎮 4 kelas karakter dengan statistik dan skill unik: **Ksatria**, **Penyihir**, **Pemanah**, **Penyembuh**
- ⚔️ Pertarungan bergiliran: Serang / Skill / Bertahan
- 🛡️ Sistem status efek sementara (buff pertahanan, regenerasi)
- 📊 HP & mana bar berbasis teks (`[####------] 40/100`)
- 📜 Log pertarungan lengkap tercetak setiap giliran
- 🤖 AI lawan sederhana yang menyesuaikan aksi berdasarkan sisa HP/mana
- ⏳ *Tekanan Arena* — mekanik penyeimbang agar pertarungan selalu berakhir
- 🔁 Opsi main lagi tanpa perlu menjalankan ulang program

---

## 🗂️ Struktur Project

```
ArenaBattle/
├── README.md                    ← Anda di sini
├── docs/
│   ├── KONSEP-OOP.md             ← penjelasan detail 4 pilar OOP
│   └── screenshots/
│       └── running-program.png   ← ganti dengan screenshot asli Anda
└── src/arenabattle/
    ├── ArenaBattle.java          ← main class
    ├── model/                    ← Karakter (abstract) + 4 subclass + 2 interface
    ├── logic/                    ← mesin pertarungan, AI, factory
    ├── cli/                      ← tampilan console (Scanner + System.out)
    └── util/                     ← utilitas angka acak
```

**14 class + 2 interface.**

---

## 🛠️ Tech Stack

- **Bahasa:** Java (JDK 17+)
- **IDE:** Apache NetBeans
- **Tampilan:** Console murni (`java.util.Scanner`, `System.out`) — tanpa GUI/library eksternal

---

## 📄 Lisensi

Project ini dibuat untuk keperluan tugas akademik.
Bebas digunakan sebagai referensi belajar dengan mencantumkan sumber.
