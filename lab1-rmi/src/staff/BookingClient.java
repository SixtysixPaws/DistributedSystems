package staff;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Set;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import hotel.BookingDetail;
import hotel.IBookingManager;

public class BookingClient extends AbstractScriptedSimpleTest {

	private IBookingManager bm = null;

	public static void main(String[] args) throws Exception {
		BookingClient client = new BookingClient();
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/
	public BookingClient() {
		try {
			//Look up the registered remote instance
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);
			bm = (IBookingManager) registry.lookup("BookingManager");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws Exception {
		return bm.isRoomAvailable(roomNumber, date);
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) {
		try {
			bm.addBooking(bookingDetail);
		} catch (Exception e) {
			System.out.println("Caught expected exception: " + e.getMessage());
		}
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
		return bm.getAvailableRooms(date);
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException {
		return bm.getAllRooms();
	}
}
