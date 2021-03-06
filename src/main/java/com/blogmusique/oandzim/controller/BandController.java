package com.blogmusique.oandzim.controller;

import com.blogmusique.oandzim.entity.User;
import com.blogmusique.oandzim.entity.Band;
import com.blogmusique.oandzim.repository.BandRepository;
import com.blogmusique.oandzim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class BandController {

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/addBand")
    public String addBand(HttpSession session, Model out) {

        out.addAttribute("bandList", bandRepository.findAll());
        return "add";
    }

    @GetMapping("/addBand/list")
    public String addBandList(HttpSession session,
                              @RequestParam Long bandId) {

        User user = userRepository.findById((Long) session.getAttribute("userId")).get();

        Band band = bandRepository.findById(bandId).get();
        Set<Band> bandList = user.getBands();
        bandList.add(band);
        user.setBands(bandList);

        userRepository.save(user);

        return "redirect:/addBand";
    }

    @PostMapping("/addBand")
    public String addBandPost(HttpSession session,
                              @RequestParam String name,
                              @RequestParam String genre,
                              @RequestParam(required = false) String cover) {

        if (cover.isEmpty()) {
            cover = "/img/cover.jpg";
        }

        User user = userRepository.findById((Long) session.getAttribute("userId")).get();

        Set<Band> bandList = user.getBands();
        bandList.add(new Band(name, genre, cover));
        user.setBands(bandList);

        userRepository.save(user);

        return "redirect:/addBand";
    }

    @GetMapping("/removeBand")
    public String removeBand(HttpSession session,
                             @RequestParam Long bandId) {

        User user = userRepository.findById((Long) session.getAttribute("userId")).get();

        Set<Band> bandList = user.getBands();
        for (Band band : bandList) {
            System.out.println(band.getName().toString());
            if (band.getId() == bandId) {
                bandList.remove(band);
                break;
            }
        }

        userRepository.save(user);

        return "redirect:/list";
    }

    @GetMapping("/list")
    public String list(HttpSession session,
                       Model out) {

        User user = userRepository.findById((Long) session.getAttribute("userId")).get();
        Set<Band> bandList = user.getBands();
        out.addAttribute("bandList", bandList);
        return "list";
    }
}
