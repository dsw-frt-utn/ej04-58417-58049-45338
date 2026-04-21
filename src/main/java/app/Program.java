package app;
import java.util.Scanner;
import data.Persistencia;
import java.util.InvalidPropertiesFormatException;
import views.MenuView;

public class Program {
    public static void main(String[] args) throws IllegalArgumentException, InvalidPropertiesFormatException {
       Persistencia.inicializar();

Scanner sc = new Scanner(System.in);

System.out.println("MENU PRINCIPAL");
System.out.println("1. Listar Vehiculos");
System.out.println("2. Agregar Vehiculo");
System.out.print("Seleccione una opcion: ");

int opcion = sc.nextInt();

if (opcion == 1) {
    ListarVehiculosView view = new ListarVehiculosView();
    view.setVisible(true);
} 
else if (opcion == 2) {
    System.out.println("Agregar vehiculo...");
} 
else {
    System.out.println("Opcion invalida");
}

        Persistencia.inicializar();
        MenuView view = new MenuView();
        view.setVisible(true);

    }
}
