import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Puerto por defecto de RMI
            ServidorContador servidor = new ServidorContador();
            Naming.rebind("rmi://localhost/ContadorRemoto", servidor);
            System.out.println("Servidor listo y esperando conexiones...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
