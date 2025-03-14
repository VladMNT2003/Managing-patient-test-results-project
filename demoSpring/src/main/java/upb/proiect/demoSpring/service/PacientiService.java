/** Clasa cu functii pentru Pacienti * @author Munteanu Vlad-George * @version 12 Ianuarie 2025 */

package upb.proiect.demoSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upb.proiect.demoSpring.model.Pacienti;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PacientiService {

    private final DataSource dataSource;

    @Autowired
    public PacientiService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Metoda pentru a prelua toți pacienții din baza de date
    public List<Pacienti> getAllPacienti() {
        List<Pacienti> pacientiList = new ArrayList<>();
        String query = "SELECT * FROM pacienti";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Pacienti pacient = new Pacienti();
                pacient.setIdPacient(resultSet.getInt("id_pacient"));
                pacient.setNume(resultSet.getString("nume"));
                pacient.setPrenume(resultSet.getString("prenume"));
                pacient.setCnp(resultSet.getString("cnp"));
                pacient.setVarsta(resultSet.getInt("varsta"));
                pacient.setEmail(resultSet.getString("email"));
                pacient.setTelefon(resultSet.getString("telefon"));
                pacient.setParolaCont(resultSet.getString("parola_cont"));
                pacient.setSex(resultSet.getString("sex"));

                pacientiList.add(pacient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientiList;
    }

    public Pacienti getPacientById(Integer id) {
        String query = "SELECT * FROM pacienti WHERE id_pacient = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Pacienti pacient = new Pacienti();
                    pacient.setIdPacient(resultSet.getInt("id_pacient"));
                    pacient.setNume(resultSet.getString("nume"));
                    pacient.setPrenume(resultSet.getString("prenume"));
                    pacient.setCnp(resultSet.getString("cnp"));
                    pacient.setVarsta(resultSet.getInt("varsta"));
                    pacient.setEmail(resultSet.getString("email"));
                    pacient.setTelefon(resultSet.getString("telefon"));
                    pacient.setParolaCont(resultSet.getString("parola_cont"));
                    pacient.setSex(resultSet.getString("sex"));
                    return pacient;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metoda pentru a adăuga un nou pacient
    public void addPacient(Pacienti pacient) {
        String query = "INSERT INTO pacienti (nume, prenume, cnp, varsta, email, telefon, parola_cont, sex) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, pacient.getNume());
            preparedStatement.setString(2, pacient.getPrenume());
            preparedStatement.setString(3, pacient.getCnp());
            preparedStatement.setInt(4, pacient.getVarsta());
            preparedStatement.setString(5, pacient.getEmail());
            preparedStatement.setString(6, pacient.getTelefon());
            preparedStatement.setString(7, pacient.getParolaCont());
            preparedStatement.setString(8, pacient.getSex());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Șterge un pacient după ID
    public void deletePacient(Integer id) {
        String query = "DELETE FROM pacienti WHERE id_pacient = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePacient(Pacienti pacient) {
        String query = "UPDATE pacienti SET nume = ?, prenume = ?, cnp = ?, varsta = ?, email = ?, telefon = ?, parola_cont = ?, sex = ? WHERE id_pacient = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, pacient.getNume());
            preparedStatement.setString(2, pacient.getPrenume());
            preparedStatement.setString(3, pacient.getCnp());
            preparedStatement.setInt(4, pacient.getVarsta());
            preparedStatement.setString(5, pacient.getEmail());
            preparedStatement.setString(6, pacient.getTelefon());
            preparedStatement.setString(7, pacient.getParolaCont());
            preparedStatement.setString(8, pacient.getSex());
            preparedStatement.setInt(9, pacient.getIdPacient());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodă pentru a obține pacienții în funcție de ID-ul categoriei
    public List<Pacienti> getPacientiByCategorie(Integer idCategorie) {
        List<Pacienti> pacientiList = new ArrayList<>();
        String query = "SELECT pacienti.nume, pacienti.prenume, pacienti.telefon " +
                "FROM pacienti " +
                "WHERE pacienti.id_pacient IN (" +
                "    SELECT trimiteri.pacient_id " +
                "    FROM trimiteri " +
                "    WHERE trimiteri.id_trimitere IN (" +
                "        SELECT buletin_de_analize.trimitere_id " +
                "        FROM buletin_de_analize " +
                "        WHERE buletin_de_analize.id_buletin IN (" +
                "            SELECT analiza_buletin.buletin_id " +
                "            FROM analiza_buletin " +
                "            WHERE analiza_buletin.analiza_id IN (" +
                "                SELECT analize.id_analiza " +
                "                FROM analize " +
                "                WHERE analize.id_categorie = ?" +
                "            )" +
                "        )" +
                "    )" +
                ");";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, idCategorie); // Setăm parametrul pentru ID-ul categoriei

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pacienti pacient = new Pacienti();
                    pacient.setNume(resultSet.getString("nume"));
                    pacient.setPrenume(resultSet.getString("prenume"));
                    pacient.setTelefon(resultSet.getString("telefon"));
                    pacientiList.add(pacient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientiList;
    }

    public List<Pacienti> getPacientiByMedic(Integer medicId) {
        List<Pacienti> pacientiList = new ArrayList<>();
        String query = "SELECT pacienti.nume, pacienti.prenume, pacienti.telefon " +
                "FROM pacienti " +
                "WHERE EXISTS (" +
                "    SELECT 1 " +
                "    FROM trimiteri " +
                "    WHERE trimiteri.pacient_id = pacienti.id_pacient " +
                "    AND EXISTS (" +
                "        SELECT 1 " +
                "        FROM medici " +
                "        WHERE medici.id_medic = trimiteri.medic_id " +
                "        AND medici.id_medic = ?" +
                "    )" +
                ")";



        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, medicId); // Setăm parametru pentru medicul specificat

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pacienti pacient = new Pacienti();
                    pacient.setNume(resultSet.getString("nume"));
                    pacient.setPrenume(resultSet.getString("prenume"));
                    pacient.setTelefon(resultSet.getString("telefon"));
                    pacientiList.add(pacient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientiList;
    }



}