# HR Management System - Kurulum Kılavuzu

Bu proje, bir İK Yönetim Sistemi'nin frontend (Ionic/Angular) ve backend (Spring Boot) uygulamalarını içermektedir.

## Gereksinimler

### Frontend için:
- Node.js (v14 veya üzeri)
- npm (Node Package Manager)
- Ionic CLI (`npm install -g @ionic/cli`)

### Backend için:
- Java JDK 17
- Gradle 7.6 veya üzeri

## Backend Kurulumu

1. **Proje Dizinine Git:**
   ```bash
   cd backend
   ```

2. **Gradle Wrapper ile Projeyi Derle:**
   ```bash
   # Windows için:
   gradlew.bat build

   # Linux/MacOS için:
   ./gradlew build
   ```

3. **Uygulamayı Başlat:**
   ```bash
   # Windows için:
   gradlew.bat bootRun

   # Linux/MacOS için:
   ./gradlew bootRun
   ```

   Backend uygulaması `http://localhost:8080` adresinde çalışacaktır.

### Önemli Backend Notları:
- Uygulama H2 veritabanı kullanmaktadır ve `./hrdb` konumunda otomatik olarak oluşturulacaktır
- CV dosyaları `./uploads/cvs` dizininde saklanacaktır
- H2 Console'a `http://localhost:8080/h2-console` adresinden erişilebilir
- API endpoint'leri `http://localhost:8080/api/candidates` altında bulunmaktadır

## Frontend Kurulumu

1. **Proje Dizinine Git:**
   ```bash
   cd frontend
   ```

2. **Bağımlılıkları Yükle:**
   ```bash
   npm install
   ```

3. **Uygulamayı Başlat:**
   ```bash
   ionic serve
   ```

   Frontend uygulaması `http://localhost:8100` adresinde çalışacaktır.

### Önemli Frontend Notları:
- Uygulama Ionic/Angular framework'ü kullanmaktadır
- Backend bağlantısı `http://localhost:8080` adresine yapılandırılmıştır
- Arayüz responsive tasarıma sahiptir ve mobil cihazlarda da çalışır

## Uygulama Özellikleri

- Aday listesi görüntüleme
- Yeni aday ekleme
- Aday silme
- CV yükleme ve indirme
- Adayları filtreleme:
  - İsim
  - Pozisyon
  - Askerlik durumu
  - İhbar süresi

## Sorun Giderme

### Backend için:
- Port 8080 kullanımda ise: `application.properties` dosyasından `server.port` değerini değiştirin
- Gradle derlemesi başarısız olursa: Gradle cache'ini temizleyin:
  ```bash
  # Windows için:
  gradlew.bat clean

  # Linux/MacOS için:
  ./gradlew clean
  ```

### Frontend için:
- Port 8100 kullanımda ise: Farklı bir port ile başlatın:
  ```bash
  ionic serve --port 8101
  ```
- `npm install` hata verirse:
  ```bash
  npm cache clean --force
  rm -rf node_modules
  npm install
  ```

## Güvenlik Notları

- Backend CORS yapılandırması sadece `localhost:8100` ve `localhost:8101` için izin vermektedir
- Dosya yükleme limiti 10MB ile sınırlandırılmıştır.


