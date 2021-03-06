package com.bookshop.mybookshop.services.impl;

import com.bookshop.mybookshop.dao.BookRatingRepository;
import com.bookshop.mybookshop.dao.BookRepository;
import com.bookshop.mybookshop.dao.IBookRatingCount;
import com.bookshop.mybookshop.domain.book.Book;
import com.bookshop.mybookshop.domain.book.BookRate;
import com.bookshop.mybookshop.domain.book.BookRating;
import com.bookshop.mybookshop.dto.RatingDto;
import com.bookshop.mybookshop.services.RatingService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final BookRepository bookRepository;
    private final BookRatingRepository bookRatingRepository;

    @Override
    public void addRateIntoOverallRating(String slug, Integer value) {
        Book book = bookRepository.findBookBySlug(slug);
        BookRating newRate = new BookRating();
        newRate.setDateTime(LocalDateTime.now());
        newRate.setBook(book);
        switch (value) {
            case (1):
                newRate.setBookRate(BookRate.ONE);
                break;
            case (2):
                newRate.setBookRate(BookRate.TWO);
                break;
            case (3):
                newRate.setBookRate(BookRate.THREE);
                break;
            case (4):
                newRate.setBookRate(BookRate.FOUR);
                break;
            case (5):
                newRate.setBookRate(BookRate.FIVE);
                break;
            default:
                break;
        }
        BookRating savedRating = bookRatingRepository.save(newRate);
        book.getRating().add(savedRating);
        bookRepository.save(book);
    }

    @Override
    public RatingDto receiveBookRating(String slug) {
        Book bookBySlug = bookRepository.findBookBySlug(slug);
        List<IBookRatingCount> ratingCount = bookRatingRepository.countBookRatesCount(bookBySlug.getId());
        RatingDto ratingDto = new RatingDto();
        ratingDto.setRatingOverallCount((int) bookRatingRepository.countByBookSlug(slug));
        ratingCount.forEach(bookRatingCount -> {
            switch (bookRatingCount.getRate()) {
                case ONE:
                    ratingDto.setOneStarCount(bookRatingCount.getBookRateCount());
                    break;
                case TWO:
                    ratingDto.setTwoStarsCount(bookRatingCount.getBookRateCount());
                    break;
                case THREE:
                    ratingDto.setThreeStarsCount(bookRatingCount.getBookRateCount());
                    break;
                case FOUR:
                    ratingDto.setFourStarsCount(bookRatingCount.getBookRateCount());
                    break;
                case FIVE:
                    ratingDto.setFiveStarsCount(bookRatingCount.getBookRateCount());
                    break;
                default:
                    break;
            }
        });
        return ratingDto;
    }
}
