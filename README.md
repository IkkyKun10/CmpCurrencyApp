# 🪙 CmpCurrencyApp

**CmpCurrencyApp** adalah aplikasi konversi mata uang lintas platform (Android dan iOS) menggunakan **Kotlin Compose Multiplatform**, menampilkan UI komposabel dan kemampuan multiplatform Compose. Diilhami dari gaya aplikasi seperti CurrencyApp oleh lng8212.

---

## 📌 Daftar Isi

1. [Deskripsi](#deskripsi)  
2. [Fitur](#fitur)  
3. [Instalasi & Build](#instalasi--build)  
4. [Arsitektur & Struktur Proyek](#arsitektur--struktur-proyek)  
5. [Pengujian](#pengujian)  
6. [Teknologi & Dependensi](#teknologi--dependensi)  
7. [Kontribusi](#kontribusi)  
8. [Lisensi](#lisensi)  

---

## 📝 Deskripsi

Aplikasi ini memungkinkan pengguna untuk mengonversi antara berbagai mata uang secara realtime di perangkat Android dan iOS, memanfaatkan satu basis kode dengan **Kotlin Multiplatform** dan **Jetpack Compose**.

---

## ⚙️ Fitur

- Konversi mata uang antar banyak negara  
- Antarmuka yang responsif dan modern  
- Mendukung platform Android & iOS dari satu basis kode  
- Komposisi UI dengan Kotlin Compose Multiplatform  
- Internalasi & format angka modern

---

## 🧩 Instalasi & Build

### Prasyarat

- [JDK 11+](https://adoptopenjdk.net/)  
- [Android Studio Giraffe+](https://developer.android.com/studio)  
- Xcode 14+ (untuk iOS)

### Langkah Build

1. Clone repo:
   ```bash
   git clone https://github.com/IkkyKun10/CmpCurrencyApp.git
   cd CmpCurrencyApp
   ```
2. Jalankan build:
   - Android:
     ```bash
     ./gradlew :androidApp:installDebug
     ```
   - iOS:
     Buka `iosApp/` di Xcode, pilih target simulator, lalu run.

---

## 📂 Arsitektur & Struktur Proyek

```
/
|-- composeApp/          # Kode bersama (commonMain, androidMain, iosMain)
|-- androidApp/          # Entry point Android
|-- iosApp/              # Entry point iOS (SwiftUI + ComposeMultiplatform)
|-- build.gradle.kts     
|-- settings.gradle.kts  
```

- `composeApp/commonMain`: logika bisnis & domain model  
- `composeApp/androidMain`: adaptasi untuk Android  
- `composeApp/iosMain`: adaptasi untuk iOS  
- `iosApp/`: integrasi Compose dalam SwiftUI entry

---

## 🧪 Pengujian

- Unit test berada di `composeApp/test/`  
- Android UI testing menggunakan JUnit + Espresso/Kaspresso  
- iOS UI testing ditangani via Xcode testing suite

---

## 🛠️ Teknologi & Dependensi

- **Kotlin Multiplatform**
- **Jetpack Compose Multiplatform**
- **Ktor** – HTTP client
- **Realm MongoDB** Realm Database by MongoDB
- **Voyager** Compose Navigation for Kotlin Multiplatform
- **Koin** Dependency Injection
- **Kotlinx Coroutines**  
- **Kotlinx Serialization**  
- Android & SwiftUI UI integrations  

---

## 🤝 Kontribusi

Kontribusi sangat dipersilakan! Untuk kontribusi:

1. Fork repositori ini  
2. Buat branch feature: `git checkout -b feature-name`  
3. Commit perubahan  
   ```bash
   git commit -m "Menambahkan fitur X"
   ```
4. Push & kirim PR

Harap sertakan dokumentasi singkat dan tes untuk perubahan besar.

---

## 📄 Lisensi

Distribusi di bawah MIT License. Untuk informasi lengkap, baca file `LICENSE`.

---

## 🪄 Screenshots

🎥 ![Android](assets/Android.mp4)
🎥 ![IOS](assets/IOS.mp4)
