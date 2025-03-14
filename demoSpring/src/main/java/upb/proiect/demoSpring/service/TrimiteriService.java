/** Clasa cu functii pentru Trimiteri * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upb.proiect.demoSpring.model.Trimiteri;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.util.Map;
import java.util.HashMap;

@Service
public class TrimiteriService {

    @Autowired
    private DataSource dataSource;

    // Afișează toate trimiterile
    public List<Trimiteri> getAllTrimiteri() {
        List<Trimiteri> trimiteriList = new ArrayList<>();
        String query = "SELECT * FROM trimiteri";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Trimiteri trimitere = new Trimiteri(
                        resultSet.getInt("id_trimitere"),
                        resultSet.getString("data_emiterii"),
                        resultSet.getLong("pacient_id"),
                        resultSet.getLong("medic_id")
                );
                trimiteriList.add(trimitere);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trimiteriList;
    }

    // Adaugă o trimitere nouă
    public void addTrimitere(Trimiteri trimitere) {
        String query = "INSERT INTO trimiteri (data_emiterii, pacient_id, medic_id) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Convertește String-ul în java.sql.Date
            Date sqlDate = Date.valueOf(trimitere.getDataEmiterii()); // dataEmiterii trebuie să fie în format "yyyy-MM-dd"
            preparedStatement.setDate(1, sqlDate);
            preparedStatement.setLong(2, trimitere.getPacientId());
            preparedStatement.setLong(3, trimitere.getMedicId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Șterge o trimitere după ID
    public void deleteTrimitere(Integer id) {
        String query = "DELETE FROM trimiteri WHERE id_trimitere = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Map<String, Object>> getTrimiteriWithDetails() {
        List<Map<String, Object>> result = new ArrayList<>();
        String query = """
        SELECT t.id_trimitere, t.data_emiterii, 
               p.nume AS pacient_nume, p.prenume AS pacient_prenume,
               m.nume AS medic_nume, m.prenume AS medic_prenume
        FROM trimiteri t
        JOIN pacienti p ON t.pacient_id = p.id_pacient
        JOIN medici m ON t.medic_id = m.id_medic
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("idTrimitere", resultSet.getInt("id_trimitere"));
                row.put("dataEmiterii", resultSet.getDate("data_emiterii"));
                row.put("pacientNume", resultSet.getString("pacient_nume"));
                row.put("pacientPrenume", resultSet.getString("pacient_prenume"));
                row.put("medicNume", resultSet.getString("medic_nume"));
                row.put("medicPrenume", resultSet.getString("medic_prenume"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean checkIfPacientExists(Integer pacientId) {
        String query = "SELECT COUNT(*) FROM pacienti WHERE id_pacient = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, pacientId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;  // Pacientul există
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Pacientul nu există
    }

    public boolean checkIfMedicExists(Integer medicId) {
        String query = "SELECT COUNT(*) FROM medici WHERE id_medic = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, medicId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;  // Medic exista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Medic nu exista
    }


}
