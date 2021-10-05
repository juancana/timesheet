# Timesheet

Timesheet is a web application for generating the weekly timesheets of the workers of a company. The weekly timesheets show the hours worked (or in vacation, sick leave, etc.) for a given week and worker. All the data the web application handles is stored in a MySQL database.
Timesheet is a Spring Boot application. The back end is written in Java and the front end in HTML taking advantage of Thymeleaf.

## Usage

Since this is a Spring Boot application, it is a stand-alone application and there is no need to install. The steps for testing and using this application are the following:
1. Download the source files.
2. Setup the database. Source files contain a SQL file that will setup a database with the required tables. You will need to have a MySQL server running. 
3. Modify properties file of the application with the connection to your database (URL, username and password).
4. Run the application and test it using a web browser.

## Future work
This application is in a very primitive state and can be improved in many different ways. Here are some suggestions of possible future improvements:
- Security: take advantage of Spring Security to allow certain users to all CRUD operations and allow other to only read certain information.
- Generate downloadable PDF files with all the information of a given week.
- Front end needs many improvements to make the web app more user friendly, dynamic and visually attractive.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)