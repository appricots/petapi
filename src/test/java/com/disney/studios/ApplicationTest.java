package com.disney.studios;


import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.ArraySizeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {


    @Test
    public void contextLoads() {
    }


    @Autowired
    TestRestTemplate restTemplate;


    @Test
    public void testListAll() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.getForEntity("/pets", String.class);

        JSONAssert.assertEquals("{total:90}", response.getBody(), false);
        JSONAssert.assertEquals("{groups:[4]}", response.getBody(), new ArraySizeComparator(JSONCompareMode.LENIENT));

    }
  @Test
    public void testList() throws JSONException {


        ResponseEntity<String> response = restTemplate.getForEntity("/pets/Pug", String.class);

        JSONAssert.assertEquals("{total:39}", response.getBody(), false);
        JSONAssert.assertEquals("{items:[39]}", response.getBody(), new ArraySizeComparator(JSONCompareMode.LENIENT));

    }

    @Test
    public void testGet() throws JSONException {


        ResponseEntity<String> response = restTemplate.getForEntity("/pet/7", String.class);

        JSONAssert.assertEquals("{id:7}", response.getBody(), false);

    }

    @Test
    public void testVoteUp() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/vote/16/true", entity, String.class
        );

        JSONAssert.assertEquals("{id:16, voteCount:1}", response.getBody(), false);
    }

    @Test
    public void testVoteUpDups() throws JSONException {
        MultiValueMap<String, String> headers = new HttpHeaders();


        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/vote/15/true", entity, String.class
        );

        JSONAssert.assertEquals("{id:15, voteCount:1}", response.getBody(), false);

        response = restTemplate.postForEntity(
                "/vote/15/true", entity, String.class
        );

        JSONAssert.assertEquals("{id:15, voteCount:2}", response.getBody(), false);

        headers.put(HttpHeaders.COOKIE, response.getHeaders().get("Set-Cookie")); //pass along the cookies


        entity = new HttpEntity<String>(null, headers);
        response = restTemplate.postForEntity(
                "/vote/15/true", entity, String.class
        );

        Assert.assertEquals("Vote ignored. Already voted up", response.getBody());


        headers.set(HttpHeaders.COOKIE, "cid=differentone");


        entity = new HttpEntity<String>(null, headers);
        response = restTemplate.postForEntity(
                "/vote/15/true", entity, String.class
        );

        JSONAssert.assertEquals("{id:15, voteCount:3}", response.getBody(), false);

    }


    @Test
    public void testVoteUpAndDown() throws JSONException {
        MultiValueMap<String, String> headers = new HttpHeaders();


        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/vote/14/true", null, String.class
        );

        List<String> mycookies = response.getHeaders().get("Set-Cookie");
        JSONAssert.assertEquals("{id:14, voteCount:1}", response.getBody(), false);

        //vote down by different client
        response = restTemplate.postForEntity("/vote/14/false", entity, String.class);

        // vote down from different person should not affect count of favorites (count of vote up)
        JSONAssert.assertEquals("{id:14, voteCount:1}", response.getBody(), false);


        headers.put(HttpHeaders.COOKIE, mycookies); //pass along the cookies
        entity = new HttpEntity<String>(null, headers);
        //vote down by user that voted up and changed their mind
        response = restTemplate.postForEntity("/vote/14/false", entity, String.class);

        JSONAssert.assertEquals("{id:14, voteCount:0}", response.getBody(), false);


    }

}
