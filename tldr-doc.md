# TLDR summary for each component in project
### \[DIR] .
#### ``build.gradle``
* includes external binaries or other library modules in our build as dependencies
  * e.g. Springframework's libraries (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-session-jdbc`, 
    `spring-boot-starter-mustache`, ...), `lombok`, and `h2database` (for h2-console).
* sets the springBootVersion

#### gradle/wrapper/`gradle-wrapper.properties`
* can **check** gradle version
* to change the gradle version, use terminal command: e.g. `./gradlew wrapper --gradle-version 4.10.2`

---
## \[DIR] domain
_java/com/binchoi/springboot/domain_
* contains the domain layer of the project which includes the entity classes (i.e. tables in DB), domain services, 
  and value objects
* all logic should be implemented here!

#### `BaseTimeEntity.java`
* an abstract class which entity classes can extend in order to automatically add the columns 
  `createdDate` and `modifiedDate`

### \[DIR] posts
_java/com/binchoi/springboot/domain/posts_
* contains the Posts entity class and PostsRepository interface

#### `Posts.java`
* Entity class that represents the **check-in's** (i.e. records) for the habits
  * columns include: id (postId), date, isCompleted, author, and comment **(raceid?)**
* Posts builder (constructor)
* update method used to modify the `date` and `comment` field of a post (used by `PostsService`)

#### `PostsRepository.java`
* standard repository interface that inherits from JpaRepository to automatically provide basic CRUD methods for 
  Posts class
  * to use the CRUD methods, instantiate `PostsRepository` (usually `postsRepository`) and use its methods (e.g. 
    delete, findAll, findAllBy, save, etc...). 
* additional methods can be created inside the interface
  * can specify the query with annotation `@Query("...")`

### \[DIR] user
_java/com/binchoi/springboot/domain/user_
* contains the User entity class, UserRepository interface, and Role enum

#### `User.java`
* Entity class that represents the users of the service
    * columns include: id (userId), name, email, picture, and role
* User builder (constructor)
* update method used to modify the `name` and `picture` field of a user entity
* `getRoleKey()` returns the role of the user as a String

#### `UserRepository.java`
* standard repository interface that inherits from JpaRepository to automatically provide basic CRUD methods for
  User class
* `findByEmail` method added just by declaring the method (no Query required)

#### `Role.java`
* An enum for the roles of users
* ENUM is a special 'class' that represents a group of constants (i.e. unchangeable variables)
* Roles: GUEST, USER, (ADMIN)
  * consists of two attributes - key, title
___
## \[DIR] service
_java/com/binchoi/springboot/service_
* contains the service layer of the project which consists of the application services and infrastructure services
  * Service layer is the mid-layer between the repository layer and web layer; acts as a bridge between the Controller 
    and Dao (Data Access Object)
* Classes will be annotated with `@Service` and many methods with `@Transactional`
  * transaction script must simply ensure the ordering between transactions and domains (and 
    not handle the business logic)

### \[DIR] posts
_java/com/binchoi/springboot/service/posts_

#### `PostsService.java`
* Service script for Posts related operations
* instantiates PostsRepository to leverage the logic implemented by PostsRepository (JPA)
* `save()`: input=PostsSaveRequestDto; uses `PostsRepository.save` to save into Posts table; output=id (postId)
* `findById()`: input=id; uses `PostsRepository.findById` to retrieve the requested Posts object and convert it
  to a PostsResponseDto; output=PostsResponseDto
  * **NOTE:** You must **NEVER** use Entity class as Request/Response Class. Entity class is a critical/core class that 
  is in contact with the Database. If Entity class is modified, it may influence countless business logic and service classes
  * **NOTE:** Always separate Entity class and the Dto which the Controller will use
* `update()`: input=id, PostsUpdateRequestDto; uses `PostsRepository.findById` to retrieve the requested Posts object 
and uses `Posts.update()` to update the post; output=id
* `delete()`: input=id, PostsUpdateRequestDto; uses `PostsRepository.findById` to retrieve the requested Posts object
  and uses `PostsRepository.delete()` to delete the post; output=void
