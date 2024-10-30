# Tarif Rehberi Uygulaması

Kullanıcıların yemek tariflerini saklayabileceği ve mevcut malzemelerle hangi yemeklerin yapılabileceğini gösteren bir masaüstü uygulaması. Bu uygulama, veritabanı yönetimi, dinamik arama ve filtreleme gibi işlevleri desteklemektedir.

## Amaç
1. Dinamik arama ve filtreleme özelliklerine sahip bir masaüstü uygulaması geliştirilmesi.
2. Uygulama içinde istenilen özellikteki ürünlerin filtrelenmesi ve sıralanması.
3. Veritabanı yönetimi ve algoritma geliştirme konularındaki becerilerin geliştirilmesi.
4. Kullanıcı arayüzü tasarımı ve kullanıcı dostu yazılım geliştirme hakkında deneyim kazandırılması.

---

## 1. Veritabanı Tasarımı
Uygulamanın veritabanı, tarifleri ve malzemeleri yönetmek için birkaç ilişkilendirilmiş tabloya sahiptir:

### Veritabanı Tabloları
- **Tarifler Tablosu**:
    - `TarifID` (Primary Key, int): Her tarif için benzersiz bir ID.
    - `TarifAdi` (varchar): Tarifin adı.
    - `Kategori` (varchar): Tarifin kategorisi (ör. Ana Yemek, Tatlı).
    - `HazirlamaSuresi` (int): Tarifin hazırlanma süresi (dakika).
    - `Talimatlar` (text): Tarifin hazırlanış adımları.
- **Malzemeler Tablosu**:
    - `MalzemeID` (Primary Key, int): Her malzeme için benzersiz bir ID.
    - `MalzemeAdi` (varchar): Malzemenin adı.
    - `ToplamMiktar` (varchar): Malzemenin toplam miktarı.
    - `MalzemeBirim` (varchar): Malzemenin birim ölçüsü (ör. kilo, litre).
    - `BirimFiyat` (float): Malzemenin birim maliyeti.
- **Tarif-Malzeme İlişkisi Tablosu**:
    - `TarifID` (Foreign Key): İlgili tarifin ID'si.
    - `MalzemeID` (Foreign Key): İlgili malzemenin ID'si.
    - `MalzemeMiktar` (float): Tarif için gerekli malzeme miktarı.

**Normalizasyon**: Tablolar, veritabanı normalizasyon kurallarına göre tasarlanmıştır. Tarif-Malzeme İlişkisi tablosu, tarifler ve malzemeler arasında many-to-many ilişkiyi temsil eder.

---

## 2. Kullanıcı Arayüzü (GUI) Tasarımı
- **Ana Ekran**: Tüm tariflerin listelendiği bir alan.
- **Menü**: Tarif ekleme, güncelleme ve silme işlemleri için seçenekler.
- **Tarif Listesi**: Tariflerin adı, hazırlama süresi ve maliyet bilgisiyle görüntülendiği alan.
- **Arama ve Filtreleme Alanı**: Tarif arama ve filtreleme işlemleri için üst kısımda yer alır.
- **Sonuç Ekranı**: Farklı kriterlerde arama sonuçlarına göre yapılabilecek yemeklerin listesi.

---

## 3. Fonksiyonel Özellikler

### Tarif Ekleme
- Kullanıcı, tarifin adı, kategorisi, hazırlama süresi ve yapılış talimatlarını ekleyebilir.
- Tarif birden fazla malzeme içerebilir; kayıtlı malzemelerden seçim yapabilir veya yeni malzeme ekleyebilir.
- "Tarif Ekle" butonuna basıldığında, tarif ve malzeme bilgileri veritabanına kaydedilir.

### Tarif Önerisi
- Eksik malzemeli tarifler kırmızı, yeterli malzemeli tarifler yeşil olarak görüntülenir.
- Kırmızı tariflerde eksik malzemelerin toplam maliyeti ayrıca hesaplanır.
- Tarif önerisi tüm arama ve filtreleme işlemlerinde aktif olarak kullanılır.

### Dinamik Arama
- **Tarif Adına Göre Arama**: Tarif adına göre uygun tarifleri listeler.
- **Malzemeye Göre Arama**: Seçilen malzemelere göre tarifler, eşleşme yüzdesine göre sıralanır.

### Filtreleme ve Sıralama
- Kullanıcı tarifleri hazırlama süresi, maliyet, malzeme sayısı, kategori veya maliyet aralığına göre filtreleyebilir.

### Tarif Güncelleme ve Silme
- Kullanıcı, tarifleri güncelleyebilir veya silebilir; bu işlemler veritabanında otomatik olarak güncellenir.

---

## Kullanılan Teknolojiler
- Programlama Dili: C# veya JAVA
- Veritabanı: İlişkisel veritabanı (ör. PostgreSQL)
- Kullanıcı Arayüzü: Masaüstü uygulama için GUI tasarımı (Swing veya benzer GUI kütüphaneleri)

---

## Kurulum
1. Projeyi klonlayın:
   ```bash
   git clone <repo-link>
