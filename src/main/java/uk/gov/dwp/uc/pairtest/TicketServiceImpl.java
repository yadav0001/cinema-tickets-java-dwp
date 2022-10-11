package uk.gov.dwp.uc.pairtest;

import java.util.HashMap;
import java.util.Map;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private int totalAmountToPay = 0;
    private int totalSeatsToAllocate = 0;
    private final int INFANT_TICKET_PRICE = 0;
    private final int CHILD_TICKET_PRICE = 10;
    private final int ADULT_TICKET_PRICE = 20;
    private Map<TicketTypeRequest.Type, Integer> ticketTypeCountMap = new HashMap<>();

    /**
     * This method calulates total ticket amount.
     * 
     * @return total amount of type int.
     */
    private int getTotalAmountToPay() {
        totalAmountToPay = ticketTypeCountMap.get(Type.ADULT) * ADULT_TICKET_PRICE
                + ticketTypeCountMap.get(Type.CHILD) * CHILD_TICKET_PRICE
                + ticketTypeCountMap.get(Type.INFANT) * INFANT_TICKET_PRICE;
        return totalAmountToPay;
    }

    /**
     * This method calulates total number of seats to be reserved.
     * 
     * @return total number of seats to be reserved of type int.
     */
    private int getTotalSeatsToAllocate() {
        totalSeatsToAllocate = ticketTypeCountMap.get(Type.ADULT) + ticketTypeCountMap.get(Type.CHILD);
        return totalSeatsToAllocate;
    }

    /**
     * This method will validate the ticket requests and calulates total ticket
     * amount, and number of seats to be reserved.
     * 
     * @param accountId          a valid account ID of type Long
     * @param ticketTypeRequests an array of ticket objects of type
     *                           TicketTypeRequest
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {

        if (accountId <= 0)
            throw new InvalidPurchaseException("Invalid Account Id");

        int totalTicketsRequest = 0;

        for (TicketTypeRequest req : ticketTypeRequests) {
            Type ticketType = req.getTicketType();
            int noOfTickets = req.getNoOfTickets();
            if (ticketTypeCountMap.containsKey(ticketType)) {
                ticketTypeCountMap.put(ticketType, ticketTypeCountMap.get(ticketType) + noOfTickets);
            } else {
                ticketTypeCountMap.put(ticketType, noOfTickets);
            }

            totalTicketsRequest += noOfTickets;
        }
        if (ticketTypeCountMap.get(Type.ADULT) < 0 || ticketTypeCountMap.get(Type.CHILD) < 0
                || ticketTypeCountMap.get(Type.INFANT) < 0)
            throw new InvalidPurchaseException("Invalid number of tickets requested");
        if (totalTicketsRequest > 20)
            throw new InvalidPurchaseException("Cannot purchase more than 20 Tickets");
        if ((ticketTypeCountMap.get(Type.ADULT) == 0
                && (ticketTypeCountMap.get(Type.CHILD) > 0 || ticketTypeCountMap.get(Type.INFANT) > 0)))
            throw new InvalidPurchaseException("Child and Infants tickets must be accompanied with atleast 1 adult");
        if (ticketTypeCountMap.get(Type.INFANT) > ticketTypeCountMap.get(Type.ADULT))
            throw new InvalidPurchaseException("Cannot buy more infants tickets than adults ticket");

        totalAmountToPay = getTotalAmountToPay();
        totalSeatsToAllocate = getTotalSeatsToAllocate();

        TicketPaymentServiceImpl ticketPayment = new TicketPaymentServiceImpl();
        ticketPayment.makePayment(accountId, totalAmountToPay);

        SeatReservationServiceImpl seatReservation = new SeatReservationServiceImpl();
        seatReservation.reserveSeat(accountId, totalSeatsToAllocate);

    }

}
