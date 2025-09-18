package br.edu.toledoprudente.controller;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import br.edu.toledoprudente.dao.UsuariosDAO;
import br.edu.toledoprudente.pojo.Sites;
import br.edu.toledoprudente.pojo.Usuarios;
import br.edu.toledoprudente.dao.SitesDAO;

@Controller
@RequestMapping("/sites")
public class SitesController {

    public static Validator validator;

    @Autowired
    private SitesDAO dao;

    @Autowired
    private UsuariosDAO usersdao;

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();
            if (userName.equals("anonymousUser")) {
                userName = null;
            }
            Usuarios usuarios = usersdao.findByUserName(userName);
            if (usuarios != null) {
                model.addAttribute("lista", usuarios.getSites());
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/sites/listar";
    }

    @GetMapping("/listarF")
    public String listarF(ModelMap model) {
        model.addAttribute("lista",
                dao.findAll());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();
            if (userName.equals("anonymousUser")) {
                userName = null;
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/sites/listarF";
    }

    @GetMapping("/excluir")
    public String excluir(@RequestParam("id") int id,
            ModelMap model, HttpServletRequest request) {
        dao.delete(id);
        return redirectToCurrentPage(request);
    }

    private String redirectToCurrentPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/cadastroF")
    public String cadastroF(ModelMap model) {
        model.addAttribute("objsite", new Sites());
        model.addAttribute("usuarios", usersdao.findAll());
        return "/sites/cadastroF";
    }

    @GetMapping("/cadastro")
    public String cadastro(ModelMap model) {
        model.addAttribute("objsite", new Sites());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();
            if (userName.equals("anonymousUser")) {
                userName = null;
            }
            Usuarios usuarios = usersdao.findByUserName(userName);
            if (usuarios != null) {
                model.addAttribute("usuarios", usuarios);
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/sites/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("objsite") Sites objsite,
            BindingResult result, ModelMap model, HttpServletRequest request) {

        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
            Set<ConstraintViolation<Sites>> constraintViolations = validator.validate(objsite);

            String errors = "";
            for (ConstraintViolation<Sites> constraintViolation : constraintViolations) {
                errors = errors + constraintViolation.getMessage() + ". ";

            }
            if (errors != "") {
                // tem erros
                model.addAttribute("objsite", objsite);
                model.addAttribute("mensagem", errors);
                model.addAttribute("retorno", false);
                return redirectToCurrentPage(request);
            } else {
                if (objsite.getId() == null) {
                    dao.save(objsite);
                } else {
                    dao.update(objsite);
                }
                model.addAttribute("objsite", objsite);
                model.addAttribute("mensagem", "Cadastrado com sucesso!!!");
                model.addAttribute("retorno", true);
                return "/sites/cadastro";
            }
        }

        catch (Exception e) {
            model.addAttribute("retorno", false);
            model.addAttribute("objsite",
                    objsite);

            model.addAttribute("mensagem",
                    "Erro ao salvar. "
                            + e.getMessage());
            return redirectToCurrentPage(request);
        }
    }

    @GetMapping("/alterar")
    public String prealterar(@RequestParam("id") int id,
            ModelMap model) {
        model.addAttribute("objsite", dao.findById(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();
            if (userName.equals("anonymousUser")) {
                userName = null;
            }
            Usuarios usuarios = usersdao.findByUserName(userName);
            if (usuarios != null) {
                model.addAttribute("usuarios", usuarios);
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/sites/alterar";
    }

    @GetMapping("/alterarF")
    public String prealterarF(@RequestParam("id") int id,
            ModelMap model) {
        model.addAttribute("objsite", dao.findById(id));
        model.addAttribute("usuarios", usersdao.findAll());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();
            if (userName.equals("anonymousUser")) {
                userName = null;
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/sites/alterarF";
    }
}
