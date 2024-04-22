package paf.day2.repo;

public interface RSVPQueries {
    public static final String COUNT_RSVP = """
        SELECT COUNT(*) FROM rsvp;
        """;
    public static final String ALL_RSVP = "SELECT * FROM rsvp;";
    public static final String INSERT_RSVP = """
        INSERT INTO rsvp (full_name, email, phone, confirmation_date, comment) VALUES (?, ?, ?, ?, ?)
        """;
    public static final String UPDATE_RSVP = "UPDATE rsvp SET phone=?, confirmation_date=?, comment=? WHERE email=?";
}
