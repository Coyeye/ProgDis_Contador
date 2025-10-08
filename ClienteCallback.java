import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteCallback extends Remote {
    void notificarFin() throws RemoteException;
}
