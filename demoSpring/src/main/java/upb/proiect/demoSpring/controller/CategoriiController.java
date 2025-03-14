/** Clasa pentru Controller Categorii * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upb.proiect.demoSpring.model.Categorie;
import upb.proiect.demoSpring.service.CategoriiService;

@Controller
@RequestMapping("/categorii")
public class CategoriiController {

    @Autowired
    private CategoriiService categoriiService;

    @GetMapping
    public String viewCategoriiPage(Model model) {
        model.addAttribute("listaCategorii", categoriiService.getAllCategorii());
        return "categorii";
    }

    @PostMapping("/adauga")
    public String addCategorie(@ModelAttribute Categorie categorie) {
        categoriiService.addCategorie(categorie);
        return "redirect:/categorii";
    }

    @GetMapping("/sterge/{id}")
    public String deleteCategorie(@PathVariable("id") int idCategorie) {
        categoriiService.deleteCategorie(idCategorie);
        return "redirect:/categorii";
    }

}
