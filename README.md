Küçük bir İnsan Kaynakları Yönetim Sistemi (Java Backend + Angular Frontend) gerekli olan;

Proje Gereksinimleri:

1. Genel Açıklama:

Bir işe alım yönetim sistemi tasarlayın. Sistemde:

Kayıtlı adayların bir listesini görüntüleyebileceğiniz bir sayfa olsun.
Yeni aday ekleyebileceğiniz, mevcut adayları düzenleyip silebileceğiniz bir sistem oluşturun.
Backend tarafında Java kullanılarak bir REST API geliştirilecek, veriler bir veritabanında saklanacaktır.
2. Teknik Gereksinimler:

Backend:

Java 8 veya üstü kullanılarak geliştirilmelidir.
Framework: Spring Boot.
Veritabanı: H2 (geliştirme sırasında) veya PostgreSQL (opsiyonel).
CRUD işlemleri için REST API oluşturulmalıdır.
Adayları listeleme, ekleme, düzenleme ve silme işlemleri yapılabilir olmalıdır.
Frontend:

Ionic Framework kullanılarak geliştirilmelidir.
Backend'deki REST API'ye bağlanarak gerekli işlemleri yapmalıdır.
Minimum 10 kayıt ile başlamalı ve kullanıcılar yeni adaylar ekleyebilmelidir.
3. Detaylar:

Backend (Java + Spring Boot):

Aday Tablosu (Candidate):
id: Benzersiz kimlik.
firstName: Ad.
lastName: Soyad.
position: Başvurduğu pozisyon.
militaryStatus: Askerlik durumu (tamamlandı/muaf/tecilli).
noticePeriod: İhbar süresi (örneğin: 2 hafta/1 ay).
phone: Telefon numarası.
email: Eposta adresi.
cv: CV dosya adı ya da yolu.
CRUD API:
Aday ekleme: POST /api/candidates.
Aday listeleme: GET /api/candidates.
Aday güncelleme: PUT /api/candidates/{id}.
Aday silme: DELETE /api/candidates/{id}.
Filtreleme: Pozisyon, askerlik durumu ve ihbar süresine göre sorgular yapılabilir.
Veritabanı:
H2: Geliştirme sırasında kullanılabilir.
PostgreSQL: Opsiyonel olarak tercih edilebilir, PostgreSQL script dosyası paylaşılabilir.
Frontend (Angular + Ionic):

Aday Listesi:
Kayıtlı adayların bir listesi görüntülenir.
Kullanıcı, pozisyon, askerlik durumu ve ihbar süresine göre listeyi filtreleyebilir.
Yeni Aday Ekle/Düzenle Formu:
Kullanıcı ad, soyad, başvurduğu pozisyon, askerlik durumu, ihbar süresi, telefon ve e-posta bilgilerini girebilir.
CV yüklenebilir.
Form kaydedildiğinde, veri backend'e gönderilir ve liste güncellenir.
Silme İşlemi:
Her adayın yanında bir "Sil" butonu bulunmalıdır.
4. Teslimat Koşulları:

Proje, bir GitHub deposu üzerinden paylaşılmalıdır.
Backend ve frontend kodları ayrı klasörler içinde düzenlenmelidir.
README dosyasında şu bilgiler yer almalıdır:
Projenin nasıl çalıştırılacağı.
Veritabanı ayarları (PostgreSQL kullanılacaksa gerekli bilgiler).
API uç noktaları (endpoint) listesi.
5. Bonus (Opsiyonel):

Validation: Formlardaki veri girişleri için Angular reactive forms ve backend tarafında Spring Validation kullanabilirsiniz.
Frontend Tasarımı: Mobil uyumlu bir arayüz oluşturulabilir.