* `findAll()`: input=N/A; uses `PostsRepository.findAll` to retrieve the List of Posts and maps them as a PostsResponseDto 
array; output=PostsResponseDto[]
* `findByAuthor()`: input=author; uses `PostsRepository.findByAuthor` to retrieve the List of Posts and maps them as a PostsResponseDto
  list; output=List<PostsResponseDto> (i.e. will change to [] if it is deemed neccessary)

___
## \[DIR] web
_java/com/binchoi/springboot/web_
* contains the web layer of the project which consists of the controllers, exception handlers, filters, view templates, etc...
  * Communicates with the service layer using DTOs (Data Transfer Objects) which are also implemented under this directory
* Encompasses Filter (@Filter), Intercept, ControllerAdvice (@ControllerAdvice), and is the layer that deals with external 
request and response
* Classes will be annotated with `@Controller` and many methods with `@...Mapping("...")`

#### `IndexController.java`
* Contains **ALL Controllers related to pages**
* instantiates postsService (to connect to Service layer and thereby the repository layer)
* instantiates HttpSession (unnecessary - research more)
* Annotation `@GetMapping("...")` used for each controller (corresponding to each website url type)
* Model (Java Object carrying data) is used to send data to the html/mustache/js files
* Controllers return String - Thanks to the Mustache Starter dependency, when the controller converts Strings, it 
automatically appends the prefix and suffix (i.e. prefix: “src/main/resources/templates” ; suffix: “.mustache”) ⇒ 
src/main/resources/templates/index.mustache
  * ViewResolver - the managing party that determines the type and value of what is to be returned in response to URL request - 
  handles the rest
* PAGES: 
  * "/": main page
  * "/posts/save": post/record saving page
  * "/posts/update/{id}": post/record updating page for specific post 

#### `PostsApiController.java`
* Contains all Controllers related to the Posts API
* `@RestController` on class makes all controllers in this class return JSON
  * alternative method: attaching `@ResponseBody` to all methods (inefficient)
* instantiates postsService (to connect to Service layer and thereby the repository layer)
* Annotations `@GetMapping("...")`, `@PostMapping("...")`, `@DeleteMapping("...")`, `@PutMapping("...")` used to distinguish 
each HTTP protocol/request
* `save()`: in response to POST; input=PostsSaveRequestDto; uses `postsService.save`; output=id
* `findById()`: in response to GET; input=id; uses `postsService.findById`; output=PostsResponseDto
* `update()`: in response to PUT; input=id, PostsUpdateRequestDto; uses `postsService.update`; output=id
* `delete()`: in response to DELETE; input=id; uses `postsService.delete`; output=id
* `findAll()`: in response to GET; input=N/A; uses `postsService.findnAll`; output=PostsResponseDto[]

### \[DIR] dto
_java/com/binchoi/springboot/web/dto_
#### `PostsUpdateRequestDto.java`
* standard dto ; fields = date, comment
* builder: input=date, comment
#### `PostsListResponseDto.java`
* standard dto ; fields = id, date, isComplicated, author, comment, modifiedDate
* builder: input=Posts (entity)
#### `PostsResponseDto.java`
* standard dto ; fields = id, date, isComplicated, author, comment, (**INCOHERENT WITH ABOVE - fix**)
* builder: input=Posts (entity)
#### `PostsSaveRequestDto.java`
* standard dto ; fields = date, isComplicated, author, comment
* builder: input=date, isComplicated, author, comment
* method `toEntity()` takes the dto and builds a corresponding Post object (e.g. which can then be saved to table)

&nbsp;
___
## Annotation TLDR
#### `@Autowired`
* provides classes with declarative way to resolve dependencies. 
* like `@Inject` but in Spring Framework
```
\\ declarative method
@Autowired 
ArbitraryClass arbObject;

\\ vs. imperative method
ArbitraryClass arbObject = new ArbitraryClass();
```

#### `@Enumerated(EnumType.STRING)`
* most common option when mapping enum value to and from its database representation in JPA
* instructs JPA provider to convert the enum to its STRING value

#### `@RestController`
* makes the Controller one that returns JSON