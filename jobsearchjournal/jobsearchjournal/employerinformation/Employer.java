package jobsearchjournal.employerinformation;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Christina Aiello on 3/22/2015.
 */
public class Employer {
    private String id;
    private String name;
    private String position;
    private String size;
    private String location;
    private String misc;
    private String goal;
    private String website;
    private String industry;
    private String overallRating;
    private String cultureAndValuesRating;
    private String seniorLeadershipRating;
    private String compensationAndBenefitsRating;
    private String careerOpportunitiesRating;
    private String workLifeBalanceRating;
    private Boolean couldRetrieveFromGlassdoor = false;
    private String step;
    private byte[] logoByteArray;
    private byte[] streetByteArray;
    private String linkToJobPosting;

    public Employer() {
    }

    /**
     * Getter for an employer object
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for an employer object
     *
     * @return position
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * Getter for an employer object
     *
     * @return size
     */
    public String getSize() {
        return this.size;
    }

    /**
     * Getter for an employer object
     *
     * @return location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Getter for an employer object
     *
     * @return misc
     */
    public String getMisc() {
        return this.misc;
    }

    /**
     * Getter for an employer object
     *
     * @return goal
     */
    public String getGoal() {
        return this.goal;
    }

    /**
     * Getter for an employer object
     *
     * @return step
     */
    public String getStep() {
        return this.step;
    }

    /**
     * Getter for an employer object
     *
     * @return website
     */
    public String getWebsite() {
        return this.website;
    }

    /**
     * Getter for an employer object
     *
     * @return industry
     */
    public String getIndustry() {
        return this.industry;
    }

    /**
     * Getter for an employer object
     *
     * @return overallRating
     */
    public String getOverallRating() {
        return this.overallRating;
    }

    /**
     * Getter for an employer object
     *
     * @return cultureAndValuesRating
     */
    public String getCultureAndValuesRating() {
        return this.cultureAndValuesRating;
    }

    /**
     * Getter for an employer object
     *
     * @return seniorLeadershipRating
     */
    public String getSeniorLeadershipRating() {
        return this.seniorLeadershipRating;
    }

    /**
     * Getter for an employer object
     *
     * @return compensationAndBenefitsRating
     */
    public String getCompensationAndBenefitsRating() {
        return this.compensationAndBenefitsRating;
    }

    /**
     * Getter for an employer object
     *
     * @return careerOpportunitiesRating
     */
    public String getCareerOpportunitiesRating() {
        return this.careerOpportunitiesRating;
    }

    /**
     * Getter for an employer object
     *
     * @return workLifeBalanceRating
     */
    public String getWorkLifeBalanceRating() {
        return this.workLifeBalanceRating;
    }

    /**
     * Getter for an employer object
     * @return
     */
    public String getID() {
        return this.id;
    }

    /**
     * Setter for an employer object
     * @param id
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setStep(String step) {
        this.step = step;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setMisc(String misc) {
        this.misc = misc;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setGoal(String goal) {
        this.goal = goal;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setCultureAndValuesRating(String cultureAndValuesRating) {
        this.cultureAndValuesRating = cultureAndValuesRating;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setSeniorLeadershipRating(String seniorLeadershipRating) {
        this.seniorLeadershipRating = seniorLeadershipRating;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setCompensationAndBenefitsRating(String compensationAndBenefitsRating) {
        this.compensationAndBenefitsRating = compensationAndBenefitsRating;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setCareerOpportunitiesRating(String careerOpportunitiesRating) {
        this.careerOpportunitiesRating = careerOpportunitiesRating;
    }

    /**
     * Setter for an employer object
     *
     */
    public void setWorkLifeBalanceRating(String workLifeBalanceRating) {
        this.workLifeBalanceRating = workLifeBalanceRating;
    }

    /**
     * This method extracts company information from the JSON that was returned
     *
     * @param jsonString is the JSON string
     * @throws java.io.FileNotFoundException if this file could not be found
     * @throws org.json.JSONException
     */
    public void extractCompanyInformation(String jsonString) throws IOException, JSONException {
        // Reader for this URL
        JSONObject reader = new JSONObject(jsonString);

        // Getting the response information for this URL
        JSONObject responseInfo = reader.getJSONObject("response");
        JSONArray employersInfoArray = responseInfo.getJSONArray("employers");

        // If there was at least one result found, use the first one (closest match):
        if (employersInfoArray.length() >= 1) {
            // Now we need to get the necessary information from the first item in this array:
            JSONObject employersInfoObject = employersInfoArray.getJSONObject(0);
            this.setWebsite(employersInfoObject.getString("website"));
            this.setIndustry(employersInfoObject.getString("industry"));
            this.setLogoByteArray(makeImageByteArray(employersInfoObject.getString("squareLogo")));
            this.setOverallRating(employersInfoObject.getString("overallRating"));
            this.setCultureAndValuesRating(employersInfoObject.getString("cultureAndValuesRating"));
            this.setSeniorLeadershipRating(employersInfoObject.getString("seniorLeadershipRating"));
            this.setCompensationAndBenefitsRating(employersInfoObject.getString("compensationAndBenefitsRating"));
            this.setCareerOpportunitiesRating(employersInfoObject.getString("careerOpportunitiesRating"));
            this.setWorkLifeBalanceRating(employersInfoObject.getString("workLifeBalanceRating"));
            this.setCouldRetrieveFromGlassdoor(true);
        } else {
            this.setWebsite("");
            this.setIndustry("");
            this.setOverallRating("");
            this.setCultureAndValuesRating("");
            this.setSeniorLeadershipRating("");
            this.setCompensationAndBenefitsRating("");
            this.setCareerOpportunitiesRating("");
            this.setWorkLifeBalanceRating("");
            this.setCouldRetrieveFromGlassdoor(false);
        }

    }

