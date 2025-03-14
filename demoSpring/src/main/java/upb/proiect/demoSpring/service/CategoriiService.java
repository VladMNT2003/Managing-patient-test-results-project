/** Clasa cu functii pentru Categorii * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upb.proiect.demoSpring.model.Categorie;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class CategoriiService {

    @Autowired
    private DataSource dataSource;

    public List<Categorie> getAllCategorii() {
        List<Categorie> categoriiList = new ArrayList<>();
        String query = "SELECT * FROM categorii";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Categorie categorie = new Categorie();
                categorie.setIdCategorie(resultSet.getInt("id_categorie"));
                categorie.setTip(resultSet.getString("tip"));
                categorie.setUnitateMasura(resultSet.getString("unitate_masura"));
                categoriiList.add(categorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoriiList;
    }

    public void addCategorie(Categorie categorie) {
        String query = "INSERT INTO categorii (tip, unitate_masura) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categorie.getTip());
            preparedStatement.setString(2, categorie.getUnitateMasura());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategorie(int idCategorie) {
        String query = "DELETE FROM categorii WHERE id_categorie = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idCategorie);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
