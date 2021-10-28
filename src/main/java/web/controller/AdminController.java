package web.controller;

import web.model.User;
import web.service.RoleService;
import web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping()
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("admin")
    public String pageForAdmin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("admin/new")
    public String pageCreateUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles",roleService.getAllRoles());
        return "create";
    }

    @PostMapping("admin/new")
    public String pageCreate(@ModelAttribute("user")
                             @Valid User user, BindingResult bindingResult,
                             @RequestParam("listRoles") ArrayList<Long> roles) {
        if (bindingResult.hasErrors()) {
            return "create";
        } else {
            user.setRoles(roleService.findByIdRoles(roles));
            userService.addUser(user);
            return "redirect:/admin";
        }
    }

    @DeleteMapping("admin/delete/{id}")
    public String pageDelete(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/edit/{id}")
    public String pageEditUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user",userService.getUserById(id));
        model.addAttribute("listRoles", roleService.getAllRoles());
        return "edit";
    }

    @PutMapping("admin/edit")
    public String pageEdit(@Valid User user, BindingResult bindingResult,
                           @RequestParam("listRoles") ArrayList<Long>roles) {
        if (bindingResult.hasErrors()) {
            return "edit";
        } else {
            user.setRoles(roleService.findByIdRoles(roles));
            userService.updateUser(user);
            return "redirect:/admin";
        }
    }
}