package de.neusta.ncc;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Redirect to swagger-ui.html.html.
 */
@Controller
public class RedirectToIndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:swagger-ui.html";
    }

}
