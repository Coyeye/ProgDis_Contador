import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Servidor extends UnicastRemoteObject implements ContadorRemoto {

    private int contador = 0;
    private String ultimoCliente = "";
    private final Map<String, Integer> historial = new HashMap<>();
    private final Queue<String> colaClientes = new LinkedList<>();

    protected Servidor() throws java.rmi.RemoteException {
        super();
    }

    @Override
    public synchronized int incrementar(String clienteId) throws java.rmi.RemoteException {
        if (contador >= 100) {
            System.out.println("El contador ha finalizado.");
            return -1;
        }

        // Evita atender dos veces seguidas al mismo cliente
        if (clienteId.equals(ultimoCliente)) {
            colaClientes.add(clienteId);
            System.out.println("Cliente " + clienteId + " debe esperar su turno.");
            return contador;
        }

        contador++;
        ultimoCliente = clienteId;
        historial.put(clienteId, contador);

        System.out.println("Cliente " + clienteId + " incrementó a " + contador);

        if (contador >= 100) {
            System.out.println(">>> Contador finalizado. Enviando notificación a clientes...");
        }

        return contador;
    }

    @Override
    public synchronized Map<String, Integer> obtenerHistorial() {
        return historial;
    }

    @Override
    public synchronized boolean haFinalizado() {
        return contador >= 100;
    }

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ContadorRemoto", servidor);
            System.out.println("Servidor RPC iniciado y esperando clientes...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
