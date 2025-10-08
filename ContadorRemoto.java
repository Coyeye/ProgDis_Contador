import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ContadorRemoto extends Remote {
    int incrementar(String clienteId) throws RemoteException;
    boolean haFinalizado() throws RemoteException;
    void registrarCliente(ClienteCallback cliente) throws RemoteException;
}
