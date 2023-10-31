package cmu.edu.myip;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
public class IPmodel {
    HashMap<String, Integer> searchIP = new HashMap<>();
    Integer ipCount;

    /**
     * calculate the number of searched IP and store in hashMap
     * @param ip
     * @return the Top 1 IP search terms
     */
    public String doCalSearch( String ip) {
            ipCount =searchIP.get(ip);
            System.out.println(ip);
            if(ipCount == null) {
                searchIP.put(ip, 0);
            }
            int tmp = searchIP.get(ip) + 1;
            searchIP.put(ip,tmp );
            System.out.println("Num of search:" + searchIP.get(ip));


        Map.Entry<String, Integer> maxEntry = null;
        //https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map
        for (Map.Entry<String, Integer> entry : searchIP.entrySet())
        {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue())
            {
                maxEntry = entry;
            }
        }
        StringBuilder max = new StringBuilder();
        for (Map.Entry<String, Integer> entry : searchIP.entrySet())
        {
            if (maxEntry.getValue() == entry.getValue())
            {
                max.append(entry.getKey());
                max.append(" & ");
            }
        }
        max.deleteCharAt(max.length()-1);
        max.deleteCharAt(max.length()-1);
        max.deleteCharAt(max.length()-1);
        return max.toString();
    }

    /**
     * get data from MongoDb
     */
    String uri = "mongodb+srv://ooisabellelol:cute0128@cluster0.kfo8q.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    public String getFromdb(){
        String numOfSearch = null;
        long now = 0;
        long start = System.currentTimeMillis();
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("MyIpInfo");
            MongoCollection<Document> collection = database.getCollection("ipaddress");

            try {
                Bson allfields = Projections.fields();
                MongoCursor<Document> getAll = collection.find()
                        .projection(allfields)
                        .iterator();
                now = System.currentTimeMillis();


                Bson projectionFields = Projections.fields(
                        Projections.include("ip"),
                        Projections.excludeId());

                MongoCursor<Document> cursor = collection.find()
                        .projection(projectionFields)
                        .sort(Sorts.descending("title")).iterator();

                try {
                    while (cursor.hasNext()) {
                        JSONObject ipAdrr = new JSONObject(cursor.next().toJson());
                        numOfSearch = doCalSearch(ipAdrr.getString("ip"));
                    }

                    while (getAll.hasNext()) {
                        JSONObject zone = new JSONObject(getAll.next().toJson());
                        getTimeZone(zone.getString("timezone"));
                        parse(zone);
                    }

                } finally {
                    cursor.close();
                }



            } catch (MongoException | JSONException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }

        String time = String.valueOf(now - start);
        String zone =displayZone();
        int count = countData();
        String dash = dashBoard();
        timeZoneMap.clear();
        searchIP.clear();
        parsedata.setLength(0);
        return time +"@"+ zone +"@"+ numOfSearch +"@"+ count + "@" + dash;

    }
    String timeStamp;

    /**
     * save data to mongoDb
     * @param data new data fetched from 3rd party API
     */
    public void saveDatatoDb( String data) {
        JSONObject objectIp = new JSONObject(data);
        String ipAdr;
        if (objectIp.has("ip")) {
            ipAdr = objectIp.getString("ip");
        } else {
            ipAdr = "N/A";
        }
        String city;
        if (objectIp.has("city")) {
            city = objectIp.getString("city");
        } else {
            city = "N/A";
        }
        String region;
        if (objectIp.has("region")) {
            region = objectIp.getString("region");
        } else {
            region = "N/A";
        }
        String country;
        if (objectIp.has("country")) {
            country = objectIp.getString("country");
        } else {
            country = "N/A";
        }
        String loc;
        if (objectIp.has("loc")) {
            loc = objectIp.getString("loc");
        } else {
            loc = "N/A";
        }
        String postal;
        if (objectIp.has("postal")) {
            postal = objectIp.getString("postal");
        } else {
            postal = "N/A";
        }

        String timezone;
        if (objectIp.has("timezone")) {
            timezone = objectIp.getString("timezone");
        } else {
            timezone = "N/A";
        }

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("MyIpInfo");
            MongoCollection<Document> collection = database.getCollection("ipaddress");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            timeStamp =dtf.format(now);
            try {
                InsertOneResult result1 = collection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("TimeStamp", timeStamp)
                        .append("ip", ipAdr)
                        .append("city", city)
                        .append("region", region)
                        .append("country", country)
                        .append("loc", loc)
                        .append("postal", postal)
                        .append("timezone", timezone)
                );
                System.out.println("Success! Inserted document id: " + result1.getInsertedId());

            } catch (MongoException | JSONException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }

    }

    /**
     * calculate number of each timeZone
     */
    HashMap<String, Integer> timeZoneMap = new HashMap<>();
    Integer zoneCount;
    public void getTimeZone( String timeZone) {
        zoneCount =timeZoneMap.get(timeZone);
        System.out.println(timeZone);
        if(zoneCount == null) {
            timeZoneMap.put(timeZone, 0);
        }
        int tmp = timeZoneMap.get(timeZone) + 1;
        timeZoneMap.put(timeZone,tmp );
        System.out.println("timeZone:" + timeZoneMap.get(timeZone));
    }

    public String displayZone() {
        String s = "<br>";
        for (Map.Entry<String, Integer> entry : timeZoneMap.entrySet())
        {
            s+=entry.getKey();
            s+=": ";
            s+=entry.getValue();
            s+="<br>";
        }
        return s;
    }

    /**
     * the data displayed to result.jsp
     */
    StringBuilder parsedata = new StringBuilder();
    int count =0 ;
    public void parse(JSONObject arg) {
        parsedata.append("<tr>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("TimeStamp"));
        parsedata.append("</th>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("ip"));
        parsedata.append("</th>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("city"));
        parsedata.append("</th>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("region"));
        parsedata.append("</th>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("country"));
        parsedata.append("</th>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("loc"));
        parsedata.append("</th>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("postal"));
        parsedata.append("</th>");
        parsedata.append("<th>");
        parsedata.append(arg.getString("timezone"));
        parsedata.append("</th>");
        parsedata.append("</tr>");
        count++;
    }

    public String dashBoard() {
        return parsedata.toString();
    }

    public int countData() {
        return count;
    }


}
