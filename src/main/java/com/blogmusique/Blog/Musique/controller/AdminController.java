package com.blogmusique.Blog.Musique.controller;

import com.blogmusique.Blog.Musique.entity.Band;
import com.blogmusique.Blog.Musique.entity.User;
import com.blogmusique.Blog.Musique.repository.BandRepository;
import com.blogmusique.Blog.Musique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BandRepository bandRepository;

    @GetMapping("/admin")
    public String admin(HttpSession session, Model out) {

        List<User> userList = new ArrayList<>();
        for (User user : userRepository.findAllByRoleEquals("user")) {
            userList.add(user);
        }

        out.addAttribute("userList", userList);
        return "admin-users";
    }

    @GetMapping("/admin/delete")
    public String deleteUser(HttpSession session,
                             @RequestParam(required = false) Long idUser) {

        userRepository.deleteById(idUser);
        return "redirect:/admin";
    }

    @GetMapping("/admin/disable")
    public String disableUser(HttpSession session,
                              @RequestParam(required = false) Long idUser) {

        User user = userRepository.findById(idUser).get();
        user.setActive(false);
        userRepository.save(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/activate")
    public String activateUser(HttpSession session,
                              @RequestParam(required = false) Long idUser) {

        User user = userRepository.findById(idUser).get();
        user.setActive(true);
        userRepository.save(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/groups")
    public String listGroups(HttpSession session, Model out) {

        List<Band> bands = bandRepository.findAll();

        out.addAttribute("bandList", bands);

        return "admin-groups";
    }

    @GetMapping("/admin/groups/update")
    public String updateGroup(HttpSession session,
                              Model out,
                              @RequestParam Long bandId) {

        Band band = bandRepository.findById(bandId).get();

        out.addAttribute("band", band);

        return "admin-update";
    }

    @PostMapping("/admin/groups/update")
    public String updateGroupForm(HttpSession session,
                              @RequestParam(required = false) String cover,
                              @RequestParam(required = false) String genre,
                              @RequestParam(required = false) String name,
                              @RequestParam Long id) {

        Band band = bandRepository.findById(id).get();

        band.setCover(cover);
        band.setGenre(genre);
        band.setName(name);

        bandRepository.save(band);

        return "redirect:/admin/groups";
    }

    @GetMapping("/admin/groups/delete")
    public String deleteBand(HttpSession session,
                             @RequestParam Long bandId) {

        Band band = bandRepository.findById(bandId).get();

        bandRepository.delete(band);

        return "redirect:/admin/groups";
    }
}
