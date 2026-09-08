package com.sigepa;

import com.sigepa.dao.ProductoDAO;
import com.sigepa.model.*;
import com.sigepa.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final AuthService authService =
            new AuthService();

    private static final ClienteService clienteService =
            new ClienteService();

    private static final PedidoService pedidoService =
            new PedidoService();

    private static final StockService stockService =
            new StockService();

    private static final ProduccionService produccionService =
            new ProduccionService();

    private static final EntregaService entregaService =
            new EntregaService();

    private static final ProductoDAO productoDAO =
            new ProductoDAO();

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "                 SIGEPA"
        );

        System.out.println(
                " Sistema Integral de Gestión de Pedidos"
        );

        System.out.println(
                "=============================================="
        );

        try {

            Usuario usuario =
                    iniciarSesion();

            if (usuario == null) {

                System.out.println(
                        "Acceso denegado."
                );

                return;
            }

            System.out.println(
                    "\nBienvenido/a "
                            + usuario.getNombre()
                            + " "
                            + usuario.getApellido()
            );

            menuPrincipal(
                    usuario
            );

        } catch (Exception exception) {

            System.out.println(
                    "ERROR: "
                            + exception.getMessage()
            );
        }
    }

    private static Usuario iniciarSesion()
            throws Exception {

        for (
                int intento = 1;
                intento <= 3;
                intento++
        ) {

            System.out.print(
                    "Usuario: "
            );

            String usuario =
                    scanner.nextLine();

            System.out.print(
                    "Contraseña: "
            );

            String password =
                    scanner.nextLine();

            Usuario usuarioEncontrado =
                    authService.login(
                            usuario,
                            password
                    );

            if (usuarioEncontrado != null) {

                return usuarioEncontrado;
            }

            System.out.println(
                    "Credenciales incorrectas."
            );
        }

        return null;
    }

    private static void menuPrincipal(
            Usuario usuario
    ) {

        boolean salir =
                false;

        while (!salir) {

            try {

                System.out.println(
                        "\n=============================="
                );

                System.out.println(
                        "        MENÚ PRINCIPAL"
                );

                System.out.println(
                        "=============================="
                );

                System.out.println(
                        "1. Clientes"
                );

                System.out.println(
                        "2. Productos"
                );

                System.out.println(
                        "3. Pedidos"
                );

                System.out.println(
                        "4. Stock"
                );

                System.out.println(
                        "5. Producción"
                );

                System.out.println(
                        "6. Entregas"
                );

                System.out.println(
                        "0. Cerrar sesión"
                );

                System.out.print(
                        "Opción: "
                );

                String opcion =
                        scanner.nextLine();

                switch (opcion) {

                    case "1":
                        menuClientes();
                        break;

                    case "2":
                        listarProductos();
                        break;

                    case "3":
                        menuPedidos();
                        break;

                    case "4":
                        menuStock(
                                usuario
                        );
                        break;

                    case "5":
                        menuProduccion();
                        break;

                    case "6":
                        menuEntregas();
                        break;

                    case "0":

                        salir = true;

                        break;

                    default:

                        System.out.println(
                                "Opción inválida."
                        );
                }

            } catch (Exception exception) {

                System.out.println(
                        "ERROR: "
                                + exception.getMessage()
                );
            }
        }

        System.out.println(
                "Sesión finalizada."
        );
    }

    // ==============================
    // CLIENTES
    // ==============================

    private static void menuClientes()
            throws Exception {

        System.out.println(
                "\n1. Listar clientes"
        );

        System.out.println(
                "2. Registrar cliente"
        );

        System.out.print(
                "Opción: "
        );

        String opcion =
                scanner.nextLine();

        if (opcion.equals("1")) {

            listarClientes();

        } else if (opcion.equals("2")) {

            registrarCliente();
        }
    }

    private static void listarClientes()
            throws Exception {

        List<Cliente> clientes =
                clienteService.listar();

        System.out.println(
                "\n--- CLIENTES ---"
        );

        for (
                Cliente cliente :
                clientes
        ) {

            System.out.println(
                    cliente
            );
        }
    }

    private static void registrarCliente()
            throws Exception {

        System.out.println(
                "\n--- NUEVO CLIENTE ---"
        );

        System.out.print(
                "Nombre: "
        );

        String nombre =
                scanner.nextLine();

        System.out.print(
                "Documento: "
        );

        String documento =
                scanner.nextLine();

        System.out.print(
                "Teléfono: "
        );

        String telefono =
                scanner.nextLine();

        System.out.print(
                "Email: "
        );

        String email =
                scanner.nextLine();

        System.out.print(
                "Dirección: "
        );

        String direccion =
                scanner.nextLine();

        Cliente cliente =
                new Cliente(
                        nombre,
                        documento,
                        telefono,
                        email,
                        direccion
                );

        clienteService.registrar(
                cliente
        );

        System.out.println(
                "Cliente registrado."
        );
    }

    // ==============================
    // PRODUCTOS
    // ==============================

    private static void listarProductos()
            throws Exception {

        System.out.println(
                "\n--- PRODUCTOS ---"
        );

        for (
                Producto producto :
                productoDAO.listar()
        ) {

            System.out.println(
                    producto
            );
        }
    }

    // ==============================
    // PEDIDOS
    // ==============================

    private static void menuPedidos()
            throws Exception {

        System.out.println(
                "\n1. Listar pedidos"
        );

        System.out.println(
                "2. Registrar pedido"
        );

        System.out.println(
                "3. Confirmar pedido"
        );

        System.out.print(
                "Opción: "
        );

        String opcion =
                scanner.nextLine();

        switch (opcion) {

            case "1":
                listarPedidos();
                break;

            case "2":
                registrarPedido();
                break;

            case "3":
                confirmarPedido();
                break;
        }
    }

    private static void listarPedidos()
            throws Exception {

        System.out.println(
                "\n--- PEDIDOS ---"
        );

        for (
                Pedido pedido :
                pedidoService.listar()
        ) {

            System.out.println(
                    pedido
            );
        }
    }

    private static void registrarPedido()
            throws Exception {

        listarClientes();

        System.out.print(
                "\nID del cliente: "
        );

        int idCliente =
                Integer.parseInt(
                        scanner.nextLine()
                );

        Pedido pedido =
                new Pedido();

        pedido.setIdCliente(
                idCliente
        );

        System.out.print(
                "Observaciones: "
        );

        pedido.setObservaciones(
                scanner.nextLine()
        );

        boolean agregarProductos =
                true;

        while (agregarProductos) {

            listarProductos();

            System.out.print(
                    "\nID producto: "
            );

            int idProducto =
                    Integer.parseInt(
                            scanner.nextLine()
                    );

            Producto producto =
                    productoDAO.buscarPorId(
                            idProducto
                    );

            if (producto == null) {

                System.out.println(
                        "Producto inexistente."
                );

                continue;
            }

            System.out.print(
                    "Cantidad: "
            );

            BigDecimal cantidad =
                    new BigDecimal(
                            scanner.nextLine()
                    );

            DetallePedido detalle =
                    new DetallePedido(
                            producto.getIdProducto(),
                            cantidad,
                            producto.getPrecio()
                    );

            pedido.agregarDetalle(
                    detalle
            );

            System.out.println(
                    "Subtotal: $"
                            + detalle.getSubtotal()
            );

            System.out.print(
                    "¿Agregar otro producto? (S/N): "
            );

            String respuesta =
                    scanner.nextLine();

            agregarProductos =
                    respuesta.equalsIgnoreCase(
                            "S"
                    );
        }

        int idPedido =
                pedidoService.registrar(
                        pedido
                );

        System.out.println(
                "Pedido registrado correctamente."
        );

        System.out.println(
                "Número de pedido: "
                        + idPedido
        );

        System.out.println(
                "Total: $"
                        + pedido.getTotal()
        );
    }

    private static void confirmarPedido()
            throws Exception {

        listarPedidos();

        System.out.print(
                "ID pedido a confirmar: "
        );

        int idPedido =
                Integer.parseInt(
                        scanner.nextLine()
                );

        pedidoService.confirmarPedido(
                idPedido
        );

        System.out.println(
                "Pedido confirmado."
        );
    }

    // ==============================
    // STOCK
    // ==============================

    private static void menuStock(
            Usuario usuario
    ) throws Exception {

        System.out.println(
                "\n1. Consultar stock"
        );

        System.out.println(
                "2. Registrar ingreso"
        );

        System.out.println(
                "3. Registrar egreso"
        );

        System.out.print(
                "Opción: "
        );

        String opcion =
                scanner.nextLine();

        if (opcion.equals("1")) {

            listarStock();

        } else if (
                opcion.equals("2")
                        || opcion.equals("3")
        ) {

            registrarMovimiento(
                    usuario,
                    opcion.equals("2")
                            ? "INGRESO"
                            : "EGRESO"
            );
        }
    }

    private static void listarStock()
            throws Exception {

        System.out.println(
                "\n--- STOCK ---"
        );

        for (
                StockItem item :
                stockService.listar()
        ) {

            System.out.println(
                    item
            );

            if (
                    item.necesitaReposicion()
            ) {

                System.out.println(
                        "  *** ALERTA DE STOCK BAJO ***"
                );
            }
        }
    }

    private static void registrarMovimiento(
            Usuario usuario,
            String tipo
    ) throws Exception {

        listarStock();

        System.out.print(
                "ID material: "
        );

        int idMaterial =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Cantidad: "
        );

        BigDecimal cantidad =
                new BigDecimal(
                        scanner.nextLine()
                );

        System.out.print(
                "Motivo: "
        );

        String motivo =
                scanner.nextLine();

        stockService.registrarMovimiento(
                idMaterial,
                usuario.getIdUsuario(),
                tipo,
                cantidad,
                motivo
        );

        System.out.println(
                "Movimiento registrado."
        );
    }

    // ==============================
    // PRODUCCIÓN
    // ==============================

    private static void menuProduccion()
            throws Exception {

        System.out.println(
                "\n1. Listar órdenes"
        );

        System.out.println(
                "2. Generar orden"
        );

        System.out.println(
                "3. Finalizar orden"
        );

        System.out.print(
                "Opción: "
        );

        String opcion =
                scanner.nextLine();

        switch (opcion) {

            case "1":

                listarProduccion();

                break;

            case "2":

                generarProduccion();

                break;

            case "3":

                finalizarProduccion();

                break;
        }
    }

    private static void listarProduccion()
            throws Exception {

        System.out.println(
                "\n--- PRODUCCIÓN ---"
        );

        for (
                OrdenProduccion orden :
                produccionService.listar()
        ) {

            System.out.println(
                    orden
            );
        }
    }

    private static void generarProduccion()
            throws Exception {

        listarPedidos();

        System.out.print(
                "ID pedido: "
        );

        int idPedido =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Observaciones: "
        );

        String observaciones =
                scanner.nextLine();

        produccionService.generarOrden(
                idPedido,
                observaciones
        );

        System.out.println(
                "Orden de producción creada."
        );
    }

    private static void finalizarProduccion()
            throws Exception {

        listarProduccion();

        System.out.print(
                "ID orden: "
        );

        int idOrden =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "ID pedido: "
        );

        int idPedido =
                Integer.parseInt(
                        scanner.nextLine()
                );

        produccionService.finalizarOrden(
                idOrden,
                idPedido
        );

        System.out.println(
                "Producción finalizada."
        );
    }

    // ==============================
    // ENTREGAS
    // ==============================

    private static void menuEntregas()
            throws Exception {

        System.out.println(
                "\n1. Listar entregas"
        );

        System.out.println(
                "2. Registrar entrega"
        );

        System.out.print(
                "Opción: "
        );

        String opcion =
                scanner.nextLine();

        if (opcion.equals("1")) {

            listarEntregas();

        } else if (
                opcion.equals("2")
        ) {

            registrarEntrega();
        }
    }

    private static void listarEntregas()
            throws Exception {

        System.out.println(
                "\n--- ENTREGAS ---"
        );

        for (
                Entrega entrega :
                entregaService.listar()
        ) {

            System.out.println(
                    entrega
            );
        }
    }

    private static void registrarEntrega()
            throws Exception {

        listarPedidos();

        System.out.print(
                "ID pedido: "
        );

        int idPedido =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Responsable de entrega: "
        );

        String responsable =
                scanner.nextLine();

        System.out.print(
                "Observaciones: "
        );

        String observaciones =
                scanner.nextLine();

        entregaService.registrarEntrega(
                idPedido,
                responsable,
                observaciones
        );

        System.out.println(
                "Entrega registrada correctamente."
        );
    }
}