package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
        
        // Dịch các lỗi SQL phổ biến nếu nó nằm trong RuntimeException
        if (message != null && message.contains("could not execute statement")) {
            if (message.contains("CK_stu_gender")) {
                error.put("message", "Lỗi: Giới tính đã chọn không đúng quy định.");
            } else {
                error.put("message", "Không thể lưu dữ liệu vào cơ sở dữ liệu. Vui lòng kiểm tra lại thông tin.");
            }
        } else {
            error.put("message", message);
        }
        
        error.put("status", "error");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Đã xảy ra lỗi hệ thống: " + ex.getMessage());
        error.put("status", "error");
        return ResponseEntity.internalServerError().body(error);
    }
}