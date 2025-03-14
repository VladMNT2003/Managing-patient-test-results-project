/** Clasa pentru Controller AnalizeBuletin * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upb.proiect.demoSpring.model.AnalizaBuletin;
import upb.proiect.demoSpring.service.AnalizaBuletinService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analiza-buletin")
public class AnalizaBuletinController {

    @Autowired
    private AnalizaBuletinService analizaBuletinService;

    // Endpoint pentru vizualizarea detaliilor
    @GetMapping("/detalii-buletine")
    public String viewAnalizeBuletinDetails(Model model) {
        List<Map<String, Object>> analizeBuletinDetails = analizaBuletinService.getAnalizeBuletinWithDetails();
        model.addAttribute("analizeBuletinDetails", analizeBuletinDetails);
        return "analizeBuletinBuletineDetails"; // Rendează pagina HTML analizeBuletinBuletineDetails.html
    }

    @GetMapping("/detalii-analize")
    public String viewDetailedAnalize(Model model) {
        List<Map<String, Object>> detailedAnalize = analizaBuletinService.getDetailedAnalize();
        model.addAttribute("detailedAnalize", detailedAnalize);
        return "analizeBuletinAnalizeDetails";  // Numele fișierului HTML va fi analizeBuletinBuletineDetails.html
    }

    @GetMapping
    public String viewAnalizaBuletinPage(Model model) {
        model.addAttribute("listaAnalizaBuletin", analizaBuletinService.getAllAnalizeBuletin());
        return "analiza_buletin";
    }

    @PostMapping("/adauga")
    public String addAnalizaBuletin(
            @RequestParam("analizaId") int analizaId,
            @RequestParam("buletinId") int buletinId,
            @RequestParam("statusAnaliza") String statusAnaliza,
            @RequestParam("valoareNumerica") double valoareNumerica,
            Model model) {

        // Verificăm dacă analiza există
        boolean analizaExists = analizaBuletinService.checkIfAnalizaExists(analizaId);
        if (!analizaExists) {
            model.addAttribute("analizaError", "Analiza cu ID-ul " + analizaId + " nu există în baza de date.");
            model.addAttribute("listaAnalizaBuletin", analizaBuletinService.getAllAnalizeBuletin());
            return "analiza_buletin";  // Rămânem pe pagina de analiza_buletin cu mesajul de eroare
        }

        // Verificăm dacă buletinul există
        boolean buletinExists = analizaBuletinService.checkIfBuletinExists(buletinId);
        if (!buletinExists) {
            model.addAttribute("buletinError", "Buletinul cu ID-ul " + buletinId + " nu există în baza de date.");
            model.addAttribute("listaAnalizaBuletin", analizaBuletinService.getAllAnalizeBuletin());
            return "analiza_buletin";  // Rămânem pe pagina de analiza_buletin cu mesajul de eroare
        }

        // Dacă ambele există, adăugăm analiza buletin
        AnalizaBuletin analizaBuletin = new AnalizaBuletin();
        analizaBuletin.setAnalizaId(analizaId);
        analizaBuletin.setBuletinId(buletinId);
        analizaBuletin.setStatusAnaliza(statusAnaliza);
        analizaBuletin.setValoareNumerica(valoareNumerica);

        analizaBuletinService.addAnalizaBuletin(analizaBuletin);
        return "redirect:/analiza-buletin";  // Redirecționăm la lista de analize buletin
    }

    @GetMapping("/sterge/{analizaId}/{buletinId}")
    public String deleteAnalizaBuletin(@PathVariable("analizaId") int analizaId,
                                       @PathVariable("buletinId") int buletinId) {
        analizaBuletinService.deleteAnalizaBuletin(analizaId, buletinId);
        return "redirect:/analiza-buletin";
    }

    @GetMapping("/buletine")
    public String viewBuletineDetails(Model model) {
        List<Map<String, Object>> buletineDetails = analizaBuletinService.getBuletineDetails();
        model.addAttribute("buletineDetails", buletineDetails);
        return "detaliiBuletin";  // Vom folosi fișierul detaliiBuletin.html
    }

    @GetMapping("/analize")
    public String viewDetaliiAnalize(Model model) {
        List<Map<String, Object>> detaliiAnalize = analizaBuletinService.getDetaliiAnalize();
        model.addAttribute("detaliiAnalize", detaliiAnalize);
        return "detaliiAnalize";  // Pagina HTML pentru detalii analize
    }


}
