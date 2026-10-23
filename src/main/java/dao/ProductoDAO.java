package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Producto;

public interface ProductoDAO {
	void guardar(Producto producto) throws SQLException;

	List<Producto> listarTodos() throws SQLException;

	Optional<Producto> buscarPorId(int id) throws SQLException;

	boolean eliminar(int id) throws SQLException;
}
