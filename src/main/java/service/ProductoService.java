package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import dao.ProductoDAO;
import model.Producto;

public class ProductoService {
	private final ProductoDAO productoDAO;

	public ProductoService(ProductoDAO productoDAO) {
		this.productoDAO = productoDAO;
	}

	public void registrarProducto(String nombre, double precio, int stock) throws SQLException {

		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre es obligatorio.");
		}

		if (precio <= 0) {

			throw new IllegalArgumentException("El precio debe ser mayor que cero.");
		}

		if (stock < 0) {
			throw new IllegalArgumentException("El stock no puede ser negativo.");
		}

		Producto producto = new Producto(nombre.trim(), precio, stock);

		productoDAO.guardar(producto);
	}

	public List<Producto> obtenerProductos() throws SQLException {

		return productoDAO.listarTodos();
	}

	public Optional<Producto> obtenerProductoPorId(int id) throws SQLException {

		if (id <= 0) {
			throw new IllegalArgumentException("El ID debe ser mayor que cero.");
		}

		return productoDAO.buscarPorId(id);
	}

	public boolean eliminarProducto(int id) throws SQLException {

		if (id <= 0) {
			throw new IllegalArgumentException("El ID debe ser mayor que cero.");
		}

		return productoDAO.eliminar(id);
	}
}
