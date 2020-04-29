package ai.polito.es1;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 *
 */
@Controller
@Log
@RequiredArgsConstructor // https://www.baeldung.com/spring-injection-lombok
public class HomeController {
    private static final String COMMAND = "command";
    //    Using @RequiredArgsConstructor instead of @Autowired to avoid field injection (hidden dependencies, troublesome in testing)
    private final RegistrationManager registrationManager;

    @GetMapping("/")
    public String home(
            @ModelAttribute(COMMAND) LoginCommand lc,
            Model m
    ) {
        m.addAttribute("currentPageNav", "home");
        return "home";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute(COMMAND) LoginCommand lc,
            HttpSession session,
            RedirectAttributes ra) {
        log.info("login(POST) LoginCommand=" + lc);
        synchronized (session) {
            if (lc.getEmail() != null && registrationManager.containsKey(lc.getEmail()) && lc.getPassword() != null && registrationManager.get(lc.getEmail()).getPassword().equals(lc.getPassword())) {
                session.setAttribute("username", lc.getEmail());
                return "redirect:/private";
            }
        }
        ra.addFlashAttribute(COMMAND, lc);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        synchronized (session) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/private")
    public String privatePage(
            Model m,
            HttpSession session) {
        synchronized (session) {
            if (session.getAttribute("username") != null) {
                m.addAttribute("currentPageNav", "private");
                return "private";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registrationPage(
            @ModelAttribute(COMMAND) RegistrationCommand rc,
            Model m,
            HttpSession session) {
        synchronized (session) {
            if (session.getAttribute("username") != null) {
                return "redirect:/";
            }
        }
        m.addAttribute("currentPageNav", "register");
        m.addAttribute("minlength", RegistrationCommand.getMIN());
        log.info("registrationPage(GET) registrationCommand=" + rc);

        return "register";
    }

    /**
     * WARN: BindingResult *must* immediately follow the Command (otherwise silent non-validation).
     * https://stackoverflow.com/a/29883178/1626026
     */
    @PostMapping("/register")
    public String register(
            @ModelAttribute(COMMAND) @Valid RegistrationCommand rc,
            BindingResult bindingResult,
            Model m,
            HttpSession session) {
        synchronized (session) {
            if (session.getAttribute("username") != null) {
                return "redirect:/";
            }
        }
        m.addAttribute("currentPageNav", "register");
        m.addAttribute("minlength", RegistrationCommand.getMIN());
        log.info("register(POST) registrationCommand=" + rc.toString());
//        log.info("register(POST) " + bindingResult.toString());

        // Necessario per evitare NullPointerException quando i campi di rc sono null
        if (bindingResult.toString().contains("[null]")) {
            return "forward:/myerror";
        }
        // Check se passwords non uguali
        if (!rc.getPassword1().equals(rc.getPassword2())) {
            bindingResult.addError(new FieldError(COMMAND, "form", "passwords dont match"));
        }
        //Check se utente già presente
        if (registrationManager.containsKey(rc.getEmail())) {
            bindingResult.addError(new FieldError(COMMAND, "form", "user already registered"));
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        RegistrationDetails registrationDetails = RegistrationDetails.builder()
                                                      .name(rc.getName())
                                                      .surname(rc.getSurname())
                                                      .email(rc.getEmail())
                                                      .password(rc.getPassword1())
                                                      .registrationDate(LocalDateTime.now())
                                                      .build();
        // "usando lo username come chiave" --> dovrebbe essere il campo email
        if (registrationManager.putIfAbsent(rc.getEmail(), registrationDetails) != null) {
            // check con if superfluo, ho già controllato prima, però veniva chiesto esplicitamente nella specifica.
            // -> Non cambio ordine perchè mi sembra più chiaro separare logica di controllo errore da logica di inserimento dati,
            // anche perchè se la password è già presente avrei creato l'oggetto RegistrationDetails inutilmente
            return "register";
        }
        session.setAttribute("username", registrationDetails.getEmail());
    
        log.info("register(POST) # POST then GET #");
        return "redirect:/private";
    }

    @RequestMapping(value = {"/myerror", "/myexception"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String error() {
        return "myerror";
    }
}
