package paf.day3.repo;

public interface Queries {
    
    public static final String SEARCH_GAMES = """
            SELECT g.name, COUNT(c.c_id) AS num_of_reviews, AVG(c.rating) AS average_rating
            FROM game AS g
            LEFT JOIN comment AS c
            ON g.gid = c.gid
            WHERE name LIKE ?
            GROUP BY g.gid
            ORDER BY average_rating DESC;
            """;
}
