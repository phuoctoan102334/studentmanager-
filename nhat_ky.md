# Nhật ký thay đổi

1. **Thời điểm thực hiện:** 21/04/2026
   - **Yêu cầu:** Sửa các lỗi hiển thị trên trang Cố vấn học tập (Advisor Class Section).
   - **Đã sửa / thêm / xóa những file nào:**
     - Sửa `src/main/java/com/example/demo/dto/advisorsection/AdvisorSectionListDTO.java`: Thêm các trường `isActive`, `createdAt`, `createdBy`, `updatedAt`, `updatedBy`.
     - Sửa `src/main/java/com/example/demo/service/impl/StudentClassAdvisorServiceImpl.java`: Cập nhật mapper để điền đầy đủ thông tin hệ thống.
     - Sửa `src/main/java/com/example/demo/repository/AdvisorClassSectionRepository.java`: Sử dụng `JOIN FETCH` để tải trước thông tin Nhân viên và Lớp học, tránh lỗi không hiển thị tên.
     - Sửa `src/main/resources/static/advisor_sections.html`: Định dạng lại ngày bắt đầu, xử lý hiển thị "Hiện tại" cho ngày kết thúc, và hiển thị các trường thông tin hệ thống trong modal chi tiết.
   - **Kết quả sau khi hoàn thành:** Các vấn đề về hiển thị tên, định dạng ngày tháng và thiếu thông tin lịch sử hệ thống đã được giải quyết.

2. **Thời điểm thực hiện:** 21/04/2026
   - **Yêu cầu:** Sửa các lỗi trong file `bug.txt`.
   - **Đã sửa / thêm / xóa những file nào:**
     - `src/main/java/com/example/demo/dto/studentsection/StudentSectionDTO.java`: Thêm trường `trainingProgramName`.
     - `src/main/java/com/example/demo/repository/StudentClassSectionRepository.java`: Cập nhật `JOIN FETCH` cho `TrainingProgram`.
     - `src/main/java/com/example/demo/service/impl/StudentClassSectionServiceImpl.java`: Map thêm `trainingProgramName`.
     - `src/main/resources/static/student_sections.html`: Thêm cột Chương trình học, định dạng ngày tháng và tối ưu hiệu năng render dropdown.
     - `src/main/java/com/example/demo/controller/DiscoveryController.java`: Tối ưu API lấy danh mục để tăng tốc độ load web.
     - `src/main/java/com/example/demo/repository/StudentClassRepository.java`: Thêm `JOIN FETCH` cho `employee` và `department`.
     - `src/main/java/com/example/demo/service/impl/StudentClassServiceImpl.java`: Map thêm trường `advisorName` trong DTO.
     - `src/main/resources/static/classes.html`: Tối ưu hiệu năng dropdown.
   - **Kết quả sau khi hoàn thành:** Sửa xong lỗi `undefined`, xóa được phần giờ thừa trong ngày tháng, hiển thị đầy đủ chương trình học và cố vấn lớp hành chính, đồng thời giảm giật lag toàn hệ thống.

3. **Thời điểm thực hiện:** 21/04/2026
   - **Yêu cầu:** Tối ưu tốc độ load danh sách sinh viên.
   - **Đã sửa / thêm / xóa những file nào:**
     - Sửa `src/main/resources/static/students.html`: Áp dụng `DocumentFragment` trong hàm `renderTable` để tối ưu hiệu năng render DOM, thêm trạng thái Loading và xử lý lỗi chuyên nghiệp.
   - **Kết quả sau khi hoàn thành:** Danh sách sinh viên hiển thị mượt mà, không còn hiện tượng giật lag khi có nhiều dữ liệu, trải nghiệm người dùng tốt hơn với thông báo trạng thái rõ ràng.

4. **Thời điểm thực hiện:** 21/04/2026
   - **Yêu cầu:** Giải quyết triệt để lỗi không hiển thị Chương trình học ở Phân lớp Sinh viên.
   - **Đã sửa / thêm / xóa những file nào:**
     - Sửa `src/main/java/com/example/demo/dto/studentsection/StudentSectionDTO.java`: Thêm constructor cho Projection.
     - Sửa `src/main/java/com/example/demo/repository/StudentClassSectionRepository.java`: Áp dụng **Constructor Projection** trong JPQL để lấy dữ liệu trực tiếp, bỏ qua tầng Hibernate Proxy.
     - Sửa `src/main/java/com/example/demo/service/impl/StudentClassSectionServiceImpl.java`: Đơn giản hóa Service, sử dụng trực tiếp kết quả từ Repository.
   - **Kết quả sau khi hoàn thành:** Chương trình học luôn hiển thị chính xác theo dữ liệu thực tế trong Database.
