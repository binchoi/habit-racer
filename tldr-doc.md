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

___
## \[DIR] config
_java/com/binchoi/springboot/config_
* Contains all classes related to security
  * This includes Config classes (often annotated with ``@Configuration``), custom OAuth2UserService implementations, 
  dtos (like `OAuthAttributes` and `SessionUser`), and interfaces (e.g. `LoginUser`)

#### `SecurityConfig.java`
* A security configuration class that extends the abstract class `WebSecurityConfigurerAdapter` provided by Spring Security to 
configure the settings surrounding URL permissions, log-in processes (& userInfoEndpoint settings), and log-out processes. 
* `@EnableWebSecurity` activates Spring Security settings
##### `configure()`: input=`HttpSecurity` instance; to configure the security settings of the project; output=void
* `.csrf().disable().headers().frameOptions().disable()`: disables CSRF support (which is activated by default) and 
frame option (of Headers) in order to support h2-console use
* `authorizeRequests()`: marks the starting point for setting permission/privileges for each URL
  * needs to be declared in order to use the option `antMatchers`
* `antMatchers`: selects the target URL for which we wish to manage permission. Tagged with the following:
  * `permitAll()`: URLs are allowed by anyone
  * `authenticated()`: URLs are allowed by any authenticated user
  * `hasRole(String role)`: URLs require a particular role (e.g. \"ADMIN\", \"USER\", etc...). Do not start `role` with 
  \"ROLE_\" as it is automatically appended
  * `access(String attribute)`: URLs are secured by an arbitrary expression; `attribute`: the expression to secure the URLs (i.e. "hasRole('ROLE_USER') and hasRole('ROLE_SUPER')")
  * `anonymous()`: URLs are allowed by anonymous users.
  * `denyAll()`: URLs are not allowed by anyone.
  * `hasIpAddress(String ipaddressExpression)`: URLs requires a specific IP Address or subnet. `ipaddressExpression` – the ipaddress (i.e. 192.168.1.79) or local subnet (i.e. 192.168.0/24)
  * `fullyAuthenticated()`: URLs are allowed by users who have authenticated and were not "remembered".
  * `hasAnyAuthority(String authorities)`: URLs requires any of a number authorities; `authorities` – the requests require at least one of 
  the authorities (i.e. "ROLE_USER","ROLE_ADMIN" would mean either "ROLE_USER" or "ROLE_ADMIN" is required).
  * `rememberMe`: URLs are allowed by users that have been remembered.
  * etc... all of the above methods returns `ExpressionUrlAuthorizationConfigurer` (which `antMatchers` can take) for further customization
* `logout()`: entry point for setting different configurations for the log-out functionality (e.g. `logoutSuccessUrl("/")`)
* `oauth2Login()`: entry point for setting various configurations for OAuth2 Log-in functionality
  * `userInfoEndpoint()`: In charge of settings related to retrieving the User information after successful OAuth2 log-in
    * `userService(userService)`: Sets the OAuth 2.0 service used for obtaining the user attributes of the End-User from the UserInfo Endpoint;
      param `userService` – the OAuth 2.0 service used for the described tasks

#### `CustomOAuth2UserService.java`
* Our implementation of the interface `OAuth2UserService<OAuth2UserRequest, OAuth2User>`
* Implementations of this interface are responsible for obtaining the user attributes of the End-User (Resource 
Owner) from the UserInfo Endpoint using the Access Token granted to the Client and returning an AuthenticatedPrincipal 
in the form of an `OAuth2User`.
* `loadUser(OAuth2UserRequest userRequest)`: returns an OAuth2User after obtaining the user attributes of the End-User from the UserInfo Endpoint
* In a nutshell, it extracts the important user information, saves them as `OAuthAttributes` object, (using helper method `saveOrUpdate`) saves as new user (i.e.
  create user object, then save using userRepository) or updates the existing user's information in the user table, sets attribute "user" as
  the corresponding SessionUser object, and returns a DefaultOAuth2User (with the appropriate authorities, attributes, and nameAttributeKey)
  * `userRequest.getClientRegistration()`: access point for client registration attributes
    * `getRegistrationId()`: Returns the identifier for the registration - it is useful to distinguish between the type of log-in
    * `getClientId()`: Returns the client identifier
    * `getClientName()`: Returns the logical name of the client or registration
  * `userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()`: access point for details of 
  the UserInfo Endpoint (e.g. `nameAttributeKey` - which is the value of the \[primary\] key used in OAuth2 Log-in process)
```
OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
OAuth2User oAuth2User = delegate.loadUser(userRequest);
oAuth2User.getAttributes() // map of user attributes including name, email, picture
```

#### `OAuthAttributes.java`
* A class (dto) that contains the attributes of OAuth2User retrieved through OAuth2UserService
* As the user returned by OAuth2User is a Map, each value must be converted and retrieved individually (as fields)
* Behaves differently based on the social log-in type (e.g. Google, Naver, etc...)
* static method `of()` is used to construct OAuthAttributes object by UserService
* `toEntity()`: returns the corresponding User object 
  * can set the default Role for users

#### `SessionUser.java`
* A class (dto) that contains the user information of verified users (no unnecessary information included)
* Its constructor requires the corresponding User object
* Why create SessionUser when User can be used? 
  * User object cannot be saved to the session as it is not Serializable. (why not make it serializable you ask?)
  * User is an ENTITY CLASS. Be careful and don't mess around with it. REMINDER: You must **NEVER** use Entity class as Request/Response Class.
    * e.g. making User class serializable can have detrimental implications if a user object has children / one-to-many relationships

#### `LoginUser.java` 
* This script creates the parameter annotation `@LoginUser` which allows us to access and receive session value directly 
as a method argument (annotation-based programming 😎)
* `@Target(ElementType.PARAMETER)`: determines where this annotation can be used/created; We chose parameter
* `@interface`: declares this file as an Annotation class

#### `LoginUserArgumentResolver.java`
* A class that implements the interface `HandlerMethodArgumentResolver`
* `HandlerMethodArgumentResolver` provides one purpose: when conditions are met, a value (determined by the implementation 
of HandlerMethodArgumentResolver) is passed as a parameter to a given method
* `supportsParameter(MethodParameter parameter)`: determines whether the specific parameter of the controller method is supported and returns boolean
* `resolveArgument(...)`: creates the object that will be passed to the parameter
* For this resolver to be recognized within Spring, we need to add it to `WebMvcConfigurer` (i.e. `WebConfig` is our implementation)

#### `WebConfig.java`
* standard implementation of `WebMvcConfigurer`
* `HandlerMethodArgumentResolver` must ALWAYS be added through `WebMvcConfigurer`’s `addArgumentResolvers()`

#### `JpaConfig.java`
* created to debug test error: `JPA metamodel must not be empty` and allow the use of WebMvcTest
* We didn't want `@EnableJpaAuditing` to be scanned by `@WebMvcTest` (which it did when the annotation was attached in `Application.main`)
* So, this config file was created to separate `@EnableJpaAuditing` and `@SpringBootApplication`

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

#### `@EnableWebSecurity`
* activates Spring Security settings
* applied to SecurityConfig which extends `WebSecurityConfigurerAdapter`

#### `@RequiredArgsConstructor`
* Generates a constructor with required arguments. Required arguments are final fields and fields with 
constraints such as @NonNull.

#### `@Service`
* Indicates that an annotated class is a "Service", originally defined by Domain-Driven Design (Evans, 2003) as 
"an operation offered as an interface that stands alone in the model, with no encapsulated state." (intelliJ)