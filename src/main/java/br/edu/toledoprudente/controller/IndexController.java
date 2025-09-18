package br.edu.toledoprudente.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.toledoprudente.pojo.Index;

@Controller
@RequestMapping("/homeindex")
public class IndexController {

	@GetMapping("/index")
	public String i(ModelMap model) {
		model.addAttribute("homeindex", new Index());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String userName = authentication.getName();
			if (userName.equals("anonymousUser")) {
				userName = null;
			}
			model.addAttribute("userName", userName);
		} else {
			model.addAttribute("userName", null);
		}
		return "/homeindex/index";
	}

	@GetMapping("/login")
	public String login() {
		return "/homeindex/login";
	}

	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		model.addAttribute("mensagem", "Usuário ou senha inválidos!!");
		model.addAttribute("retorno", true);
		return "/homeindex/login";
	}

	@GetMapping("/logout")
	public String logout() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/homeindex/login";
	}
}
