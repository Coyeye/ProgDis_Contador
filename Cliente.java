import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Cliente {
    public static void main(String[] args) {
        try {
            String clienteId = "Cliente-" + new Random().nextInt(1000);
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ContadorRemoto contador = (ContadorRemoto) registry.lookup("ContadorRemoto");

            while (!contador.haFinalizado()) {
                int valor = contador.incrementar(clienteId);
                if (valor == -1) break;
                System.out.println(clienteId + " recibió valor: " + valor);
                Thread.sleep(500); // Espera medio segundo antes de volver a intentar
            }

            System.out.println(clienteId + " ha sido notificado del fin del contador.");
            System.out.println("Historial: " + contador.obtenerHistorial());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
