# habit-racer

## Progress
* Designed and brainstormed the architecture of the web service
* created and configured the basic settings of the Spring Boot project (lombok, spring boot starter kit, gradle version, etc...)
* HelloController, HelloResponseDto implemented
* JUnit, MockMvc, SpringRunner.class testing for basic api / mapping functionality
* Constructed the skeleton of the Posts class (Entity; table)
* Constructing a basic API with the core components:
  * Dto - which receives the Request data
  * Controller - which receives the API request
  * Service - which ensures the order between transactions, domain function, etc...

## Next step

## Problems faced
* A getter is not created for one boolean field in Posts class (i.e. completed). 
  * could it be about primitive vs. object reference variables?
    * Perhaps -- changing the field from boolean to Boolean immediately generated the 
      desired getter method. Whether the field is a boolean or Boolean doesn't affect the 
      logic of my project too much so let's go with it
* 
