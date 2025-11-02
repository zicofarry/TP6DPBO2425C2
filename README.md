# Janji
_Saya Muhammad 'Azmi Salam dengan NIM 2406010 mengerjakan Tugas Praktikum 6 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin_

# Deskripsi & Desain Program
Program ini merupakan implementasi permainan **Flappy Bird** yang dibangun menggunakan **Java Swing GUI**, dengan arsitektur yang memisahkan antara logic (game mechanics) dan view (tampilan grafis).
Pemain mengendalikan karakter utama agar terus melayang sambil menghindari pipa-pipa yang bergerak dari sisi kanan layar. Program ini mencakup pengaturan elemen grafis seperti latar belakang, sprite pemain, pipa, serta sistem kontrol dan skor.

Program terdiri dari __5__ class, yaitu `App`, `Logic`, `Player`, `Pipe`, dan `View`. Program memiliki runnable class `App` yang akan menjalankan program utama (aplikasi, dengan membuka window).

## App.java
Kelas ini adalah kelas utama yang menjalankan program dengan membuat window menggunakan `JFrame`, menambahkan panel permainan `FlappyBird`, dan mengatur properti seperti ukuran, lokasi, serta visibilitas window. Kelas ini bertindak sebagai entry point aplikasi dan belum mengandung logika permainan.

## Logic.java
Kelas ini menangani **inti logika permainan**, seperti pergerakan pemain, deteksi tabrakan, pembaruan skor, serta pengaturan status permainan (running, paused, game over).

Logic bertanggung jawab untuk:
- Mengatur posisi dan kecepatan semua entitas (player, pipe).
- Mengatur gravitasi dan kontrol input.
- Menghitung skor saat pemain berhasil melewati pipa.

Menentukan kapan permainan berakhir (collision).

Kelas ini tidak melakukan rendering apa pun, tetapi menyediakan data dan status yang dibutuhkan oleh View.

## View.java
Kelas View menangani **tampilan dan interaksi pengguna**, sebagai turunan dari JPanel.
Ia berkomunikasi dengan GameLogic untuk menampilkan posisi terbaru dari pemain, pipa, skor, serta kondisi permainan.
Tanggung jawab GameView mencakup:
- Menggambar seluruh elemen permainan di layar menggunakan paintComponent.
- Menangani input pengguna melalui KeyListener dan MouseListener.
- Menampilkan menu utama, tampilan pause, dan tampilan game over.

Selain itu, View juga mengatur main menu dan tampilan game saat running.

## Player.java
Kelas ini adalah kelas yang merepresentasikan player sprite dalam permainan, bertanggung jawab untuk menyimpan data posisi, ukuran, gambar, dan kecepatan vertikal player sprite. Kelas ini memiliki atribut seperti `posX` dan `posY` untuk menentukan posisi player sprite, `width` dan `height` untuk ukuran, serta `image` untuk menampilkan sprite player sprite.

Selain itu, atribut `velocityY` digunakan untuk mengatur kecepatan vertikal player sprite, yang dipengaruhi oleh gravitasi atau input dari pengguna. Kelas ini menyediakan getter dan setter untuk setiap atribut, memungkinkan fleksibilitas dalam mengatur atau mengambil nilai properti player sprite. Konstruktor kelas ini digunakan untuk menginisialisasi semua atribut saat objek player sprite dibuat, termasuk posisi awal, ukuran, dan gambar sprite.

## Pipe.java
Tidak jauh berbeda dengan penjelasan kelas sebelumnya, kelas ini adalah kelas yang merepresentasikan rintangan dalam bentuk pipa-pipa yang bergerak dari kanan ke kiri layar dalam permainan. Kelas ini juga memiliki atribut seperti `posX` dan `posY` untuk menentukan posisi pipa, `width` dan `height` untuk ukuran, serta `image` untuk menampilkan gambar pipa. Atribut `velocityX` digunakan untuk mengatur kecepatan horizontal pipa, yang secara default bergerak ke kiri dengan nilai negatif.

Selain itu, atribut `passed` digunakan untuk menandai apakah pipa telah dilewati oleh pemain, yang berguna untuk penghitungan skor. Kelas ini juga menyediakan getter dan setter untuk setiap atribut dan konstruktor kelas ini digunakan untuk menginisialisasi semua atribut saat objek pipa dibuat, termasuk posisi awal, ukuran, gambar, dan kecepatan.

# Penjelasan
Setelah dijalankan, akan muncul jendela permainan dengan **menu utama**.
Tombol “Start” digunakan untuk memulai permainan, di mana pemain harus menjaga karakter agar tidak menabrak pipa atau jatuh ke tanah.

Selama permainan:

* Tekan **Spasi** → karakter naik.
* Tekan **ESC** → jeda permainan (pause overlay muncul).
* Tekan **R** → restart permainan setelah game over.

Ketika **game over**, layar akan menampilkan pesan akhir dan skor terakhir pemain. Menu **pause** menyediakan tiga pilihan:

* **Resume** → melanjutkan permainan.
* **Restart** → memulai ulang dari awal.
* **Main Menu** → kembali ke tampilan awal.

# Dokumentasi
## Screen Record
https://github.com/user-attachments/assets/4d16f2fa-9033-43d6-86f1-4dfa4b1c8931

