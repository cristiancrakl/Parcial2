package Util;

import java.io.*;

public class Funciones {





    // crear archivo en ruta especifica
    public static void crearArchivo(String ruta) throws IOException {

        FileWriter archivo = null;

        try {

            archivo = new FileWriter(ruta);

        } catch (Exception e) {

            System.out.println(e);
        } finally {
            archivo.close();
        }

    }

    //crear carpetas
    public static void crearCarpetas(String ruta) throws IOException {

        File rutaCarpeta = null;

        try {

            rutaCarpeta = new File(ruta);

            if (!rutaCarpeta.exists()) {

                if (rutaCarpeta.mkdirs()) {
                    System.out.println("carpeta creada correctamente");
                } else {

                    System.out.println("Error al crear carpeta");
                }

            } else {
                System.out.println("La carpeta ya existe");
            }

        } catch (Exception e) {

            System.out.println(e);
        }

    }

}
