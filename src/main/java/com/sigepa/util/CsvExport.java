package com.sigepa.util;

import com.sigepa.model.StockItem;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Exportación de archivo CSV para respaldar/recuperar información del inventario. */
public final class CsvExport {
    private CsvExport() {}
    private static String csv(Object o) {
        String s = o == null ? "" : o.toString();
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
    public static void writeStock(Path file, List<StockItem> items) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write("id_material,material,cantidad,stock_minimo,ubicacion\n");
            for (StockItem item : items) {
                writer.write(item.getIdMaterial() + "," + csv(item.getMaterial()) + "," +
                        item.getCantidad() + "," + item.getStockMinimo() + "," +
                        csv(item.getUbicacion()) + "\n");
            }
        }
    }
}
