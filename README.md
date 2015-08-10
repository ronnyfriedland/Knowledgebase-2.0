# Knowledgebase2.0

Java based content store to store text-based information.

## Features

- save text-based information
- possibility of tagging all entries
- paging support
- full text search with result highlighting
- XML export
- Windows application which can be accessed by taskbar icon
- REST interface to connect your custom frontend to the "Knowledgebase 2.0" backend

## Screenshots

### List all entries

![List of entries](src/main/resources/public/images/list.png)

### Add a new entry

![Add new entry](src/main/resources/public/images/add.png)

## TODOs:

- fix logging
- unit testing (ongoing)
- encryption support (xml + https)
- support binary content

## Technology:

- Java SE 7
- Spring 4
- Apache Jackrabbit 2.10
- H2 Database
- Lucene Search
- Apache JCS 1.3
- Sparkjava
- Freemarker
- jQuery JS
- Bootstrap CSS/JS
- Launch4j
