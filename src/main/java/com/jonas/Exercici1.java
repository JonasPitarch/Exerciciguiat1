package com.jonas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Exercici1 {
    public static void main(String[] args) {
        ;
        Statement sentenciaSQL = null;
        ResultSet resultado = null;
        try (Connection con =DriverManager.getConnection("jdbc:sqlite:src/main/resources/Empresa.db") ){
            System.out.println("Conexión establecida.");
            System.out.println("********");

            String sql = "SELECT * FROM Empleats";
            sentenciaSQL = con.createStatement();
            resultado = sentenciaSQL.executeQuery(sql);
            while(resultado.next()) {
                String nif =resultado.getString("nif");
                String nom=resultado.getString("nom");
                String cognoms=resultado.getString("cognoms");
                Double salari=resultado.getDouble("salari");
                System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + salari);
            }

            String sql2= "CREATE TABLE IF NOT EXISTS Departaments (\n" +
                    "    IdDepartament INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    NomDepartament VARCHAR(100) NOT NULL,\n" +
                    "    Responsable VARCHAR(100)\n" +
                    ");\n";
            sentenciaSQL.executeUpdate(sql2);
            System.out.println("Tabla departamentos creada correctamente.");

            String sql3= "ALTER TABLE Empleats ADD COLUMN IdDepartament INTEGER REFERENCES Departaments(IdDepartament);\n";
            sentenciaSQL.executeUpdate(sql3);
            System.out.println("Tabla empleados actualizada correctamente.");

            String sql4= "INSERT INTO Departaments (NomDepartament, Responsable) VALUES\n" +
                    "('Recursos Humans', 'Marta Pérez'),\n" +
                    "('Desenvolupament', 'Jaume Martí'),\n" +
                    "('Comptabilitat', 'Fina Soler');\n";
            sentenciaSQL.executeUpdate(sql4);
            System.out.println("Tabla departamentos actualizada correctamente.");

            String sql5= "UPDATE Empleats SET IdDepartament = CASE\n" +
                    "    WHEN NIF = '123456789' THEN 1 \n" +
                    "    WHEN NIF = '987654321' THEN 2\n" +
                    "    WHEN NIF = '111111111' THEN 3 \n" +
                    "    WHEN NIF = '222222222' THEN 1\n" +
                    "    WHEN NIF = '333333333' THEN 2 \n" +
                    "    WHEN NIF = '444444444' THEN 3 \n" +
                    "END;\n";
            sentenciaSQL.executeUpdate(sql5);
            System.out.println("Añadido a la tabla empleados el id del departamento al que pertenecen");




            resultado.close();
            sentenciaSQL.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
