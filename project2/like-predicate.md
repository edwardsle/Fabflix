LIKE PREDICATE: (Inside MovieListServlet.java)
- String titleCondition = ((title_key != null) ? "title LIKE '%"+ title_key+"%'" : null);
- String directorCondition = ((director_key != null) ? "director LIKE '%"+ director_key+"%'" : null);;
-	String starCondition = ((star_key != null) ? "id IN (SELECT movieId FROM stars_in_movies WHERE starId IN (SELECT id FROM stars WHERE name LIKE '%"+ star_key + "%'))" : null);;
- String yearCondition = ((year_key != -1) ? "year ='"+ year_key+"'" : null);;

These predicates are used to compare column with a pattern. "%" is used to represent a string of zero, one, or more character while "_" represents exactly 1 arbitrary character.
