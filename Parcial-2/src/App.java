
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import Util.*;
import db.Personas.Docente.Materia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {
    @SuppressWarnings({ "resource", "unused" })
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Funciones.crearCarpetas(Variables.rutaAlumno);
        Funciones.crearCarpetas(Variables.rutaDocente);

        try (Scanner scanner = new Scanner(System.in)) {

            // Crear el menú con un Map
            Map<Integer, String> menu = new HashMap<>();
            menu.put(1, "Ingresar Materia");
            menu.put(2, "Ingresar Estudiantes");
            menu.put(3, "Verificar información");
            menu.put(4, "Salir");

            int opcion;

            do {
                // pa mostrar el menu
                System.out.println("\n--- MENÚ ---");
                for (Map.Entry<Integer, String> entry : menu.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }

                System.out.print("Seleccione una opción: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrada inválida. Ingrese un número.");
                    scanner.next(); // limpiar entrada incorrecta
                    System.out.print("Seleccione una opción: ");
                }

                opcion = scanner.nextInt();
                scanner.nextLine();

                // opciones seleccionadas
                switch (opcion) {
                    case 1:

                        System.out.println("Ingresar Materia");
                        System.out.print("Escriba el nombre de la materia a crear: ");
                        String MateriaCrear = scanner.nextLine();
                        Materia mt = new Materia(MateriaCrear);
                        Funciones.crearArchivo(Variables.rutaDocente + "\\" + MateriaCrear + ".txt");

                        break;
                    case 2:
                        System.out.println("Ingresar estudiantes");

                        // lista de las materias en el archivo ese de docentes
                        File carpetaDocente = new File(Variables.rutaDocente);
                        String[] materias = carpetaDocente.list((dir, name) -> name.endsWith(".txt"));

                        if (materias == null || materias.length == 0) {
                            System.out.println("No hay materias registradas.");
                            break;
                        }

                        System.out.println("Seleccione una materia:");
                        for (int i = 0; i < materias.length; i++) {
                            System.out.println((i + 1) + ". " + materias[i].replace(".txt", ""));
                        }

                        int opcionMateria = 0;
                        do {
                            System.out.print("Ingrese el número de la materia: ");
                            while (!scanner.hasNextInt()) {
                                System.out.println("Entrada inválida. Ingrese un número.");
                                scanner.next();
                                System.out.print("Ingrese el número de la materia: ");
                            }
                            opcionMateria = scanner.nextInt();
                            scanner.nextLine();
                        } while (opcionMateria < 1 || opcionMateria > materias.length);

                        String materiaSeleccionada = materias[opcionMateria - 1];
                        String rutaMateria = Variables.rutaDocente + "\\" + materiaSeleccionada;

                        // preguntar numeros de estudiantes
                        int cantidadEstudiantes = 0;
                        do {
                            System.out.print("Ingrese la cantidad de estudiantes: ");
                            while (!scanner.hasNextInt()) {
                                System.out.println("Entrada inválida. Ingrese un número.");
                                scanner.next();
                                System.out.print("Ingrese la cantidad de estudiantes: ");
                            }
                            cantidadEstudiantes = scanner.nextInt();
                            scanner.nextLine();
                            if (cantidadEstudiantes <= 0) {
                                System.out.println("La cantidad debe ser mayor a 0.");
                            }
                        } while (cantidadEstudiantes <= 0);

                        try (FileWriter fwMateria = new FileWriter(rutaMateria, true)) {
                            for (int i = 0; i < cantidadEstudiantes; i++) {
                                System.out.println("Estudiante " + (i + 1));

                                System.out.print("Nombre completo: ");
                                String nombre = scanner.nextLine();

                                System.out.print("Código: ");
                                String codigo = scanner.nextLine();

                                double[] notas = new double[3];
                                for (int j = 0; j < 3; j++) {
                                    do {
                                        System.out.print("Nota " + (j + 1) + " (0.0 - 5.0): ");
                                        while (!scanner.hasNextDouble()) {
                                            System.out.println("Entrada inválida. Ingrese un número decimal.");
                                            scanner.next();
                                            System.out.print("Nota " + (j + 1) + " (0.0 - 5.0): ");
                                        }
                                        notas[j] = scanner.nextDouble();
                                        scanner.nextLine();
                                        if (notas[j] < 0.0 || notas[j] > 5.0) {
                                            System.out.println("La nota debe estar entre 0.0 y 5.0.");
                                        }
                                    } while (notas[j] < 0.0 || notas[j] > 5.0);
                                }

                                // pa crear archivos de los estudiantes
                                String rutaAlumno = Variables.rutaAlumno + "\\" + codigo + ".txt";
                                try (FileWriter fwAlumno = new FileWriter(rutaAlumno)) {
                                    fwAlumno.write(notas[0] + "," + notas[1] + "," + notas[2]);
                                } catch (Exception e) {
                                    System.out.println("Error al crear archivo del alumno: " + e.getMessage());
                                }

                                // calcular promdeio
                                double promedio = (notas[0] + notas[1] + notas[2]) / 3.0;

                                fwMateria.write(codigo + " - " + nombre + ": " + String.format("%.1f", promedio)+ System.lineSeparator());
                            }
                        } catch (Exception e) {
                            System.out.println("Error al escribir en el archivo de la materia: " + e.getMessage());
                        }

                        break;
                    case 3:
                        System.out.println("Verificar Información");

                        // lista de materias en la ruta sujetos
                        File carpetaDocenteVer = new File(Variables.rutaDocente);
                        String[] materiasVer = carpetaDocenteVer.list((dir, name) -> name.endsWith(".txt"));

                        if (materiasVer == null || materiasVer.length == 0) {
                            System.out.println("No hay materias registradas.");
                            break;
                        }

                        System.out.println("Seleccione una materia:");
                        for (int i = 0; i < materiasVer.length; i++) {
                            System.out.println((i + 1) + ". " + materiasVer[i].replace(".txt", ""));
                        }

                        int opcionMateriaVer = 0;
                        do {
                            System.out.print("Ingrese el número de la materia: ");
                            while (!scanner.hasNextInt()) {
                                System.out.println("Entrada inválida. Ingrese un número.");
                                scanner.next();
                                System.out.print("Ingrese el número de la materia: ");
                            }
                            opcionMateriaVer = scanner.nextInt();
                            scanner.nextLine();
                        } while (opcionMateriaVer < 1 || opcionMateriaVer > materiasVer.length);

                        String materiaSeleccionadaVer = materiasVer[opcionMateriaVer - 1];
                        String rutaMateriaVer = Variables.rutaDocente + "\\" + materiaSeleccionadaVer;

                        try {
                            System.out.println(
                                    "Contenido de la materia " + materiaSeleccionadaVer.replace(".txt", "") + ":");
                            Files.lines(Path.of(rutaMateriaVer)).forEach(System.out::println);
                        } catch (IOException e) {
                            System.out.println("Error al leer el archivo: " + e.getMessage());
                        }

                        break;
                    case 4:
                        System.out.println("Saliendo del programa.");

                        // pa borrar todos los archivos de alumnos
                        File carpetaAlumno = new File(Variables.rutaAlumno);
                        File[] archivosAlumno = carpetaAlumno.listFiles();
                        if (archivosAlumno != null) {
                            for (File archivo : archivosAlumno) {
                                if (archivo.isFile()) {
                                    if (!archivo.delete()) {
                                        System.out.println("No se pudo eliminar el archivo: " + archivo.getName());
                                    }
                                }
                            }
                        }

                        System.exit(0);
                        break;
                    
                    default:
                        System.out.println("Opción incorrecta. Por favor seleccione una opción válida.");
                }

            } while (opcion != 0);

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
