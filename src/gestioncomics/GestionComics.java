package gestioncomics;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

// Clase que representa un cómic
class Comic implements Serializable {
    private static final long serialVersionUID = 1L; // Identificador para la serialización
    private String titulo;
    private String autor;
    private int anoPublicacion;

    // Constructor con validación de datos
    public Comic(String titulo, String autor, int anoPublicacion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (anoPublicacion < 0) {
            throw new IllegalArgumentException("El año de publicación no puede ser negativo.");
        }
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
    }

    // Getters para acceder a los atributos
    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoPublicacion() {
        return anoPublicacion;
    }

    // Representación en texto del cómic
    @Override
    public String toString() {
        return String.format("Título: %s, Autor: %s, Año de publicación: %d", titulo, autor, anoPublicacion);
    }
}

// Clase para gestionar la lista de cómics
public class GestionComics {
    private ArrayList<Comic> comics;
    private static final String ARCHIVO = "comics.dat";

    // Constructor
    public GestionComics() {
        comics = cargarComics(); // Cargar cómics al iniciar
    }

    // Método para agregar un cómic
    public void agregarComic(String titulo, String autor, int anoPublicacion) {
        Comic nuevoComic = new Comic(titulo, autor, anoPublicacion);
        comics.add(nuevoComic);
        guardarComics(); // Guardar cambios
        System.out.println("Cómic agregado: " + nuevoComic);
    }

    // Método para mostrar todos los cómics
    public void mostrarComics() {
        if (comics.isEmpty()) {
            System.out.println("La lista de cómics está vacía.");
        } else {
            System.out.println("Lista de cómics:");
            for (Comic comic : comics) {
                System.out.println(comic);
            }
        }
    }

    // Método para eliminar un cómic por título
    public void eliminarComic(String titulo) {
        Iterator<Comic> iterator = comics.iterator();
        boolean encontrado = false;
        while (iterator.hasNext()) {
            Comic comic = iterator.next();
            if (comic.getTitulo().equalsIgnoreCase(titulo)) {
                iterator.remove();
                guardarComics(); // Guardar cambios
                System.out.println("Cómic eliminado: " + comic);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("Cómic no encontrado.");
        }
    }

    // Método para guardar los cómics en un archivo
    private void guardarComics() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(comics);
        } catch (IOException e) {
            System.err.println("Error al guardar los cómics: " + e.getMessage());
        }
    }

    // Método para cargar los cómics desde un archivo
    @SuppressWarnings("unchecked")
    private ArrayList<Comic> cargarComics() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (ArrayList<Comic>) ois.readObject();
        } catch (FileNotFoundException e) {
            // El archivo no existe, devolvemos una lista vacía
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los cómics: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Método principal con validación de entradas
    public static void main(String[] args) {
    // Configuración de la consola para usar UTF-8
    try {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
        System.err.println("Error configurando UTF-8: " + e.getMessage());
    }

        GestionComics gestionComics = new GestionComics();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Agregar cómic");
            System.out.println("2. Mostrar cómics");
            System.out.println("3. Eliminar cómic");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    try {
                        System.out.print("Ingrese el título del cómic: ");
                        String titulo = scanner.nextLine();
                        System.out.print("Ingrese el autor del cómic: ");
                        String autor = scanner.nextLine();
                        System.out.print("Ingrese el año de publicación del cómic: ");
                        int ano = Integer.parseInt(scanner.nextLine());

                        gestionComics.agregarComic(titulo, autor, ano);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    gestionComics.mostrarComics();
                    break;
                case 3:
                    System.out.print("Ingrese el título del cómic a eliminar: ");
                    String tituloEliminar = scanner.nextLine();
                    gestionComics.eliminarComic(tituloEliminar);
                    break;
                case 4:
                    System.out.println("Saliendo del programa.");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
}
