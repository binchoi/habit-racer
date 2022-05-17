# habit-racer

## Progress
* Designed and brainstormed the architecture of the web service
* created and configured the basic settings of the Spring Boot project (lombok, spring boot starter kit, gradle version, etc...)
* HelloController, HelloResponseDto implemented
* JUnit, MockMvc, SpringRunner.class testing for basic api / mapping functionality
* Constructed the skeleton of the Posts class (Entity; table)
* Constructed a basic save API with the core components:
    * Dto - which receives the Request data
    * Controller - which receives the API request
    * Service - which ensures the order between transactions, domain function, etc...
* Constructed the save, update, get, and getAll features of the Posts API and implemented unit tests for each.
* Built a basic race view page using mustache, bootstrap, and js that leverages the pre-constructed PostsAPI to allows for
  posting of new record/post and updating or deleting of posted records.
* Implemented OAuth2 social log-in (via Google)
* Implemented the User entity class and related classes (e.g. SessionUser)
* Configured our program to be more annotation-based by implementing a custom parameter annotation ``@LoginUser`` that
  allows me to retrieve session information values from the parameter directly
* Edited the testing suites to incorporate Spring Security
* Created documentation for readers/users that outlines each of the important components of the project (check: `tldr-doc.md`)
* Implemented the Race class and its Repository
* Experimented with Test-driven development by creating the unit tests before implementing the APIs for the Race Entity class
* Basic API for Race class constructed
* Start a new race button and basic UI created
* Changed URI naming to better conform with RESTful architecture principles
* modified OAuthAttributes to include userId in attributes map at log-in (userLoad) such that it can be directly
  accessed by SpEL in `pre/postAuthorize` annotations (e.g. `@PreAuthorize("principal?.attributes['userId'] == #user.id")
  `)
* Implemented method security (via expression-based access control) (`hasPermission`, `@PreAuthorize`, `@PostAuthorize`) by implementing my own
  custom Permission Evaluator (and setting it via `ExpressionHandler`)
* Renovated Front-end using BootStrap 5 and javascript
* Automated CI/CD pipeline such that a push to github master branch results in TravisCI, CodeDeploy, S3, nginx chain
  procedure that results in automatic testing, building, and deployment all with zero downtime.
* Bought a domain from namecheap.com and created Route53 hosted zone to connect the ec2 to the newly purchased domain
* Implemented custom validation exception class to create a more clean experience for user when submitting forms (error displayed
  below the form field in case of invalid value)
* Set up SSL using Let's Encrypt. Working on it...
* Added previous pages table that appears when a user has one or more completed races
* Race Update / Delete feature added..

## Next steps
* change and check zdd!
    * does multiple repeated commits result in overlapping deployment and problems in zdd?
