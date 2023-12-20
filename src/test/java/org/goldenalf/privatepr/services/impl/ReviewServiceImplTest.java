package org.goldenalf.privatepr.services.impl;

import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Review;
import org.goldenalf.privatepr.repositories.ReviewRepository;
import org.goldenalf.privatepr.utils.VerifyingAccess;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.ReviewErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private VerifyingAccess verifyingAccess;
    @Mock
    private ErrorHandler errorHandler;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void save_forExistsReview_shouldSetDateAndSaveReview() {
        Review review = new Review();
        Client client = getClient();
        String login = client.getLogin();
        client.setLogin(login);
        review.setClient(client);

        doNothing().when(verifyingAccess).checkPossibilityAction(login);

        reviewService.save(review);

        //Проверяем, что дата установилась
        assertNotNull(review.getDate());

        //Метод сохранения вызывается ровно один раз
        verify(reviewRepository, times(1)).save(review);

        //checkPossibilityAction вызывается с правильным логином
        verify(verifyingAccess, times(1)).checkPossibilityAction(login);
    }

    @Test
    void delete_shouldDeleteExistsReview() {
        Review reviewInDB = getReview();
        int reviewId = reviewInDB.getId();
        Client client = getClient();
        reviewInDB.setClient(client);

        when(reviewService.getReview(reviewId)).thenReturn(Optional.of(reviewInDB));
        doNothing().when(verifyingAccess).checkPossibilityAction(client.getLogin());

        reviewService.delete(reviewId);

        //checkPossibilityAction вызывается с правильным логином
        verify(verifyingAccess, times(1)).checkPossibilityAction(client.getLogin());

        //deleteById вызывается ровно один раз
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void delete_forNotExistsReview_shouldThrowReviewErrorException() {
        int reviewId = 22;

        when(reviewService.getReview(reviewId)).thenReturn(Optional.empty());
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");

        assertThrows(ReviewErrorException.class, () -> reviewService.delete(reviewId));

        // Убедимся, что checkPossibilityAction никогда не вызывается
        verify(verifyingAccess, never()).checkPossibilityAction(any());

        // Убедимся, что метод deleteById никогда не вызывается
        verify(reviewRepository, never()).deleteById(anyInt());
    }

    @Test
    void update_ReviewExistsAndCorrect_shouldUpdateReview() {
        Review reviewInDB = getReview();
        int reviewId = reviewInDB.getId();
        Client clientInDB = getClient();
        String login = clientInDB.getLogin();
        reviewInDB.setClient(clientInDB);

        Review updatedReview = new Review();
        updatedReview.setClient(clientInDB);

        when(reviewService.getReview(reviewId)).thenReturn(Optional.of(reviewInDB));
        doNothing().when(verifyingAccess).checkPossibilityAction(clientInDB.getLogin(), login);

        reviewService.update(reviewId, updatedReview);

        //Проверяем, что id правильно назначен
        assertEquals(reviewId, updatedReview.getId());

        //Проверяем, что дата установилась
        assertNotNull(updatedReview.getDate());

        // Проверяем, что метод save вызван один раз
        verify(reviewRepository, times(1)).save(updatedReview);
    }

    private Client getClient() {
        Client client = new Client();
        client.setLogin("Monarch");
        return client;
    }

    private Review getReview() {
        return new Review(1, LocalDate.now());
    }
}