package com.jonas;

import java.sql.*;
import java.util.Scanner;

public class Exercici1 {
    public static void main(String[] args) {
        ResultSet resultado = null;

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Empresa.db")) {
            System.out.println("Conexión establecida.");
            System.out.println("********");

            // Mostrar datos de la tabla Empleats
            try (Statement stmt = con.createStatement()) {
                String sql = "SELECT * FROM Empleats;";
                resultado = stmt.executeQuery(sql);
                System.out.println("Dades de la taula Empleats:");
                while (resultado.next()) {
                    String nif = resultado.getString("nif");
                    String nom = resultado.getString("nom");
                    String cognoms = resultado.getString("cognoms");
                    Double salari = resultado.getDouble("salari");
                    System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + salari);
                }
            }

            // Crear la tabla Departaments
            try (Statement stmt = con.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS Departaments (" +
                        "IdDepartament INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "NomDepartament VARCHAR(100) NOT NULL," +
                        "Responsable VARCHAR(100)" +
                        ");";
                stmt.executeUpdate(sql);
                System.out.println("Tabla Departaments creada correctamente.");
            }

            // Modificar la tabla Empleats
            try (Statement stmt = con.createStatement()) {
                String sql = "ALTER TABLE Empleats ADD COLUMN IdDepartament INTEGER REFERENCES Departaments(IdDepartament);";
                stmt.executeUpdate(sql);
                System.out.println("Tabla Empleats actualizada correctamente.");
            } catch (SQLException e) {
                if (e.getMessage().contains("duplicate column name")) {
                    System.out.println("La columna IdDepartament ya existe.");
                } else {
                    throw e;
                }
            }

            // Insertar datos en Departaments
            try (Statement stmt = con.createStatement()) {
                String sql = "INSERT INTO Departaments (NomDepartament, Responsable) VALUES " +
                        "('Recursos Humans', 'Marta Pérez')," +
                        "('Desenvolupament', 'Jaume Martí')," +
                        "('Comptabilitat', 'Fina Soler');";
                stmt.executeUpdate(sql);
                System.out.println("Datos insertados en la tabla Departaments correctamente.");
            }

            // Actualizar Empleats con IdDepartament
            try (Statement stmt = con.createStatement()) {
                String sql = "UPDATE Empleats SET IdDepartament = CASE " +
                        "WHEN NIF = '123456789' THEN 1 " +
                        "WHEN NIF = '987654321' THEN 2 " +
                        "WHEN NIF = '111111111' THEN 3 " +
                        "WHEN NIF = '222222222' THEN 1 " +
                        "WHEN NIF = '333333333' THEN 2 " +
                        "WHEN NIF = '444444444' THEN 3 " +
                        "END;";
                stmt.executeUpdate(sql);
                System.out.println("Añadido a la tabla Empleats el id del departamento al que pertenecen.");
            }

            // Consulta combinando Empleats y Departaments
            try (Statement stmt = con.createStatement()) {
                String sql = "SELECT e.NIF, e.Nom, e.Cognoms, d.NomDepartament " +
                        "FROM Empleats e " +
                        "LEFT JOIN Departaments d ON e.IdDepartament = d.IdDepartament;";
                resultado = stmt.executeQuery(sql);
                System.out.println("Dades combinades de Empleats i Departaments:");
                while (resultado.next()) {
                    String nif = resultado.getString("NIF");
                    String nom = resultado.getString("Nom");
                    String cognoms = resultado.getString("Cognoms");
                    String nomDepartament = resultado.getString("NomDepartament");
                    System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + nomDepartament);
                }
            }

