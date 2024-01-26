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
public class TeacherController {
    private final jdbcRepository jdbcRepository;

    @GetMapping("/teacher_list")
    public String list(Model model) {
        List<Teacher>  teachersList = this.jdbcRepository.getAllTeacher();
        model.addAttribute("teacherList", teachersList);
        return "/teacher_list";
    }

    @PostMapping("/add_teacher")
    public String addTeacher(@RequestParam(value="name") String name, @RequestParam(value="phone") String phone, @RequestParam(value="email") String email) {
        this.jdbcRepository.insertTeacher(name, phone, email);
        return "redirect:/teacher_list";
    }

    @PostMapping("/del_teacher")
    public String deleteTeacher(@RequestParam(value="teacherId") Integer teacherId) {
        this.jdbcRepository.deleteManager(teacherId);
        return "redirect:/teacher_list";
    }

    @PostMapping("/search_teacher")
    public String search_list(Model model, @RequestParam(value="key") String key, @RequestParam(value="data") String data) {
        List<Teacher> teachersList = null;
        if ("tc_name".equals(key)) {
            teachersList = this.jdbcRepository.getNameTeacher(data);
            model.addAttribute("teacherList", teachersList);
        }
        else if ("mg_phone".equals(key)) {
            teachersList = this.jdbcRepository.getPhoneTeacher(data);
            model.addAttribute("teacherList", teachersList);
        }
        else {
            teachersList = this.jdbcRepository.getMailTeacher(data);
            model.addAttribute("teacherList", teachersList);
        }
        model.addAttribute("teacherList", teachersList);
        return "teacher_list";
    }
}