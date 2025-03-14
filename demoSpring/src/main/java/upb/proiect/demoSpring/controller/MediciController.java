/** Clasa pentru Controller Medici * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upb.proiect.demoSpring.model.Medici;
import upb.proiect.demoSpring.model.Pacienti;
import upb.proiect.demoSpring.service.MediciService;

import java.util.List;

@Controller
@RequestMapping("/medici")
public class MediciController {

    @Autowired
    private MediciService mediciService;

    // Endpoint pentru afișarea tuturor medicilor
    @GetMapping
    public String showMediciPage(Model model) {
        model.addAttribute("medici", mediciService.getAllMedici());
        return "medici"; // Rendează pagina HTML pentru afișare
    }

    // Endpoint pentru afișarea formularului de adăugare a unui medic
    @GetMapping("/add")
    public String showAddMedicForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null && error.equals("invalid_phone")) {
            model.addAttribute("phoneError", "Numărul de telefon introdus este invalid! Vă rugăm să introduceți doar cifre.");
        }
        if (error != null && error.equals("invalid_email")) {
            model.addAttribute("emailError", "Mail-ul introdus este invalid! Vă rugăm să introduceți formatul corect.");
        }
        if (error != null && error.equals("invalid_cnp")) {
            model.addAttribute("cnpError", "CNP-ul introdus este invalid! Vă rugăm să introduceți 13 cifre.");
        }

        model.addAttribute("medici", mediciService.getAllMedici());  // Adăugăm pacienții în model pentru a fi vizibili în tabel
        return "medici"; // Afișează pagina de pacienți (cu formularul de adăugare pacient)
    }


    // Endpoint pentru a adăuga un nou medic
    @PostMapping("/add")
    public String addMedic(@RequestParam String nume,
                           @RequestParam String prenume,
                           @RequestParam String cnp,
                           @RequestParam String email,
                           @RequestParam String telefon) {

        // Validare pentru telefon (numai cifre)
        if (!telefon.matches("[0-9]+")) {
            return "redirect:/medici/add?error=invalid_phone";  // Redirecționează la formularul de adăugare cu eroare
        }

        // Validare pentru email (trebuie să aibă formatul valid: orice@orice.com)
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            return "redirect:/medici/add?error=invalid_email";  // Redirecționează la formularul de adăugare cu eroare
        }

        // Validare pentru CNP (doar cifre și lungimea de 13 caractere)
        if (!cnp.matches("[0-9]+") || cnp.length() != 13) {
            return "redirect:/medici/add?error=invalid_cnp";  // Redirecționează la formularul de adăugare cu eroare
        }

        // Creare medic
        Medici medic = new Medici();
        medic.setNume(nume);
        medic.setPrenume(prenume);
        medic.setCnp(cnp);
        medic.setEmail(email);
        medic.setTelefon(telefon);


        mediciService.addMedic(medic);
        return "redirect:/medici";
    }


    // Endpoint pentru a șterge un medic
    @PostMapping("/delete")
    public String deleteMedic(@RequestParam int idMedic) {
        mediciService.deleteMedic(idMedic);
        return "redirect:/medici";
    }

    @GetMapping("/edit/{id}")
    public String showEditMedicForm(@PathVariable("id") int id, Model model) {
        Medici medic = mediciService.getMedicById(id);
        if (medic == null) {
            return "redirect:/medici"; // Dacă medicul nu există, redirecționează la listă
        }
        model.addAttribute("medic", medic);
        return "editMedici"; // Pagina HTML pentru actualizare
    }

    @PostMapping("/update")
    public String updateMedic(@ModelAttribute Medici medic, Model model){
        // Validare pentru telefon (numai cifre)
        if (!medic.getTelefon().matches("[0-9]+")) {
            model.addAttribute("phoneError", "Numărul de telefon introdus este invalid! Vă rugăm să introduceți doar cifre.");
            model.addAttribute("medic", medic); // Pasăm pacientul înapoi la formular
            return "editMedici"; // Revine la formularul de editare
        }

        // Validare pentru email (trebuie să aibă formatul valid: orice@orice.com)
        if (!medic.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            model.addAttribute("emailError", "Mail-ul introdus este invalid! Vă rugăm să introduceți formatul corect.");
            model.addAttribute("medic", medic);
            return "editMedici";
        }

        // Validare pentru CNP (doar litere și lungimea de 13 caractere)
        if (!medic.getCnp().matches("[0-9]+") || medic.getCnp().length() != 13) {
            model.addAttribute("cnpError", "CNP-ul introdus este invalid! Vă rugăm să introduceți 13 cifre.");
            model.addAttribute("medic", medic);
            return "editMedici";
        }

        mediciService.updateMedic(medic);
        return "redirect:/medici";
    }

    // Afișează medicii care au scris cel puțin un anumit număr de trimiteri
    @GetMapping("/experientaMedici")
    public String showMediciByTrimiteri(@RequestParam("minTrimiteri") int minTrimiteri, Model model) {
        // Apelăm serviciul pentru a obține medicii care au scris cel puțin 'minTrimiteri' trimiteri
        List<Medici> mediciByTrimiteri = mediciService.getMediciByTrimiteriCount(minTrimiteri);

        // Dacă nu sunt găsiți medici, adăugăm un mesaj de eroare
        if (mediciByTrimiteri.isEmpty()) {
            model.addAttribute("error", "Nu au fost găsiți medici cu acest număr de trimiteri.");
        }

        model.addAttribute("medici", mediciByTrimiteri);
        return "experientaMedici";  // Afișează pagina cu medicii găsiți
    }
}
