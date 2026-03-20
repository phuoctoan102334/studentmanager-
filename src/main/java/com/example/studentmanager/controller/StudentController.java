package com.example.studentmanager.controller;

import com.example.studentmanager.entity.Student;
import com.example.studentmanager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller

public class StudentController {

    @Autowired
    private StudentService studentService;

  
    @GetMapping("/students")
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students"; // students.html
    }

    @GetMapping("/api/students")
    @ResponseBody
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }

    @GetMapping("/api/students/{id}")
    @ResponseBody
    public Student getById(@PathVariable int id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.orElse(null);
    }

    @GetMapping("/api/students/search")
    @ResponseBody
    public List<Student> search(@RequestParam String name) {
        return studentService.searchByName(name);
    }

    @PostMapping("/api/students")
    @ResponseBody
    public Student add(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @PostMapping("/api/students/update/{id}")
    @ResponseBody
    public Student update(@PathVariable int id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @PostMapping("/api/students/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable int id) {
        studentService.deleteStudent(id);
        return "Deleted student " + id;
    }
}