# Knowledgebase 2.0

Java based content store to store text-based information in a blog style.

## Features

* save text-based information
* possibility of tagging all entries
* paging support
* full text search with result highlighting
* XML export and import
  * response contains calculated sha-256 hash of xml data
* Windows application which can be accessed by taskbar icon
* REST interface to connect your custom frontend to the "Knowledgebase 2.0" backend
* SSL support for HTTP
* language support with message properties
* management view to browse the whole repository

## Screenshots

### List all entries

![List of entries](src/main/resources/public/images/list.png)

### Add a new entry

![Add new entry](src/main/resources/public/images/add.png)

### Management view

![Browse repository](src/main/resources/public/images/management.png)


## TODOs:

- unit testing (ongoing)


## Technology:

- Java SE 7
- Spring 4
- Apache Jackrabbit 2.10
- H2 Database
- Lucene Search
- Apache JCS 1.3
- JAX-RS
- Grizzly Http Server
- Freemarker 2.3
- jQuery JS
- JSTree
- Bootstrap CSS/JS
- Launch4j
- CKEditor 4

## API:


``Context          | Description``   
``-----------------+--------------------------------------``  
``/data            | list view``   
``/data/add        | add document``   
``/data/{id}       | edit document with 'id'``   
``/data/import     | import xml``
``/data/xml/export | export current (filtered) data as xml``   
``/data/management | management view``   
