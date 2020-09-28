# Job Search Journal
 By Christina

## Technologies Used
Java, SQLite, Google API, Glassdoor API, Android Studio


## Running On Your Android Phone
* Download Android Studio developer.android.com/androidstudio/download
* Plug your phone into your laptop
* Go to Settings
* Type "Developer" into the search bar
* Choose "Developer Options"
* Scroll down to the "Debugging" section
* Turn on where it says "USB Debugging"
* Now install Android Studio
* Open project folder (seen in gif below)
* Go to "Run" in top menu bar (seen in gif below)
* Click "Select Device" (seen in gif below)
* Choose your phone (seen in gif below)
* Now click the green right-facing triangle button to run (seen in gif below)
![Running On Your Android Phone](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/how-to-run-on-android-phone.gif)


## Description
The process of applying for jobs can be time-consuming and stressful: You’re filling out applications, attending interviews, and keeping track of what step you are in the application process with each company you apply to. Eventually names, dates, emails, and interviews all blend together. JobApp Tracker is an application that helps individuals track the applying-to-jobs process for various jobs, keeping all potentially-useful information within the application for later retrieval.




## Demo
Watch a demo here: 
https://drive.google.com/file/d/1g8MDqFtlZWOhInPrjE1HvCb7VQUCkPDM/view?usp=sharing


![Add a Company](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/add-a-company.gif)

![Add Interview](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/add-interview.gif)

![Company Info](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/company-info-2.gif)

![Company Info](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/company-info.gif)

![Reorder and Delete](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/reorder-and-delete.gif)






## Main page

![Main page](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/Job-Search-Journal-1.png)

The main screen shown when the application is first started lists each company that a user has entered (either alphabetically by name or sorted by step in the application process, based on a user’s choice in the settings). This information is stored in an SQLite database on the device. Each company’s logo, name, website, position the user applied for, and step in the application process is shown. Companies’ logos and websites are retrieved via the Glassdoor Web API.






## Logging Company Info

![Adding a new company](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/Job-Search-Journal-2.png)

![Company view](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/Job-Search-Journal-3.png)

When a user clicks on a company’s name in this list, the user is brought to a screen that contains information about the company. The Glassdoor Web API provides access to a company’s logo, industry, website, and ratings. At the top of this screen is a button that reads “Click to track progress with this company!” This button brings the user to the “Track Your Progress” screen, which offers the aforementioned functionality. Another API that is used is the Google Street-View API, which will provide the user with a street-view image of a company based on its address.





## Interview Process Logging

![Initial Contact Info](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/Job-Search-Journal-4.png)

![Interview](https://github.com/cjaiello/Job-Search-Journal/blob/master/screenshots/Job-Search-Journal-5.png)

The initial contact fragment within the “Track Your Progress” activity accesses the phone’s call logs to count how many times calls have been exchanged with a particular phone number. The scheduled interview fragment uses an intent to bring up the user’s email client with an email already addressed to the recipient (based on the “Contact Email Address” provided). All of these features exist to help users track their application process, in addition to granting users quick-and-easy access to recruiters’ and interviewers’ contact information.

