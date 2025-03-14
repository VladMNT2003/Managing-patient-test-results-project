/** Clasa pentru Controller BuletineDeAnaliza * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upb.proiect.demoSpring.model.BuletinDeAnalize;
import upb.proiect.demoSpring.service.BuletineDeAnalizeService;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/buletine")
public class BuletineDeAnalizeController {

    @Autowired
    private BuletineDeAnalizeService buletineService;

    @GetMapping
    public String viewBuletinePage(Model model) {
        model.addAttribute("listaBuletine", buletineService.getAllBuletine());
        return "buletine";
    }

    @PostMapping("/adauga")
    public String addBuletin(
            @RequestParam("dataEmiterii") String dataEmiterii,
            @RequestParam("dataRecoltarii") String dataRecoltarii,
            @RequestParam("trimitereId") int trimitereId,
            Model model) {

        // Verificăm dacă data emiterii este mai mare decât data recolterii
        if (java.sql.Date.valueOf(dataEmiterii).after(java.sql.Date.valueOf(dataRecoltarii))) {
            model.addAttribute("dataError", "Data emiterii nu poate fi mai mare decât data recoltării.");
            // Adăugăm lista de buletine pentru a rămâne vizibilă
            model.addAttribute("listaBuletine", buletineService.getAllBuletine());
            return "buletine";  // Rămânem pe pagina de buletine cu mesajul de eroare
        }

        // Verificăm dacă trimiteria există
        boolean trimitereExists = buletineService.checkIfTrimitereExists(trimitereId);

        if (!trimitereExists) {
            model.addAttribute("trimitereError", "Trimiterea cu ID-ul " + trimitereId + " nu există în baza de date.");
            // Adăugăm lista de buletine pentru a rămâne vizibilă
            model.addAttribute("listaBuletine", buletineService.getAllBuletine());
            return "buletine";  // Rămânem pe pagina de buletine cu mesajul de eroare
        }

        BuletinDeAnalize buletin = new BuletinDeAnalize();
        buletin.setDataEmiterii(java.sql.Date.valueOf(dataEmiterii));
        buletin.setDataRecoltarii(java.sql.Date.valueOf(dataRecoltarii));
        buletin.setTrimitereId(trimitereId);

        buletineService.addBuletin(buletin);
        return "redirect:/buletine";  // Redirecționăm la lista de buletine
    }

    @GetMapping("/sterge/{id}")
    public String deleteBuletin(@PathVariable("id") int idBuletin) {
        buletineService.deleteBuletin(idBuletin);
        return "redirect:/buletine";
    }

    @GetMapping("/detalii")
    public String getBuletineDetails(Model model) {
        List<Map<String, Object>> buletineDetails = buletineService.getBuletineWithTrimiteri();
        model.addAttribute("buletineDetails", buletineDetails);
        return "buletineDetails"; // Rendează pagina HTML buletineDetails.html
    }



}
