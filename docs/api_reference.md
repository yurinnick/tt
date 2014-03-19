SSU TimeTable API Reference
==========================

General
-------

All methods use common prefix /api/```api_version```, where ```api_version``` is the major API version number.


Basic Requests
--------------

Common API for interacting with a service.

---
### [GET] /api/1/departments/

Get list of department id's

Status code:

* 200 - Success
* 404 - Error

Response:
```json
[
	"gg",
	"fg",
	...
]
```

---
### [GET] /api/1/<department_id>/groups

Get list of group numbers for selected department.

**String group names now are unsupported**

Status code:

* 200 - Success
* 404 - Error

Response:
```json
[
	"111",
	"112",
	...
]
```

---
### [GET] /api/1/department/<department_id>/group/<group_id>

Get timetable of group from department.

Status code:

* 200 - Success
* 404 - Error

Response:
```json
[
	"day_of_the_week": [
		"first_lesson",
		"second_lesson",
		...
	]
]
```

---
### More methods coming soon..
