package hotel;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BookingServer {
    public static void main(String[] args) {
        try {
            BookingManager manager = new BookingManager();
            // 获取已由 Ant 脚本在默认端c
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            registry.rebind("BookingManager", manager);
            System.out.println("Booking Server is running and registered.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
