package com.macauslot.recruitment_ms;

 import org.jsoup.Jsoup;
 import org.jsoup.safety.Whitelist;
 import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupTest {



    @Test
    public  void JsoupClean(){

     String   html =
    "jaVasCript:/*-/*`/*\\`/*'/*\"/**/(/* */oNcliCk=alert() )//%0D%0A%0d%0a//</stYle/</titLe/</teXtarEa/</scRipt/--!>\\x3csVg/<sVg/oNloAd=alert()//>\\x3e\n";


      String baseUri ="";

        String doc = Jsoup.clean(html, baseUri, Whitelist.none());
        System.out.println(doc);
        System.out.println("*******");
        doc = Jsoup.clean(html, baseUri, Whitelist.simpleText());
        System.out.println(doc);
        System.out.println("*******");
        doc = Jsoup.clean(html, baseUri, Whitelist.basic());
        System.out.println(doc);
        System.out.println("*******");
        doc = Jsoup.clean(html, baseUri, Whitelist.basicWithImages());
        System.out.println(doc);
        System.out.println("*******");
        doc = Jsoup.clean(html, baseUri, Whitelist.relaxed());
        System.out.println(doc);
        System.out.println("*******");

    }


}
