package com.dbdbdeep.dbdbdeep;

import java.sql.*;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class StudentController {
    private final jdbcRepository jdbcRepository;

    @GetMapping("/student_list")
    public String list(Model model) {
        List<Student> studentList = this.jdbcRepository.getAllStudent();
        model.addAttribute("studentList", studentList);
        return "student_list";
    }

    @PostMapping("/add_student")
    public String addManager(@RequestParam(value="name") String name, @RequestParam(value="phone") String phone, @RequestParam(value="email") String email,
                                @RequestParam(value="gen_id") Integer gen_id, @RequestParam(value="mt_id") Integer mt_id, @RequestParam(value="std_vol") String std_vol) {
        this.jdbcRepository.insertStudent(name, phone, email, gen_id, mt_id, std_vol);
        return "redirect:/student_list";
    }

    @PostMapping("/del_student")
    public String deleteStudent(@RequestParam(value="studentId") Integer studentId) {
        this.jdbcRepository.deleteStudent(studentId);
        return "redirect:/student_list";
    }

    @PostMapping("/search_student")
    public String search_list(Model model, @RequestParam(value="key") String key, @RequestParam(value="data") String data) {
        List<Student> studentList = null;
        if ("std_name".equals(key)) {
            studentList = this.jdbcRepository.getNameStudent(data);
            model.addAttribute("studentList", studentList);
        }
        else if ("std_phone".equals(key)) {
            studentList = this.jdbcRepository.getPhoneStudent(data);
            model.addAttribute("studentList", studentList);
        }
        else {
            studentList = this.jdbcRepository.getMailStudent(data);
            model.addAttribute("studentList", studentList);
        }
        model.addAttribute("studentList", studentList);
        return "student_list";
    }

    @GetMapping("/student_roll")
    public String student_roll(Model model, @RequestParam(value="studentId") Integer key) {
        List<Rollbook> rollbookList = this.jdbcRepository.getRollbooksByStudentId(key);
        model.addAttribute("rollbookList", rollbookList);
        System.out.println(rollbookList);
        return "rollbook_list";
    }

}
