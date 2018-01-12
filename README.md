 Java API to manage movies

 4 main endpoints :

 - Authentication :

 - Genres : list all the movie genres

 - People : list people per speciality
 /people?spec=(actor,director,musician...)

 - Movies : filter movies per title, genres, offset, limit, actors, directors
 /movies?title=the&genres=action,drama,thriller&offset=0&limit=3&directors=1,2,3&actors=1,2,3