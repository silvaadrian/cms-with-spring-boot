package br.edu.toledoprudente.controller;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.toledoprudente.dao.PaginasDAO;
import br.edu.toledoprudente.dao.SitesDAO;
import br.edu.toledoprudente.dao.UsuariosDAO;
import br.edu.toledoprudente.pojo.Paginas;
import br.edu.toledoprudente.pojo.Usuarios;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/paginas")
public class PaginasController {

    @Autowired
    private PaginasDAO dao;

    @Autowired
    private UsuariosDAO usersdao;

    @Autowired
    private SitesDAO sitesdao;

    public static Validator validator;

    @GetMapping("/cadastro")
    public String main(Model model) {
        model.addAttribute("objpaginas", new Paginas());
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
                model.addAttribute("sites", usuarios.getSites());
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/paginas/cadastro";
    }

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
            if (usuarios != null && usuarios.getSites() != null) {
                model.addAttribute("lista", usuarios.getSites());
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/paginas/listar";
    }

    @PostMapping("/salvarF")
    public String salvarF(@ModelAttribute("objpaginas") Paginas objpaginas,
            BindingResult result, ModelMap model) {

        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
            Set<ConstraintViolation<Paginas>> constraintViolations = validator.validate(objpaginas);

            String errors = "";
            for (ConstraintViolation<Paginas> constraintViolation : constraintViolations) {
                errors = errors + constraintViolation.getMessage() + ". ";

            }
            if (errors != "") {
                // tem erros
                model.addAttribute("objpaginas", objpaginas);
                model.addAttribute("mensagem", errors);
                model.addAttribute("retorno", false);
                return "/paginas/cadastro";
            } else {
                if (objpaginas.getId() == null) {
                    dao.save(objpaginas);
                } else {
                    dao.update(objpaginas);
                }
                model.addAttribute("objpaginas", objpaginas);
                model.addAttribute("mensagem", "Cadastrado com sucesso!!!");
                model.addAttribute("retorno", true);
                return "/paginas/cadastroF";
            }
        }

        catch (Exception e) {
            model.addAttribute("retorno", false);
            model.addAttribute("objpaginas",
                    objpaginas);

            model.addAttribute("mensagem",
                    "Erro ao salvar. "
                            + e.getMessage());
            return "/paginas/cadastro";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("objpaginas") Paginas objpaginas,
            BindingResult result, ModelMap model) {

        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
            Set<ConstraintViolation<Paginas>> constraintViolations = validator.validate(objpaginas);

            String errors = "";
            for (ConstraintViolation<Paginas> constraintViolation : constraintViolations) {
                errors = errors + constraintViolation.getMessage() + ". ";

            }
            if (errors != "") {
                // tem erros
                model.addAttribute("objpaginas", objpaginas);
                model.addAttribute("mensagem", errors);
                model.addAttribute("retorno", false);
                return "/paginas/cadastro";
            } else {
                if (objpaginas.getId() == null) {
                    dao.save(objpaginas);
                } else {
                    dao.update(objpaginas);
                }
                model.addAttribute("objpaginas", objpaginas);
                model.addAttribute("mensagem", "Cadastrado com sucesso!!!");
                model.addAttribute("retorno", true);
                return "/paginas/cadastro";
            }
        }

        catch (Exception e) {
            model.addAttribute("retorno", false);
            model.addAttribute("objpaginas",
                    objpaginas);

            model.addAttribute("mensagem",
                    "Erro ao salvar. "
                            + e.getMessage());
            return "/paginas/cadastro";
        }
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

    @GetMapping("/view")
    public String save(@RequestParam("id") int id, Model model) {
        Paginas paginas = dao.findById(id);
        model.addAttribute(paginas);
        return "/paginas/view";
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
        return "/paginas/listarF";
    }

    @GetMapping("/cadastroF")
    public String cadastroF(Model model) {
        model.addAttribute("objpaginas", new Paginas());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();
            if (userName.equals("anonymousUser")) {
                userName = null;
            }
            model.addAttribute("sites", sitesdao.findAll());
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/paginas/cadastroF";
    }

    @GetMapping("/alterar")
    public String prealterar(@RequestParam("id") int id,
            ModelMap model) {
        model.addAttribute("objpaginas", dao.findById(id));
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
                model.addAttribute("sites", usuarios.getSites());
                model.addAttribute("usuarios", usuarios);
            }
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("role", null);
        }
        return "/paginas/alterar";
    }

    @GetMapping("/alterarF")
    public String prealterarF(@RequestParam("id") int id,
            ModelMap model) {
        model.addAttribute("objpaginas", dao.findById(id));
        model.addAttribute("sites", sitesdao.findAll());
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
        return "/paginas/alterarF";
    }

}
