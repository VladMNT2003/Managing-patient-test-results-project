/** Clasa pentru  Analize * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.model;

public class Analize {
    private int idAnaliza;
    private double valoareMinima;
    private double valoareMaxima;
    private int idCategorie;


    public int getIdAnaliza() {
        return idAnaliza;
    }

    public void setIdAnaliza(int idAnaliza) {
        this.idAnaliza = idAnaliza;
    }

    public double getValoareMinima() {
        return valoareMinima;
    }

    public void setValoareMinima(double valoareMinima) {
        this.valoareMinima = valoareMinima;
    }

    public double getValoareMaxima() {
        return valoareMaxima;
    }

    public void setValoareMaxima(double valoareMaxima) {
        this.valoareMaxima = valoareMaxima;
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }
}
