/** Clasa pentru  Pacienti * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.model;

public class Pacienti {
    private Integer idPacient; // id_pacient
    private String nume;       // nume
    private String prenume;    // prenume
    private String cnp;        // cnp
    private Integer varsta;    // varsta
    private String email;      // email
    private String telefon;    // telefon
    private String parolaCont; // parola_cont
    private String sex;        // sex

    // Getteri și setteri
    public Integer getIdPacient() {
        return idPacient;
    }

    public void setIdPacient(Integer idPacient) {
        this.idPacient = idPacient;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public Integer getVarsta() {
        return varsta;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getParolaCont() {
        return parolaCont;
    }

    public void setParolaCont(String parolaCont) {
        this.parolaCont = parolaCont;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
