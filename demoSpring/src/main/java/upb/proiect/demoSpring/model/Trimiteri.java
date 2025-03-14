/** Clasa pentru  Trimiteri * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.model;

public class Trimiteri {

    private Integer idTrimitere;
    private String dataEmiterii;
    private Long pacientId;
    private Long medicId;

    public Trimiteri(Integer idTrimitere, String dataEmiterii, Long pacientId, Long medicId) {
        this.idTrimitere = idTrimitere;
        this.dataEmiterii = dataEmiterii;
        this.pacientId = pacientId;
        this.medicId = medicId;
    }

    // Getter și Setter
    public Integer getIdTrimitere() {
        return idTrimitere;
    }

    public void setIdTrimitere(Integer idTrimitere) {
        this.idTrimitere = idTrimitere;
    }

    public String getDataEmiterii() {
        return dataEmiterii;
    }

    public void setDataEmiterii(String dataEmiterii) {
        this.dataEmiterii = dataEmiterii;
    }

    public Long getPacientId() {
        return pacientId;
    }

    public void setPacientId(Long pacientId) {
        this.pacientId = pacientId;
    }

    public Long getMedicId() {
        return medicId;
    }

    public void setMedicId(Long medicId) {
        this.medicId = medicId;
    }
}
