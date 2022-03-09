# Software Architecture for Habit Racer

## Posts
* posts can be created by users
* if no post has been created for a date, at the end of the 
  corresponding date, a post is automatically created with default values
  * this post can be modified in the future
* Else, another option is to create an entry for every day until the 
  end of the duration (with default value of isComplete = false). Then, user 
  essentially updates the posts only.

* The fields of Posts is as follows:
  1. `id`: Long &rarr; auto-incrementing id (primary key)
  2. `date`: Date &rarr; date of habit completion (or incompletion)
  3. `completed`: boolean &rarr; whether the habit has been accomplished 
     for that date
  4. `author`: String &rarr; author name or nickname
  5. `comment`: String &rarr; comment for self or competitor
  6. `modifiedDate`: Date &rarr; this will be automatically added via 
     BaseTimeEntity
