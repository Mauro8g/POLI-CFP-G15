package com.poli;

import java.io.*;
import java.util.*;

/**
 * Clase Main
 * Entrega 2 - Principios de Programación
 *
 * Objetivo:
 *  - Leer archivos generados por GenerateInfoFiles.
 *  - Calcular ventas totales por vendedor y por producto.
 *  - Crear reportes en formato CSV.
 *
 * Archivos de entrada:
 *  - products.txt
 *  - salesmen_info.txt
 *  - sales_[nombre]_[id].txt
 *
 * Archivos de salida:
 *  - reporteVendedores.csv
 *  - reporteProductos.csv
 *
 * Autor: Jim Owen Rey
 */
public class Main {

    // Mapas para guardar datos
    private static Map<String, Producto> productos = new HashMap<>();
    private static Map<String, Vendedor> vendedores = new HashMap<>();
    private static Map<String, Integer> ventasPorProducto = new HashMap<>();

    public static void main(String[] args) {
        try {
            // 1. Cargar productos
            cargarProductos("products.csv");

            // 2. Cargar vendedores
            cargarVendedores("salesmen_info.csv");

            // 3. Procesar ventas de todos los archivos que empiezan por "sales_"
            File carpeta = new File(".");
            File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("sales_"));
            if (archivos != null) {
                for (File archivo : archivos) {
                    procesarVentas(archivo.getName());
                }
            }

            // 4. Generar reportes
            generarReporteVendedores("reporteVendedores.csv");
            generarReporteProductos("reporteProductos.csv");

            System.out.println("✅ Reportes generados exitosamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al procesar archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ================= MÉTODOS =================

    private static void cargarProductos(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String id = partes[0];
                String nombre = partes[1];
                double precio = Double.parseDouble(partes[2]);
                productos.put(id, new Producto(id, nombre, precio));
                ventasPorProducto.put(id, 0);
            }
        }
    }

    private static void cargarVendedores(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String documento = partes[1];
                String nombreCompleto = partes[2] + " " + partes[3];
                vendedores.put(documento, new Vendedor(documento, nombreCompleto));
            }
        }
    }

    private static void procesarVentas(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            // Cabecera: TipoDocumento;NumeroDocumento
            linea = br.readLine();
            if (linea == null) return;
            String[] cabecera = linea.split(";");
            String documento = cabecera[1];

            // Procesar cada línea de venta
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String idProducto = partes[0];
                int cantidad = Integer.parseInt(partes[1]);

                Producto prod = productos.get(idProducto);
                if (prod != null) {
                    double total = prod.getPrecio() * cantidad;
                    vendedores.get(documento).sumarVentas(total);

                    // Sumar al total por producto
                    ventasPorProducto.put(idProducto, ventasPorProducto.get(idProducto) + cantidad);
                }
            }
        }
    }

    private static void generarReporteVendedores(String archivo) throws IOException {
        List<Vendedor> lista = new ArrayList<>(vendedores.values());
        lista.sort((a, b) -> Double.compare(b.getTotalVentas(), a.getTotalVentas()));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Vendedor v : lista) {
                bw.write(v.getNombre() + ";" + v.getTotalVentas());
                bw.newLine();
            }
        }
    }

    private static void generarReporteProductos(String archivo) throws IOException {
        List<Producto> lista = new ArrayList<>(productos.values());
        lista.sort((a, b) -> Integer.compare(ventasPorProducto.get(b.getId()), ventasPorProducto.get(a.getId())));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Producto p : lista) {
                int cantidad = ventasPorProducto.get(p.getId());
                bw.write(p.getNombre() + ";" + p.getPrecio() + ";" + cantidad);
                bw.newLine();
            }
        }
    }
}

// ================= CLASES DE APOYO =================

class Producto {
    private String id;
    private String nombre;
    private double precio;

    public Producto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}

class Vendedor {
    private String documento;
    private String nombre;
    private double totalVentas;

    public Vendedor(String documento, String nombre) {
        this.documento = documento;
        this.nombre = nombre;
        this.totalVentas = 0;
    }

    public void sumarVentas(double monto) {
        this.totalVentas += monto;
    }

    public String getNombre() { return nombre; }
    public double getTotalVentas() { return totalVentas; }
}
