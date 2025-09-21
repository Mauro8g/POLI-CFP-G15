package proyectoinicial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase principal que ejecuta la Etapa 2 del proyecto: leer los archivos generados
 * en la Etapa 1, procesar los datos, realizar los cálculos y generar los
 * reportes finales de ventas.
 */
public class Main {
    
    //---------------------------------------------------------------------------------
    // LÓGICA DE REPORTE - MÉTODOS DE LA ETAPA 2
    //---------------------------------------------------------------------------------

    /**
     * Lee el archivo "Product.csv" y carga los precios de los productos en un HashMap.
     * La clave es el ID del producto y el valor es el precio.
     *
     * @return Un HashMap con los IDs de los productos y sus precios.
     */
    public static HashMap<String, Long> loadProductPrices() {
        HashMap<String, Long> productPrices = new HashMap<>();
        String fileName = "Product.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // Omitir el encabezado
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    productPrices.put(parts[0], Long.parseLong(parts[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo Product.csv: " + e.getMessage());
        }
        return productPrices;
    }

    /**
     * Lee el archivo "Salesman.csv" y carga la información de los vendedores.
     * La clave es el número de documento y el valor es un array de strings
     * con el tipo de documento, nombre y apellido.
     *
     * @return Un HashMap con los datos de los vendedores.
     */
    public static HashMap<Long, String[]> loadSalesmanData() {
        HashMap<Long, String[]> salesMenData = new HashMap<>();
        String fileName = "Salesman.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // Omitir el encabezado
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    Long numDoc = Long.parseLong(parts[1]);
                    String[] salesmanInfo = {parts[0], parts[2], parts[3]};
                    salesMenData.put(numDoc, salesmanInfo);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo Salesman.csv: " + e.getMessage());
        }
        return salesMenData;
    }

    /**
     * Calcula el total de ventas por vendedor, leyendo cada archivo de ventas individual.
     *
     * @param salesMenData El HashMap con la información de los vendedores.
     * @param productPrices El HashMap con los precios de cada producto.
     * @return Un HashMap donde la clave es el número de documento del vendedor y el valor es su total de ventas.
     */
    public static HashMap<Long, Long> calculateSalesTotals(HashMap<Long, String[]> salesMenData, HashMap<String, Long> productPrices) {
        HashMap<Long, Long> salesTotals = new HashMap<>();
        for (Map.Entry<Long, String[]> salesmanEntry : salesMenData.entrySet()) {
            Long documentNumber = salesmanEntry.getKey();
            String documentType = salesmanEntry.getValue()[0];
            String fileName = "Sales_" + documentType + "_" + documentNumber + ".csv";
            long totalSalesForSalesman = 0L;
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                reader.readLine(); // Omitir el encabezado
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        String productId = parts[0];
                        int quantitySold = Integer.parseInt(parts[1]);
                        if (productPrices.containsKey(productId)) {
                            long price = productPrices.get(productId);
                            totalSalesForSalesman += (long) quantitySold * price;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de ventas " + fileName + ": " + e.getMessage());
            }
            salesTotals.put(documentNumber, totalSalesForSalesman);
        }
        return salesTotals;
    }
    
    /**
     * Calcula la cantidad total de cada producto vendido, sumando las ventas de todos los vendedores.
     *
     * @param productPrices No se utiliza directamente en este método, pero se mantiene para consistencia.
     * @param salesMenData El HashMap con la información de los vendedores.
     * @return Un HashMap donde la clave es el ID del producto y el valor es la cantidad total vendida.
     */
    public static HashMap<String, Long> calculateProductSales(HashMap<String, Long> productPrices, HashMap<Long, String[]> salesMenData) {
        HashMap<String, Long> productTotals = new HashMap<>();

        for (Map.Entry<Long, String[]> salesmanEntry : salesMenData.entrySet()) {
            Long documentNumber = salesmanEntry.getKey();
            String documentType = salesmanEntry.getValue()[0];
            String fileName = "Sales_" + documentType + "_" + documentNumber + ".csv";

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                reader.readLine(); // Omitir el encabezado
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        String productId = parts[0];
                        int quantitySold = Integer.parseInt(parts[1]);
                        
                        // Si el producto ya existe, suma la cantidad; si no, agrégalo.
                        productTotals.put(productId, productTotals.getOrDefault(productId, 0L) + quantitySold);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de ventas " + fileName + ": " + e.getMessage());
            }
        }
        return productTotals;
    }
    
