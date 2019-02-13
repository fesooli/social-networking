The test statement is as follows:
You are tasked with making Nukr, a new social media product by Nu Everything S / A. The initial step is to create a prototype service that provides a REST API where we can simulate connections between people, and explore how we would offer new connection suggestions.
These are the features required:

- Be able to add a new profile;
- Be able to tag two profiles as connected;
- Be able to generate a list of new connection suggestions for a certain profile, taking the stance that the more connections to profile have with another profile's connections, the better ranked the suggestion should be;
- Some profiles can, for privacy reasons, opt to be hidden from the connection suggestions.

**The application is a microservice written in the Scala language using the Play framework and MVC arquiteture.
The application has 4 endpoints that can be accessed as soon as the application is started.**

`1 - GET - http://localhost: 9000`

Accessing this URL in the browser or making a GET request in postman for example, will be listed all the profiles registered in the social network. At first there will be none.

`2 - POST - http://localhost:9000/profile`

Doing a POST request at this address, and passing a JSON like this:

```
{
"id": "",
"name": "John",
"age": 30,
"uf": "SP",
"hiddenFromConnectionsSuggestions": false,
"connections": []
}
```

The profile of joão will be registered in the social network. And a JSON like this will be returned:

```
{
"id": "1a81830e-94c9-46cb-988e-83f0ce675da4",
"name": "John",
"age": 30,
"uf": "SP",
"hiddenFromConnectionsSuggestions": false,
"connections": []
}
```

**NOTE: This is my first microservice in Scala and for lack of experience I was not able to make only some JSON information necessary, such as not passing the blank ID.**

`3 - PUT - http://localhost:9000/connectProfiles`

Making a PUT request at this address, and passing a JSON like this:

```
{
"idProfileOne": "1a81830e-94c9-46cb-988e-83f0ce675da4",
"idProfileTwo": "753d4cdc-5491-44dc-bd46-447ca750d91c"
}
```

Where idProfileOne is one of the profiles that you want to connect to and consequently idProfileTwo is the ID of the other profile that you want to connect to. The order of the IDs does not matter, the connection will be made to the two profiles.
And it will be returned something like:

```
{
    "id": "1a81830e-94c9-46cb-988e-83f0ce675da4",
    "name": "John",
    "age": 30,
    "uf": "SP",
    "hiddenFromConnectionsSuggestions": false,
    "connections": [
        "753d4cdc-5491-44dc-bd46-447ca750d91c"
    ]
}
```

4 - POST - http://localhost:9000/generateConnectionsSuggestions

Making a PUT request at this address, and passing a JSON like this:

```
{
"idProfile": "8a22d329-552f-49ee-9653-49bc66260e0d"
}
```

Where idProfile is the ID that you want connections to be suggested for.
A JSON will be returned with suggestions for connections, such as:

```
[
    {
        "id": "1a81830e-94c9-46cb-988e-83f0ce675da4",
        "name": "Peter",
        "age": 30,
        "uf": "SP",
        "hiddenFromConnectionsSuggestions": false,
        "connections": [
            "4deb5e53-7a89-4d00-b6c0-90d6073305b0",
            "753d4cdc-5491-44dc-bd46-447ca750d91c"
        ]
    },
    {
        "id": "c0668264-8fd8-4668-8ad1-10624a5140c0",
        "name": "Joseph",
        "age": 30,
        "uf": "SP",
        "hiddenFromConnectionsSuggestions": false,
        "connections": [
            "4deb5e53-7a89-4d00-b6c0-90d6073305b0"
        ]
    },
    {
        "id": "f58533c2-8be6-4657-8a87-517a8ecf1e7c",
        "name": "Mary",
        "age": 30,
        "uf": "SP",
        "hiddenFromConnectionsSuggestions": false,
        "connections": [
            "753d4cdc-5491-44dc-bd46-447ca750d91c"
        ]
    }
]
```


The way the connections are suggested is as follows:
A search is done with the ID used in the request, bringing the profile of the person and the connections of the same and, based on these connections, searches are made on the connections of the connections of the profile found, and those who have more connections in common, is better classified.
If the profile is new and there are no connections yet to be searched, it will be searched and suggested randomly according to the UF registered in the profile and if there is no record with that UF, no connection suggestion will be returned.
