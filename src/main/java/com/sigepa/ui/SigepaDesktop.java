package com.sigepa.ui;

import com.sigepa.model.StockItem;
import com.sigepa.service.StockService;
import com.sigepa.service.ClienteService;
import com.sigepa.service.PedidoService;
import com.sigepa.service.ProduccionService;
import com.sigepa.service.EntregaService;
import com.sigepa.util.CsvExport;
import com.sigepa.dao.ProductoDAO;
import com.sigepa.model.Producto;
import com.sigepa.model.DetallePedido;
import com.sigepa.model.Cliente;
import com.sigepa.model.Entrega;
import com.sigepa.model.OrdenProduccion;
import com.sigepa.model.Pedido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Interfaz Swing que reutiliza los servicios y DAO de SIGEPA. */
public final class SigepaDesktop extends JFrame {
    private final StockService stock = new StockService();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID material", "Material", "Cantidad", "Mínimo", "Ubicación", "Estado"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel status = new JLabel("Listo");
    private final JComboBox<String> tipo = new JComboBox<>(new String[]{"INGRESO", "EGRESO"}); // arreglo fijo
    private final JTextField material = new JTextField(8);
    private final JTextField usuario = new JTextField(8);
    private final JTextField cantidad = new JTextField(8);
    private final JTextField motivo = new JTextField(16);
    private List<StockItem> datos = new ArrayList<>(); // colección variable

