/** Clasa pentru Controller Pacienti * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import upb.proiect.demoSpring.model.Pacienti;
import upb.proiect.demoSpring.service.PacientiService;

import java.util.List;

@Controller
@RequestMapping("/pacienti")
public class PacientiController {

    @Autowired
    private PacientiService pacientiService;

    // Afișează toți pacienții
    @GetMapping
    public String showPacienti(Model model) {
        model.addAttribute("pacienti", pacientiService.getAllPacienti());
        return "pacienti";
    }

    // Afișează formularul de adăugare a unui pacient
    @GetMapping("/add")
    public String showAddPacientForm(@RequestParam(value = "error", required = false) String error, Model model) {

        if (error != null && error.equals("invalid_phone")) {
            model.addAttribute("phoneError", "Numărul de telefon introdus este invalid! Vă rugăm să introduceți doar cifre.");
        }
        if (error != null && error.equals("invalid_email")) {
            model.addAttribute("emailError", "Mail-ul introdus este invalid! Vă rugăm să introduceți formatul corect.");
        }
        if (error != null && error.equals("invalid_cnp")) {
            model.addAttribute("cnpError", "CNP-ul introdus este invalid! Vă rugăm să introduceți 13 cifre.");
        }
        if (error != null && error.equals("invalid_sex")) {
            model.addAttribute("sexError", "Sexul introdus este invalid! Vă rugăm să introduceți 'M' sau 'F'.");
        }

        model.addAttribute("pacienti", pacientiService.getAllPacienti());  // Adăugăm pacienții în model pentru a fi vizibili în tabel
        return "pacienti"; // Afișează pagina de pacienți (cu formularul de adăugare pacient)
    }

    // Adaugă un pacient nou în baza de date
    @PostMapping("/add")
    public String addPacient(
            @RequestParam("nume") String nume,
            @RequestParam("prenume") String prenume,
            @RequestParam("cnp") String cnp,
            @RequestParam("varsta") Integer varsta,
            @RequestParam("email") String email,
            @RequestParam("telefon") String telefon,
            @RequestParam("parolaCont") String parolaCont,
            @RequestParam("sex") String sex) {

        // Validare pentru telefon (numai cifre)
        if (!telefon.matches("[0-9]+")) {
            return "redirect:/pacienti/add?error=invalid_phone";
        }

        // Validare pentru email (trebuie să aibă formatul valid: orice@orice.com)
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            return "redirect:/pacienti/add?error=invalid_email";
        }

        // Validare pentru CNP (doar litere și lungimea de 13 caractere)
        if (!cnp.matches("[0-9]+") || cnp.length() != 13) {
            return "redirect:/pacienti/add?error=invalid_cnp";
        }

        // Validare pentru Sex (trebuie să fie 'M' sau 'F')
        if (!sex.equals("M") && !sex.equals("F")) {
            return "redirect:/pacienti/add?error=invalid_sex"; // Rămâne pe formularul de adăugare pacient
        }

        Pacienti pacient = new Pacienti();
        pacient.setNume(nume);
        pacient.setPrenume(prenume);
        pacient.setCnp(cnp);
        pacient.setVarsta(varsta);
        pacient.setEmail(email);
        pacient.setTelefon(telefon);
        pacient.setParolaCont(parolaCont);
        pacient.setSex(sex);

        pacientiService.addPacient(pacient);
        return "redirect:/pacienti";
    }

    // Șterge un pacient după ID
    @GetMapping("/delete/{id}")
    public String deletePacient(@PathVariable("id") Integer id) {
        pacientiService.deletePacient(id);
        return "redirect:/pacienti";
    }

    // Afișează formularul de editare a unui pacient
    @GetMapping("/edit/{id}")
    public String showEditPacientForm(@PathVariable("id") Integer id, Model model) {
        Pacienti pacient = pacientiService.getPacientById(id);
        if (pacient == null) {
            return "redirect:/pacienti"; // Dacă pacientul nu există, redirecționează
        }
        model.addAttribute("pacient", pacient);
        return "editPacienti"; // Afișează formularul de editare
    }

    // Actualizează un pacient
    @PostMapping("/update")
    public String updatePacient(@ModelAttribute Pacienti pacient, Model model) {
        // Validare pentru telefon (numai cifre)
        if (!pacient.getTelefon().matches("[0-9]+")) {
            model.addAttribute("phoneError", "Numărul de telefon introdus este invalid! Vă rugăm să introduceți doar cifre.");
            model.addAttribute("pacient", pacient); // Pasăm pacientul înapoi la formular
            return "editPacienti"; // Revine la formularul de editare
        }

        // Validare pentru email (trebuie să aibă formatul valid: orice@orice.com)
        if (!pacient.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            model.addAttribute("emailError", "Mail-ul introdus este invalid! Vă rugăm să introduceți formatul corect. Mesaj modificat!!!");
            model.addAttribute("pacient", pacient);
            return "editPacienti";
        }

        // Validare pentru CNP (doar litere și lungimea de 13 caractere)
        if (!pacient.getCnp().matches("[0-9]+") || pacient.getCnp().length() != 13) {
            model.addAttribute("cnpError", "CNP-ul introdus este invalid! Vă rugăm să introduceți 13 cifre.");
            model.addAttribute("pacient", pacient);
            return "editPacienti";
        }

        // Validare pentru Sex (trebuie să fie 'M' sau 'F')
        if (!pacient.getSex().equals("M") && !pacient.getSex().equals("F")) {
            model.addAttribute("sexError", "Sexul introdus este invalid! Vă rugăm să introduceți 'M' sau 'F'.");
            model.addAttribute("pacient", pacient);
            return "editPacienti"; // Revine la formularul de editare
        }

        pacientiService.updatePacient(pacient); // Actualizează pacientul în baza de date
        return "redirect:/pacienti"; // Redirecționează către lista de pacienți
    }

    // Afișează formularul de căutare pacienți după ID categorie
    @GetMapping("/analiza")
    public String showPacientiByCategorie(@RequestParam("tipAnaliza") Integer idCategorie, Model model) {
        // Apelăm serviciul pentru a obține pacienții care corespund ID-ului categoriei
        List<Pacienti> pacientiByCategorie = pacientiService.getPacientiByCategorie(idCategorie);

        // Dacă nu sunt găsiți pacienți, adăugăm un mesaj de eroare
        if (pacientiByCategorie.isEmpty()) {
            model.addAttribute("error", "Nu au fost găsiți pacienți pentru această categorie.");
        }

        // Transmitere către view
        model.addAttribute("pacienti", pacientiByCategorie);
        return "cautarePacientiDupaAnaliza";  // Afișează pagina cu pacienții găsiți
    }

    @GetMapping("/medic")
    public String showPacientiByMedic(@RequestParam("medicId") Integer medicId, Model model) {
        List<Pacienti> pacientiByMedic = pacientiService.getPacientiByMedic(medicId);

        // Dacă nu sunt găsiți pacienți, adaugă un mesaj de eroare
        if (pacientiByMedic.isEmpty()) {
            model.addAttribute("error", "Nu au fost găsiți pacienți pentru acest medic.");
        }

        model.addAttribute("pacienti", pacientiByMedic);
        return "cautarePacientiDupaMedic";  // Afișează pagina cu pacienții găsiți
    }

}

