# Hệ thống Quản lý Vườn thú (Zoo Management System)

Ứng dụng console Java quản lý động vật, loài, khu vực sống và thông tin chăm sóc tại vườn thú. Thiết kế theo mô hình MVC, áp dụng đầy đủ các nguyên lý OOP: trừu tượng hóa, kế thừa, đa hình, đóng gói.

## Nhóm 6

| MSSV | Họ và tên |
|---|---|
| K25DTCN108 | Lương Thế Tài |
| B24DTCN038 | Nguyễn Thị Thuý Uyên |
| B25DTCN040 | Nguyễn Trung Hiếu |
| K25DTCN266 | Huỳnh Lê Hiếu Nam |

## Chức năng

- Thêm động vật mới
- Xóa động vật
- Sửa thông tin động vật
- Tìm kiếm động vật (theo mã / tên / loài / khu vực sống / tình trạng sức khỏe)
- Hiển thị danh sách động vật
- Lưu dữ liệu ra file CSV
- Tải dữ liệu từ file CSV

## Cấu trúc thư mục

```
src/zoo/
├── model/       # Thực thể domain: Animal (abstract), Mammal, Bird, Reptile, Habitat,
│                #   FeedingSchedule, Gender, HealthStatus, AnimalType
├── repository/  # Lưu trữ trong bộ nhớ: AnimalRepository (interface), InMemoryAnimalRepository
├── file/        # Đọc/ghi file: AnimalFileHandler (interface), CsvAnimalFileHandler, LoadResult
├── service/     # Nghiệp vụ: ZooService (validate, điều phối repository + file handler)
├── ui/          # Console I/O: ConsoleIO (menu chính + I/O tổng quát), AnimalPrompter
│                #   (toàn bộ use case Animal: add/delete/edit/search/list/save/load),
│                #   ZooApplication (main, composition root)
└── exception/   # ValidationException
```

## Kiến trúc

- **Model** không phụ thuộc bất kỳ lớp nào khác — thuần domain.
- **Service** (`ZooService`) chỉ phụ thuộc interface (`AnimalRepository`, `AnimalFileHandler`), không phụ thuộc implementation cụ thể → dễ thay thế (ví dụ đổi CSV sang JSON, đổi in-memory sang database) mà không sửa service.
- **UI** tách 2 lớp: `ConsoleIO` (menu chính + I/O tổng quát: đọc/hiển thị, không biết gì về Animal), `AnimalPrompter` (điều phối toàn bộ use case Animal — add/delete/edit/search/list/save/load — phụ thuộc `ConsoleIO` để I/O và `ZooService` để gọi nghiệp vụ).
- **ZooApplication.main** chỉ là composition root + vòng lặp điều phối `ConsoleIO`/`AnimalPrompter`, không gọi trực tiếp `ZooService` hay thao tác `Animal`.

## OOP áp dụng

| Nguyên lý | Áp dụng |
|---|---|
| Trừu tượng hóa | `Animal` là lớp abstract, khai báo method trừu tượng `getType()`, `getSpecies()`, `getDietInfo()`, `getExtraFieldForCsv()` |
| Kế thừa | `Mammal`, `Bird`, `Reptile` kế thừa `Animal` |
| Đa hình | Gọi `getDietInfo()`/`getSpecies()` qua tham chiếu `Animal`, thực thi khác nhau tùy subclass thực tế |
| Đóng gói | Thuộc tính private, truy cập qua getter/setter có validate |

## Validation

- Mã động vật (`Animal.id`) bắt buộc và duy nhất; tên, ngày sinh, tên/loại khu vực sống, loại thức ăn không được để trống.
- Ngày sinh không được ở tương lai.
- Độ dài lông (Mammal), sải cánh (Bird) không được âm; sức chứa khu vực sống và số lần cho ăn mỗi ngày phải lớn hơn 0.
- Vi phạm validation ném `ValidationException`, được bắt ở `ZooApplication` và hiển thị thông báo lỗi, không làm crash chương trình.

## Định dạng file CSV

Mỗi dòng là 1 động vật, cột `type` (MAMMAL/BIRD/REPTILE) quyết định subclass khi load:

```
id,type,name,gender,dob,habitatId,habitatName,habitatType,habitatCapacity,healthStatus,foodType,timesPerDay,feedingTimes,extraField
```

- `feedingTimes`: nhiều giờ ăn phân cách bởi `;` (vd `08:00;16:00`)
- `extraField`: thuộc tính riêng từng loại — furLengthCm (Mammal) / wingspanCm (Bird) / venomous (Reptile)

File mẫu: [animals_sample.csv](animals_sample.csv) (10+ dòng dữ liệu mẫu).

Khi tải file (`CsvAnimalFileHandler.load` trả về `LoadResult`): dòng nào sai định dạng hoặc trùng mã động vật với dòng trước đó trong cùng file sẽ bị **bỏ qua** (không làm hỏng cả lần tải), và được liệt kê lại kèm số dòng + lý do để người dùng biết.

## Biên dịch và chạy

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -cp out zoo.ui.ZooApplication
```

Khi chọn lưu/tải file, nhập đường dẫn tương đối theo working directory đang chạy `java`, ví dụ `animals_sample.csv`.
