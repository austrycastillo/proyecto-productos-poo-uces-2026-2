package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	private static final String URL = "jdbc:mysql://localhost:3306/base_productos";

	private static final String USUARIO = "root";
	private static final String PASSWORD = "";

	private Conexion() {

	}

	public static Connection obtenerConexion() {

		try {
			Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);

			System.out.println("¡Conexión exitosa con la base de datos!");

			return conexion;

		} catch (SQLException e) {

			System.err.println("Error al conectar con MySQL: " + e.getMessage());

			return null;
		}
	}
}
