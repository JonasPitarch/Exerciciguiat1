package com.jonas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Exercici1 {
    public static void main(String[] args) {
        Statement sentenciaSQL = null;
        ResultSet resultado = null;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Empresa.db")) {
            System.out.println("Conexión establecida.");
            System.out.println("********");

            // Mostrar datos de la tabla Empleats
            String sql = "SELECT * FROM Empleats;";
            sentenciaSQL = con.createStatement();
            resultado = sentenciaSQL.executeQuery(sql);
            System.out.println("Dades de la taula Empleats:");
            while (resultado.next()) {
                String nif = resultado.getString("nif");
                String nom = resultado.getString("nom");
                String cognoms = resultado.getString("cognoms");
                Double salari = resultado.getDouble("salari");
                System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + salari);
            }
            resultado.close(); // Tancar després d'usar

            // Crear la tabla Departaments
            String sql2 = "CREATE TABLE IF NOT EXISTS Departaments (" +
                    "    IdDepartament INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    NomDepartament VARCHAR(100) NOT NULL," +
                    "    Responsable VARCHAR(100)" +
                    ");";
            sentenciaSQL.executeUpdate(sql2);
            System.out.println("Tabla Departaments creada correctamente.");

            // Modificar la tabla Empleats
            String sql3 = "ALTER TABLE Empleats ADD COLUMN IdDepartament INTEGER REFERENCES Departaments(IdDepartament);";
            sentenciaSQL.executeUpdate(sql3);
            System.out.println("Tabla Empleats actualizada correctamente.");

            // Insertar datos en Departaments
            String sql4 = "INSERT INTO Departaments (NomDepartament, Responsable) VALUES" +
                    "('Recursos Humans', 'Marta Pérez')," +
                    "('Desenvolupament', 'Jaume Martí')," +
                    "('Comptabilitat', 'Fina Soler');";
            sentenciaSQL.executeUpdate(sql4);
            System.out.println("Datos insertados en la tabla Departaments correctamente.");

            // Actualizar Empleats con IdDepartament
            String sql5 = "UPDATE Empleats SET IdDepartament = CASE" +
                    "    WHEN NIF = '123456789' THEN 1 " +
                    "    WHEN NIF = '987654321' THEN 2" +
                    "    WHEN NIF = '111111111' THEN 3 " +
                    "    WHEN NIF = '222222222' THEN 1" +
                    "    WHEN NIF = '333333333' THEN 2 " +
                    "    WHEN NIF = '444444444' THEN 3 " +
                    "END;";
            sentenciaSQL.executeUpdate(sql5);
            System.out.println("Añadido a la tabla Empleats el id del departamento al que pertenecen.");

            // Consulta combinando Empleats y Departaments
            String sql6 = "SELECT e.NIF, e.Nom, e.Cognoms, d.NomDepartament " +
                    "FROM Empleats e " +
                    "LEFT JOIN Departaments d ON e.IdDepartament = d.IdDepartament;";
            resultado = sentenciaSQL.executeQuery(sql6);
            System.out.println("Dades combinades de Empleats i Departaments:");
            while (resultado.next()) {
                String nif = resultado.getString("NIF");
                String nom = resultado.getString("Nom");
                String cognoms = resultado.getString("Cognoms");
                String nomDepartament = resultado.getString("NomDepartament");
                System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + nomDepartament);
            }

            // Consulta1 específica per obtenir NIF, Nom i Cognoms
            String cons1 = "SELECT NIF, Nom, Cognoms FROM Empleats;";
            resultado = sentenciaSQL.executeQuery(cons1);
            System.out.println("Dades de Empleats (NIF, Nom, Cognoms):");
            while (resultado.next()) {
                String nif = resultado.getString("NIF");
                String nom = resultado.getString("Nom");
                String cognoms = resultado.getString("Cognoms");
                System.out.println(nif + "\t" + nom + "\t" + cognoms);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
