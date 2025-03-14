/** Clasa pentru Controller Trimiteri * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upb.proiect.demoSpring.service.TrimiteriService;
import upb.proiect.demoSpring.model.Trimiteri;

import java.util.Map;
import java.util.HashMap;

import java.util.List;

@Controller
@RequestMapping("/trimiteri")
public class TrimiteriController {

    @Autowired
    private TrimiteriService trimiteriService;

    // Endpoint pentru afișarea tuturor trimiterilor
    @GetMapping
    public String showAllTrimiteri(Model model) {
        List<Trimiteri> trimiteriList = trimiteriService.getAllTrimiteri();
        model.addAttribute("trimiteri", trimiteriList);
        return "trimiteri"; // Rendează pagina HTML trimiteri.html
    }

    // Endpoint pentru afișarea formularului de adăugare
    @GetMapping("/add")
    public String showAddForm() {
        return "addTrimiteri";
    }

    // Endpoint pentru adăugarea unei trimiteri
    @PostMapping("/add")
    public String addTrimitere(
            @RequestParam("dataEmiterii") String dataEmiterii,
            @RequestParam("pacientId") Long pacientId,
            @RequestParam("medicId") Long medicId,
            Model model) {

        // Verificăm dacă pacientul există
        boolean pacientExists = trimiteriService.checkIfPacientExists(pacientId.intValue());

        // Verificăm dacă medicul există
        boolean medicExists = trimiteriService.checkIfMedicExists(medicId.intValue());

        // Dacă pacientul sau medicul nu există, adăugăm un mesaj de eroare
        if (!pacientExists) {
            model.addAttribute("pacientError", "Pacientul cu ID-ul " + pacientId + " nu există în baza de date.");
        }
        if (!medicExists) {
            model.addAttribute("medicError", "Medicul cu ID-ul " + medicId + " nu există în baza de date.");
        }

        // Afișăm toate trimiterile, chiar și în caz de eroare
        List<Trimiteri> trimiteriList = trimiteriService.getAllTrimiteri();
        model.addAttribute("trimiteri", trimiteriList);

        // Dacă pacientul și medicul există, adăugăm trimiterea
        if (pacientExists && medicExists) {
            Trimiteri trimitere = new Trimiteri(null, dataEmiterii, pacientId, medicId);
            trimiteriService.addTrimitere(trimitere);
            return "redirect:/trimiteri"; // Redirecționează către lista de trimiteri
        }

        return "trimiteri"; // Rămâne pe pagina de trimiteri
    }





    // Endpoint pentru ștergerea unei trimiteri
    @GetMapping("/delete/{id}")
    public String deleteTrimitere(@PathVariable("id") Integer id) {
        trimiteriService.deleteTrimitere(id);
        return "redirect:/trimiteri";
    }

    @GetMapping("/detalii")
    public String getTrimiteriDetails(Model model) {
        List<Map<String, Object>> trimiteriDetails = trimiteriService.getTrimiteriWithDetails();
        model.addAttribute("trimiteriDetails", trimiteriDetails);
        return "trimiteriDetails";
    }

}
