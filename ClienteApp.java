import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ClienteApp extends UnicastRemoteObject implements ClienteCallback {

    private static final long serialVersionUID = 1L;
    private final String clienteId;
    private boolean finalizar = false;
    private final List<Integer> historial = new ArrayList<>();

    protected ClienteApp(String id) throws RemoteException {
        super();
        this.clienteId = id;
    }

    public void iniciar() {
        try {
            ContadorRemoto contador = (ContadorRemoto) Naming.lookup("rmi://localhost/ContadorRemoto");
            contador.registrarCliente(this);

            while (!finalizar && !contador.haFinalizado()) {
                int valor = contador.incrementar(clienteId);
                if (valor > 0) {
                    historial.add(valor);
                    System.out.println(clienteId + " recibió valor: " + valor);
                }
                Thread.sleep(500); // Espera medio segundo entre peticiones
            }

            System.out.println(clienteId + " ha terminado. Valores recibidos: " + historial);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método que el servidor llama cuando el contador llega a 100
    @Override
    public void notificarFin() throws RemoteException {
        System.out.println("Cliente " + clienteId + ": Notificación recibida. Contador llegó a 100.");
        finalizar = true;
    }

    public static void main(String[] args) {
        try {
            String id = args.length > 0 ? args[0] : "Cliente_" + (int) (Math.random() * 1000);
            ClienteApp cliente = new ClienteApp(id);
            cliente.iniciar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
