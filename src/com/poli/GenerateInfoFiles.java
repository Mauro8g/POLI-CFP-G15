import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GenerateInfoFiles {
    
    //---------------------------------------------------------------------------------
    //  MÉTODOS PARA GENERAR Y GESTIONAR LA INFORMACIÓN DE LOS VENDEDORES
    //---------------------------------------------------------------------------------

    /**
     * Genera datos aleatorios para un número específico de vendedores.
     * @param salesmanCount El número de vendedores a generar.
     * @return Un HashMap donde la clave es el número de documento y el valor es un array con los datos del vendedor.
     */
    public static HashMap<Long, String[]> generateSalesmenData(int salesmanCount) {
        HashMap<Long, String[]> salesmenData = new HashMap<>();
        Random random = new Random();
        
        String[] documentTypes = {"TI", "CC", "CE"};
        String[] names = {"Sara", "Andrea", "Estefany", "Jessica", "Juliana", "Francisco", "Antonio", "Camilo", "Michael", "German", "Eduar", "Jhon", "Viviana"};
        String[] lastnames = {"Rios", "Cardona", "Rivera", "Alvares", "Murillo", "Montoya", "Contreras", "Jimenes"};
        
        for (int i = 0; i < salesmanCount; i++) {
            String randomDocType = documentTypes[random.nextInt(documentTypes.length)];
            long documentNumber = 1000000000L + random.nextInt(900000000);
            String randomName = names[random.nextInt(names.length)];
            String randomLastname = lastnames[random.nextInt(lastnames.length)];
            
            String[] salesmanInfo = {randomDocType, randomName, randomLastname};
            salesmenData.put(documentNumber, salesmanInfo);
        }
        return salesmenData;
    }
    
    /**
     * Crea un archivo CSV con la información de los vendedores.
     * @param salesmenData El HashMap con los datos de los vendedores.
     */
    public static void createSalesmanInfoFile(HashMap<Long, String[]> salesmenData) {
        String fileName = "Salesman.csv";
        try (FileWriter fileWriter = new FileWriter(fileName)) { // Uso de try-with-resources para cerrar el escritor automáticamente
            fileWriter.write("TipoDocumento;NúmeroDocumento;NombresVendedor;ApellidosVendedor\n");
            
            for (Map.Entry<Long, String[]> entry : salesmenData.entrySet()) {
                Long documentNumber = entry.getKey();
                String[] info = entry.getValue();
                fileWriter.write(info[0] + ";" + documentNumber + ";" + info[1] + ";" + info[2] + "\n");
            }
            
            System.out.println("Archivo " + fileName + " creado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al crear o escribir en el archivo: " + e.getMessage());
        }
    }
    
    //---------------------------------------------------------------------------------
    //  MÉTODOS PARA GENERAR Y GESTIONAR LA INFORMACIÓN DE LOS PRODUCTOS
    //---------------------------------------------------------------------------------

    /**
     * Genera un HashMap de productos con ID, nombre y precio únicos.
     * @return Un HashMap donde la clave es el nombre del producto y el valor es un array con el ID y el precio.
     */
    public static HashMap<String, String[]> generateProductData() {
        String[] productNames = {"Tornillo", "Tuerca", "Arandela", "Broca", "Atornillador", "Martillo", "Llave", "Serrucho", "Taladro", "Destornillador"};
        HashMap<String, String[]> productData = new HashMap<>();
        Random random = new Random();
        
        for (String productName : productNames) {
            long productId = random.nextLong(9000000000L) + 1000000000L;
            long productPrice = random.nextLong(90000L) + 10000L;
            
            String[] productInfo = {String.valueOf(productId), String.valueOf(productPrice)};
            productData.put(productName, productInfo);
        }
        return productData;
    }
    
    /**
     * Crea un archivo CSV con la información de los productos.
     * @param productData El HashMap con la información de los productos.
     */
    public static void createProductFile(HashMap<String, String[]> productData) {
        String fileName = "Product.csv";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write("IDProducto;NombreProducto;PrecioPorUnidad\n");
            
            for (Map.Entry<String, String[]> entry : productData.entrySet()) {
                String productName = entry.getKey();
                String[] productInfo = entry.getValue();
                fileWriter.write(productInfo[0] + ";" + productName + ";" + productInfo[1] + "\n");
            }
            
            System.out.println("Archivo " + fileName + " creado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al crear o escribir en el archivo: " + e.getMessage());
        }
    }

    //---------------------------------------------------------------------------------
    //  MÉTODO PARA CREAR ARCHIVOS DE VENTAS INDIVIDUALES
    //---------------------------------------------------------------------------------
    
    /**
     * Crea un archivo de ventas para un vendedor específico, usando IDs de producto del HashMap.
     * @param randomSalesCount Número de ventas a generar.
     * @param documentType Tipo de documento del vendedor.
     * @param documentNumber Número de documento del vendedor.
     * @param productData El HashMap con la información de todos los productos.
     */
    public static void createSalesmenFile(int randomSalesCount, String documentType, long documentNumber, HashMap<String, String[]> productData) {
        String fileName = "Sales_" + documentType + "_" + documentNumber + ".csv";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write("ID_Producto;Cantidad_Producto_vendido\n");
            Random random = new Random();
            
            List<String> productNames = new ArrayList<>(productData.keySet());
            
            for (int i = 0; i < randomSalesCount; i++) {
                String randomProductName = productNames.get(random.nextInt(productNames.size()));
                String[] productInfo = productData.get(randomProductName);
                String productId = productInfo[0];
                int amountSold = random.nextInt(20) + 1;
                fileWriter.write(productId + ";" + amountSold + "\n");
            }
            
            System.out.println("Archivo " + fileName + " creado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al crear o escribir en el archivo: " + e.getMessage());
        }
    }

    //---------------------------------------------------------------------------------
    //  MÉTODO PRINCIPAL (MAIN)
    //---------------------------------------------------------------------------------

    public static void main(String[] args) {
        // La cantidad de vendedores que queremos generar
        int totalSalesmen = 10;
        
        // 1. Generar los datos de todos los vendedores
        HashMap<Long, String[]> salesmenData = generateSalesmenData(totalSalesmen);
        
        // 2. Usar los datos para crear el archivo Salesman.csv
        createSalesmanInfoFile(salesmenData);

        // 3. Generamos los datos de los productos (IDs, nombres y precios)
        HashMap<String, String[]> productData = generateProductData();
        
        // 4. Usamos esos datos para crear el archivo Product.csv
        createProductFile(productData);

        // 5. Usamos los datos de los vendedores para crear los archivos de ventas.
        for (Map.Entry<Long, String[]> entry : salesmenData.entrySet()) {
            Long documentNumber = entry.getKey();
            String[] info = entry.getValue();
            String documentType = info[0];

            createSalesmenFile(10, documentType, documentNumber, productData); 
        }

        System.out.println("Proceso de creación de archivos finalizado.");
    }
}