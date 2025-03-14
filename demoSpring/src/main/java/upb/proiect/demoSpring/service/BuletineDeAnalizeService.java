/** Clasa cu functii pentru BuletineDeAnaliza * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upb.proiect.demoSpring.model.BuletinDeAnalize;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class BuletineDeAnalizeService {

    @Autowired
    private DataSource dataSource;

    public List<BuletinDeAnalize> getAllBuletine() {
        List<BuletinDeAnalize> buletineList = new ArrayList<>();
        String query = "SELECT * FROM buletin_de_analize";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                BuletinDeAnalize buletin = new BuletinDeAnalize();
                buletin.setIdBuletin(resultSet.getInt("id_buletin"));
                buletin.setDataEmiterii(resultSet.getDate("data_emiterii"));
                buletin.setDataRecoltarii(resultSet.getDate("data_recoltarii"));
                buletin.setTrimitereId(resultSet.getInt("trimitere_id"));
                buletineList.add(buletin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buletineList;
    }

    public void addBuletin(BuletinDeAnalize buletin) {
        String query = "INSERT INTO buletin_de_analize (data_emiterii, data_recoltarii, trimitere_id) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, buletin.getDataEmiterii());
            preparedStatement.setDate(2, buletin.getDataRecoltarii());
            preparedStatement.setInt(3, buletin.getTrimitereId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBuletin(int idBuletin) {
        String query = "DELETE FROM buletin_de_analize WHERE id_buletin = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idBuletin);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getBuletineWithTrimiteri() {
        List<Map<String, Object>> buletineList = new ArrayList<>();
        String query = "SELECT b.id_buletin, b.data_emiterii, b.data_recoltarii, t.data_emiterii AS data_emiterii_trimitere, " +
                "p.nume AS pacient_nume, p.prenume AS pacient_prenume, m.nume AS medic_nume, m.prenume AS medic_prenume " +
                "FROM buletin_de_analize b " +
                "JOIN trimiteri t ON b.trimitere_id = t.id_trimitere " +
                "JOIN pacienti p ON t.pacient_id = p.id_pacient " +
                "JOIN medici m ON t.medic_id = m.id_medic";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> buletinData = new HashMap<>();
                buletinData.put("id_buletin", resultSet.getInt("id_buletin"));
                buletinData.put("data_emiterii", resultSet.getDate("data_emiterii"));
                buletinData.put("data_recoltarii", resultSet.getDate("data_recoltarii"));
                buletinData.put("data_emiterii_trimitere", resultSet.getDate("data_emiterii_trimitere"));
                buletinData.put("pacient_nume", resultSet.getString("pacient_nume"));
                buletinData.put("pacient_prenume", resultSet.getString("pacient_prenume"));
                buletinData.put("medic_nume", resultSet.getString("medic_nume"));
                buletinData.put("medic_prenume", resultSet.getString("medic_prenume"));
                buletineList.add(buletinData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buletineList;
    }

    // Metoda care verifică existența unei trimiteri
    public boolean checkIfTrimitereExists(int trimitereId) {
        String query = "SELECT COUNT(*) FROM trimiteri WHERE id_trimitere = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, trimitereId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;  // Trimiterea există
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Trimiterea nu există
    }

}
