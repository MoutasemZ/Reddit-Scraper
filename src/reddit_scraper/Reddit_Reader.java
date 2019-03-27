/*
 * Copyright (C) 2019  Moutasem Zakkar, www.linkedin.com/in/mzakkar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package reddit_scraper;

import DateTimeHelperLib.UtcDateConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dbmodule.DBController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.utils.URIBuilder;


/**
 *
 * @author Moutasem Zakkar, moutasem.zakkar@gmail.com  
 */
public class Reddit_Reader {

    int _LoadingSize = 100 ; // 0 = default loading size set by the data provider pushshift, which is 25. 
    int AttemptsPerSubereddit = 10;
    long stopDate; // the latest date before which the submissions are downloaded.
    private ArrayList<SubRedditInfo> subRedditsList;
    private Gson gsonParser;
    private PostInfoComparator pi_Comparator;
    private DBController myDb;
    String TextScrappingDirectory; 

    // Constructor
    public Reddit_Reader(String _TextScrappingDirectory) {
        TextScrappingDirectory = _TextScrappingDirectory ;

        // Step #1 Read the list of Subreddit in the Text file health_subreddits.txt
        ReadSubredditsMetaData();
        // Calculate the stop date for the crawler, which is 12:00 AM of today
        stopDate = UtcDateConverter.getTodayBeginningTimeStampDBForm();

        // Step #2 Initilize the gsonParser
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Post_list.class, new Post_list_Deserializer());
        gsonBuilder.registerTypeAdapter(PostInfo.class, new PostInfo_Deserializer());
        gsonParser = gsonBuilder.create();

        pi_Comparator = new PostInfoComparator(); // will be used for sorting the list of posts

