/** Clasa cu functii pentru AnalizeBuletin * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upb.proiect.demoSpring.model.AnalizaBuletin;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalizaBuletinService {

    @Autowired
    private DataSource dataSource;

    public List<AnalizaBuletin> getAllAnalizeBuletin() {
        List<AnalizaBuletin> analizeBuletinList = new ArrayList<>();
        String query = "SELECT * FROM analiza_buletin";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                AnalizaBuletin analizaBuletin = new AnalizaBuletin();
                analizaBuletin.setAnalizaId(resultSet.getInt("analiza_id"));
                analizaBuletin.setBuletinId(resultSet.getInt("buletin_id"));
                analizaBuletin.setStatusAnaliza(resultSet.getString("status_analiza"));
                analizaBuletin.setValoareNumerica(resultSet.getDouble("valoare_numerica"));
                analizeBuletinList.add(analizaBuletin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analizeBuletinList;
    }

    public void addAnalizaBuletin(AnalizaBuletin analizaBuletin) {
        String query = "INSERT INTO analiza_buletin (analiza_id, buletin_id, status_analiza, valoare_numerica) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, analizaBuletin.getAnalizaId());
            preparedStatement.setInt(2, analizaBuletin.getBuletinId());
            preparedStatement.setString(3, analizaBuletin.getStatusAnaliza());
            preparedStatement.setDouble(4, analizaBuletin.getValoareNumerica());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAnalizaBuletin(int analizaId, int buletinId) {
        String query = "DELETE FROM analiza_buletin WHERE analiza_id = ? AND buletin_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, analizaId);
            preparedStatement.setInt(2, buletinId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getAnalizeBuletinWithDetails() {
        List<Map<String, Object>> analizeBuletinDetails = new ArrayList<>();
        String query = "SELECT ab.analiza_id, ab.buletin_id, ab.status_analiza, ab.valoare_numerica, " +
                "b.data_emiterii AS data_emiterii_buletin, b.data_recoltarii, " +
                "t.data_emiterii AS data_emiterii_trimitere, " +
                "p.nume AS pacient_nume, p.prenume AS pacient_prenume, " +
                "m.nume AS medic_nume, m.prenume AS medic_prenume " +
                "FROM analiza_buletin ab " +
                "JOIN buletin_de_analize b ON ab.buletin_id = b.id_buletin " +
                "JOIN trimiteri t ON b.trimitere_id = t.id_trimitere " +
                "JOIN pacienti p ON t.pacient_id = p.id_pacient " +
                "JOIN medici m ON t.medic_id = m.id_medic";




        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> analizaBuletinData = new HashMap<>();
                analizaBuletinData.put("analiza_id", resultSet.getInt("analiza_id"));
                analizaBuletinData.put("buletin_id", resultSet.getInt("buletin_id"));
                analizaBuletinData.put("status_analiza", resultSet.getString("status_analiza"));
                analizaBuletinData.put("valoare_numerica", resultSet.getDouble("valoare_numerica"));
                analizaBuletinData.put("data_emiterii_buletin", resultSet.getDate("data_emiterii_buletin"));
                analizaBuletinData.put("data_recoltarii", resultSet.getDate("data_recoltarii"));
                analizaBuletinData.put("data_emiterii_trimitere", resultSet.getDate("data_emiterii_trimitere"));
                analizaBuletinData.put("pacient_nume", resultSet.getString("pacient_nume"));
                analizaBuletinData.put("pacient_prenume", resultSet.getString("pacient_prenume"));
                analizaBuletinData.put("medic_nume", resultSet.getString("medic_nume"));
                analizaBuletinData.put("medic_prenume", resultSet.getString("medic_prenume"));
                analizeBuletinDetails.add(analizaBuletinData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return analizeBuletinDetails;
    }

    public List<Map<String, Object>> getDetailedAnalize() {
        List<Map<String, Object>> results = new ArrayList<>();

        String query = "SELECT ab.analiza_id, ab.buletin_id, ab.status_analiza, ab.valoare_numerica, " +
                "(SELECT a.valoare_minima FROM analize a WHERE a.id_analiza = ab.analiza_id) AS valoare_minima, " +
                "(SELECT a.valoare_maxima FROM analize a WHERE a.id_analiza = ab.analiza_id) AS valoare_maxima, " +
                "(SELECT c.tip FROM categorii c WHERE c.id_categorie = (SELECT a.id_categorie FROM analize a WHERE a.id_analiza = ab.analiza_id)) AS tip, " +
                "(SELECT c.unitate_masura FROM categorii c WHERE c.id_categorie = (SELECT a.id_categorie FROM analize a WHERE a.id_analiza = ab.analiza_id)) AS unitate_masura " +
                "FROM analiza_buletin ab " +
                "JOIN buletin_de_analize b ON ab.buletin_id = b.id_buletin";  // Adăugăm un JOIN pentru a conecta analiza_buletin cu buletin_de_analize


        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("analiza_id", rs.getInt("analiza_id"));
                row.put("buletin_id", rs.getInt("buletin_id"));
                row.put("status_analiza", rs.getString("status_analiza"));
                row.put("valoare_numerica", rs.getDouble("valoare_numerica"));
                row.put("valoare_minima", rs.getDouble("valoare_minima"));
                row.put("valoare_maxima", rs.getDouble("valoare_maxima"));
                row.put("tip", rs.getString("tip"));
                row.put("unitate_masura", rs.getString("unitate_masura"));
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Map<String, Object>> getBuletineDetails() {
        List<Map<String, Object>> buletineDetails = new ArrayList<>();
        String query = "SELECT ab.analiza_id, ab.buletin_id, ab.status_analiza, ab.valoare_numerica, " +
                "b.data_emiterii AS data_emiterii_buletin, b.data_recoltarii " +
                "FROM analiza_buletin ab " +
                "JOIN buletin_de_analize b ON ab.buletin_id = b.id_buletin"; // Eliminăm subinterogările pentru pacient și medic

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> buletinData = new HashMap<>();
                buletinData.put("analiza_id", resultSet.getInt("analiza_id"));
                buletinData.put("buletin_id", resultSet.getInt("buletin_id"));
                buletinData.put("status_analiza", resultSet.getString("status_analiza"));
                buletinData.put("valoare_numerica", resultSet.getDouble("valoare_numerica"));
                buletinData.put("data_emiterii_buletin", resultSet.getDate("data_emiterii_buletin"));
                buletinData.put("data_recoltarii", resultSet.getDate("data_recoltarii"));
                buletineDetails.add(buletinData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buletineDetails;
    }

    public List<Map<String, Object>> getDetaliiAnalize() {
        List<Map<String, Object>> detaliiAnalize = new ArrayList<>();
        String query = "SELECT ab.analiza_id, ab.buletin_id, ab.status_analiza, ab.valoare_numerica, " +
                "a.valoare_minima, a.valoare_maxima " +  // Adăugăm coloanele valoare_minima și valoare_maxima
                "FROM analiza_buletin ab " +
                "JOIN analize a ON ab.analiza_id = a.id_analiza";  // Facem JOIN pentru tabela analize

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> analizaData = new HashMap<>();
                analizaData.put("analiza_id", resultSet.getInt("analiza_id"));
                analizaData.put("buletin_id", resultSet.getInt("buletin_id"));
                analizaData.put("status_analiza", resultSet.getString("status_analiza"));
                analizaData.put("valoare_numerica", resultSet.getDouble("valoare_numerica"));
                analizaData.put("valoare_minima", resultSet.getDouble("valoare_minima"));
                analizaData.put("valoare_maxima", resultSet.getDouble("valoare_maxima"));
                detaliiAnalize.add(analizaData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detaliiAnalize;
    }

    public boolean checkIfAnalizaExists(int analizaId) {
        String query = "SELECT COUNT(*) FROM analize WHERE id_analiza = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, analizaId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfBuletinExists(int buletinId) {
        String query = "SELECT COUNT(*) FROM buletin_de_analize WHERE id_buletin = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, buletinId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
