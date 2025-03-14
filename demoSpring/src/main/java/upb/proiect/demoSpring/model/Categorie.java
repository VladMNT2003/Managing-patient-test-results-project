/** Clasa pentru  Categorii * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.model;

public class Categorie {
    private int idCategorie;
    private String tip;
    private String unitateMasura;

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUnitateMasura() {
        return unitateMasura;
    }

    public void setUnitateMasura(String unitateMasura) {
        this.unitateMasura = unitateMasura;
    }
}