        // Step #3
        myDb = new DBController();

    }

    // Main Processing Loop
    public void ScrapeSubredditsList() {
        try {
            try {
                for (int i = 0; i < subRedditsList.size(); i++) {
                    System.out.println("Now processing: " + subRedditsList.get(i).name);
                    ProcessSubreddit(subRedditsList.get(i));
                    SaveSubredditsMetaData();
                } // for
            } catch (Exception E) {
                System.out.println(E);
            } //
        } finally {
            myDb.endSession();
        }
    }

    private void ProcessSubreddit(SubRedditInfo subReddit) {

        URL myURL = null;
        InputStream myWebStream = null;
        BufferedWriter mybf = OpenSubRedditFile(subReddit);
        // Repeat until you receive an empty result then exit from inside the loop

        
        for (int i = 0; i <= AttemptsPerSubereddit; i++) {
            try {
                // Form the URL for the target
                myURL = BuildSubmissionChunkURL(subReddit.name, _LoadingSize, subReddit.getLatestSaved(), stopDate);
                String hg = myURL.toString();
                // Download  the target data
                myWebStream = ExecuteWebRequest(myURL);
                
                //Convert the JSON data to JAVA objects (list of posts)
                Post_list myList = gsonParser.fromJson(new InputStreamReader(myWebStream), Post_list.class);
                PostInfo[] data = myList.getData();
                if (data.length == 0) {
                    break;
                }
                // sort the list of posts
                Arrays.sort(data, pi_Comparator);
                // save it to the database and to the file system
                SavePostList(subReddit, data, mybf);
                // Update the latestsaved date 
                PostInfo latestPost = data[data.length - 1]; // last element in the list
                subReddit.setLatestSaved(latestPost.getPost_Date());
            } catch (SocketTimeoutException ex) {
                System.out.println(ex);
            } catch (IOException e) {
                System.out.println(e);
            } catch (URISyntaxException e) {
                System.out.println(e);
            }
        } // for
        try {
            mybf.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    } // ProcessSubreddit

    private void SavePostList(SubRedditInfo subReddit, PostInfo[] data, BufferedWriter bf) {
        long j = subReddit.getTotalPosts() ;
        for (int i = 0; i < data.length; i++) {
            if (data[i].getBody() != "") {
                
                try {
                  // Save it to the database
                    myDb.InsertPostInfo(data[i]);
                  // Save it to the file system
                   /* bf.write(StringEscapeUtils.unescapeJson(gsonParser.toJson(data[i]))); */
                   bf.write(gsonParser.toJson(data[i]));
                   bf.newLine();
                   j++;
                } catch (IOException ioe) {
                    System.out.println(ioe);
                }
                catch (Exception e) {
                     System.out.println(e);
                }
            }
        } // for
       subReddit.setTotalPosts(j);
    }

    // Save the updated subreddit list to the file system , with the new latestsaved dates
    private void SaveSubredditsMetaData() {
        SubRedditInfo sInfo ;
        File infilePath = new File(TextScrappingDirectory + "Metadata\\List_subreddits.txt");
        try (BufferedWriter myWriter = new BufferedWriter(new FileWriter(infilePath))) {
            myWriter.write("#Name of the subreedit, Latest reading date (TimeStamp), Total posts saved so far, Latest reading date (Normal Format)");
            myWriter.newLine();
            for (int i = 0; i < subRedditsList.size(); i++) {
                sInfo = subRedditsList.get(i);
                myWriter.write(sInfo.name + ',' + String.valueOf(sInfo.getLatestSaved())+','+ String.valueOf(sInfo.getTotalPosts())+','+ UtcDateConverter.ConvertTimeStampToStr(sInfo.getLatestSaved()));
                myWriter.newLine();
            } // for
        } catch (IOException ioE) {
            System.out.println(ioE);
        } //
    }

    private URL BuildSubmissionChunkURL(String SubReddit, int size, long after_timestamp, long before_timestamp) throws URISyntaxException, MalformedURLException {
        URIBuilder myBuilder = new URIBuilder();
        myBuilder.setScheme("https");
        myBuilder.setHost("api.pushshift.io");
        myBuilder.setPath("/reddit/search/submission");
        myBuilder.setParameter("subreddit", SubReddit);
        if (size != 0) myBuilder.setParameter("size", String.valueOf(size));
        myBuilder.setParameter("sort", "asc");
        if (after_timestamp != 0) {
            myBuilder.setParameter("after", String.valueOf(after_timestamp));
        }
        if (before_timestamp != 0) {
            myBuilder.setParameter("before", String.valueOf(before_timestamp));
        }
        URI uri = myBuilder.build();
        return uri.toURL();
    }

    private InputStream ExecuteWebRequest(URL _url) throws IOException, SocketTimeoutException{

        HttpURLConnection myHttpConn = (HttpURLConnection) _url.openConnection();
        // All the request headers should be put beffore calling the connect() method.
        myHttpConn.setRequestMethod("GET");
        
         myHttpConn.connect();
                 
        //System.out.println (myHttpConn.getResponseMessage());
        return myHttpConn.getInputStream();
    }

    private void ReadSubredditsMetaData() {

        String TmpS;
        String _title = null;
        long _latestSaved;
        long _totalPosts;
        int totalItems = 0;
        int arrayCapacity = 20;
        int i;
        SubRedditInfo sInfo;
        File infilePath = new File(TextScrappingDirectory + "Metadata\\List_subreddits.txt");
        subRedditsList = new ArrayList<SubRedditInfo>(arrayCapacity);

        try (BufferedReader myinReader = new BufferedReader(new FileReader(infilePath))) {
            // Read the header line
            TmpS = myinReader.readLine();
            // Start reading the content 
            while ((TmpS = myinReader.readLine()) != null) {
                if (TmpS.isEmpty()) {
                    continue;
                }
                if (totalItems == arrayCapacity) {
                    arrayCapacity += 20;
                    subRedditsList.ensureCapacity(arrayCapacity);
                }
                String [] parts = TmpS.split(",") ;
                _title = parts[0];
                _latestSaved = Long.parseLong(parts[1]);
                _totalPosts = Long.parseLong(parts[2]);
                /*
                i = TmpS.indexOf(',');
                if (i != -1) {
                    _latestSaved = Long.parseLong(TmpS.substring(i + 1));
                    _title = TmpS.substring(0, i);
                } else {
                    _latestSaved = 0;
                    _title = TmpS;
                }
                */
                sInfo = new SubRedditInfo(_title, _latestSaved, _totalPosts);
                subRedditsList.add(sInfo);
                totalItems++;
            } // While

        } // try
        catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private BufferedWriter OpenSubRedditFile(SubRedditInfo subReddit) {
        BufferedWriter mybf = null;
        File aFile;
        String sbr_filename = TextScrappingDirectory + subReddit.name + ".json";
        boolean aNewFile = false;

        if (subReddit.getLatestSaved() == 0) { // first time crawling, do any initialization

        } // if
        else { // subReddit already exist

        } // else
        // Prepare to write the posts to the file system in addition to the DB
        aFile = new File(sbr_filename);
        /* This logic is to create the file if the
    	 * file is not already present
         */
        try {
            if (!aFile.exists()) {
                aNewFile = true;
                aFile.createNewFile();

            }
            mybf = new BufferedWriter(new FileWriter(aFile, true));
            if (aNewFile) {
                mybf.write("Author,Title,Post_Date,Post_Id,Body,Subreddit,Subreddit_Id,Score,Total_Comments");
                mybf.newLine();
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
        return mybf;
    }

}
