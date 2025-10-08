import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ContadorRemoto extends Remote {
    int incrementar(String clienteId) throws RemoteException;
    Map<String, Integer> obtenerHistorial() throws RemoteException;
    boolean haFinalizado() throws RemoteException;
}
