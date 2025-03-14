/** Clasa pentru  BuletineDeAnaliza * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.model;

import java.sql.Date;

public class BuletinDeAnalize {
    private int idBuletin;
    private Date dataEmiterii;
    private Date dataRecoltarii;
    private int trimitereId;

    public int getIdBuletin() {
        return idBuletin;
    }

    public void setIdBuletin(int idBuletin) {
        this.idBuletin = idBuletin;
    }

    public Date getDataEmiterii() {
        return dataEmiterii;
    }

    public void setDataEmiterii(Date dataEmiterii) {
        this.dataEmiterii = dataEmiterii;
    }

    public Date getDataRecoltarii() {
        return dataRecoltarii;
    }

    public void setDataRecoltarii(Date dataRecoltarii) {
        this.dataRecoltarii = dataRecoltarii;
    }

    public int getTrimitereId() {
        return trimitereId;
    }

    public void setTrimitereId(int trimitereId) {
        this.trimitereId = trimitereId;
    }
}
