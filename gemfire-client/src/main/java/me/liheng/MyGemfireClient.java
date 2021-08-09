package me.liheng;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;

public class MyGemfireClient {

    public static void main( String[] args ) {
        ClientCache clientCache = new ClientCacheFactory()
                .set("cache-xml-file", "xml/clientCache.xml")
                .create();

        Region<Integer, String> customers = clientCache.getRegion("Customer");

        System.out.println(customers == null);
    }
}
