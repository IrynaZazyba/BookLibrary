# BookLibrary

##### Task#1 - Proof of concept
Branch - PoC  
I offer project structure, which based on a layered architecture. Used the command pattern to process requests on 
the controller layer.

##### Task#2 - DB initialization
Branch - db-initialization
- add db.properties to resources
- create connection pool
- create ConnectionPoolListener to initialize connection pool
- create SqlBookDaoImpl
- create a factory for DAO implementation

##### Task#3 - Add logger
Branch - add-logger
- add config files
- add filter to log user activity
- logging existing exceptions

##### Task#4 - Add main page
Branch - add-main-page
- add welcome page to web.xml
- add lombok to Book 
- add BookService implementation 
- add getting list of books
- add MainPage.jsp 
- add navigation bar as import

##### Task#5 - Add functionality to main page
Branch - add-functionality-to-main-page
- add CharsetFilter
- add functionality to filter out unavailable books
- add pagination

##### Task#6 - Add search page
Branch - add-search-page
- add DAO and Service methods
- add SearchBooksCommand
- add SearchPage.jsp

##### Task#7 - Add book page
Branch - add-book-page
- add beans
- add GetBookCommand
- add DAO and Service methods
- add dto object
- add BookPage.jsp

##### Task#8 - Add actions to book page
Branch - book-page-action
- add functionality to update book info, return/lend book
- add DAO and Service methods
- add dto objects
- add commands
- add actions to BookPage.jsp
