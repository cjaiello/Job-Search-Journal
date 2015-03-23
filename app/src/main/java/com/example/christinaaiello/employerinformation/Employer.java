package com.example.christinaaiello.employerinformation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Christina Aiello on 3/22/2015.
 */
public class Employer {
    private String website;
    private String industry;
    private String squareLogo;
    private String overallRating;
    private String cultureAndValuesRating;
    private String seniorLeadershipRating;
    private String compensationAndBenefitsRating;
    private String careerOpportunitiesRating;
    private String workLifeBalanceRating;

    public Employer() {
    }

    /**
     * Getter for an employer object
     * @return website
     */
    public String getWebsite(){
        return this.website;
    }

    /**
     * Getter for an employer object
     * @return industry
     */
    public String getIndustry(){
        return this.industry;
    }

    /**
     * Getter for an employer object
     * @return squareLogo
     */
    public String getSquareLogo(){
        return this.squareLogo;
    }

    /**
     * Getter for an employer object
     * @return overallRating
     */
    public String getOverallRating(){
        return this.overallRating;
    }

    /**
     * Getter for an employer object
     * @return cultureAndValuesRating
     */
    public String getCultureAndValuesRating(){
        return this.cultureAndValuesRating;
    }

    /**
     * Getter for an employer object
     * @return seniorLeadershipRating
     */
    public String getSeniorLeadershipRating(){
        return this.seniorLeadershipRating;
    }

    /**
     * Getter for an employer object
     * @return compensationAndBenefitsRating
     */
    public String getCompensationAndBenefitsRating(){
        return this.compensationAndBenefitsRating;
    }

    /**
     * Getter for an employer object
     * @return careerOpportunitiesRating
     */
    public String getCareerOpportunitiesRating(){
        return this.careerOpportunitiesRating;
    }

    /**
     * Getter for an employer object
     * @return workLifeBalanceRating
     */
    public String getWorkLifeBalanceRating(){
        return this.workLifeBalanceRating;
    }

    /**
     * Getter for an employer object
     * @return website
     */
    public void setWebsite(String website){
        this.website = website;
    }

    /**
     * Getter for an employer object
     * @return industry
     */
    public void setIndustry(String industry){
        this.industry = industry;
    }

    /**
     * setter for an employer object
     * @return squareLogo
     */
    public void setSquareLogo(String squareLogo){
        this.squareLogo = squareLogo;
    }

    /**
     * setter for an employer object
     * @return overallRating
     */
    public void setOverallRating(String overallRating){
        this.overallRating = overallRating;
    }

    /**
     * setter for an employer object
     * @return cultureAndValuesRating
     */
    public void setCultureAndValuesRating(String cultureAndValuesRating){
        this.cultureAndValuesRating = cultureAndValuesRating;
    }

    /**
     * setter for an employer object
     * @return seniorLeadershipRating
     */
    public void setSeniorLeadershipRating(String seniorLeadershipRating){
        this.seniorLeadershipRating = seniorLeadershipRating;
    }

    /**
     * setter for an employer object
     * @return compensationAndBenefitsRating
     */
    public void setCompensationAndBenefitsRating(String compensationAndBenefitsRating){
        this.compensationAndBenefitsRating = compensationAndBenefitsRating;
    }

    /**
     * setter for an employer object
     * @return careerOpportunitiesRating
     */
    public void setCareerOpportunitiesRating(String careerOpportunitiesRating){
        this.careerOpportunitiesRating = careerOpportunitiesRating;
    }

    /**
     * setter for an employer object
     * @return workLifeBalanceRating
     */
    public void setWorkLifeBalanceRating(String workLifeBalanceRating){
        this.workLifeBalanceRating = workLifeBalanceRating;
    }

    /**
     * This method extracts company information from the JSON that was returned
     * @param jsonString is the JSON string
     * @throws java.io.FileNotFoundException if this file could not be found
     * @throws org.json.JSONException
     */
    public void extractCompanyInformation(String jsonString) throws FileNotFoundException, JSONException {
        // Reader for this URL
        JSONObject reader = new JSONObject(jsonString);

        // Getting the response information for this URL
        JSONObject responseInfo  = reader.getJSONObject("response");
        JSONArray employersInfoArray = responseInfo.getJSONArray("employers");

        // Now we need to loop through this JSON array to retrieve the necessary information
        for(int i = 0; i < employersInfoArray .length(); i++)
        {
            JSONObject employersInfoObject = employersInfoArray.getJSONObject(i);
            this.setWebsite(employersInfoObject.getString("website"));
            Log.e("Website is", "Website is: " + employersInfoObject.getString("website"));
            this.setIndustry(employersInfoObject.getString("industry"));
            this.setSquareLogo(employersInfoObject.getString("squareLogo"));
            this.setOverallRating(employersInfoObject.getString("overallRating"));
            this.setCultureAndValuesRating(employersInfoObject.getString("cultureAndValuesRating"));
            this.setSeniorLeadershipRating(employersInfoObject.getString("seniorLeadershipRating"));
            this.setCompensationAndBenefitsRating(employersInfoObject.getString("compensationAndBenefitsRating"));
            this.setCareerOpportunitiesRating(employersInfoObject.getString("careerOpportunitiesRating"));
            this.setWorkLifeBalanceRating(employersInfoObject.getString("workLifeBalanceRating"));
        }
    }

    /**
     * Referenced from http://www.tutorialspoint.com/android/android_json_parser.htm
     * @param urlString is the URL that you are fetching and parsing JSON from
     */
    public void fetchCompanyInformation(final String urlString) throws InterruptedException {
        Thread thread = new Thread(new Runnable(){
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
     * @param is is the input stream
     * @return a String of JSON data
     */
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}