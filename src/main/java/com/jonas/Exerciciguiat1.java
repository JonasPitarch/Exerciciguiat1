package com.jonas;

import java.sql.*;

public class Exerciciguiat1 {
    public static void main(String[] args) {
        Connection con = null;
        Statement sentenciaSQL = null;
        ResultSet resultado = null;

        try {
            //Connectar BDA
            con= DriverManager.getConnection("jdbc:sqlite:src/main/resources/Empresa.db");
            System.out.println("Connxion establecida");
            System.out.println("********************");

            //Crear consulta
            String sql = "SELECT * FROM Empleats";
            sentenciaSQL=con.createStatement();
            resultado=sentenciaSQL.executeQuery(sql);

            while(resultado.next()) {
                String nif =resultado.getString("nif");
                String nom=resultado.getString("nom");
                String cognoms=resultado.getString("cognoms");
                Double salari=resultado.getDouble("salari");

                System.out.println(nif + "\t" + nom + "\t" + cognoms + "\t" + salari);

            }

            // Consulta 1
            System.out.println("***********");
            System.out.println("Consulta 1:");
            String sql1="SELECT * FROM Empleats WHERE salari > 2000";
            resultado=sentenciaSQL.executeQuery(sql1);

            while(resultado.next()) {
                String nif =resultado.getString("nif");
                String nom=resultado.getString("nom");
                String cognoms=resultado.getString("cognoms");
                Double salari=resultado.getDouble("salari");
                System.out.println("Empleados con salario mayor a 2000" + "" + nif + "\t" + nom + "\t" + cognoms + "\t" + salari);

            }
            // Consulta 2
//            System.out.println("***********");
//            System.out.println("Consulta 2:");
//            String sql2="SELECT nif, nom FROM Empleats WHERE cognoms = 'Soler Sánchez'";
//            resultado=sentenciaSQL.executeQuery(sql2);
//            while(resultado.next()) {
//                String nif =resultado.getString("nif");
//                String nom=resultado.getString("nom");
//                String cognoms=resultado.getString("cognoms");
//                Double salari=resultado.getDouble("salari");
//                System.out.println("Empleado cuyo apellido es Soler Sanchez"+""+nif + "\t" + nom + "\t" + cognoms+ "\t" + salari);
//            }
            // Consulta 3

            System.out.println("***********");
            System.out.println("Consulta 3:");
            String sql3="SELECT * FROM Empleats ORDER BY salari DESC";
            resultado=sentenciaSQL.executeQuery(sql3);
            while(resultado.next()) {
                String nif =resultado.getString("nif");
                String nom=resultado.getString("nom");
                String cognoms=resultado.getString("cognoms");
                Double salari=resultado.getDouble("salari");
                System.out.println("Salario ordenado en orden descendente "+""+nif + "\t" + nom + "\t" + cognoms+ "\t" + salari);
            }

            //Consulta 4
            System.out.println("************");
            System.out.println("Consulta 4:");
            String sql4="SELECT AVG(salari) AS salari_mitja FROM Empleats";
            resultado=sentenciaSQL.executeQuery(sql4);
            while(resultado.next()) {
                String nif =resultado.getString("nif");
                String nom=resultado.getString("nom");
                String cognoms=resultado.getString("cognoms");
                Double salari=resultado.getDouble("salari");
                System.out.println("Media de los salarios"+""+nif + "\t" + nom + "\t" + cognoms+ "\t" + salari);
            }

            resultado.close();
            sentenciaSQL.close();
            con.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
