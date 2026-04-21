package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((org.springframework.validation.FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            error.put(fieldName, errorMessage);
        });
        error.put("status", "error");
        error.put("message", "Dữ liệu không hợp lệ");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();
        
        if (message != null && message.contains("java.time.LocalDateTime")) {
            error.put("message", "Định dạng thời gian không hợp lệ. Vui lòng kiểm tra lại các trường ngày tháng.");
        } else if (message != null && message.contains("JSON parse error")) {
            error.put("message", "Dữ liệu gửi lên không đúng định dạng yêu cầu.");
        } else {
            error.put("message", "Máy chủ không thể đọc được dữ liệu bạn gửi.");
        }
        
        error.put("status", "error");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMostSpecificCause().getMessage();
        
        if (message.contains("CK_stu_gender")) {
            error.put("message", "Giá trị giới tính không hợp lệ. Vui lòng chọn lại.");
        } else if (message.contains("UK_") || message.contains("duplicate key")) {
            if (message.contains("code")) error.put("message", "Mã số sinh viên này đã tồn tại.");
            else if (message.contains("personal_identification_number")) error.put("message", "Số CCCD này đã tồn tại trên hệ thống.");
            else error.put("message", "Dữ liệu bị trùng lặp với một bản ghi khác.");
        } else if (message.contains("FOREIGN KEY")) {
            error.put("message", "Không thể thực hiện do dữ liệu liên quan không tồn tại.");
        } else {
            error.put("message", "Lỗi vi phạm quy tắc dữ liệu: " + message);
        }
        
        error.put("status", "error");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();
        
        // Dịch các lỗi SQL/JPA phổ biến sang tiếng Việt
        if (message != null) {
            if (message.contains("Query did not return a unique result") || message.contains("NonUniqueResultException")) {
                error.put("message", "Lỗi dữ liệu hệ thống: Tìm thấy nhiều hơn một bản ghi cho yêu cầu này. Vui lòng liên hệ quản trị viên để chuẩn hóa dữ liệu.");
            } else if (message.contains("could not execute statement")) {
                if (message.contains("CK_stu_gender")) {
                    error.put("message", "Lỗi: Giá trị giới tính không đúng định dạng cho phép.");
                } else if (message.contains("UK_") || message.contains("duplicate key") || message.contains("Duplicate entry")) {
                    error.put("message", "Dữ liệu bị trùng lặp: Thông tin này đã tồn tại trên hệ thống (ví dụ: Mã số hoặc Số định danh đã được sử dụng).");
                } else if (message.contains("FOREIGN KEY") || message.contains("violates foreign key constraint")) {
                    error.put("message", "Không thể thực hiện do dữ liệu này đang được liên kết với các thông tin khác trong hệ thống.");
                } else {
                    error.put("message", "Không thể thực hiện yêu cầu này do vi phạm quy tắc ràng buộc dữ liệu.");
                }
            } else if (message.contains("EntityNotFoundException") || message.contains("No static resource") || message.contains("not found")) {
                if (message.contains("Student")) error.put("message", "Không tìm thấy thông tin sinh viên yêu cầu.");
                else if (message.contains("Class")) error.put("message", "Không tìm thấy thông tin lớp học yêu cầu.");
                else error.put("message", "Không tìm thấy tài nguyên hoặc dữ liệu yêu cầu trên hệ thống.");
            } else if (message.contains("OptimisticLockException") || message.contains("stale data")) {
                error.put("message", "Dữ liệu đã được thay đổi bởi một người dùng khác. Vui lòng tải lại trang và thử lại.");
            } else if (message.contains("Access is denied") || message.contains("AccessDeniedException")) {
                error.put("message", "Bạn không có quyền thực hiện chức năng này.");
            } else {
                error.put("message", message);
            }
        } else {
            error.put("message", "Đã xảy ra lỗi thực thi không xác định trên máy chủ.");
        }
        
        error.put("status", "error");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();
        
        if (message != null) {
            if (message.contains("Query did not return a unique result")) {
                error.put("message", "Lỗi hệ thống: Dữ liệu trả về không duy nhất. Vui lòng liên hệ bộ phận kỹ thuật.");
            } else if (message.contains("Connection refused") || message.contains("Communications link failure")) {
                error.put("message", "Lỗi kết nối cơ sở dữ liệu. Vui lòng kiểm tra lại trạng thái máy chủ.");
            } else {
                error.put("message", "Đã xảy ra lỗi hệ thống không mong muốn. Vui lòng thử lại sau.");
            }
        } else {
            error.put("message", "Lỗi hệ thống không xác định.");
        }
        
        error.put("status", "error");
        return ResponseEntity.internalServerError().body(error);
    }
}