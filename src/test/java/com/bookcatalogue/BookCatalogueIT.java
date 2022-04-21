package com.bookcatalogue;

import com.bookcatalogue.dto.Book;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;


@SpringBootTest
public class BookCatalogueIT {

    TestRestTemplate restTemplate =new TestRestTemplate();

    String expected= "[\r\n" +
            "    {\r\n" +
            "        \"id\": 4,\r\n" +
            "        \"author\": \"J.R.R.Tolkien\",\r\n" +
            "        \"title\": \"The lord of the Rings\",\r\n" +
            "        \"isbn\": \"9780007136582\",\r\n" +
            "        \"isAvailable\": false\r\n" +
            "    },\r\n" +
            "    {\r\n" +
            "        \"id\": 5,\r\n" +
            "        \"author\": \"J.R.R.Tolkien\",\r\n" +
            "        \"title\": \"The lord of the Rings\",\r\n" +
            "        \"isbn\": \"9780007136582\",\r\n" +
            "        \"isAvailable\": true\r\n" +
            "    }\r\n" +
            "]";


    @Test
    public void getAllBooksTest() {
        ResponseEntity<List> bookList =	restTemplate.getForEntity("http://localhost:8082/books", List.class);
        Assert.assertEquals(HttpStatus.OK, bookList.getStatusCode());
        Assert.assertNotNull(bookList);
    }

    @Test
    public void getBookByIdTest() throws JSONException {

        String book=
                "{\"id\":4,\"author\":\"J.R.R.Tolkien\",\"title\":\"The lord of the Rings\",\"isbn\":\"9780007136582\",\"isAvailable\":false}";

        ResponseEntity<String> myString1 =restTemplate.getForEntity("http://localhost:8082/books/4", String.class);
        ResponseEntity<String> myString2 =restTemplate.getForEntity("http://localhost:8082/books/10", String.class);
        Assert.assertEquals(HttpStatus.OK, myString1.getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND, myString2.getStatusCode());
        Assert.assertEquals(book, myString1.getBody());
    }

    @Test
    public void findBooksByAuthorTest() throws JSONException {
        ResponseEntity<String> myString1 =restTemplate.getForEntity("http://localhost:8082/books/authors/J.R.R.Tolkien", String.class);
        ResponseEntity<String> myString2 =restTemplate.getForEntity("http://localhost:8082/books/authors/Asimov", String.class);
        Assert.assertEquals(HttpStatus.OK, myString1.getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND, myString2.getStatusCode());
        JSONAssert.assertEquals(expected, myString1.getBody(), false);
    }

    @Test
    public void findBooksByTitleTest() throws JSONException {
        ResponseEntity<String> myString1 =restTemplate.getForEntity("http://localhost:8082/books/titles/The lord of the Rings", String.class);
        ResponseEntity<String> myString2 =restTemplate.getForEntity("http://localhost:8082/books/titles/Foundation", String.class);
        Assert.assertEquals(HttpStatus.OK, myString1.getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND, myString2.getStatusCode());
        JSONAssert.assertEquals(expected, myString1.getBody(), false);
    }

    @Test
    public void findBooksByIsbnTest() throws JSONException {
        ResponseEntity<String> myString1 =restTemplate.getForEntity("http://localhost:8082/books/isbn/9780007136582", String.class);
        ResponseEntity<String> myString2 =restTemplate.getForEntity("http://localhost:8082/books/isbn/2222222222222", String.class);
        Assert.assertEquals(HttpStatus.OK, myString1.getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND, myString2.getStatusCode());
        JSONAssert.assertEquals(expected, myString1.getBody(), false);
    }

    @Test
    public void createBookTest() {

        Book book1 = new Book("Douglas Adams", "And Another Thing", "9781401395216", true);
        Book book2 = new Book("Douglas Adams", "And Another Thing", "9781401395a", true);
        Book book3 = new Book("", "And Another Thing", "9781401395", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Book> request1 = new HttpEntity<Book>(book1, headers);
        HttpEntity<Book> request2 = new HttpEntity<Book>(book2, headers);
        HttpEntity<Book> request3 = new HttpEntity<Book>(book3, headers);

        ResponseEntity<String> response1 =	restTemplate.postForEntity("http://localhost:8082/books", request1, String.class);
        ResponseEntity<String> response2 =	restTemplate.postForEntity("http://localhost:8082/books", request2, String.class);
        ResponseEntity<String> response3 =	restTemplate.postForEntity("http://localhost:8082/books", request3, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
    }

    @Test
    public void updateBookByIdTest() {

        String id = "7";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Book> book =restTemplate.getForEntity("http://localhost:8082/books/"+id, Book.class);

        if(HttpStatus.OK == book.getStatusCode()){
            Book myBook = book.getBody();
            if (myBook.getIsAvailable()){
                myBook.setIsAvailable(false);
            } else {
                myBook.setIsAvailable(true);
            }

            HttpEntity<Book> requestUpdate = new HttpEntity<>(myBook, headers);
            restTemplate.exchange("http://localhost:8082/books/"+id, HttpMethod.PUT, requestUpdate, Void.class);

        } else {
            Assert.assertEquals(HttpStatus.NOT_FOUND, book.getStatusCode());
        }
    }

    @Test
    public void deleteBookByIdTest() {

        String id = "8";

        ResponseEntity<String> myString =restTemplate.getForEntity("http://localhost:8082/books/"+id, String.class);

        if(HttpStatus.OK == myString.getStatusCode()){
            restTemplate.delete("http://localhost:8082/books/"+id);

        } else {
            Assert.assertEquals(HttpStatus.NOT_FOUND, myString.getStatusCode());
        }
    }

}