            // Demanar NIF per pantalla i mostrar informació completa de l'empleat
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT e.NIF, e.Nom, e.Cognoms, e.Salari, d.NomDepartament " +
                            "FROM Empleats e " +
                            "LEFT JOIN Departaments d ON e.IdDepartament = d.IdDepartament " +
                            "WHERE e.NIF = ?")) {

                Scanner scanner = new Scanner(System.in);
                System.out.print("Introdueix el NIF de l'empleat: ");
                String nif = scanner.nextLine();

                pstmt.setString(1, nif);
                resultado = pstmt.executeQuery();

                if (resultado.next()) {
                    String nom = resultado.getString("Nom");
                    String cognoms = resultado.getString("Cognoms");
                    double salari = resultado.getDouble("Salari");
                    String nomDepartament = resultado.getString("NomDepartament");
                    System.out.println("Informació de l'empleat:");
                    System.out.println("NIF: " + nif);
                    System.out.println("Nom: " + nom);
                    System.out.println("Cognoms: " + cognoms);
                    System.out.println("Salari: " + salari);
                    System.out.println("Departament: " + (nomDepartament != null ? nomDepartament : "No assignat"));
                } else {
                    System.out.println("No s'ha trobat cap empleat amb el NIF indicat.");
                }
            }

            // Demana per pantalla un valor mínim de salari i mostra els empleats que el superen
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT * FROM Empleats WHERE salari > ?")) {

                Scanner scanner = new Scanner(System.in);
                System.out.print("Introdueix un salari mínim: ");
                double salari = scanner.nextDouble();

                pstmt.setDouble(1, salari);
                resultado = pstmt.executeQuery();

                System.out.println("Empleats amb salari superior a " + salari + ":");
                while (resultado.next()) {
                    String nif = resultado.getString("NIF");
                    String nom = resultado.getString("Nom");
                    String cognoms = resultado.getString("Cognoms");
                    double salariEmp = resultado.getDouble("salari");
                    System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + salariEmp);
                }
            }

            // Subconsulta: salari mitjà de cada departament i empleats amb salari superior
            try (Statement stmt = con.createStatement()) {
                String sql = "SELECT e.NIF, e.Nom, e.Cognoms, e.Salari, d.NomDepartament " +
                        "FROM Empleats e " +
                        "INNER JOIN Departaments d ON e.IdDepartament = d.IdDepartament " +
                        "WHERE e.Salari > ( " +
                        "    SELECT AVG(salari) " +
                        "    FROM Empleats " +
                        "    WHERE IdDepartament = e.IdDepartament " +
                        ");";
                resultado = stmt.executeQuery(sql);
                System.out.println("Empleats amb salari superior al mitjà del seu departament:");
                while (resultado.next()) {
                    String nif = resultado.getString("NIF");
                    String nom = resultado.getString("Nom");
                    String cognoms = resultado.getString("Cognoms");
                    double salari = resultado.getDouble("Salari");
                    String nomDepartament = resultado.getString("NomDepartament");
                    System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + salari + "\t" + nomDepartament);
                }
            }

            // Consulta departaments amb suma de salaris superior a un valor
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT d.NomDepartament, SUM(e.Salari) AS SumaSalari " +
                            "FROM Empleats e " +
                            "INNER JOIN Departaments d ON e.IdDepartament = d.IdDepartament " +
                            "GROUP BY d.NomDepartament " +
                            "HAVING SUM(e.Salari) > ?")) {

                Scanner scanner = new Scanner(System.in);
                System.out.print("Introdueix un valor mínim de suma de salaris: ");
                double valorMinim = scanner.nextDouble();

                pstmt.setDouble(1, valorMinim);
                resultado = pstmt.executeQuery();

                System.out.println("Departaments amb la suma de salaris superior a " + valorMinim + ":");
                while (resultado.next()) {
                    String nomDepartament = resultado.getString("NomDepartament");
                    double sumaSalari = resultado.getDouble("SumaSalari");
                    System.out.println(nomDepartament + "\t" + sumaSalari);
                }
            }

            // Consulta empleats sense departament assignat
            try (Statement stmt = con.createStatement()) {
                String sql = "SELECT NIF, Nom, Cognoms, Salari FROM Empleats WHERE IdDepartament IS NULL;";
                resultado = stmt.executeQuery(sql);

                System.out.println("Empleats sense departament assignat:");
                while (resultado.next()) {
                    String nif = resultado.getString("NIF");
                    String nom = resultado.getString("Nom");
                    String cognoms = resultado.getString("Cognoms");
                    double salari = resultado.getDouble("Salari");
                    System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + salari);
                }
            }

            // Salari màxim i mínim d'un departament específic
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT MAX(e.Salari) AS MaxSalari, MIN(e.Salari) AS MinSalari " +
                            "FROM Empleats e " +
                            "INNER JOIN Departaments d ON e.IdDepartament = d.IdDepartament " +
                            "WHERE d.NomDepartament = ?")) {

                Scanner scanner = new Scanner(System.in);
                System.out.print("Introdueix el nom del departament: ");
                String nomDepartament = scanner.nextLine();

                pstmt.setString(1, nomDepartament);
                resultado = pstmt.executeQuery();

                if (resultado.next()) {
                    double maxSalari = resultado.getDouble("MaxSalari");
                    double minSalari = resultado.getDouble("MinSalari");
                    System.out.println("Per al departament " + nomDepartament + ":");
                    System.out.println("Salari màxim: " + maxSalari);
                    System.out.println("Salari mínim: " + minSalari);
                } else {
                    System.out.println("No s'han trobat dades per al departament indicat.");
                }
            }

            // Empleats en departaments amb 'Desenvolupament' al nom
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT e.NIF, e.Nom, e.Cognoms, d.NomDepartament " +
                            "FROM Empleats e " +
                            "INNER JOIN Departaments d ON e.IdDepartament = d.IdDepartament " +
                            "WHERE d.NomDepartament LIKE ?")) {

                pstmt.setString(1, "%Desenvolupament%");
                resultado = pstmt.executeQuery();

                System.out.println("Empleats dels departaments amb 'Desenvolupament' al nom:");
                while (resultado.next()) {
                    String nif = resultado.getString("NIF");
                    String nom = resultado.getString("Nom");
                    String cognoms = resultado.getString("Cognoms");
                    String nomDepartament = resultado.getString("NomDepartament");
                    System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + nomDepartament);
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
