/** Clasa cu functii pentru Medici * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upb.proiect.demoSpring.model.Medici;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MediciService {

    @Autowired
    private DataSource dataSource;

    // Metoda pentru a prelua toți medicii
    public List<Medici> getAllMedici() {
        List<Medici> mediciList = new ArrayList<>();
        String query = "SELECT * FROM medici";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Medici medic = new Medici();
                medic.setIdMedic(resultSet.getInt("id_medic"));
                medic.setNume(resultSet.getString("nume"));
                medic.setPrenume(resultSet.getString("prenume"));
                medic.setCnp(resultSet.getString("cnp"));
                medic.setEmail(resultSet.getString("email"));
                medic.setTelefon(resultSet.getString("telefon"));

                mediciList.add(medic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mediciList;
    }

    // Metoda pentru a adăuga un nou medic
    public void addMedic(Medici medic) {
        String query = "INSERT INTO medici (nume, prenume, cnp, email, telefon) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, medic.getNume());
            preparedStatement.setString(2, medic.getPrenume());
            preparedStatement.setString(3, medic.getCnp());
            preparedStatement.setString(4, medic.getEmail());
            preparedStatement.setString(5, medic.getTelefon());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda pentru a șterge un medic după ID
    public void deleteMedic(int idMedic) {
        String query = "DELETE FROM medici WHERE id_medic = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idMedic);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMedic(Medici medic) {
        String query = "UPDATE medici SET nume = ?, prenume = ?, cnp = ?, email = ?, telefon = ? WHERE id_medic = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, medic.getNume());
            preparedStatement.setString(2, medic.getPrenume());
            preparedStatement.setString(3, medic.getCnp());
            preparedStatement.setString(4, medic.getEmail());
            preparedStatement.setString(5, medic.getTelefon());
            preparedStatement.setInt(6, medic.getIdMedic());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Medici getMedicById(int id) {
        String query = "SELECT * FROM medici WHERE id_medic = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Medici medic = new Medici();
                    medic.setIdMedic(resultSet.getInt("id_medic"));
                    medic.setNume(resultSet.getString("nume"));
                    medic.setPrenume(resultSet.getString("prenume"));
                    medic.setCnp(resultSet.getString("cnp"));
                    medic.setEmail(resultSet.getString("email"));
                    medic.setTelefon(resultSet.getString("telefon"));
                    return medic;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metoda pentru a obține medicii care au scris cel puțin un anumit număr de trimiteri
    public List<Medici> getMediciByTrimiteriCount(int minTrimiteri) {
        List<Medici> mediciList = new ArrayList<>();
        String query = "SELECT m.nume, m.prenume " +
                "FROM medici m " +
                "WHERE m.id_medic = (" +
                "    SELECT t.medic_id " +
                "    FROM trimiteri t " +
                "    WHERE t.medic_id = m.id_medic " +
                "    GROUP BY t.medic_id " +
                "    HAVING COUNT(t.id_trimitere) = ?" +
                "    LIMIT 1" +
                ")";



        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, minTrimiteri);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Medici medic = new Medici();
                    medic.setNume(resultSet.getString("nume"));
                    medic.setPrenume(resultSet.getString("prenume"));
                    mediciList.add(medic);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mediciList;
    }
}
