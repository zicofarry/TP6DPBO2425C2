# Janji
_Saya Muhammad 'Azmi Salam dengan NIM 2406010 mengerjakan Tugas Praktikum 6 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin_

# Deskripsi & Desain Program
Program ini merupakan implementasi permainan **Flappy Bird** yang dibangun menggunakan **Java Swing GUI**. User diarahkan untuk mengendalikan player sprite yang harus terus terbang dan menghindari rintangan yang bermunculan dari sisi kanan layar. Program ini mencakup berbagai komponen grafis untuk menampilkan player sprite, pipa/rintangan, latar belakang, serta kontrol dalam permainan.

Program terdiri dari __5__ class, yaitu `App`, `FlappyBird`, `Player`, `Pipe`, dan `Score`. Program memiliki runnable class `App` yang akan menjalankan program utama (aplikasi, dengan membuka window).

## App.java
Kelas ini adalah kelas utama yang menjalankan program dengan membuat window menggunakan `JFrame`, menambahkan panel permainan `FlappyBird`, dan mengatur properti seperti ukuran, lokasi, serta visibilitas window. Kelas ini bertindak sebagai entry point aplikasi dan belum mengandung logika permainan.

## FlappyBird.java
Kelas ini adalah kelas utama yang mengatur logika dalam permainan, termasuk rendering grafis, kontrol pemain, dan mekanisme permainan. Kelas ini merupakan turunan dari `JPanel` dan mengimplementasikan beberapa listener seperti `ActionListener`, `KeyListener`, dan `MouseListener` untuk memproses input pengguna.

Kelas ini memuat berbagai elemen permainan seperti latar belakang, pipa, player sprite, dan elemen UI lainnya, yang dirender menggunakan metode `paintComponent` dan `draw`. Logika permainan mencakup pergerakan pemain dengan gravitasi, deteksi tabrakan dengan pipa atau tanah, serta penghitungan skor saat pemain melewati pipa. Timer digunakan untuk mengatur loop permainan dan cooldown pipa, memastikan permainan berjalan dengan kecepatan konstan.

Selain itu, kelas ini juga menangani kondisi seperti memulai permainan, menjeda, dan memulai ulang melalui input keyboard atau mouse. Secara keseluruhan, `FlappyBird.java` mengelola seluruh aspek permainan, mulai dari logika hingga tampilan grafis.

## Player.java
Kelas ini adalah kelas yang merepresentasikan player sprite dalam permainan, bertanggung jawab untuk menyimpan data posisi, ukuran, gambar, dan kecepatan vertikal player sprite. Kelas ini memiliki atribut seperti `posX` dan `posY` untuk menentukan posisi player sprite, `width` dan `height` untuk ukuran, serta `image` untuk menampilkan sprite player sprite.

Selain itu, atribut `velocityY` digunakan untuk mengatur kecepatan vertikal player sprite, yang dipengaruhi oleh gravitasi atau input dari pengguna. Kelas ini menyediakan getter dan setter untuk setiap atribut, memungkinkan fleksibilitas dalam mengatur atau mengambil nilai properti player sprite. Konstruktor kelas ini digunakan untuk menginisialisasi semua atribut saat objek player sprite dibuat, termasuk posisi awal, ukuran, dan gambar sprite.

## Pipe.java
Tidak jauh berbeda dengan penjelasan kelas sebelumnya, kelas ini adalah kelas yang merepresentasikan rintangan dalam bentuk pipa-pipa yang bergerak dari kanan ke kiri layar dalam permainan. Kelas ini juga memiliki atribut seperti `posX` dan `posY` untuk menentukan posisi pipa, `width` dan `height` untuk ukuran, serta `image` untuk menampilkan gambar pipa. Atribut `velocityX` digunakan untuk mengatur kecepatan horizontal pipa, yang secara default bergerak ke kiri dengan nilai negatif.

Selain itu, atribut `passed` digunakan untuk menandai apakah pipa telah dilewati oleh pemain, yang berguna untuk penghitungan skor. Kelas ini juga menyediakan getter dan setter untuk setiap atribut dan konstruktor kelas ini digunakan untuk menginisialisasi semua atribut saat objek pipa dibuat, termasuk posisi awal, ukuran, gambar, dan kecepatan.

## Score.java
Kelas terakhir dalam penjelasan kelas ini juga merupakan kelas yang merepresentasikan objek pada permainan. pada `Score.java`, objek yang direpresentasikan adalah skor selama permainan berlangsung. Kelas ini hanya memiliki 1 atribut, yaitu `score` untuk mengatur poin dari user ketika sprite berhasil melintasi setiap pipa sebagai rintangannya.

# Penjelasan
Setelah program dijalankan, akan muncul `Jframe` window yang menampilkan menu awal dari permainan `Flappy Bird`. Pada menu awal, terdapat judul permainan, tombol untuk memulai permainan, dan instruksi untuk mengklik tombol tersebut untuk memulai permainan. Sesaat setelah klik, permainan akan dimulai dan user harus mempertahakan posisi player sprite agar tidak mencapai kondisi **game over**.

Selama permainan, user dapat menekan tombol `space` untuk membuat player sprite mengepakkan sayapnya dan melompat lebih tinggi agar tidak terjatuh. Skor selama permainan dapat dilihat di atas-tengah layar yang bertambah setiap player melewati pipa. Selain itu, player dapat menghentikan permainan sementara dengan menekan tombol `esc`, dan window akan menampilkan menu saat **game paused**. Terdapat 3 opsi, yaitu **Resume** untuk melanjutkan permainan, **Restart** untuk mengulang dari awal, dan **Main Menu** untuk kembali pada kondisi awal permainan (judul dan tombol start).

Ketika player sprite bertemu pada kondisi game over (terlalu rendah, terlalu tinggi, dan menabrak sisi pipa), maka window akan menampilkan pesan game over dan skor user selama permainan berlangsung. Selain itu, juga terdapat instruksi untuk menekan karakter `R` untuk mengulang/restart permainan.

# Dokumentasi
## Screen Record