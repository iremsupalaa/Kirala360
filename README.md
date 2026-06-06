# 🚗 Kirala360 — Araç Kiralama Yönetim Sistemi

Kirala360, **Java 11+** ve **Java Swing** teknolojileri kullanılarak Component-Driven (Bileşen Tabanlı) tasarım yaklaşımıyla geliştirilmiş, modern arayüze sahip bir masaüstü araç kiralama yönetim sistemidir. 

Proje; veri modelleri, iş mantığı ve görsel arayüz katmanlarının birbirinden tamamen soyutlandığı **Katmanlı Mimari (Layered Architecture)** prensiplerine sadık kalınarak inşa edilmiştir.

---

## ✨ Özellikler

- **Gelişmiş Araç Yönetimi (CRUD):** Araç ekleme, görüntüle, form doldurarak düzenleme ve silme işlemleri. Her işlem sonrası veriler otomatik olarak diske kaydedilir.
- **Şanzıman Sınıflandırması:** `MANUEL` ve `OTOMATIK` enum tipleri ile araç sınıflandırma. Tabloda dinamik sütun ve filtreleme panelinde dropdown desteği.
- **Müşteri Tabanlı Kiralama & İade:** ID ile araç seçimi, müşteri bilgileri ve gün sayısı girişiyle kiralama süreci. Kiralama anında araç durumunun otomatik yönetimi (`musaitMi` durum kontrolü).
- **Esnek Filtreleme & Arama:** Marka/model metin arama, min-max fiyat aralığı, şanzıman türü ve uygunluk durumuna (Tüm/Müsait/Kirada) göre anlık filtreleme.
- **Özel Grafik & İstatistik Paneli:** `paintComponent()` override edilerek sıfırdan çizilmiş Donut Grafik (Müsait/Kirada Oranı), Sütun Grafik (Fiyat Dağılımı), ilerleme çubukları ve animasyonlu özet metrik kartları.
- **Güvenli Kullanıcı Giriş Sistemi:** Dosya tabanlı kimlik doğrulama, 3 kez üst üste yanlış girişte **30 saniyelik akıllı geri sayım kilidi** ve `DISPOSE_ON_CLOSE` ile JVM seviyesinde güvenli pencere yönetimi.
- **Kullanıcı Yönetim Paneli:** Sistem yöneticileri için şifre güncelleme, yeni kullanıcı ekleme ve silme diyalogu (Admin hesabı silmeye karşı korumalıdır).

---

## 🛠️ Uygulanan Yazılım Mühendisliği ve OOP Prensipleri

- **Component-Driven Tasarım:** Standart Swing bileşenleri doğrudan kullanılmamış; `JButton`, `JTextField` ve `JTable` gibi yapılar genişletilerek (`inheritance`) projeye özgü `GradientButton`, `ModernTextField` ve `ModernTable` gibi modern bileşenler üretilmiştir.
- **Factory Design Pattern:** `UIFactory` sınıfı aracılığıyla arayüz ekranlarının ihtiyaç duyduğu özel bileşenlerin üretimi tek bir fabrikadan soyutlanarak yönetilmiştir.
- **Geriye Dönük Uyumluluk (Backward Compatibility):** Veri tabanında (`araclar.txt`) yeni eklenen şanzıman kolonu olmasa dahi, eski formattaki dosyalar otomatik tespit edilerek sistem çökmeden varsayılan değerle (`MANUEL`) güvenle yüklenir.
- **Veri Güvenliği (Immutability):** Sıralama algoritmalarında orijinal listenin bozulmaması adına `ArrayList` kopyaları üzerinden işlem yapılmıştır.
- **Loose Coupling (Gevşek Bağlılık):** Tablo içi aksiyon butonları (`ActionCellEditor/Renderer`), tetiklenecek fonksiyonları ana ekrana (`MainFrame`) Lambda ifadeleri (Callback) aracılığıyla iletir.

---

## 📊 Algoritma ve Veri Yapıları Analizi

Projede ham veri listelerinin yönetiminde dinamik `ArrayList` yapısı kullanılmış olup, arama ve listeleme performansını artırmak amacıyla iki adet özel sıralama algoritması sıfırdan implemente edilmiştir:

1. **Bubble Sort (Fiyata Göre):** Günlük kiralama ücretine göre artan veya azalan sıralama gerçekleştirir. Zaman karmaşıklığı: En kötü ve ortalama durumda $\mathcal{O}(n^2)$'dir.
2. **Insertion Sort (Markaya Göre):** Marka adına göre alfabetik (A-Z / Z-A) sıralama yapar. Kararlı (stable) bir algoritmadır; markaların eşitliği durumunda ikincil kriter olarak **modele göre** sıralama derinliği sunar. Zaman karmaşıklığı: En iyi durumda $\mathcal{O}(n)$, ortalama ve en kötü durumda $\mathcal{O}(n^2)$'dir.


