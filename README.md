# OSM Query Handler
OSM Query Handler is intended to search geographical objects into OpenStreetMap (OSM).  
Search is handling by pushing the GET query to Nominatim - instrument for searching in OSM.  
From Nominatim comes JSON string that parse by Jackson to geographical object. 
Objects have a lot of info and main of it is a GeoJSON.  
Objects can return coordinate array of their biggest area. 
Also they can return coordinate of their center.

##### GeoJSON
GeoJSON spec define a lot of object types. But in this program used only four of them:
+ MultiPolygon
+ Polygon
+ LineString
+ Point

There is only four types because most of searching queries to OSM return one of this types.

##### Calculating
All calculates neglect spheroidality of Earth.  
While calculating the coordinate array of the objects with different types of GeoJSON - 
different arrays can be return:
+ Point - array with only one coordinate
+ LineString - array with all LineString coordinates
+ Polygon - array with only polygon surface coordinates
+ MultiPolygon - array with coordinates of polygon with biggest area

#### Technologies
+ JDK 12
+ Jackson
+ GeoJSON
+ JUnit 5
+ Logback
+ Ehcache

#### See also
+ [OpenStreetMap](https://www.openstreetmap.org/)
+ [GeoJSON spec](https://tools.ietf.org/html/rfc7946)
+ [Nominatim spec](https://nominatim.org/release-docs/latest/)