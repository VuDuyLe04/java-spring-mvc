package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUsers = this.userService.getAllUsersByEmail("levdhe187349@fpt.edu.vn ");
        System.out.println(arrUsers);
        model.addAttribute("eric", "test");
        model.addAttribute("hoidanit", "from controller with model");
        return "hello";
    }

    @RequestMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping("/admin/user")
    public String getTableUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        System.out.println(users);
        model.addAttribute("users", users);
        return "admin/user/user";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        System.out.println("Check path id =" + id);
        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "admin/user/show";
    }

    // Update
    @RequestMapping("/admin/user/update/{id}") // get
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("newUser", user);
        model.addAttribute("id", id);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update") // get
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        User user = this.userService.getUserById(hoidanit.getId());
        model.addAttribute("newUser", user);
        if (user != null) {
            user.setPhone(hoidanit.getPhone());
            user.setFullName(hoidanit.getFullName());
            user.setAddress(hoidanit.getAddress());
            this.userService.handleSaveUser(user);
        }
        return "redirect:/admin/user";
    }

    // Delete
    @GetMapping("/admin/user/delete/{id}") // get
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete") // get
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        this.userService.deleteUserById(hoidanit.getId());
        return "redirect:/admin/user";
    }

    // Save new user
    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String requestMethodName(Model model, @ModelAttribute("newUser") User hoidanit) {
        this.userService.handleSaveUser(hoidanit);

        return "redirect:/admin/user";
    }

}
