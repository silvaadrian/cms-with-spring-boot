package br.edu.toledoprudente.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.toledoprudente.dao.AppAuthorityDAO;
import br.edu.toledoprudente.dao.UsuariosDAO;
import br.edu.toledoprudente.pojo.AppAuthority;
import br.edu.toledoprudente.pojo.Usuarios;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	public static Validator validator;

	@Autowired
	private UsuariosDAO dao;

	@Autowired
	private AppAuthorityDAO AppAuthorityDAO;

	@GetMapping("/cadastro")
	public String novo(ModelMap model) {

		model.addAttribute("objusuario", new Usuarios());

		return "/usuario/cadastro";
	}

	@GetMapping("/cadastroF")
	public String cadastroF(ModelMap model) {

		model.addAttribute("objusuario", new Usuarios());

		return "/usuario/cadastroF";
	}

	@GetMapping("/excluir")
	public String excluir(@RequestParam("id") int id,
			ModelMap model, HttpServletRequest request) {
		dao.delete(id);
		return redirectToCurrentPage(request);
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
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
		return "/usuario/listar";
	}

	@GetMapping("/alterar")
	public String prealterar(@RequestParam("id") int id,
			ModelMap model) {
		model.addAttribute("objusuario", dao.findById(id));
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
		return "/usuario/alterar";
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute("objusuario") Usuarios objusuario,
			BindingResult result, ModelMap model, HttpServletRequest request) {

		try {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			Set<ConstraintViolation<Usuarios>> constraintViolations = validator.validate(objusuario);

			String errors = "";
			for (ConstraintViolation<Usuarios> constraintViolation : constraintViolations) {
				errors = errors + constraintViolation.getMessage() + ". ";

			}
			if (errors != "") {
				// tem erros
				model.addAttribute("objusuario", objusuario);
				model.addAttribute("mensagem", errors);
				model.addAttribute("retorno", false);
				return redirectToCurrentPage(request);
			} else {

				String senhaCriptografada = new BCryptPasswordEncoder().encode(objusuario.getSenha());
				objusuario.setSenha(senhaCriptografada);

				LocalDate dataAtual = LocalDate.now();
				objusuario.setData_reg(dataAtual);

				if (objusuario.getId() == null) {
					String email = objusuario.getEmail();
					Usuarios usuarioExistente = dao.findByUserName(email);
					if (usuarioExistente != null) {
						model.addAttribute("objusuario", objusuario);
						model.addAttribute("mensagem", "E-mail já está em uso!!");
						model.addAttribute("retorno", false);
						return "/usuario/cadastro";
					}
				}

				if (objusuario.getAdmin() == null || objusuario.getAdmin() == false) {
					if (objusuario.getId() == null) {
						boolean admin = false;
						objusuario.setAdmin(admin);
						Set<AppAuthority> appAuthorities = new HashSet<AppAuthority>();
						AppAuthority app = new AppAuthority();
						app.setAuthority("USER");
						app.setUsername(objusuario.getEmail());
						app.setAppUser(objusuario);
						appAuthorities.add(app);
						objusuario.setAppAuthorities(appAuthorities);
					} else {
						boolean admin = false;
						objusuario.setAdmin(admin);
						AppAuthority app = AppAuthorityDAO.findByUserName(objusuario.getEmail());
						if (app != null) {
							app.setAuthority("USER");
							app.setUsername(objusuario.getEmail());
							app.setAppUser(objusuario);
							AppAuthorityDAO.update(app);
						}
					}
				} else {
					if (objusuario.getId() == null) {
						Set<AppAuthority> appAuthorities = new HashSet<AppAuthority>();
						AppAuthority app = new AppAuthority();
						app.setAuthority("ADM");
						app.setUsername(objusuario.getEmail());
						app.setAppUser(objusuario);
						appAuthorities.add(app);
						objusuario.setAppAuthorities(appAuthorities);
					} else {
						AppAuthority app = AppAuthorityDAO.findByUserName(objusuario.getEmail());
						if (app != null) {
							app.setAuthority("ADM");
							app.setUsername(objusuario.getEmail());
							app.setAppUser(objusuario);
							AppAuthorityDAO.update(app);
						}
					}
				}

				boolean ativo = true;
				objusuario.setUsuario_ativo(ativo);

				if (objusuario.getId() == null) {
					dao.save(objusuario);
				} else {
					dao.update(objusuario);
				}
				model.addAttribute("objusuario", objusuario);
				model.addAttribute("mensagem", "Cadastrado com sucesso!!!");
				model.addAttribute("retorno", true);
				return "/homeindex/login";
			}
		}

		catch (Exception e) {
			model.addAttribute("retorno", false);
			model.addAttribute("objusuario",
					objusuario);

			model.addAttribute("mensagem",
					"Erro ao salvar. "
							+ e.getMessage());
			return "/usuario/cadastro";

		}
	}

	@PostMapping("/salvarU")
	public String salvarU(@ModelAttribute("objusuario") Usuarios objusuario,
			BindingResult result, ModelMap model, HttpServletRequest request) {

		try {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			Set<ConstraintViolation<Usuarios>> constraintViolations = validator.validate(objusuario);

			String errors = "";
			for (ConstraintViolation<Usuarios> constraintViolation : constraintViolations) {
				errors = errors + constraintViolation.getMessage() + ". ";

			}
			if (errors != "") {
				// tem erros
				model.addAttribute("objusuario", objusuario);
				model.addAttribute("mensagem", errors);
				model.addAttribute("retorno", false);
				return redirectToCurrentPage(request);
			} else {

				String senhaCriptografada = new BCryptPasswordEncoder().encode(objusuario.getSenha());
				objusuario.setSenha(senhaCriptografada);

				LocalDate dataAtual = LocalDate.now();
				objusuario.setData_reg(dataAtual);

				if (objusuario.getId() == null) {
					String email = objusuario.getEmail();
					Usuarios usuarioExistente = dao.findByUserName(email);
					if (usuarioExistente != null) {
						model.addAttribute("objusuario", objusuario);
						model.addAttribute("mensagem", "E-mail já está em uso!!");
						model.addAttribute("retorno", false);
						return "/usuario/cadastro";
					}
				}

				if (objusuario.getAdmin() == null || objusuario.getAdmin() == false) {
					if (objusuario.getId() == null) {
						boolean admin = false;
						objusuario.setAdmin(admin);
						Set<AppAuthority> appAuthorities = new HashSet<AppAuthority>();
						AppAuthority app = new AppAuthority();
						app.setAuthority("USER");
						app.setUsername(objusuario.getEmail());
						app.setAppUser(objusuario);
						appAuthorities.add(app);
						objusuario.setAppAuthorities(appAuthorities);
					} else {
						AppAuthority app = AppAuthorityDAO.findByUserName(objusuario.getEmail());
						if (app != null) {
							app.setAuthority("USER");
							app.setUsername(objusuario.getEmail());
							app.setAppUser(objusuario);
							AppAuthorityDAO.update(app);
						}
					}
				} else {
					if (objusuario.getId() == null) {
						Set<AppAuthority> appAuthorities = new HashSet<AppAuthority>();
						AppAuthority app = new AppAuthority();
						app.setAuthority("ADM");
						app.setUsername(objusuario.getEmail());
						app.setAppUser(objusuario);
						appAuthorities.add(app);
						objusuario.setAppAuthorities(appAuthorities);
					} else {
						AppAuthority app = AppAuthorityDAO.findByUserName(objusuario.getEmail());
						if (app != null) {
							app.setAuthority("ADM");
							app.setUsername(objusuario.getEmail());
							app.setAppUser(objusuario);
							AppAuthorityDAO.update(app);
						}
					}
				}

				boolean ativo = true;
				objusuario.setUsuario_ativo(ativo);

				if (objusuario.getId() == null) {
					dao.save(objusuario);
				} else {
					dao.update(objusuario);
				}
				model.addAttribute("objusuario", objusuario);
				model.addAttribute("mensagem", "Cadastrado com sucesso!!!");
				model.addAttribute("retorno", true);
				return "redirect:/usuario/perfil";
			}
		}

		catch (Exception e) {
			model.addAttribute("retorno", false);
			model.addAttribute("objusuario",
					objusuario);

			model.addAttribute("mensagem",
					"Erro ao salvar. "
							+ e.getMessage());
			return "/usuario/cadastro";

		}
	}

	private String redirectToCurrentPage(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}

	@GetMapping("/perfil")
	public String perfil(ModelMap model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String userName = authentication.getName();
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			String role = authorities.iterator().next().getAuthority();
			if (userName.equals("anonymousUser")) {
				userName = null;
			}
			model.addAttribute("objusuario", dao.findByUserName(userName));
			model.addAttribute("userName", userName);
			model.addAttribute("role", role);
		} else {
			model.addAttribute("userName", null);
			model.addAttribute("role", null);
		}
		return "/usuario/perfil";
	}

	@PostMapping("/salvarF")
	public String salvarf(@ModelAttribute("objusuario") Usuarios objusuario,
			BindingResult result, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		try {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			Set<ConstraintViolation<Usuarios>> constraintViolations = validator.validate(objusuario);

			String errors = "";
			for (ConstraintViolation<Usuarios> constraintViolation : constraintViolations) {
				errors = errors + constraintViolation.getMessage() + ". ";

			}
			if (errors != "") {
				// tem erros
				redirectAttributes.addFlashAttribute("objusuario", objusuario);
				redirectAttributes.addFlashAttribute("mensagem", errors);
				redirectAttributes.addFlashAttribute("retorno", false);
				return "redirect:/usuario/cadastroF";
			} else {

				String senhaCriptografada = new BCryptPasswordEncoder().encode(objusuario.getSenha());
				objusuario.setSenha(senhaCriptografada);

				LocalDate dataAtual = LocalDate.now();
				objusuario.setData_reg(dataAtual);

				if (objusuario.getId() == null) {
					String email = objusuario.getEmail();
					Usuarios usuarioExistente = dao.findByUserName(email);
					if (usuarioExistente != null) {
						redirectAttributes.addFlashAttribute("objusuario", objusuario);
						redirectAttributes.addFlashAttribute("mensagem", "E-mail já está em uso!!");
						redirectAttributes.addFlashAttribute("retorno", false);
						return "redirect:/usuario/cadastroF";
					}
				}

				if (objusuario.getAdmin() == null || objusuario.getAdmin() == false) {
					if (objusuario.getId() == null) {
						boolean admin = false;
						objusuario.setAdmin(admin);
						Set<AppAuthority> appAuthorities = new HashSet<AppAuthority>();
						AppAuthority app = new AppAuthority();
						app.setAuthority("USER");
						app.setUsername(objusuario.getEmail());
						app.setAppUser(objusuario);
						appAuthorities.add(app);
						objusuario.setAppAuthorities(appAuthorities);
					} else {
						AppAuthority app = AppAuthorityDAO.findByUserName(objusuario.getEmail());
						if (app != null) {
							app.setAuthority("USER");
							app.setUsername(objusuario.getEmail());
							app.setAppUser(objusuario);
							AppAuthorityDAO.update(app);
						}
					}
				} else {
					if (objusuario.getId() == null) {
						Set<AppAuthority> appAuthorities = new HashSet<AppAuthority>();
						AppAuthority app = new AppAuthority();
						app.setAuthority("ADM");
						app.setUsername(objusuario.getEmail());
						app.setAppUser(objusuario);
						appAuthorities.add(app);
						objusuario.setAppAuthorities(appAuthorities);
					} else {
						AppAuthority app = AppAuthorityDAO.findByUserName(objusuario.getEmail());
						if (app != null) {
							app.setAuthority("ADM");
							app.setUsername(objusuario.getEmail());
							app.setAppUser(objusuario);
							AppAuthorityDAO.update(app);
						}
					}
				}

				boolean ativo = true;
				objusuario.setUsuario_ativo(ativo);

				if (objusuario.getId() == null) {
					dao.save(objusuario);
				} else {
					dao.update(objusuario);
				}
				redirectAttributes.addFlashAttribute("objusuario", objusuario);
				redirectAttributes.addFlashAttribute("mensagem", "Cadastrado com sucesso!!");
				redirectAttributes.addFlashAttribute("retorno", true);
				return "redirect:/usuario/listar";
			}
		}

		catch (Exception e) {
			redirectAttributes.addFlashAttribute("retorno", false);
			redirectAttributes.addFlashAttribute("objusuario",
					objusuario);

			redirectAttributes.addFlashAttribute("mensagem",
					"Erro ao salvar. "
							+ e.getMessage());
			return "redirect:/usuario/cadastroF";

		}
	}

}
