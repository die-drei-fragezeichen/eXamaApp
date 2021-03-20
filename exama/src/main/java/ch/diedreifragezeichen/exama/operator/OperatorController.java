package ch.diedreifragezeichen.exama.operator;

import java.time.*;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperatorController {

    @GetMapping("/datenAnzeige")
    public String dates(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("java8Instant", Instant.now());

        return "datenAnzeige";
    }
}