* Write more tests for IndexController and APIs such that the test results can make me confident that things are working
    * figure out how to resolve the issue with mockmvc's incompatibility with OAuth2User
        * cast error (may have to implement userdetails, may have to look into mock.mvc.perform.with (oauthlogin)
* Review and update the tldr-doc for config and resources directory
* Consider the safety implications of using hidden data fields and brainstorm how to bolster defense against malicious attacks.
* Consider where the security should be placed to prevent some authenticated user being able to manipulate the race of stragers by accessing through url
    * How to implement an attribute/... -based access control
* Implement the Races entity class and do everything that has been done for the Posts class (until API)
* Incorporating a cache layer such that when the post table page is shown, it doesn't have
  to make a query unless it has been more than a couple of minutes (TTL)
* Change center prompt "Did you complete ..." after completing the habit for the day

## Problems faced (& self-QnA)
* A getter is not created for one boolean field in Posts class (i.e. completed).
    * could it be about primitive vs. object reference variables?
        * Perhaps -- changing the field from boolean to Boolean immediately generated the
          desired getter method. Whether the field is a boolean or Boolean doesn't affect the
          logic of my project too much so let's go with it
* When trying to construct the test Posts_can_be_getted() to check the functionality of
  the GET api for Posts, I came across the error ``Cannot construct instance of 'PostsResponseDto'``
    * at first, I naively added Lombok's ``@NoArgsConstructor`` which resolved the exception. However,
      after googling a bit more, I came across this article (https://careydevelopment.us/blog/spring-boot-and-jackson-how-to-get-around-that-invaliddefinitionexception)
      which pursuaded me to add ``@JsonCreator`` over our constructor instead. The only thing that bothers me is
      that I didn't specify ``@JsonProperty`` as the only parameter for the constructor is ``Entity``.
* I implemented an additional api to get all the posts. In the process, I learned that we cannot send lists of objects (i.e. RestTemplate
  does not support List as the data in the list can't be deserialized into the appropriate type). For more information,
  consider the following link (https://www.baeldung.com/spring-rest-template-list).
    * To resolve, I used an array :-)
* Problem debugging index.js.
    * solution: ``console.log()`` -- view the log from browser's `inspect` option
* MockMvc test problem - error 400 for ``put`` and ``post`` methods.
    * tagging ``@JsonDeserialize(using = LocalDateDeserializer.class)`` and ``@JsonSerialize(using = LocalDateSerializer.class)`` to ``date`` didn't solve
    * requesting ``get`` works as expected with the MockMvc
    * **\[RESOLVED\]**: problem was due to the inability to serialize LocalDate field. The resulting JSON object would not contain
      one key and value representing the LocalDate field (but rather a long set of key-value pairs). It should instead be serialized as ``ISO 8601`` format
      (e.g. "date":"2022-03-27"). To solve this there are several methods. The common way is configuring ObjectMapper to handle LocalDate in ISO 8601 format.
      This process can take multiple lines of code and overcomplicate the issue (but if you are curious, simply google 'jackson localdate iso 8601'). Instead,
      we leverage the fact that Spring has a ObjectMapper which they provide with the above configuration already made. Hence, we can simply use it by inserting the
      following code: ``@Autowired ObjectMapper objectMapper;`` and replacing ``new ObjectMapper().writeValueAsString(dto)`` with ``objectMapper.writeValueAsString(dto)``
      (cred: Inflearn question 30590)
* Consider the safety implications of using hidden data fields (e.g. `userId`) in JS as means of passing values to `index.js`
    * Upon inspection I discovered that I can edit the hidden field before pressing `save` in order to save the post under some other user's Id.
    * I need to defend against any malicious attacks!
* What kinds of information should be saved in `HttpSession` via `setAttribute()`? What should the primary function of `HttpSession` be? **\[\*]**
* MockMvc and JUnit 5 assertion error with identical field (e.g. Expected: `2022-05-30` vs. Actual: `2022-05-30`)
    * This occurs because JsonPath operates on JSON and the LocalDate (or any type) field has been converted to a JSON string
      value. Hence, whenever you get a matcher error indicating that two values that look identical do not match, it is likely due to
      difference in type! Simply use `toString()` to resolve.
* Spring DATA JPA @Query uses JPQL by default for query definition. However, we can also use native SQL by setting the value of `nativeQuery`
  attribute to true.
    * note that query definition with syntax error can lead to `Failed to load ApplicationContext` error.
* Testing for exception nested inside NestedServletException (ApiControllerTests) by `assertThatThrownBy`. Example template
  below:
```
assertThatThrownBy(() ->
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto))) //then
).hasCause(new IllegalArgumentException("The race does not exist. id="+ raceId));
```

```
\\jpql
@Query("SELECT r FROM Race r WHERE r.fstUserId = ?1 OR r.sndUserId = ?1 ORDER BY r.startDate DESC")
List<Race> findByUserId(Long userId);

\\sql
@Query(
  value = "SELECT * FROM RACE r WHERE r.fstUserId = ?1 OR r.sndUserId = ?1 ORDER BY r.startDate DESC",
  nativeQuery = true)
List<Race> findByUserId(Long userId);
```
* note: if lombok builder constructor does not use a particular parameter, its value is set as null
* Consideration of JWT Token based authentication vs. Session based authentication
    * Biggest difference is that the user's state is not stored on the server when using JWT (it's stored inside the
      token on the client side). Most modern web applications use JWT for authentication for reasons including scalability
      and mobile device authentication
    * Scalability: if large number of users are using the system at once, server's memory can run out (as sesisons are
      stored on the server side)
    * cross-domain (mobile and web devices) are handled with more trouble using Session based authentication
* It is difficult to implement `@PreAuthorize/@PostAuthorize` as most online resources are instructed from a non-OAuth2
  context. After playing around with PermissionEvaluators and principal/authentication, I now know the following:
    * Calling `authentication.principal` in Spring EL expressions will return `org.springframework.security.oauth2.core.user.DefaultOAuth2User`.
      As we know from extracting data from that class during the implementation of `SessionUser` and `@LoginUser` annotation,
      to go from that object to anything meaningful like `username` or `id` requires unpacking of the map referenced by its variable
      `attributes`. This will be adding redundancy to our operation.
    * `DefaultOAuth2User` has instance variables `authorities`, `attributes`, and `nameAttributeKey`
    * I am debating whether a pretty and structured but inefficient code is better than a less structured but efficient code.
    * IDEA: change OAuthAttributes to include the userId as one of the attributes when loading a user such that additional queries
      need not be made using UserRepository (findByEmail).
* The power of documentation and official documentation. Instead of looking at other people's posts which may contain outdated
  information and unreliable suggestions, I learned that the official documentation is most likely the best source for clear and
  thoroughly reviewed information. https://docs.spring.io/spring-security/site/docs/5.2.12.RELEASE/reference/html/oauth2.html#oauth2login-advanced-custom-user
* ERROR: Caused by: org.springframework.aop.framework.AopConfigException: Could not generate CGLIB subclass of class com.binchoi.springboot.web.IndexController: Common causes of this problem include using a final class or a non-visible class; nested exception is java.lang.IllegalArgumentException: Failed to parse expression '#oauth2.throwOnError(hasPermission(#id, 'posts', 'write')'
    * occurred when I tried to implement hasPermission()
* Calling `<img src="...">` in mustache. Spring Boot file directory structure acclimatization!
    * images, css, js all go into static folder which can be accessed directly by spring boot (i.e. `<img src="/images/race-track.png" alt="race-track" class="img-fluid">`)
* Validation JS script that is supposed to append `'<span class="error-message taxt-small text-danger">'+error.defaultMessage+'</span>'` after
  the error field causes the input fields to change shape because I added `input-group` divs.
    * solution:
        * first identify if the input field to mark is inside an `input-group` div by `($field.parent('.input-group').length)`
        * because of difference in siblings structure, I had to learn more about jquery and came to this solution
```
\\originally 

```
* zero downtime deployment - error
```
[2022-04-22 00:41:18.616] [d-IR48ENC1H]LifecycleEvent - AfterInstall
[2022-04-22 00:41:18.617] [d-IR48ENC1H]Script - stop.sh
[2022-04-22 00:41:18.671] [d-IR48ENC1H][stdout]> Checking the application pid of the application running at port 8081
[2022-04-22 00:41:18.714] [d-IR48ENC1H][stdout]> There is no currently running application so no application is killed
[2022-04-22 00:41:19.675] [d-IR48ENC1H]LifecycleEvent - ApplicationStart
[2022-04-22 00:41:19.675] [d-IR48ENC1H]Script - start.sh
[2022-04-22 00:41:19.690] [d-IR48ENC1H][stdout]> Copy Build file
[2022-04-22 00:41:19.691] [d-IR48ENC1H][stdout]> cp /home/ec2-user/app/step3/zip/*.jar /home/ec2-user/app/step3/
[2022-04-22 00:41:19.749] [d-IR48ENC1H][stdout]> Deploying new application
[2022-04-22 00:41:19.768] [d-IR48ENC1H][stdout]> JAR Name: /home/ec2-user/app/step3/habit-racer-1.0.1-SNAPSHOT-20220421214015.jar
[2022-04-22 00:41:19.768] [d-IR48ENC1H][stdout]> add execution permission to /home/ec2-user/app/step3/habit-racer-1.0.1-SNAPSHOT-20220421214015.jar
[2022-04-22 00:41:19.784] [d-IR48ENC1H][stdout]> Run /home/ec2-user/app/step3/habit-racer-1.0.1-SNAPSHOT-20220421214015.jar
[2022-04-22 00:41:19.809] [d-IR48ENC1H][stdout]> Run /home/ec2-user/app/step3/habit-racer-1.0.1-SNAPSHOT-20220421214015.jar with profile=real1
[2022-04-22 00:41:20.744] [d-IR48ENC1H]LifecycleEvent - ValidateService
[2022-04-22 00:41:20.744] [d-IR48ENC1H]Script - health.sh
[2022-04-22 00:41:20.791] [d-IR48ENC1H][stdout]> Health Check Start!
[2022-04-22 00:41:20.791] [d-IR48ENC1H][stdout]> IDLE_PORT: 8081
[2022-04-22 00:41:20.791] [d-IR48ENC1H][stdout]> curl -s http://localhost:8081/profile
[2022-04-22 00:41:30.801] [d-IR48ENC1H][stdout]> Health check's response is either unknown or the application is not running
[2022-04-22 00:41:30.801] [d-IR48ENC1H][stdout]> Health check:
[2022-04-22 00:41:30.801] [d-IR48ENC1H][stdout]> Health check connection failure. Retrying...
[2022-04-22 00:41:40.811] [d-IR48ENC1H][stdout]> Health check's response is either unknown or the application is not running
[2022-04-22 00:41:40.811] [d-IR48ENC1H][stdout]> Health check:
[2022-04-22 00:41:40.811] [d-IR48ENC1H][stdout]> Health check connection failure. Retrying...
[2022-04-22 00:41:50.820] [d-IR48ENC1H][stdout]> Health check's response is either unknown or the application is not running
[2022-04-22 00:41:50.820] [d-IR48ENC1H][stdout]> Health check:
[2022-04-22 00:41:50.820] [d-IR48ENC1H][stdout]> Health check connection failure. Retrying...
[2022-04-22 00:42:00.830] [d-IR48ENC1H][stdout]> Health check's response is either unknown or the application is not running
[2022-04-22 00:42:00.830] [d-IR48ENC1H][stdout]> Health check:
[2022-04-22 00:42:00.830] [d-IR48ENC1H][stdout]> Health check connection failure. Retrying...
[2022-04-22 00:42:10.840] [d-IR48ENC1H][stdout]> Health check's response is either unknown or the application is not running
[2022-04-22 00:42:10.840] [d-IR48ENC1H][stdout]> Health check:
[2022-04-22 00:42:10.840] [d-IR48ENC1H][stdout]> Health check connection failure. Retrying...
```
* After receiving SSL certificate, I noticed that my Zero Downtime Deployment seems to be faulty
  as I witnessed nginx error screen during deployment (there was downtime)!
    * upon further inspection using commands like `curl` to retrieve the localhost/profile, etc,
      I realized that I can no longer access the webservice using localhost from the ec2 machine.

```
[ec2-user@habit-racer ~]$ curl -s http://localhost/
<html>
<head><title>404 Not Found</title></head>
<body>
<center><h1>404 Not Found</h1></center>
<hr><center>nginx/1.20.0</center>
</body>
</html>
[ec2-user@habit-racer ~]$ curl -s http://localhost/profile
<html>
<head><title>404 Not Found</title></head>
<body>
<center><h1>404 Not Found</h1></center>
<hr><center>nginx/1.20.0</center>
</body>
</html>
[ec2-user@habit-racer ~]$ curl -s -o /dev/null -w "%{http_code}" http://localhost/profile
404
```
* Inquiring what localhost specifically refers to
  * I understood it as the computer/machine I am working on
* problem with testing for OAuth2 secured & PreAuthorize secured services
  * Created custom `WithMockCustomOAuth2User` which invokes
    my custom implementation of `WithSecurityContextFactory` to produce and set the appropriate 
    authentication token in the security context (which contains the oauth user prinicipal)
  * dealing with problem of user's id being uncontrollable (as it is generated using `(strategy = GenerationType.IDENTITY)`)
    * cannot flexibly set the MockOAuthUser's id as the attributes in the annotation must be constant...
  * options include: 
    * use different role 
      * (-) integration testing is different real user scenario 
    * fix the user id in the repository to a specified testing user id value
      * difficult as the method for user id is (strategy = GenerationType.IDENTITY)
      which means that even if I change the user entity's id value, when userRepository.save() is called the id in the repo 
      will follow the sequential pattern. 
        * database allocates the ID and having deterministic control over it is impossible
      * therefore, preauthorize (i.e. )
* performance testing features in a testing environment (with different database, using different privilege)
* how does httpsession differentiate between the attributes `user` which is the same string value for each user when handling session information?
* what should I prioritize -- unit or integration testing?
