import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServidorContador extends UnicastRemoteObject implements ContadorRemoto {

    private int contador = 0;
    private final Map<String, List<Integer>> historial = new HashMap<>();
    private final Queue<String> colaClientes = new LinkedList<>();
    private String ultimoCliente = null;
    private final List<ClienteCallback> clientesRegistrados = new ArrayList<>();

    public ServidorContador() throws RemoteException {
        super();
    }

    @Override
    public synchronized int incrementar(String clienteId) throws RemoteException {
        if (contador >= 100) {
            notificarFinAClientes();
            return -1;
        }

        // Si el cliente fue el último, debe esperar
        if (clienteId.equals(ultimoCliente)) {
            System.out.println("Servidor: " + clienteId + " debe esperar su turno.");
            return contador; // No avanza
        }

        // Incrementar contador
        contador++;
        ultimoCliente = clienteId;

        // Registrar en el historial del cliente
        historial.computeIfAbsent(clienteId, k -> new ArrayList<>()).add(contador);

        System.out.println("Servidor: " + clienteId + " recibió el valor " + contador);

        // Cuando llega a 100, avisar a todos los clientes
        if (contador >= 100) {
            notificarFinAClientes();
        }

        return contador;
    }

    @Override
    public boolean haFinalizado() throws RemoteException {
        return contador >= 100;
    }

    @Override
    public synchronized void registrarCliente(ClienteCallback cliente) throws RemoteException {
        clientesRegistrados.add(cliente);
        System.out.println("Servidor: Cliente registrado (" + clientesRegistrados.size() + " en total).");
    }

    // Notifica a todos los clientes registrados que el contador llegó a 100
    private void notificarFinAClientes() {
        System.out.println("Servidor: Contador llegó a 100. Notificando a todos los clientes...");
        for (ClienteCallback c : clientesRegistrados) {
            try {
                c.notificarFin();
            } catch (RemoteException e) {
                System.err.println("Error notificando cliente: " + e.getMessage());
            }
        }
    }
}
