/*
    Stan Mijten
    28 feb 2022
 */
package org.makingstan;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebsiteStatusChecker
{
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final File outPutFile = new File("/Users/Stan/programming/programming/Website Status Checker/resources/output.json");

    public static void main(String[] fileDirectory) throws IOException
    {
        //fileDirectory at the index of 0 will be the url the user has located the file in. other parameters will be ignored
        checkWebsiteStatus(fileDirectory[0]);
    }

    private static void checkWebsiteStatus(String websiteFilePath) throws IOException
    {
        File websiteFile = new File(websiteFilePath);
        //make a buffered reader to read the file that is given to us
        BufferedReader websiteFileReader = new BufferedReader(new FileReader(websiteFile));

        //get the amount of lines the file has. we need this to be able to iterate to through every line in the file, to get ever domain name
        long lineCount = Files.lines(websiteFile.toPath()).count();

        //make the websiteFileLine array. this will later contain every that is in the file
        String websiteFileLine[] = new String[(int) lineCount];
        //first we make an array that gets all of the websites that are in the file
        for(int i = 0; i < (int) lineCount; i++)
        {
            //get the line at the index of i, i can be anything from 0 to linecounts value
            websiteFileLine[i] = Files.readAllLines(websiteFile.toPath()).get(i);
        }

        //remove every content of the file
        PrintWriter writer = new PrintWriter(outPutFile);
        writer.print("");
        writer.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outPutFile, true));
        bufferedWriter.write("{\n");

        for(int i = 0; i < lineCount; i++)
        {
            try
            {
                //make an url object
                URL url = new URL("https://"+websiteFileLine[i]);


                //establish the connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //specify that we are using a get request
                connection.setRequestMethod("GET");
                //set our user agent.
                connection.setRequestProperty("User-Agent", USER_AGENT);

                //write the json data to the output file
                bufferedWriter.append("\""+websiteFileLine[i]+"\":\""+connection.getResponseCode()+"\"\n");

                System.out.println("done with "+websiteFileLine[i]);
            }
            catch(Exception e)
            {
                //the url is malformed. or something else went wrong
            }
        }
        bufferedWriter.write("}");
        bufferedWriter.close();
        System.out.println("done!");
    }
}
