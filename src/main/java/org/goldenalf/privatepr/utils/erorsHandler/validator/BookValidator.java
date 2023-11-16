package org.goldenalf.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@AllArgsConstructor
public class BookValidator implements Validator {
    private final ErrorHandler errorHandler;

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        Room room = book.getRoom();

        List<Book> bookList = room.getBookList();
        for (Book existingBook : bookList) {
            if (!isFreePeriodBetweenExistingDates(existingBook.getCheckIn(), existingBook.getCheckOut(), book.getCheckIn(), book.getCheckOut())) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String checkIn = formatter.format(existingBook.getCheckIn());
                String checkOut = formatter.format(existingBook.getCheckOut());
                String dateRangeMessage = errorHandler
                        .getErrorMessage("validation.hotelBook.date.exception.range-is-occupied")
                        .formatted(checkIn, checkOut);
                errors.rejectValue("checkIn", "409", dateRangeMessage);
                return;
            }
        }
    }

    public static boolean isFreePeriodBetweenExistingDates(LocalDate existingCheckIn,
                                                           LocalDate existingCheckOut,
                                                           LocalDate newCheckIn,
                                                           LocalDate newCheckOut) {
        boolean isNewCheckInAfterExistingCheckOut = newCheckIn.isAfter(existingCheckOut);
        boolean isNewCheckOutBeforeExistingCheckIn = newCheckOut.isBefore(existingCheckIn);
        return isNewCheckInAfterExistingCheckOut || isNewCheckOutBeforeExistingCheckIn;
    }
}
