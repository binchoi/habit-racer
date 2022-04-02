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

## Next steps
* Spring OAuth2 token system implementation
* Renovate the front-end and add the buttons and sites to support the creation and joining of Races
* Think of the logic more -- it's not there yet. 
* Update the SecurityConfig with new URI's
* Review and update the tldr-doc for config and resources directory
* Prevent double entry per day
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
* Testing for exception underneath NestedServletException (RaceApiControllerTest) by `assertThatThrownBy`
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
