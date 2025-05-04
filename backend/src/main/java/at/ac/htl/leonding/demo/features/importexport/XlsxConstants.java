package at.ac.htl.leonding.demo.features.importexport;

import java.time.format.DateTimeFormatter;

enum SheetNames {
    User,
    Post
}
enum UserTableHeaders {
    id
}
enum PostTableHeaders {
    userId,
    title,
    published,
    body,
    createdAt
}

class Formatters {
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
}