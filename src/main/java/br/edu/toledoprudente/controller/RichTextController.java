package br.edu.toledoprudente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.toledoprudente.pojo.Post;

@Controller
@RequestMapping("/thymeleaf")
public class RichTextController {
    @GetMapping("/thymeleafmodel")
    public String main(Model model) {
        model.addAttribute("post", new Post());
        return "/thymeleaf/thymeleafmodel";
    }

    @PostMapping("/thymeleafmodelsaved")
    public String save(Post post, Model model) {
        model.addAttribute("post", post);
        return "/thymeleaf/thymeleafmodelsaved";
    }
}