    public SigepaDesktop() {
        super("SIGEPA — Gestión de pedidos y stock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setContentPane(root);
        JLabel title = new JLabel("SIGEPA | Panel de gestión");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 23f));
        root.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Inventario", inventoryPanel());
        tabs.addTab("Módulos", modulesPanel());
        root.add(tabs, BorderLayout.CENTER);
        root.add(status, BorderLayout.SOUTH);
        refreshStock();
    }

    private JPanel inventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        table.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Actualizar inventario");
        refresh.addActionListener(e -> refreshStock());
        JButton export = new JButton("Exportar CSV");
        export.addActionListener(e -> exportCsv());

        JButton reposicion = new JButton("Ver materiales para reposición");
        reposicion.addActionListener(e -> mostrarReposicion());

        buttons.add(refresh);
        buttons.add(export);
        buttons.add(reposicion);

        panel.add(buttons, BorderLayout.NORTH);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Registrar movimiento (persistencia MySQL)"));
        form.add(new JLabel("ID material")); form.add(material);
        form.add(new JLabel("ID usuario")); form.add(usuario);
        form.add(new JLabel("Tipo")); form.add(tipo);
        form.add(new JLabel("Cantidad")); form.add(cantidad);
        form.add(new JLabel("Motivo")); form.add(motivo);
        JButton save = new JButton("Guardar movimiento");
        save.addActionListener(e -> saveMovement());
        form.add(save);
        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }


    private JPanel modulesPanel() {

        JPanel principal = new JPanel(new BorderLayout(10, 10));

        principal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel titulo = new JLabel("Gestión general de SIGEPA");
        titulo.setFont(
                titulo.getFont().deriveFont(Font.BOLD, 20f)
        );

        principal.add(titulo, BorderLayout.NORTH);

        JPanel modulos = new JPanel(new GridLayout(2, 2, 15, 15));

        // =========================
        // MÓDULO CLIENTES
        // =========================

        JPanel clientes = new JPanel(new GridLayout(0, 1, 8, 8));
        clientes.setBorder(
                BorderFactory.createTitledBorder("Clientes")
        );

        JButton consultarClientes = new JButton("Consultar clientes");
        consultarClientes.addActionListener(e -> mostrarClientes());

        JButton nuevoCliente = new JButton("Registrar cliente");
        nuevoCliente.addActionListener(e -> registrarCliente());

        JButton editarCliente = new JButton("Modificar cliente");
        editarCliente.addActionListener(e -> modificarCliente());

        clientes.add(consultarClientes);
        clientes.add(nuevoCliente);
        clientes.add(editarCliente);

        // =========================
        // MÓDULO PEDIDOS
        // =========================

        JPanel pedidos = new JPanel(new GridLayout(0, 1, 8, 8));
        pedidos.setBorder(
                BorderFactory.createTitledBorder("Pedidos")
        );

        JButton consultarPedidos = new JButton("Consultar pedidos");
        consultarPedidos.addActionListener(e -> mostrarPedidos());

        JButton nuevoPedido = new JButton("Registrar pedido");
        nuevoPedido.addActionListener(e -> registrarPedido());

        JButton confirmarPedido = new JButton("Confirmar pedido");
        confirmarPedido.addActionListener(e -> confirmarPedido());

        pedidos.add(consultarPedidos);
        pedidos.add(nuevoPedido);
        pedidos.add(confirmarPedido);

        // =========================
        // MÓDULO PRODUCCIÓN
        // =========================

        JPanel produccion = new JPanel(new GridLayout(0, 1, 8, 8));
        produccion.setBorder(
                BorderFactory.createTitledBorder("Producción")
        );

        JButton consultarProduccion =
                new JButton("Consultar producción");

        consultarProduccion.addActionListener(
                e -> mostrarProduccion()
        );

        JButton generarProduccion =
                new JButton("Generar orden de producción");

        generarProduccion.addActionListener(
                e -> generarOrdenProduccion()
        );

        JButton finalizarProduccion =
                new JButton("Finalizar orden de producción");

        finalizarProduccion.addActionListener(
                e -> finalizarOrdenProduccion()
        );

        produccion.add(consultarProduccion);
        produccion.add(generarProduccion);
        produccion.add(finalizarProduccion);

        // =========================
        // MÓDULO ENTREGAS
        // =========================

        JPanel entregas = new JPanel(new GridLayout(0, 1, 8, 8));
        entregas.setBorder(
                BorderFactory.createTitledBorder("Entregas")
        );

        JButton consultarEntregas =
                new JButton("Consultar entregas");

        consultarEntregas.addActionListener(
                e -> mostrarEntregas()
        );

        JButton nuevaEntrega =
                new JButton("Registrar entrega");

        nuevaEntrega.addActionListener(
                e -> registrarEntrega()
        );

        entregas.add(consultarEntregas);
        entregas.add(nuevaEntrega);

        // =========================
        // ORGANIZACIÓN GENERAL
        // =========================

        modulos.add(clientes);
        modulos.add(pedidos);
        modulos.add(produccion);
        modulos.add(entregas);

        principal.add(modulos, BorderLayout.CENTER);

        JLabel pie = new JLabel(
                "SIGEPA | Sistema de Gestión de Pedidos, Producción y Stock"
        );

        pie.setHorizontalAlignment(SwingConstants.CENTER);

        principal.add(pie, BorderLayout.SOUTH);

        return principal;
    }


    private interface Query { List<?> run() throws Exception; }

    private void query(String label, Query q) {
        runDb(() -> {
            List<?> items = q.run();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"N°", "Registro"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            int numero = 1;

            for (Object item : items) {
                modelo.addRow(new Object[]{
                        numero++,
                        String.valueOf(item)
                });
            }

            JTable tabla = new JTable(modelo);
            tabla.setAutoCreateRowSorter(true);
            tabla.setRowHeight(28);

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setPreferredSize(new Dimension(800, 400));

            JOptionPane.showMessageDialog(
                    this,
                    scroll,
                    "SIGEPA - " + label + " (" + items.size() + " registros)",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void mostrarClientes() {
        runDb(() -> {
            List<Cliente> clientes = new ClienteService().listar();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{
                            "ID", "Nombre", "Documento",
                            "Teléfono", "Email", "Dirección", "Activo"
                    }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Cliente cliente : clientes) {
                modelo.addRow(new Object[]{
                        cliente.getIdCliente(),
                        cliente.getNombre(),
                        cliente.getDocumento(),
                        cliente.getTelefono(),
                        cliente.getEmail(),
                        cliente.getDireccion(),
                        cliente.isActivo() ? "Sí" : "No"
                });
            }

            JTable tabla = new JTable(modelo);
            tabla.setAutoCreateRowSorter(true);
            tabla.setRowHeight(26);

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setPreferredSize(new Dimension(950, 400));

            JOptionPane.showMessageDialog(
                    this,
                    scroll,
                    "SIGEPA - Clientes (" + clientes.size() + " registros)",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void mostrarPedidos() {
        runDb(() -> {
            List<Pedido> pedidos = new PedidoService().listar();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{
                            "ID Pedido",
                            "ID Cliente",
                            "Cliente",
                            "Fecha",
                            "Estado",
                            "Total",
                            "Observaciones"
                    }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Pedido pedido : pedidos) {
                modelo.addRow(new Object[]{
                        pedido.getIdPedido(),
                        pedido.getIdCliente(),
                        pedido.getNombreCliente(),
                        pedido.getFecha(),
                        pedido.getEstado(),
                        pedido.getTotal(),
                        pedido.getObservaciones()
                });
            }

            JTable tabla = new JTable(modelo);
            tabla.setAutoCreateRowSorter(true);
            tabla.setRowHeight(28);
            tabla.setSelectionMode(
                    ListSelectionModel.SINGLE_SELECTION
            );

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setPreferredSize(new Dimension(1000, 450));

            JOptionPane.showMessageDialog(
                    this,
                    scroll,
                    "SIGEPA - Pedidos (" + pedidos.size() + " registros)",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void mostrarProduccion() {
        runDb(() -> {
            List<OrdenProduccion> ordenes =
                    new ProduccionService().listar();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{
                            "ID Orden",
                            "ID Pedido",
                            "Fecha Inicio",
                            "Fecha Fin",
                            "Estado",
                            "Observaciones"
                    }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (OrdenProduccion orden : ordenes) {
                modelo.addRow(new Object[]{
                        orden.getIdOrden(),
                        orden.getIdPedido(),
                        orden.getFechaInicio(),
                        orden.getFechaFin(),
                        orden.getEstado(),
                        orden.getObservaciones()
                });
            }

            JTable tabla = new JTable(modelo);
            tabla.setAutoCreateRowSorter(true);
            tabla.setRowHeight(28);
            tabla.setSelectionMode(
                    ListSelectionModel.SINGLE_SELECTION
            );

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setPreferredSize(new Dimension(1000, 450));

            JOptionPane.showMessageDialog(
                    this,
                    scroll,
                    "SIGEPA - Producción (" + ordenes.size() + " registros)",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void mostrarEntregas() {
        runDb(() -> {
            List<Entrega> entregas = new EntregaService().listar();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{
                            "ID Entrega",
                            "ID Pedido",
                            "Fecha",
                            "Responsable",
                            "Estado",
                            "Observaciones"
                    }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Entrega entrega : entregas) {
                modelo.addRow(new Object[]{
                        entrega.getIdEntrega(),
                        entrega.getIdPedido(),
                        entrega.getFecha(),
                        entrega.getResponsable(),
                        entrega.getEstado(),
                        entrega.getObservaciones()
                });
            }

            JTable tabla = new JTable(modelo);
            tabla.setAutoCreateRowSorter(true);
            tabla.setRowHeight(28);
            tabla.setSelectionMode(
                    ListSelectionModel.SINGLE_SELECTION
            );

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setPreferredSize(new Dimension(1000, 450));

            JOptionPane.showMessageDialog(
                    this,
                    scroll,
                    "SIGEPA - Entregas (" + entregas.size() + " registros)",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void registrarCliente() {

        JTextField nombre = new JTextField(25);
        JTextField documento = new JTextField(25);
        JTextField telefono = new JTextField(25);
        JTextField email = new JTextField(25);
        JTextField direccion = new JTextField(25);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));

        formulario.add(new JLabel("Nombre *"));
        formulario.add(nombre);

        formulario.add(new JLabel("Documento"));
        formulario.add(documento);

        formulario.add(new JLabel("Teléfono"));
        formulario.add(telefono);

        formulario.add(new JLabel("Email"));
        formulario.add(email);

        formulario.add(new JLabel("Dirección"));
        formulario.add(direccion);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "SIGEPA - Registrar nuevo cliente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        runDb(() -> {

            Cliente nuevoCliente = new Cliente(
                    nombre.getText().trim(),
                    documento.getText().trim(),
                    telefono.getText().trim(),
                    email.getText().trim(),
                    direccion.getText().trim()
            );

            new ClienteService().registrar(nuevoCliente);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente en MySQL.",
                    "SIGEPA",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void modificarCliente() {
        ClienteService servicio = new ClienteService();

        runDb(() -> {
            List<Cliente> clientes = servicio.listar();

            if (clientes.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay clientes activos para modificar."
                );
                return;
            }

            String idTexto = JOptionPane.showInputDialog(
                    this,
                    "Ingresá el ID del cliente a modificar:",
                    "SIGEPA - Modificar cliente",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (idTexto == null) return;

            int id = Integer.parseInt(idTexto.trim());

            Cliente seleccionado = null;

            for (Cliente cliente : clientes) {
                if (cliente.getIdCliente() == id) {
                    seleccionado = cliente;
                    break;
                }
            }

            if (seleccionado == null) {
                throw new IllegalArgumentException(
                        "No existe un cliente activo con ese ID."
                );
            }

            JTextField nombre = new JTextField(
                    seleccionado.getNombre(), 25);
            JTextField documento = new JTextField(
                    seleccionado.getDocumento(), 25);
            JTextField telefono = new JTextField(
                    seleccionado.getTelefono(), 25);
            JTextField email = new JTextField(
                    seleccionado.getEmail(), 25);
            JTextField direccion = new JTextField(
                    seleccionado.getDireccion(), 25);

            JPanel formulario = new JPanel(
                    new GridLayout(5, 2, 10, 10)
            );

            formulario.add(new JLabel("Nombre *"));
            formulario.add(nombre);
            formulario.add(new JLabel("Documento"));
            formulario.add(documento);
            formulario.add(new JLabel("Teléfono"));
            formulario.add(telefono);
            formulario.add(new JLabel("Email"));
            formulario.add(email);
            formulario.add(new JLabel("Dirección"));
            formulario.add(direccion);

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Modificar cliente #" + id,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) return;

            seleccionado.setNombre(nombre.getText().trim());
            seleccionado.setDocumento(documento.getText().trim());
            seleccionado.setTelefono(telefono.getText().trim());
            seleccionado.setEmail(email.getText().trim());
            seleccionado.setDireccion(direccion.getText().trim());

            servicio.actualizar(seleccionado);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente actualizado correctamente en MySQL."
            );
        });
    }


    private void registrarPedido() {
        runDb(() -> {
            List<Cliente> clientes = new ClienteService().listar();
            List<Producto> productos = new ProductoDAO().listar();

            if (clientes.isEmpty() || productos.isEmpty()) {
                throw new IllegalArgumentException(
                        "Debe existir al menos un cliente y un producto activo."
                );
            }

            JComboBox<String> comboClientes = new JComboBox<>();
            for (Cliente cliente : clientes) {
                comboClientes.addItem(
                        cliente.getIdCliente() + " - " + cliente.getNombre()
                );
            }

            JComboBox<String> comboProductos = new JComboBox<>();
            for (Producto producto : productos) {
                comboProductos.addItem(
                        producto.getIdProducto() + " - "
                                + producto.getNombre()
                                + " ($" + producto.getPrecio() + ")"
                );
            }

            JTextField campoCantidad = new JTextField("1", 8);
            JTextField campoObservaciones = new JTextField(30);

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{
                            "ID Producto", "Producto", "Cantidad",
                            "Precio unitario", "Subtotal"
                    }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable tabla = new JTable(modelo);
            tabla.setRowHeight(26);

            List<DetallePedido> detalles = new ArrayList<>();
            JLabel etiquetaTotal = new JLabel("Total: $0");

            JButton agregar = new JButton("Agregar producto");
            JButton quitar = new JButton("Quitar seleccionado");

            agregar.addActionListener(e -> {
                try {
                    int indice = comboProductos.getSelectedIndex();

                    if (indice < 0) return;

                    Producto producto = productos.get(indice);
                    BigDecimal cantidad = new BigDecimal(
                            campoCantidad.getText().trim()
                    );

                    if (cantidad.signum() <= 0) {
                        throw new IllegalArgumentException(
                                "La cantidad debe ser mayor que cero."
                        );
                    }

                    if (producto.getPrecio() == null
                            || producto.getPrecio().signum() < 0) {
                        throw new IllegalArgumentException(
                                "El producto no tiene un precio válido."
                        );
                    }

                    DetallePedido detalle = new DetallePedido(
                            producto.getIdProducto(),
                            cantidad,
                            producto.getPrecio()
                    );

                    detalles.add(detalle);

                    modelo.addRow(new Object[]{
                            producto.getIdProducto(),
                            producto.getNombre(),
                            cantidad,
                            producto.getPrecio(),
                            detalle.getSubtotal()
                    });

                    BigDecimal total = BigDecimal.ZERO;
                    for (DetallePedido item : detalles) {
                        total = total.add(item.getSubtotal());
                    }

                    etiquetaTotal.setText("Total: $" + total);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this, ex.getMessage(),
                            "Error al agregar producto",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            quitar.addActionListener(e -> {
                int fila = tabla.getSelectedRow();

                if (fila < 0) {
                    JOptionPane.showMessageDialog(
                            this, "Seleccioná un producto de la tabla."
                    );
                    return;
                }

                int indiceModelo = tabla.convertRowIndexToModel(fila);

                detalles.remove(indiceModelo);
                modelo.removeRow(indiceModelo);

                BigDecimal total = BigDecimal.ZERO;
                for (DetallePedido item : detalles) {
                    total = total.add(item.getSubtotal());
                }

                etiquetaTotal.setText("Total: $" + total);
            });

            JPanel formulario = new JPanel(new BorderLayout(10, 10));

            JPanel encabezado = new JPanel(new GridLayout(2, 2, 10, 10));
            encabezado.add(new JLabel("Cliente:"));
            encabezado.add(comboClientes);
            encabezado.add(new JLabel("Observaciones:"));
            encabezado.add(campoObservaciones);

            JPanel seleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
            seleccion.add(new JLabel("Producto:"));
            seleccion.add(comboProductos);
            seleccion.add(new JLabel("Cantidad:"));
            seleccion.add(campoCantidad);
            seleccion.add(agregar);
            seleccion.add(quitar);

            JPanel centro = new JPanel(new BorderLayout(5, 5));
            centro.add(seleccion, BorderLayout.NORTH);
            centro.add(new JScrollPane(tabla), BorderLayout.CENTER);

            JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pie.add(etiquetaTotal);

            formulario.add(encabezado, BorderLayout.NORTH);
            formulario.add(centro, BorderLayout.CENTER);
            formulario.add(pie, BorderLayout.SOUTH);
            formulario.setPreferredSize(new Dimension(900, 450));

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "SIGEPA - Registrar pedido",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) return;

            if (detalles.isEmpty()) {
                throw new IllegalArgumentException(
                        "Agregá al menos un producto al pedido."
                );
            }

            Cliente cliente = clientes.get(
                    comboClientes.getSelectedIndex()
            );

            Pedido pedido = new Pedido();
            pedido.setIdCliente(cliente.getIdCliente());
            pedido.setObservaciones(
                    campoObservaciones.getText().trim()
            );

            for (DetallePedido detalle : detalles) {
                pedido.agregarDetalle(detalle);
            }

            int idPedido = new PedidoService().registrar(pedido);

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido #" + idPedido
                            + " registrado correctamente.\n"
                            + "Total: $" + pedido.getTotal()
                            + "\nEstado: " + pedido.getEstado(),
                    "SIGEPA - Pedido registrado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void confirmarPedido() {

        runDb(() -> {

            List<Pedido> pedidos = new PedidoService().listar();

            List<Pedido> pendientes = new ArrayList<>();

            for (Pedido pedido : pedidos) {
                if ("PENDIENTE".equals(pedido.getEstado())) {
                    pendientes.add(pedido);
                }
            }

            if (pendientes.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay pedidos pendientes para confirmar.",
                        "SIGEPA",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            JComboBox<String> comboPedidos = new JComboBox<>();

            for (Pedido pedido : pendientes) {
                comboPedidos.addItem(
                        "Pedido #" + pedido.getIdPedido()
                                + " | Cliente: " + pedido.getNombreCliente()
                                + " | Total: $" + pedido.getTotal()
                );
            }

            JPanel formulario = new JPanel(
                    new GridLayout(2, 1, 10, 10)
            );

            formulario.add(new JLabel(
                    "Seleccioná el pedido pendiente a confirmar:"
            ));
            formulario.add(comboPedidos);

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "SIGEPA - Confirmar pedido",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return;
            }

            Pedido seleccionado = pendientes.get(
                    comboPedidos.getSelectedIndex()
            );

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Confirmar el pedido #"
                            + seleccionado.getIdPedido()
                            + "?\n\nCliente: "
                            + seleccionado.getNombreCliente()
                            + "\nTotal: $"
                            + seleccionado.getTotal(),
                    "Confirmar operación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            new PedidoService().confirmarPedido(
                    seleccionado.getIdPedido()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido #" + seleccionado.getIdPedido()
                            + " confirmado correctamente.\n"
                            + "Nuevo estado: CONFIRMADO",
                    "SIGEPA",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private void generarOrdenProduccion() {

        runDb(() -> {

            List<Pedido> pedidos =
                    new PedidoService().listar();

            List<Pedido> confirmados =
                    new ArrayList<>();

            for (Pedido pedido : pedidos) {

                if ("CONFIRMADO".equals(pedido.getEstado())) {
                    confirmados.add(pedido);
                }
            }

            if (confirmados.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay pedidos confirmados para producción."
                );
                return;
            }

            JComboBox<String> comboPedidos =
                    new JComboBox<>();

            for (Pedido pedido : confirmados) {

                comboPedidos.addItem(
                        "Pedido #" + pedido.getIdPedido()
                                + " | Cliente: " + pedido.getNombreCliente()
                                + " | Total: $" + pedido.getTotal()
                );
            }

            JTextField observaciones =
                    new JTextField(25);

            JPanel formulario =
                    new JPanel(new GridLayout(2, 2, 10, 10));

            formulario.add(new JLabel("Pedido confirmado:"));
            formulario.add(comboPedidos);

            formulario.add(new JLabel("Observaciones:"));
            formulario.add(observaciones);

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Generar orden de producción",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return;
            }

            Pedido seleccionado =
                    confirmados.get(
                            comboPedidos.getSelectedIndex()
                    );

            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Generar orden de producción para el pedido #"
                            + seleccionado.getIdPedido() + "?",
                    "Confirmar operación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            new ProduccionService().generarOrden(
                    seleccionado.getIdPedido(),
                    observaciones.getText().trim()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Orden de producción generada correctamente.\n"
                            + "Pedido #" + seleccionado.getIdPedido()
                            + " actualizado a EN PRODUCCION."
            );
        });
    }


    private void finalizarOrdenProduccion() {

        runDb(() -> {

            List<OrdenProduccion> ordenes =
                    new ProduccionService().listar();

            List<OrdenProduccion> activas =
                    new ArrayList<>();

            for (OrdenProduccion orden : ordenes) {

                if ("EN PRODUCCION".equals(orden.getEstado())) {
                    activas.add(orden);
                }
            }

            if (activas.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay órdenes de producción activas."
                );
                return;
            }

            JComboBox<String> comboOrdenes =
                    new JComboBox<>();

            for (OrdenProduccion orden : activas) {

                comboOrdenes.addItem(
                        "Orden #" + orden.getIdOrden()
                                + " | Pedido #" + orden.getIdPedido()
                                + " | Inicio: " + orden.getFechaInicio()
                );
            }

            JPanel formulario =
                    new JPanel(new GridLayout(1, 2, 10, 10));

            formulario.add(new JLabel("Orden activa:"));
            formulario.add(comboOrdenes);

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Finalizar orden de producción",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return;
            }

            OrdenProduccion seleccionada =
                    activas.get(comboOrdenes.getSelectedIndex());

            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Finalizar la orden #" + seleccionada.getIdOrden()
                            + " del pedido #" + seleccionada.getIdPedido()
                            + "?\n\nEsta operación actualizará ambos estados "
                            + "a TERMINADO.",
                    "Confirmar finalización",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            new ProduccionService().finalizarOrden(
                    seleccionada.getIdOrden(),
                    seleccionada.getIdPedido()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Orden #" + seleccionada.getIdOrden()
                            + " finalizada correctamente.\n"
                            + "Pedido #" + seleccionada.getIdPedido()
                            + " actualizado a TERMINADO."
            );
        });
    }


    private void registrarEntrega() {

        runDb(() -> {

            List<Pedido> pedidos =
                    new PedidoService().listar();

            List<Pedido> terminados =
                    new ArrayList<>();

            for (Pedido pedido : pedidos) {

                if ("TERMINADO".equals(pedido.getEstado())) {
                    terminados.add(pedido);
                }
            }

            if (terminados.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay pedidos terminados disponibles para entregar.",
                        "Registrar entrega",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            JComboBox<String> comboPedidos =
                    new JComboBox<>();

            for (Pedido pedido : terminados) {

                comboPedidos.addItem(
                        "Pedido #" + pedido.getIdPedido()
                                + " | Cliente: " + pedido.getNombreCliente()
                                + " | Total: $" + pedido.getTotal()
                );
            }

            JTextField campoResponsable =
                    new JTextField(25);

            JTextField campoObservaciones =
                    new JTextField(25);

            JPanel formulario =
                    new JPanel(new GridLayout(3, 2, 10, 10));

            formulario.add(new JLabel("Pedido terminado:"));
            formulario.add(comboPedidos);

            formulario.add(new JLabel("Responsable:"));
            formulario.add(campoResponsable);

            formulario.add(new JLabel("Observaciones:"));
            formulario.add(campoObservaciones);

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Registrar entrega",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return;
            }

            String responsable =
                    campoResponsable.getText().trim();

            String observaciones =
                    campoObservaciones.getText().trim();

            if (responsable.isEmpty()) {
                throw new IllegalArgumentException(
                        "Debe ingresar el responsable de la entrega."
                );
            }

            Pedido seleccionado =
                    terminados.get(comboPedidos.getSelectedIndex());

            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Registrar la entrega del pedido #"
                            + seleccionado.getIdPedido()
                            + "?\n\nResponsable: " + responsable
                            + "\n\nEl pedido pasará a estado ENTREGADO.",
                    "Confirmar entrega",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            new EntregaService().registrarEntrega(
                    seleccionado.getIdPedido(),
                    responsable,
                    observaciones
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega registrada correctamente.\n\n"
                            + "Pedido #" + seleccionado.getIdPedido()
                            + "\nResponsable: " + responsable
                            + "\nEstado: ENTREGADO",
                    "Entrega registrada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    private interface DbAction { void run() throws Exception; }
    private void runDb(DbAction action) {
        try { action.run(); status.setText("Operación finalizada correctamente"); }
        catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operación no realizada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshStock() {
        runDb(() -> {
            datos = new ArrayList<>(stock.listar());
            tableModel.setRowCount(0);
            for (StockItem item : datos) {
                tableModel.addRow(new Object[]{item.getIdMaterial(), item.getMaterial(), item.getCantidad(),
                        item.getStockMinimo(), item.getUbicacion(), item.necesitaReposicion() ? "REPOSICIÓN" : "OK"});
            }
            status.setText("Inventario: " + datos.size() + " materiales");
        });
    }

    private void saveMovement() {
        runDb(() -> {
            int idMaterial = Integer.parseInt(material.getText().trim());
            int idUsuario = Integer.parseInt(usuario.getText().trim());
            BigDecimal qty = new BigDecimal(cantidad.getText().trim());
            if (idMaterial <= 0 || idUsuario <= 0 || qty.signum() <= 0)
                throw new IllegalArgumentException("Los IDs y la cantidad deben ser positivos.");
            stock.registrarMovimiento(idMaterial, idUsuario, (String) tipo.getSelectedItem(), qty, motivo.getText().trim());
            refreshStock();
            JOptionPane.showMessageDialog(this, "Movimiento registrado en MySQL.");
        });
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("sigepa-stock.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        runDb(() -> CsvExport.writeStock(chooser.getSelectedFile().toPath(), datos));
    }


    private void mostrarReposicion() {
        runDb(() -> {
            ArrayList<StockItem> materiales =
                    stock.obtenerMaterialesParaReposicion();

            String[] estados = stock.obtenerEstadosInventario();

            StringBuilder reporte = new StringBuilder();

            reporte.append("REPORTE DE REPOSICIÓN - SIGEPA\n");
            reporte.append("============================\n\n");
            reporte.append("Estados definidos: ");
            reporte.append(String.join(", ", estados));
            reporte.append("\n\n");

            reporte.append("Materiales que necesitan reposición: ");
            reporte.append(materiales.size());
            reporte.append("\n\n");

            if (materiales.isEmpty()) {
                reporte.append("No hay materiales que necesiten reposición.");
            } else {
                for (StockItem item : materiales) {
                    reporte.append("ID: ")
                            .append(item.getIdMaterial())
                            .append("\nMaterial: ")
                            .append(item.getMaterial())
                            .append("\nCantidad actual: ")
                            .append(item.getCantidad())
                            .append("\nStock mínimo: ")
                            .append(item.getStockMinimo())
                            .append("\nUbicación: ")
                            .append(item.getUbicacion())
                            .append("\n----------------------------\n");
                }
            }

            JTextArea area = new JTextArea(reporte.toString(), 20, 55);
            area.setEditable(false);
            area.setCaretPosition(0);

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(area),
                    "SIGEPA - Reporte de reposición",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SigepaDesktop().setVisible(true));
    }
}
