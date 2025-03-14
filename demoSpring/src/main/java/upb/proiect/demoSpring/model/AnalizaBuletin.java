/** Clasa pentru  AnalizeBuletin * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.model;

public class AnalizaBuletin {
    private int analizaId;
    private int buletinId;
    private String statusAnaliza;
    private double valoareNumerica;

    public int getAnalizaId() {
        return analizaId;
    }

    public void setAnalizaId(int analizaId) {
        this.analizaId = analizaId;
    }

    public int getBuletinId() {
        return buletinId;
    }

    public void setBuletinId(int buletinId) {
        this.buletinId = buletinId;
    }

    public String getStatusAnaliza() {
        return statusAnaliza;
    }

    public void setStatusAnaliza(String statusAnaliza) {
        this.statusAnaliza = statusAnaliza;
    }

    public double getValoareNumerica() {
        return valoareNumerica;
    }

    public void setValoareNumerica(double valoareNumerica) {
        this.valoareNumerica = valoareNumerica;
    }
}
