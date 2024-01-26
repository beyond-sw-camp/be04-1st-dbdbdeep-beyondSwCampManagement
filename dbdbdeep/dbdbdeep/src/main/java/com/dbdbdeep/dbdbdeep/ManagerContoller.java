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
public class ManagerContoller {

    private final jdbcRepository jdbcRepository;

    @GetMapping("/manager_list")
    public String list(Model model) {
        List<Manager> managerList = this.jdbcRepository.getAllManagers();
        model.addAttribute("managerList", managerList);
        return "manager_list";
    }

    @PostMapping("/add_manager")
    public String addManager(@RequestParam(value="name") String name, @RequestParam(value="phone") String phone, @RequestParam(value="email") String email) {
        this.jdbcRepository.insertManager(name, phone, email);
        return "redirect:/manager_list";
    }

    @PostMapping("/del_manager")
    public String deleteManager(@RequestParam(value="managerId") Integer managerId) {
        this.jdbcRepository.deleteManager(managerId);
        return "redirect:/manager_list";
    }

    @PostMapping("/search_manager")
    public String search_list(Model model, @RequestParam(value="key") String key, @RequestParam(value="data") String data) {
        List<Manager> managerList = null;
        if ("mg_name".equals(key)) {
            managerList = this.jdbcRepository.getNameManagers(data);
            model.addAttribute("managerList", managerList);
        }
        else if ("mg_phone".equals(key)) {
            managerList = this.jdbcRepository.getPhoneManagers(data);
            model.addAttribute("managerList", managerList);
        }
        else {
            managerList = this.jdbcRepository.getMailManagers(data);
            model.addAttribute("managerList", managerList);
        }
        model.addAttribute("managerList", managerList);
        return "manager_list";
    }
}

