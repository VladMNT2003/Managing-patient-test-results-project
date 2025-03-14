/** Clasa cu functii pentru Analize * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upb.proiect.demoSpring.model.Analize;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AnalizeService {

    @Autowired
    private DataSource dataSource;

    public List<Analize> getAllAnalize() {
        List<Analize> analizeList = new ArrayList<>();
        String query = "SELECT * FROM analize";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Analize analiza = new Analize();
                analiza.setIdAnaliza(resultSet.getInt("id_analiza"));
                analiza.setValoareMinima(resultSet.getDouble("valoare_minima"));
                analiza.setValoareMaxima(resultSet.getDouble("valoare_maxima"));
                analiza.setIdCategorie(resultSet.getInt("id_categorie"));
                analizeList.add(analiza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analizeList;
    }

    public void addAnaliza(Analize analiza) {
        String query = "INSERT INTO analize (valoare_minima, valoare_maxima, id_categorie) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, analiza.getValoareMinima());
            preparedStatement.setDouble(2, analiza.getValoareMaxima());
            preparedStatement.setInt(3, analiza.getIdCategorie());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAnaliza(int idAnaliza) {
        String query = "DELETE FROM analize WHERE id_analiza = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idAnaliza);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getAnalizeWithCategories() {
        List<Map<String, Object>> analizeDetails = new ArrayList<>();
        String query = "SELECT a.id_analiza, a.valoare_minima, a.valoare_maxima, c.tip, c.unitate_masura " +
                "FROM analize a " +
                "JOIN categorii c ON a.id_categorie = c.id_categorie";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> analizaDetail = new HashMap<>();
                analizaDetail.put("idAnaliza", resultSet.getInt("id_analiza"));
                analizaDetail.put("valoareMinima", resultSet.getDouble("valoare_minima"));
                analizaDetail.put("valoareMaxima", resultSet.getDouble("valoare_maxima"));
                analizaDetail.put("tip", resultSet.getString("tip"));
                analizaDetail.put("unitateMasura", resultSet.getString("unitate_masura"));
                analizeDetails.add(analizaDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return analizeDetails;
    }

    public boolean checkIfCategorieExists(Integer idCategorie) {
        String query = "SELECT COUNT(*) FROM categorii WHERE id_categorie = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idCategorie);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;  // Categorie exista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Categorie nu exista
    }


}
