/** Clasa pentru Controller Analize * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upb.proiect.demoSpring.model.Analize;
import upb.proiect.demoSpring.service.AnalizeService;

import java.util.Map;
import java.util.HashMap;

import java.util.List;

@Controller
@RequestMapping("/analize")
public class AnalizeController {

    @Autowired
    private AnalizeService analizeService;

    @GetMapping
    public String viewAnalizePage(Model model) {
        model.addAttribute("listaAnalize", analizeService.getAllAnalize());
        return "analize";
    }

    @PostMapping("/adauga")
    public String addAnaliza(@RequestParam("valoareMinima") double valoareMinima,
                             @RequestParam("valoareMaxima") double valoareMaxima,
                             @RequestParam("idCategorie") int idCategorie,
                             Model model) {

        // Verificăm dacă valoarea minimă este mai mare decât valoarea maximă
        if (valoareMinima > valoareMaxima) {
            model.addAttribute("valoareError", "Valoarea minimă nu poate fi mai mare decât valoarea maximă.");
            // Adăugăm analiza deja existentă pentru a păstra tabelul vizibil
            model.addAttribute("listaAnalize", analizeService.getAllAnalize());
            return "analize"; // Rămânem pe pagina de analize cu mesajul de eroare
        }

        // Verificăm dacă categoria există
        boolean categorieExists = analizeService.checkIfCategorieExists(idCategorie);

        // Dacă categoria nu există, adăugăm un mesaj de eroare
        if (!categorieExists) {
            model.addAttribute("categorieError", "Categoria cu ID-ul " + idCategorie + " nu există în baza de date.");
            // Adăugăm în continuare lista de analize pentru a rămâne vizibilă
            model.addAttribute("listaAnalize", analizeService.getAllAnalize());
            return "analize";  // Rămâne pe pagina de adăugare analize cu tabelul complet
        }


        // Creăm obiectul Analize și folosim setterele pentru a-l popula
        Analize analiza = new Analize();
        analiza.setValoareMinima(valoareMinima);
        analiza.setValoareMaxima(valoareMaxima);
        analiza.setIdCategorie(idCategorie);

        // Adăugăm analiza în baza de date
        analizeService.addAnaliza(analiza);

        return "redirect:/analize"; // Redirecționează către lista de analize
    }

    @GetMapping("/sterge/{id}")
    public String deleteAnaliza(@PathVariable("id") int idAnaliza) {
        analizeService.deleteAnaliza(idAnaliza);
        return "redirect:/analize";
    }

    @GetMapping("/detalii")
    public String getAnalizeDetails(Model model) {
        List<Map<String, Object>> analizeDetails = analizeService.getAnalizeWithCategories();
        model.addAttribute("analizeDetails", analizeDetails);
        return "analizeDetails"; // Pagina HTML care va afișa detaliile analizei
    }
}
