package ar.edu.uces.proyecto_productos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import dao.ProductoDAO;
import dao.ProductoDAOImpl;
import database.Conexion;
import model.Producto;
import service.ProductoService;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Probando conexión a MySQL...");

		Connection conexion = Conexion.obtenerConexion();

		if (conexion != null) {

			System.out.println("PRUEBA EXITOSA: Java se conectó a MySQL.");
			ProductoDAO productoDAO = new ProductoDAOImpl();

			ProductoService productoService = new ProductoService(productoDAO);

			try (Scanner scanner = new Scanner(System.in)) {

				int opcion;

				do {
					mostrarMenu();

					opcion = leerEntero(scanner, "Seleccioná una opción: ");

					try {
						switch (opcion) {

						case 1 -> registrarProducto(scanner, productoService);

						case 2 -> listarProductos(productoService);

						case 3 -> buscarProducto(scanner, productoService);

						case 4 -> eliminarProducto(scanner, productoService);

						case 0 -> System.out.println("¡Hasta la próxima!");

						default -> System.out.println("Opción inválida.");
						}

					} catch (IllegalArgumentException e) {

						System.out.println("Dato inválido: " + e.getMessage());

					} catch (SQLException e) {

						System.err.println("Error de base de datos: " + e.getMessage());
					}

				} while (opcion != 0);
			}

			try {
				conexion.close();

				System.out.println("Conexión cerrada correctamente.");

			} catch (java.sql.SQLException e) {

				System.err.println("Error al cerrar la conexión: " + e.getMessage());
			}

		} else {

			System.out.println("PRUEBA FALLIDA: No fue posible conectar.");
		}
	}

	private static void mostrarMenu() {

		System.out.println("\n===== GESTIÓN DE PRODUCTOS =====");
		System.out.println("1. Registrar producto");
		System.out.println("2. Listar productos");
		System.out.println("3. Buscar producto por ID");
		System.out.println("4. Eliminar producto");
		System.out.println("0. Salir");
	}

	private static void registrarProducto(Scanner scanner, ProductoService service) throws SQLException {

		System.out.println("\n--- Registrar producto ---");

		System.out.print("Nombre: ");
		String nombre = scanner.nextLine();

		double precio = leerDouble(scanner, "Precio: ");

		int stock = leerEntero(scanner, "Stock: ");

		service.registrarProducto(nombre, precio, stock);

		System.out.println("Producto registrado correctamente.");
	}

	private static void listarProductos(ProductoService service) throws SQLException {

		System.out.println("\n--- Lista de productos ---");

		List<Producto> productos = service.obtenerProductos();

		if (productos.isEmpty()) {
			System.out.println("No hay productos registrados.");
			return;
		}

		productos.forEach(System.out::println);
	}

	private static void buscarProducto(Scanner scanner, ProductoService service) throws SQLException {

		int id = leerEntero(scanner, "Ingresá el ID: ");

		Optional<Producto> producto = service.obtenerProductoPorId(id);

		producto.ifPresentOrElse(System.out::println, () -> System.out.println("No se encontró el producto."));
	}

	private static void eliminarProducto(Scanner scanner, ProductoService service) throws SQLException {

		int id = leerEntero(scanner, "ID del producto a eliminar: ");

		boolean eliminado = service.eliminarProducto(id);

		if (eliminado) {
			System.out.println("Producto eliminado correctamente.");
		} else {
			System.out.println("No existe un producto con ese ID.");
		}
	}

	private static int leerEntero(Scanner scanner, String mensaje) {

		System.out.print(mensaje);

		String entrada = scanner.nextLine().trim();

		try {
			return Integer.parseInt(entrada);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Ingresá un número entero válido.");
		}
	}

	private static double leerDouble(Scanner scanner, String mensaje) {

		System.out.print(mensaje);

		String entrada = scanner.nextLine().trim().replace(',', '.');

		try {
			double valor = Double.parseDouble(entrada);

			if (!Double.isFinite(valor)) {
				throw new NumberFormatException();
			}

			return valor;

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Ingresá un número decimal válido.");
		}
	}
}