    /**
     * @param streetViewUrl is the URL that you are using to get the streetview image
     */
    public void fetchCompanyStreetView(final String streetViewUrl) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Log.i("Employer", "Trying to get byte array with url: " + streetViewUrl);
                    // Getting the image byte array, then setting it as this employer's streetview image byte array
                    setStreetByteArray(makeImageByteArray(streetViewUrl));
                    //Log.i("Employer", "Got byte array");
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread.join();
    }

    /**
     * Referenced from http://www.tutorialspoint.com/android/android_json_parser.htm
     *
     * @param urlString is the URL that you are fetching and parsing JSON from
     */
    public void fetchCompanyInformation(final String urlString) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Creating our request:
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(1000);
                    conn.setConnectTimeout(1200);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Requesting information
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    // Converting information to a string
                    String data = convertStreamToString(stream);

                    // Getting information from this string
                    extractCompanyInformation(data);

                    // Closing stream
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread.join();
    }

    /**
     * Source: Referenced from http://www.tutorialspoint.com/android/android_json_parser.htm
     *
     * @param is is the input stream
     * @return a String of JSON data
     */
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * This method will construct the necessary API URL for a specific company, by name.
     *
     * @param companyName is the name of the company whose information we want
     * @return the URL to get the company's information
     */
    public String constructAPIURL(String companyName) {
        // First, we trim any leading or trailing spaces off the name
        String trimmedName = companyName.trim();
        // Next, we need to replace any middle spaces with %20's
        String noSpacesInName = trimmedName.replaceAll(" ", "%20");
        // Lastly, return the URL for this company name
        return "https://api.glassdoor.com/api/api.htm?v=1&format=json&t.p=31746&t.k=f6EKkHN4wb9&action=employers&q=" + noSpacesInName + "&userip=" + getLocalIpAddress() + "&useragent=" + System.getProperty("http.agent");
    }

    /**
     * Lets the boolean for "Could retrieve information from Glassdoor" be set
     * @param couldRetrieveFromGlassdoor is whether or not Glassdoor has info for this company
     */
    public void setCouldRetrieveFromGlassdoor(boolean couldRetrieveFromGlassdoor) {
        this.couldRetrieveFromGlassdoor = couldRetrieveFromGlassdoor;
    }

    /**
     * Getter for the boolean that tells you if Glassdoor has data for this company or not.
     * @return true if Glassdoor had the info, else false.
     */
    public Boolean getCouldRetrieveFromGlassdoor() {
        return couldRetrieveFromGlassdoor;
    }

    /**
     * Source: http://www.tutorialforandroid.com/2009/10/how-to-insert-image-data-to-sqlite.html
     * Author: Almond Joseph Mendoza
     * Date: 10/12/2009
     * @param imageURL is the URL of the image being accessed
     * @return is a bytearray for this image
     * @throws IOException
     */
    public byte[] makeImageByteArray(String imageURL) throws IOException {
        byte[] imageByteArray = new byte[500];
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        HttpGet mHttpGet = new HttpGet(imageURL);
        HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
        if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = mHttpResponse.getEntity();
            if (entity != null) {
                imageByteArray = EntityUtils.toByteArray(entity);
            }
        }
        return imageByteArray;
    }

    /**
     * This will use a helper to turn a URL into an image byte array, then save the byte array in the Employer object
     * @param byteArray is an array of bytes that make this image
     * @throws IOException
     */
    public void setLogoByteArray(byte[] byteArray) throws IOException {
        this.logoByteArray = byteArray;
    }

    /**
     * This returns a byte array for this company's logo
     * @return
     */
    public byte[] getLogoByteArray() {
        return this.logoByteArray;
    }

    /**
     * Gets a company's streetview image
     * @return
     */
    public byte[] getStreetByteArray() {
        return streetByteArray;
    }

    /**
     * Sets a company's street view image
     * @param streetByteArray
     */
    public void setStreetByteArray(byte[] streetByteArray) {
        this.streetByteArray = streetByteArray;
        Log.i("setting", "Streetbytearray is: " + streetByteArray.toString());
    }

    public String getLinkToJobPosting() {
        return linkToJobPosting;
    }

    public void setLinkToJobPosting(String linkToJobPosting) {
        this.linkToJobPosting = linkToJobPosting;
    }

    // Reference: evertvandenbruel https://stackoverflow.com/users/952611/evertvandenbruel
    // https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
    public static String getLocalIpAddress() {
        try {
            forEach (Enumeration<NetworkInterface> enumeration in NetworkInterface.getNetworkInterfaces()) {
                forEach (Enumeration<InetAddress> enumIpAddr in intf.getInetAddresses()) {
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String hostAddress = inetAddress.getHostAddress();
                        Log.i("setting", "hostAddress is: " + hostAddress);
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}