package com.poli;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase GenerateInfoFiles
 * Entrega 1 - Principios de Programación
 *
 * Objetivo:
 *  - Generar archivos de prueba que servirán como entrada para la segunda parte del proyecto.
 *  - Archivos generados:
 *      1. products.txt → Lista de productos con ID, nombre y precio.
 *      2. salesmen_info.txt → Información de vendedores (documento, nombre, apellido).
 *      3. sales_[nombre]_[id].txt → Archivo de ventas de un vendedor específico.
 *
 * Autor: Jim Owen Rey
 * Comentarios:
 *  - Esta clase fue implementada para cumplir con la entrega 1.
 *  - El código genera datos pseudoaleatorios para simular entradas reales.
 */
public class GenerateInfoFiles {

    public static void main(String[] args) {
        try {
            // Jim Owen Rey: Generamos archivos de prueba con datos aleatorios
            createProductsFile(10);               // Genera archivo con 10 productos
            createSalesManInfoFile(5);            // Genera archivo con 5 vendedores
            createSalesMenFile(8, "Carlos", 1001);  // Genera archivo de ventas para "Juan"

            System.out.println("✅ Archivos de prueba generados exitosamente.");
        } catch (Exception e) {
            // Jim Owen Rey: Captura y muestra cualquier error ocurrido en la generación
            System.out.println("❌ Error al generar los archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Genera un archivo products.txt con información de productos.
     *
     * Formato: IDProducto;NombreProducto;PrecioPorUnidad
     *
     * @param productsCount número de productos a generar
     * @throws IOException en caso de error al escribir el archivo
     *
     * Jim Owen Rey: Este método crea productos de ejemplo para pruebas.
     */
    public static void createProductsFile(int productsCount) throws IOException {
        FileWriter writer = new FileWriter("products.csv");
        Random random = new Random();

        for (int i = 1; i <= productsCount; i++) {
            int price = random.nextInt(100) + 1; // Precio entre 1 y 100
            writer.write("P" + i + ";Producto" + i + ";" + price + "\n");
        }

        writer.close();
    }

    /**
     * Genera un archivo salesmen_info.txt con información de vendedores.
     *
     * Formato: TipoDocumento;NumeroDocumento;Nombres;Apellidos
     *
     * @param salesmanCount número de vendedores a generar
     * @throws IOException en caso de error al escribir el archivo
     *
     * Jim Owen Rey: Este método crea vendedores con nombres y apellidos aleatorios.
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        FileWriter writer = new FileWriter("salesmen_info.csv");
        String[] nombres = {"Juan", "Ana", "Pedro", "Maria", "Luis"};
        String[] apellidos = {"Lopez", "Perez", "Gomez", "Rodriguez", "Diaz"};
        Random random = new Random();

        for (int i = 1; i <= salesmanCount; i++) {
            String nombre = nombres[random.nextInt(nombres.length)];
            String apellido = apellidos[random.nextInt(apellidos.length)];
            writer.write("CC;" + (1000 + i) + ";" + nombre + ";" + apellido + "\n");
        }

        writer.close();
    }

    /**
     * Genera un archivo de ventas para un vendedor.
     *
     * Formato:
     *  - Cabecera: TipoDocumento;NumeroDocumento
     *  - Cuerpo: IDProducto;Cantidad
     *
     * @param randomSalesCount número de ventas a registrar
     * @param name nombre del vendedor
     * @param id número de documento del vendedor
     * @throws IOException en caso de error al escribir el archivo
     *
     * Jim Owen Rey: Este método genera ventas aleatorias para un vendedor.
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        String fileName = "sales_" + name + "_" + id + ".csv";
        FileWriter writer = new FileWriter(fileName);
        Random random = new Random();

        // Cabecera: TipoDocumento;NumeroDocumento
        writer.write("CC;" + id + "\n");

        // Cuerpo: IDProducto;Cantidad
        for (int i = 1; i <= randomSalesCount; i++) {
            int cantidad = random.nextInt(5) + 1; // Entre 1 y 5 unidades
            writer.write("P" + (random.nextInt(10) + 1) + ";" + cantidad + "\n");
        }

        writer.close();
    }
}
