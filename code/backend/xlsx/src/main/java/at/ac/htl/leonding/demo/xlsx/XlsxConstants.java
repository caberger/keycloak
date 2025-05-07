package at.ac.htl.leonding.demo.xlsx;

import java.time.format.DateTimeFormatter;

enum SheetNames {
    User,
    Post,
    Category
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
