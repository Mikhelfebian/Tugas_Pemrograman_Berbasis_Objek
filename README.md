# ArenaBattle

Tugas Individu Mata Kuliah Pemrograman Berorientasi Objek (PBO)

## Identitas

- Nama : Mikhel Febian
- NIM : 2509116056
- Kelas : B

## Studi Kasus

Aplikasi ini adalah simulasi pertarungan turn-based berbasis console (CLI).
Pengguna memilih satu karakter (Ksatria, Penyihir, Pemanah, atau Penyembuh),
lalu bertarung melawan karakter yang dikendalikan komputer secara bergantian
giliran. Setiap giliran pemain bisa memilih Serang, Skill, atau Bertahan.
Pertarungan selesai ketika salah satu karakter kehabisan HP.

Tema ini dipilih karena tiap kelas karakter punya cara kerja skill yang
benar-benar berbeda, jadi penerapan inheritance dan polymorphism-nya masuk
akal dan tidak dipaksakan.

## Hierarki Class

```
Karakter (abstract)
├── Ksatria
├── Penyihir
├── Pemanah
└── Penyembuh
```

`Karakter` adalah superclass yang menyimpan atribut dan method yang sama
untuk semua jenis karakter (HP, mana, serang, bertahan). Empat subclass di
atas mewarisi semua itu, lalu masing-masing punya implementasi skill sendiri
lewat method `gunakanSkill()`.

Class lain di luar hierarki ini:
- `Pertarungan` — mengatur jalannya giliran dan AI lawan
- `KarakterFactory` — membuat objek karakter
- `AplikasiCLI` — tampilan dan input di console

## Penerapan Inheritance

Contoh: `Ksatria` mewarisi `Karakter`.

```java
// Karakter.java (superclass)
public abstract class Karakter {
    private int hp;
    private int mana;

    protected Karakter(String nama, int hpMaks, int manaMaks,
                        int serangan, int pertahanan) {
        this.hp = hpMaks;
        this.mana = manaMaks;
        // ...
    }

    public HasilAksi serang(Karakter target) { ... }
    public abstract HasilAksi gunakanSkill(Karakter target);
}
```

```java
// Ksatria.java (subclass)
public class Ksatria extends Karakter {

    public Ksatria(String nama) {
        super(nama, 140, 50, 14, 8);
    }

    @Override
    public HasilAksi gunakanSkill(Karakter target) {
        tambahStatus(new StatusEfek("Tameng Baja", 2, 12, 0));
        pulihkanHp(12);
        return new HasilAksi(getNama() + " memakai Tameng Baja", 12);
    }
}
```

Method `serang()`, `bertahan()`, dan atribut HP/mana tidak ditulis ulang di
`Ksatria` karena sudah diwarisi dari `Karakter`. Yang ditulis di subclass
hanya bagian yang memang beda, yaitu `gunakanSkill()`.

## Screenshot Program

<img width="1083" height="617" alt="image" src="https://github.com/user-attachments/assets/b2ab7d21-f8a0-4ba5-8b56-ffddf1b35262" />

<img width="1071" height="648" alt="image" src="https://github.com/user-attachments/assets/8481bb05-bf42-4456-a8fd-d517373adcc9" />


*(ganti gambar di atas dengan screenshot hasil menjalankan program sendiri)*

## Cara Menjalankan

Lewat NetBeans:
1. Buat project baru → Java Application, jangan bikin Main Class otomatis
2. Copy folder `src/arenabattle` ke folder `src` project
3. Set Main Class ke `arenabattle.ArenaBattle` di Properties > Run
4. Jalankan dengan F6

Lewat terminal:
```
javac -d build/classes $(find src -name "*.java")
java -cp build/classes arenabattle.ArenaBattle
```

## Struktur Folder

```
ArenaBattle/
├── README.md
└── src/arenabattle/
    ├── ArenaBattle.java   (main class)
    ├── model/             (Karakter dan subclass-nya)
    ├── logic/             (aturan pertarungan)
    ├── cli/                (tampilan console)
    └── util/
```
