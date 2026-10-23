package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Conexion;
import model.Producto;

public class ProductoDAOImpl implements ProductoDAO {
	private Connection obtenerConexionSegura() throws SQLException {

		Connection conexion = Conexion.obtenerConexion();

		if (conexion == null) {
			throw new SQLException("No se pudo obtener la conexión con MySQL.");
		}

		return conexion;
	}
	public void guardar(Producto producto) throws SQLException {

		String sql = """
				INSERT INTO productos (nombre, precio, stock)
				VALUES (?, ?, ?)
				""";

		try (Connection conexion = obtenerConexionSegura();
				PreparedStatement statement = conexion.prepareStatement(sql)) {

			statement.setString(1, producto.getNombre());
			statement.setDouble(2, producto.getPrecio());
			statement.setInt(3, producto.getStock());

			statement.executeUpdate();
		}
	}

	public List<Producto> listarTodos() throws SQLException {

		String sql = """
				SELECT id, nombre, precio, stock
				FROM productos
				ORDER BY id
				""";

		List<Producto> productos = new ArrayList<>();

		try (Connection conexion = obtenerConexionSegura();
				PreparedStatement statement = conexion.prepareStatement(sql);
				ResultSet resultado = statement.executeQuery()) {

			while (resultado.next()) {

				Producto producto = new Producto(resultado.getInt("id"), resultado.getString("nombre"),
						resultado.getDouble("precio"), resultado.getInt("stock"));

				productos.add(producto);
			}
		}

		return productos;
	}

	public Optional<Producto> buscarPorId(int id) throws SQLException {

		String sql = """
				SELECT id, nombre, precio, stock
				FROM productos
				WHERE id = ?
				""";

		try (Connection conexion = obtenerConexionSegura();
				PreparedStatement statement = conexion.prepareStatement(sql)) {

			statement.setInt(1, id);

			try (ResultSet resultado = statement.executeQuery()) {

				if (resultado.next()) {

					Producto producto = new Producto(resultado.getInt("id"), resultado.getString("nombre"),
							resultado.getDouble("precio"), resultado.getInt("stock"));

					return Optional.of(producto);
				}
			}
		}

		return Optional.empty();
	}

	@Override
	public boolean eliminar(int id) throws SQLException {
		String sql = "DELETE FROM productos WHERE id = ?";

		try (Connection conexion = obtenerConexionSegura();
				PreparedStatement statement = conexion.prepareStatement(sql)) {

			statement.setInt(1, id);

			return statement.executeUpdate() > 0;
		}
	}
}
