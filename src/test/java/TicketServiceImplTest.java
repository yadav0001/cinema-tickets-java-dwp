import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketServiceImplTest {
   @Rule
   public ExpectedException exception = ExpectedException.none();

   @Test
   public void throwInvalidIDExceptionTest() {

      TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(Type.ADULT, 3);
      TicketTypeRequest ticketRequestChild = new TicketTypeRequest(Type.CHILD, 0);
      TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(Type.INFANT, 3);
      TicketTypeRequest[] ticketTypeRequests = { ticketRequestAdult, ticketRequestChild, ticketRequestInfant };
      Long accountId = 0L;

      TicketServiceImpl ticketService = new TicketServiceImpl();

      exception.expectMessage("Invalid Account Id");
      ticketService.purchaseTickets(accountId, ticketTypeRequests);
   }

   @Test
   public void throwInvalidTicketRequestExceptionTest() {

      TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(Type.ADULT, 3);
      TicketTypeRequest ticketRequestChild = new TicketTypeRequest(Type.CHILD, -2);
      TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(Type.INFANT, 3);
      TicketTypeRequest[] ticketTypeRequests = { ticketRequestAdult, ticketRequestChild, ticketRequestInfant };
      Long accountId = 1L;

      TicketServiceImpl ticketService = new TicketServiceImpl();

      exception.expectMessage("Invalid number of tickets requested");
      ticketService.purchaseTickets(accountId, ticketTypeRequests);
   }

   @Test
   public void throwInvalidTicketsNumberLimit20ExceptionTest() {

      TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(Type.ADULT, 9);
      TicketTypeRequest ticketRequestChild = new TicketTypeRequest(Type.CHILD, 9);
      TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(Type.INFANT, 3);
      TicketTypeRequest[] ticketTypeRequests = { ticketRequestAdult, ticketRequestChild, ticketRequestInfant };
      Long accountId = 1L;

      TicketServiceImpl ticketService = new TicketServiceImpl();

      exception.expectMessage("Cannot purchase more than 20 Tickets");
      ticketService.purchaseTickets(accountId, ticketTypeRequests);
   }

   @Test
   public void throwInvalidAccompanyInfantChildTest() {

      TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(Type.ADULT, 0);
      TicketTypeRequest ticketRequestChild = new TicketTypeRequest(Type.CHILD, 1);
      TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(Type.INFANT, 3);
      TicketTypeRequest[] ticketTypeRequests = { ticketRequestAdult, ticketRequestChild, ticketRequestInfant };
      Long accountId = 1L;

      TicketServiceImpl ticketService = new TicketServiceImpl();

      exception.expectMessage("Child and Infants tickets must be accompanied with atleast 1 adult");
      ticketService.purchaseTickets(accountId, ticketTypeRequests);
   }

   @Test
   public void throwInvalidInfantLimitExceptionTest() {

      TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(Type.ADULT, 1);
      TicketTypeRequest ticketRequestChild = new TicketTypeRequest(Type.CHILD, 1);
      TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(Type.INFANT, 3);
      TicketTypeRequest[] ticketTypeRequests = { ticketRequestAdult, ticketRequestChild, ticketRequestInfant };
      Long accountId = 2L;

      TicketServiceImpl ticketService = new TicketServiceImpl();

      exception.expectMessage("Cannot buy more infants tickets than adults ticket");
      ticketService.purchaseTickets(accountId, ticketTypeRequests);
   }

}