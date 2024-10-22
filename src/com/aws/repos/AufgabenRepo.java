package com.aws.repos;

import com.aws.model.Aufgabe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AufgabenRepo {
    private Connection connection;

    public AufgabenRepo(String dbPath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS aufgaben (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titel TEXT NOT NULL,
                    beschreibung TEXT,
                    prioritaet INTEGER,
                    erledigt BOOLEAN
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void aufgabeHinzufuegen(Aufgabe aufgabe) {
        String query = "INSERT INTO aufgaben (titel, beschreibung, prioritaet, erledigt) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, aufgabe.getTitel());
            pstmt.setString(2, aufgabe.getBeschreibung());
            pstmt.setInt(3, aufgabe.getPrioritaet());
            pstmt.setBoolean(4, aufgabe.isErledigt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Aufgabe> getAufgaben() {
        List<Aufgabe> aufgaben = new ArrayList<>();
        String query = "SELECT titel, beschreibung, prioritaet, erledigt FROM aufgaben";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Aufgabe aufgabe = new Aufgabe(
                        rs.getString("titel"),
                        rs.getString("beschreibung"),
                        rs.getInt("prioritaet")
                );
                aufgabe.setErledigt(rs.getBoolean("erledigt"));
                aufgaben.add(aufgabe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aufgaben;
    }

    public void aufgabeAlsErledigtMarkieren(String titel) {
        String query = "UPDATE aufgaben SET erledigt = 1 WHERE titel = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, titel);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void aufgabeLoeschen(String titel) {
        String query = "DELETE FROM aufgaben WHERE titel = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, titel);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
