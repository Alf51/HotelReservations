package org.goldenalf.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.models.Room;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@AllArgsConstructor
public class BookValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        Room room = book.getRoom();

        List<Book> bookList = room.getBookList();
        System.out.println();
        for (Book oldBook : bookList) {
            boolean isCheckInAfterOldCheckOut = book.getCheckIn().isAfter(oldBook.getCheckOut()); //Дата въезда позже уже существующей даты выезда.
            boolean isCheckOutBeforeOldCheckIn = book.getCheckOut().isBefore(oldBook.getCheckIn()); //Дата выезда раньше уже существующей даты въезда.
            if (isCheckOutBeforeOldCheckIn || isCheckInAfterOldCheckOut) {
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String checkIn = formatter.format(oldBook.getCheckIn());
                String checkOut = formatter.format(oldBook.getCheckOut());
                String dateRangeMessage = "Дата с " + checkIn + " по " + checkOut + " занята";
                errors.rejectValue("checkIn", "409", dateRangeMessage);
                return;
            }
        }
    }
}