    /**
     * Lee el archivo "Product.csv" para obtener el nombre y precio de los productos por su ID.
     *
     * @return Un HashMap donde la clave es el ID del producto y el valor es un array de strings con el nombre y precio.
     */
    public static HashMap<String, String[]> loadProductData() {
        HashMap<String, String[]> productData = new HashMap<>();
        String fileName = "Product.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // Omitir el encabezado
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String[] productInfo = {parts[1], parts[2]};
                    productData.put(parts[0], productInfo);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo Product.csv: " + e.getMessage());
        }
        return productData;
    }

    /**
     * Genera los dos archivos de reporte finales, ordenados según los requisitos del proyecto.
     *
     * @param salesTotals Un mapa con el total de ventas por vendedor.
     * @param salesmenInfo Un mapa con la información completa de los vendedores.
     * @param productTotals Un mapa con la cantidad total vendida de cada producto.
     * @param productData Un mapa con la información completa de los productos.
     */
    public static void createFinalReports(HashMap<Long, Long> salesTotals, HashMap<Long, String[]> salesmenInfo, HashMap<String, Long> productTotals, HashMap<String, String[]> productData) {
        // Reporte 1: Ventas por Vendedor (ordenado por total de mayor a menor)
        String salesmanReportFileName = "Reporte_Ventas_Vendedores.csv";
        List<Map.Entry<Long, Long>> sortedSalesmen = new ArrayList<>(salesTotals.entrySet());
        // Se usa un comparador lambda para ordenar en orden descendente
        Collections.sort(sortedSalesmen, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        try (FileWriter fileWriter = new FileWriter(salesmanReportFileName)) {
            fileWriter.write("Nombre_Vendedor;Apellido_Vendedor;Total_Ventas\n");
            for (Map.Entry<Long, Long> entry : sortedSalesmen) {
                Long documentNumber = entry.getKey();
                Long total = entry.getValue();
                String[] info = salesmenInfo.get(documentNumber);
                fileWriter.write(info[1] + ";" + info[2] + ";" + total + "\n");
            }
            System.out.println("Reporte de vendedores creado con éxito: " + salesmanReportFileName);
        } catch (IOException e) {
            System.err.println("Error al crear el reporte de vendedores: " + e.getMessage());
        }

        // Reporte 2: Productos vendidos (ordenado por cantidad de mayor a menor)
        String productReportFileName = "Reporte_Ventas_Productos.csv";
        List<Map.Entry<String, Long>> sortedProducts = new ArrayList<>(productTotals.entrySet());
        // Se usa un comparador lambda para ordenar en orden descendente
        Collections.sort(sortedProducts, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        try (FileWriter fileWriter = new FileWriter(productReportFileName)) {
            fileWriter.write("Nombre_Producto;Precio_Unidad;Cantidad_Vendida\n");
            for (Map.Entry<String, Long> entry : sortedProducts) {
                String productId = entry.getKey();
                Long totalQuantity = entry.getValue();
                String[] productInfo = productData.get(productId);
                String productName = productInfo[0];
                String productPrice = productInfo[1];
                fileWriter.write(productName + ";" + productPrice + ";" + totalQuantity + "\n");
            }
            System.out.println("Reporte de productos creado con éxito: " + productReportFileName);
        } catch (IOException e) {
            System.err.println("Error al crear el reporte de productos: " + e.getMessage());
        }
    }
    
    /**
     * El método principal (main) es el punto de entrada del programa.
     * Orquesta la ejecución de la Etapa 1 (Generación de Archivos) y la Etapa 2
     * (Análisis y Reporte).
     */
    public static void main(String[] args) {
        
        System.out.println("--- INICIANDO LA ETAPA 2: CÁLCULOS Y REPORTES ---");
        
        try {
            // Cargamos los datos de los archivos generados previamente
            HashMap<String, Long> productPrices = loadProductPrices();
            HashMap<Long, String[]> salesmenInfo = loadSalesmanData();
            
            if (productPrices.isEmpty() || salesmenInfo.isEmpty()) {
                System.err.println("Error: No se pudieron cargar los datos. Asegúrate de que los archivos 'Product.csv' y 'Salesman.csv' de la Etapa 1 existen.");
                return;
            }

            HashMap<String, String[]> productDataMap = loadProductData();

            HashMap<Long, Long> salesTotals = calculateSalesTotals(salesmenInfo, productPrices);
            HashMap<String, Long> productTotals = calculateProductSales(productPrices, salesmenInfo);

            createFinalReports(salesTotals, salesmenInfo, productTotals, productDataMap);
            
            System.out.println("\n--- PROCESO COMPLETO FINALIZADO CON ÉXITO ---\n");
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
}