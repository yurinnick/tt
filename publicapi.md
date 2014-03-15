TT Public API
=========
We are happy to announce our public part of API to give you for a free use. Since TT uses Apache Tomcat and Java Servlets we can go for nearly any platform. We are a small team, who can create and maintian only limited number of platforms, so we'd appreciate if you create an app for you own platform. 

Version
-------
0.3-demo

Platform Requirements
-------
 - Network handling (HTTP POST and GET)
 - JSON parsing
 
Servlets
--------
Which servlet packages do we have? We divide them by type of requests and all of them will be listed below in detail:

- bs - basic requests. We think that's the most simple and common. It has everything, that you need for your basic TT app.
- adv - advanced requests. Here we are going to put complex things like authentication and editing
- goatse - testing and weird things. We really love them and would like to share. 

Basic Requests
------
###Get a timetable
####request: 

`[sitename]/bs.get?faculty=Printable+Name+Of+Faculty&grp=151`

####response:
String with JSON format:

`
[ [ [{mon:class},{tue:class},...],[],[],[] ],[ ] ]	   
`
      
    timetable = [even,odd]
    even = [firstclass,secondclass,thirdclass]
    firstclass = [ {mon: a}, {tue: b}, {wed: c} ]

Simple, isn't it? You just format a request with displayable name. Then, you send it. Server returns a file from the deeps of sqlite database. You go through indicies, just don't forget that they start with zero, so 0 is even timetable, 0 is 1st class and 0 is Monday.

We can't have maps instead of lists as we need sorted information and don't waste our time on sorting.

###Get displayble names
####request:
`[sitename]/bs.faculties`
####response:
`
["Name of faculty", "Name of other faculty", ...]
`
###Get list of groups on faculty
####request:
`[sitename]/bs.groups?faculty=Printable+Name+Of+Faculty`
####response:
JSON String formatted as:

`[131,141,151,...]`

Don't be suprised that these numbers of groups are actually strings. Unfortunately, we have people, how don't like numbers and can't handle them, like social faculty masters students. That's why, we provide compatibility not only for numerical group names.

Advanced Requests
----------------
We are working on them. They will provide login and editing. 

Goatse Requests
---------------
I think that not only displaybale names can be used to address a faculty. Some people like to have really clear and small things, so *knt* for them is better than *Faculty of CS&IT*. That's why we have three more for you:
###Get a timetable
####request: 

`[sitename]/goatse.get?faculty=facultyCode&grp=151`

####response:
Same as **bs**.get


###Get codenames 
####request:
`[sitename]/goatse.faculties`
####response:
`
["code of faculty", "knt", ...]
`
###Get list of groups on faculty
####request:
`[sitename]/goatse.groups?faculty=code`
####response:
Same as **bs**.groups


Outro
-----
Thanks for getting involved in free software, it rocks! Hopefully, this short guide will be helpful to you.


Authors
-----
SADO - SlepukhinAvetisyan Development Operations
2014



License
----

MIT

