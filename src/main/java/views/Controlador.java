package views;

import data.Persistencia;
import domain.Marca;
import domain.Sucursal;
import domain.Vehiculo;
import domain.VehiculoCombustible;
import domain.VehiculoElectrico;
import domain.VehiculoTipo;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import javax.swing.JOptionPane;

public class Controlador {
    
    public static ArrayList<VehiculoViewModel> getVehiculos(){
        ArrayList<VehiculoViewModel> vehiculos = new ArrayList<>();
        for(Vehiculo vehiculo : Persistencia.getVehiculos()) {
            vehiculos.add(new VehiculoViewModel(vehiculo));
        }
        return vehiculos;
    }
    
    public static double[] calcularConsumos(Map<String, Double> vehiculos){
        double consumoElectricos = 0;
        double consumoCombustible= 0;
        for(Map.Entry<String, Double> entry : vehiculos.entrySet()){
           double consumo = 0;
           Optional<Vehiculo> vehiculo = Persistencia.getVehiculo(entry.getKey());
           if(vehiculo.isPresent()){
               consumo = vehiculo.get().calcularConsumo(entry.getValue());
               consumoElectricos += vehiculo.get().esDe(VehiculoTipo.ELECTRICO) ? consumo : 0;
               consumoCombustible += vehiculo.get().esDe(VehiculoTipo.COMBUSTIBLE) ? consumo : 0;
           }
        }
        return new double[] {consumoElectricos, consumoCombustible};
    }
    
    public static void ComboSucursal(AgregarVehiculoView view){
        for(Sucursal s : Persistencia.getSucursales()){
            view.getComboSucursal().addItem(s.getCodigo());
        }
    }
    
    public static void ComboTipo(AgregarVehiculoView view){
        view.getComboTipo().addItem("ELECTRICO");
        view.getComboTipo().addItem("COMBUSTIBLE");
    }
    
    public static void ComboMarca(AgregarVehiculoView view){
        for(Marca m : Persistencia.getMarcas()){
            view.getComboMarca().addItem(m.getNombre());
        }
    }
    
    public static Marca buscarMarca(String nombre){
        for(Marca m : Persistencia.getMarcas()){
            if(m.getNombre().equals(nombre)){
                return m;
            }
        }
        return null;
    }
    
    public static Sucursal buscarSucursal(String codigo){
        for(Sucursal s : Persistencia.getSucursales()){
            if(s.getCodigo().equals(codigo)){
                return s;
            }
        }
        return null;
    }
    
    public static VehiculoTipo convertirTipo(String tipo){
        return VehiculoTipo.valueOf(tipo);
    }
    
    public static void bloquearCampos(AgregarVehiculoView view){
        String tipo = view.getTipo();
        view.actualizarCamposTipo(tipo);
    }
    
    public static void guardar(AgregarVehiculoView view){
        try{
            String patente = view.getPatente();
            String modelo = view.getModelo();
            int anio = view.getAnio();
            double capacidad = view.getCapacidad();

            Marca marca = buscarMarca(view.getMarca());
            Sucursal sucursal = buscarSucursal(view.getSucursal());
            VehiculoTipo tipo = convertirTipo(view.getTipo());

            Vehiculo v;

            if(tipo == VehiculoTipo.ELECTRICO){
                double kwh = view.getKwhBase();
                v = new VehiculoElectrico(patente, marca, modelo, anio, capacidad, sucursal, kwh);
            } else {
                double km = view.getKmPorLitro();
                double extra = view.getLitrosExtra();
                v = new VehiculoCombustible(patente, marca, modelo, anio, capacidad, sucursal, km, extra);
            }
                Persistencia.getVehiculos().add(v);
                JOptionPane.showMessageDialog(view, "Vehículo guardado correctamente");
                view.limpiarCampos();
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(view, "Error al guardar: " + e.getMessage());
        }
    }
    

    public static void inicializarCombos(AgregarVehiculoView view){  
        view.getComboMarca().removeAllItems();
        view.getComboSucursal().removeAllItems();
        view.getComboTipo().removeAllItems();

        ComboMarca(view);
        ComboSucursal(view);
        ComboTipo(view);
        bloquearCampos(view);
        
    }
    
    public static void abrirVentanaListar(java.awt.Frame parent){
        new ListarVehiculosView(parent, true).setVisible(true);
    }
    
    public static void abrirVentanaAgregar(java.awt.Frame parent){
   //   new AltaVehiculoView(parent, true).setVisible(true);
    }
    
    
}