\
---
1. Thời điểm: 21/04/2026 15:29:09
   - Yêu cầu: Đọc file bug.txt và sửa các lỗi cấu hình Maven và Null-safety.
   - Đã sửa: StudentClassServiceImpl.java, StudentServiceImpl.java.
   - Kết quả: Đã xóa các suppression null không an toàn, thêm kiểm tra null-check tường minh. Chạy build Maven thành công (BUILD SUCCESS).\
\
---
5. Thời điểm: 21/04/2026 15:38:03
   - Yêu cầu: Fix lỗi ReferenceError khi nhấn nút chỉnh sửa ở trang Phân công cố vấn.
   - Đã sửa: advisor_sections.html.
   - Kết quả: Đã định nghĩa các hàm JS còn thiếu, hoàn thiện logic CRUD (Thêm/Sửa) cho phân công cố vấn.

---
6. Thời điểm: 21/04/2026 15:39:39
   - Yêu cầu: Khắc phục lỗi null pointer (style) và undefined function trong advisor_sections.html.
   - Đã sửa: advisor_sections.html (Thêm Error Board HTML).
   - Kết quả: Đã sửa lỗi crash JS khi mở modal, hoàn thiện hệ thống báo lỗi cho phân công cố vấn.\
---
7. Thời điểm: 21/04/2026 16:04:06
   - Yêu cầu: Khắc phục lỗi hiển thị mã nguồn (corrupted code) trên giao diện người dùng.
   - Đã sửa: src/main/resources/static/advisor_sections.html.
   - Kết quả: Đã loại bỏ phần mã lặp và hỏng sau thẻ </html>, giải quyết triệt để lỗi hiển thị nội dung lạ trên frontend.
---
8. Thời điểm: 21/04/2026 16:07:19
   - Yêu cầu: Fix lỗi JS 'Uncaught TypeError' trong file bug.txt.
   - Đã sửa: src/main/resources/static/advisor_sections.html.
   - Kết quả: Đã thêm phần tử advisor-error-board bị thiếu vào HTML, giúp hàm clearErrors() và showErrors() hoạt động bình thường, không còn crash khi nhấn Thêm/Sửa.
---
9. Thời điểm: 21/04/2026 16:12:33
   - Yêu cầu: Tối ưu hóa hiệu năng nghiệp vụ chuyển lớp trong student_sections.html.
   - Đã sửa: src/main/resources/static/student_sections.html.
   - Kết quả: Áp dụng caching cho danh sách sinh viên và lớp học, sử dụng DocumentFragment để render bảng, giảm thiểu độ trễ khi mở modal và thực hiện nghiệp vụ.
---
10. Thời điểm: 21/04/2026 16:16:08
   - Yêu cầu: Tối ưu cơ chế chọn sinh viên trong nghiệp vụ chuyển lớp.
   - Đã sửa: src/main/resources/static/student_sections.html.
   - Kết quả: Dropdown chọn sinh viên hiện chỉ hiển thị sinh viên thuộc lớp đang xem. Loại bỏ lớp hiện tại khỏi danh sách lớp đích để tránh thao tác nhầm.
---
11. Thời điểm: 21/04/2026 16:28:31
   - Yêu cầu: Dịch các thông báo lỗi kỹ thuật (Backend) sang tiếng Việt.
   - Đã sửa: src/main/java/com/example/demo/exception/GlobalExceptionHandler.java.
   - Kết quả: Các lỗi hệ thống như 'Query did not return a unique result', lỗi trùng lặp dữ liệu (Duplicate key), lỗi khóa dữ liệu (Optimistic Lock), và các lỗi JPA/Hibernate phổ biến khác đã được bắt và chuyển thành thông báo tiếng Việt dễ hiểu cho người dùng cuối.
---
12. Thời điểm: 21/04/2026 16:32:28
   - Yêu cầu: Khắc phục lỗi 'Query did not return a unique result' tại nghiệp vụ chuyển sinh viên.
   - Đã sửa: StudentClassSectionRepository.java, StudentServiceImpl.java.
   - Kết quả: Đã chuyển truy vấn tìm bản ghi active sang dạng List để tránh crash khi dữ liệu có nhiều bản ghi treo. Cập nhật logic để đóng toàn bộ các bản ghi cũ trước khi tạo bản ghi phân lớp mới.
---
13. Thời điểm: 21/04/2026 16:37:20
   - Yêu cầu: Fix các lỗi (Severity 8) trong file bug.txt sau khi thay đổi kiểu dữ liệu Repository.
   - Đã sửa: StudentServiceTest.java, StudentClassSectionRepository.java, StudentServiceImpl.java.
   - Kết quả: Đã cập nhật Unit Test để khớp với kiểu trả về List thay vì Optional. Loại bỏ các import không còn sử dụng. Đã ưu tiên sửa các lỗi nghiêm trọng, các cảnh báo (warnings) sẽ được xử lý sau.
