package at.ac.htl.leonding.demo.features.importexport;

import java.time.format.DateTimeFormatter;

enum SheetNames {
    User,
    Post,
    Categories
}
enum UserTableHeaders {
    id
}
enum PostTableHeaders {
    userId,
    title,
    published,
    body,
    createdAt,
    category
}
enum CategoryTableHeaders {
    name,
    description
}
class Formatters {
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
}
